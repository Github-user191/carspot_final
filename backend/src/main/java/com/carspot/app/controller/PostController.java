package com.carspot.app.controller;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.carspot.app.config.AzureConfig;
import com.carspot.app.entity.Post;

import com.carspot.app.entity.PostImage;
import com.carspot.app.entity.User;
import com.carspot.app.exception.exceptions.FileUploadException;
import com.carspot.app.payload.response.ApiResponse;
import com.carspot.app.repository.UserRepository;
import com.carspot.app.service.FileUploadService;
import com.carspot.app.service.PostService;
import com.carspot.app.service.PostViewCountService;
import com.carspot.app.service.WatchlistPostService;
import com.carspot.app.service.impl.*;
import com.carspot.app.util.PostFilterParameters;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

import static com.carspot.app.util.Brands.CAR_BRANDS;
import static com.carspot.app.util.Provinces.PROVINCES;



@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class PostController {

    private final ErrorValidationServiceImpl errorValidationService;
    private final PostService postService;
    private final FileUploadService fileUploadService;
    private final WatchlistPostService watchlistPostService;
    private final PostViewCountService postViewCountService;

    public PostController(ErrorValidationServiceImpl errorValidationService, PostService postService, FileUploadService fileUploadService, WatchlistPostService watchlistPostService, PostViewCountService postViewCountService) {
        this.errorValidationService = errorValidationService;
        this.postService = postService;
        this.fileUploadService = fileUploadService;
        this.watchlistPostService = watchlistPostService;
        this.postViewCountService = postViewCountService;
    }

    @PostMapping("/post/create")
    public ResponseEntity<?> createPost(@RequestPart(value = "image", required = false) List<MultipartFile> postImageList, @Valid @RequestPart(value = "post") Post post, BindingResult result, Principal principal) throws IOException {
        ResponseEntity<?> errorMap = errorValidationService.validationService(result);
        if (errorMap != null) return errorMap;

        List<String> validExtensions = Arrays.asList("jpeg", "jpg", "png");

        if(postImageList != null) {
            if (postImageList.size() > 7) {
                throw new FileUploadException("Maximum of 7 images can be added per post");
            }
            for(MultipartFile file : postImageList) {
                String extension = FilenameUtils.getExtension(file.getOriginalFilename());
                if (!validExtensions.contains(extension)) {
                    throw new FileUploadException("Only .jpg, .jpeg or .png file types are allowed");
                }
            }
        }

        postService.createPost(post, principal.getName());
        fileUploadService.uploadPostImages(postImageList, post.getId(), principal.getName());


        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Post created successfully!", true));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable Long postId) {
        Post post = postService.findPostById(postId);

        if(post != null) {
            postViewCountService.addPostViewByPostId(postId);
            return new ResponseEntity<>(post, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/posts/search")
    public ResponseEntity<?> getAllActivePosts(
        @RequestParam(required=false) String query,
        @RequestParam(required = false) List<String> brand,
        @RequestParam(required=false) String province,
        @RequestParam(required=false) List<String> city,
        @RequestParam(defaultValue = "0", required = false) int pageNo,
        @RequestParam(defaultValue = "5", required = false) int pageSize,
        @RequestParam(defaultValue = "id", required = false) String sortBy,
        @RequestParam(defaultValue = "desc", required = false) String sortDir,
        @RequestParam(defaultValue = "0", required = false) int minValue,
        @RequestParam(defaultValue = "5000000", required = false) int maxValue) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pagingSort = PageRequest.of(pageNo, pageSize, sort);
        PostFilterParameters params = new PostFilterParameters(province, city, brand , minValue, maxValue);

        Page<Post> pageTuts;

        if(query != null) {
            pageTuts = postService.findAllActivePosts(query, pagingSort);
            System.out.println(pageTuts.getTotalElements());
        } else  {
            pageTuts = postService.filterActivePosts(params, pagingSort);
        }

        return new ResponseEntity<>(createMapForPageContents(pageTuts), HttpStatus.OK);
    }

    @GetMapping("/posts/user/active/{emailAddress}")
    public ResponseEntity<?> getActiveUserPostsByEmailAddress(
            @PathVariable String emailAddress,
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "5", required = false) int pageSize,
            @RequestParam(defaultValue = "id", required = false) String sortBy,
            @RequestParam(defaultValue = "desc", required = false) String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pagingSort = PageRequest.of(pageNo, pageSize, sort);

        Page<Post> pageTuts = postService.findAllActivePostsByPostCreator(emailAddress, pagingSort);

        return new ResponseEntity<>(createMapForPageContents(pageTuts), HttpStatus.OK);
    }

    @GetMapping("/posts/brand/count")
    public ResponseEntity<?> getPostCountByBrand() {

        Map<String, Integer> carBrandMap = new TreeMap<>();

        for(int i = 0; i < CAR_BRANDS.size(); i++) {
            carBrandMap.put(CAR_BRANDS.get(i), postService.findPostCountByBrand(CAR_BRANDS.get(i)));
        }
        return new ResponseEntity<>(carBrandMap, HttpStatus.OK);
    }

    @GetMapping("/posts/province/count")
    public ResponseEntity<?> getPostCountByProvince() {

        Map<String, Integer> provinceMap = new TreeMap<>();

        for(int i = 0; i < PROVINCES.size(); i++) {
            provinceMap.put(PROVINCES.get(i), postService.findPostCountByProvince(PROVINCES.get(i)));
        }
        return new ResponseEntity<>(provinceMap, HttpStatus.OK);
    }

    @GetMapping("/posts/user/active")
    public ResponseEntity<?> getActiveUserPosts(
            Principal principal,
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "5", required = false) int pageSize,
            @RequestParam(defaultValue = "id", required = false) String sortBy,
            @RequestParam(defaultValue = "desc", required = false) String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pagingSort = PageRequest.of(pageNo, pageSize, sort);

        Page<Post> pageTuts = postService.findAllActivePostsByPostCreator(principal.getName(), pagingSort);

        return new ResponseEntity<>(createMapForPageContents(pageTuts), HttpStatus.OK);
    }

    @GetMapping("/posts/user/all")
    public ResponseEntity<?> getUserPosts(
            Principal principal,
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "5", required = false) int pageSize,
            @RequestParam(defaultValue = "id", required = false) String sortBy,
            @RequestParam(defaultValue = "desc", required = false) String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pagingSort = PageRequest.of(pageNo, pageSize, sort);

        Page<Post> pageTuts = postService.findAllPostsByPostCreator(principal.getName(), pagingSort);

        return new ResponseEntity<>(createMapForPageContents(pageTuts), HttpStatus.OK);
    }

    @PutMapping("/post/update/{postId}")
    public ResponseEntity<?> updatePost(@RequestPart(value = "image", required = false) List<MultipartFile> postImageList,
                                         @Valid @RequestPart(value = "post") Post post, @PathVariable Long postId, BindingResult result, Principal principal) throws IOException {
        ResponseEntity<?> errorMap = errorValidationService.validationService(result);
        if (errorMap != null) return errorMap;

        return new ResponseEntity<>(postService.updatePost(post, postId, principal.getName(), postImageList), HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postIdList}")
    public ResponseEntity<?> deleteOneOrMultiplePosts(@PathVariable List<Long> postIdList, Principal principal) {
        postService.deleteOneOrMultiplePosts(postIdList, principal.getName());
        return new ResponseEntity<>(createMapForPageContents(postService.findAllActivePostsByPostCreator(principal.getName(), PageRequest.of(0, 5))), HttpStatus.OK);
    }

    @DeleteMapping("/post/image/{id}")
    public ResponseEntity<?> deleteFileUpload(@PathVariable String id, Principal principal) {

        fileUploadService.deletePostImageById(id);

        return new ResponseEntity<String>("Post image " + id + " was deleted", HttpStatus.OK);
    }

    @GetMapping("/post/image/{id}")
    public ResponseEntity<?> getPostImage(@PathVariable String id, Principal principal) {

        PostImage postImage = fileUploadService.findPostImageById(id);

        return new ResponseEntity<String>(postImage.getImageUrl(), HttpStatus.OK);
    }


    @GetMapping("/posts/user/count")
    public ResponseEntity<?> getUserPostsCount(Principal principal) {
        Map<String, Integer> postCountMap = new HashMap<>();


        Integer activePostCount = postService.findAllActiveUserPostsCount(principal.getName());
        Integer allPostCount = postService.findAllUserPostsCount(principal.getName());
        Integer watchlistPostCount = watchlistPostService.findUserWatchlistPostsCount(principal.getName());

        postCountMap.put("activePosts", activePostCount);
        postCountMap.put("allPosts", allPostCount);
        postCountMap.put("watchlistedPosts", watchlistPostCount);
        return ResponseEntity.ok(postCountMap);

    }

    @GetMapping("/posts/user/watchlist")
    public ResponseEntity<?> getUserWatchlistPosts(Principal principal,
        @RequestParam(defaultValue = "0", required = false) int pageNo,
        @RequestParam(defaultValue = "5", required = false) int pageSize) {

        Pageable pagingSort = PageRequest.of(pageNo, pageSize);
        Page<Post> watchListPostsPage =  watchlistPostService.findUserWatchListPosts(principal.getName(), pagingSort);


        return new ResponseEntity<>(createMapForPageContents(watchListPostsPage), HttpStatus.OK);
    }


    @GetMapping("/post/watchlist/{id}")
    public ResponseEntity<?> addWatchlistPost(@PathVariable Long id, Principal principal) {

        watchlistPostService.addWatchlistPost(id, principal.getName());
        return new ResponseEntity<>("Post added to watchlist", HttpStatus.OK);
    }

    @DeleteMapping("/post/watchlist/{watchlistPostIds}")
    public ResponseEntity<?> deleteWatchlistPosts(@PathVariable Long[] watchlistPostIds, Principal principal) {
        watchlistPostService.deleteMultipleWatchlistPosts(Arrays.asList(watchlistPostIds), principal.getName());

        Pageable pagingSort = PageRequest.of(0, 5);
        Page<Post> watchListPostsPage =  watchlistPostService.findUserWatchListPosts(principal.getName(), pagingSort);
        return new ResponseEntity<>(createMapForPageContents(watchListPostsPage), HttpStatus.OK);
    }


    @GetMapping("/post/views/{postId}")
    public ResponseEntity<?> getViewCountByPostId(@PathVariable long postId) {
        return new ResponseEntity<>(postViewCountService.getPostViewCountByPostId(postId), HttpStatus.OK);

    }


    private Map<Object ,Object> createMapForPageContents(Page page) {
        Map<Object, Object> pageContentMapResponse = new HashMap<>();
        pageContentMapResponse.put("posts", page.getContent());
        pageContentMapResponse.put("currentPage", page.getNumber());
        pageContentMapResponse.put("totalItems", page.getTotalElements());
        pageContentMapResponse.put("totalPages", page.getTotalPages());
        return pageContentMapResponse;
    }

}
