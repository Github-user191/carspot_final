package com.carspot.app.service.impl;

import com.carspot.app.entity.*;

import com.carspot.app.exception.exceptions.FileUploadException;
import com.carspot.app.exception.exceptions.PostAlreadyExistsException;
import com.carspot.app.exception.exceptions.PostNotFoundException;
import com.carspot.app.repository.PostRepository;

import com.carspot.app.repository.UserRepository;
import com.carspot.app.service.FileUploadService;
import com.carspot.app.service.PostService;
import com.carspot.app.util.PostFilterParameterSpecification;
import com.carspot.app.util.PostFilterParameters;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FileUploadService fileUploadService;


    @Override
    public Post findPostById(Long id, String emailAddress) {
        Post post = postRepository.findPostById(id);

        if (post == null) {
            throw new PostNotFoundException("Post with id " + id + " was not found");
        }

        return post;
    }

    @Override
    public Post findPostById(Long id) {
        Post post = postRepository.findPostById(id);

        if (post == null) {
            throw new PostNotFoundException("Post with id " + id + " was not found");
        }
//        else if (!post.isActive()) {
//            throw new PostNotFoundException("Post has expired");
//        }
        return post;
    }


    @Override
    public Page<Post> findAllActivePosts(String query, Pageable pageable) {
        Page<Post> page = postRepository.searchAllActivePosts(query, pageable);


        page.stream().forEach(post -> {

            if (post.isPostExpired()) {
                User user = userRepository.findUserByEmailAddress(post.getPostCreatorEmail());
                post.setActive(false);
            }

            postRepository.save(post);
        });

        if (page.isEmpty()) throw new PostNotFoundException("There are no matching records..");
        return page;
    }

    @Override
    public Page<Post> filterActivePosts(PostFilterParameters params, Pageable pageable) {

        PostFilterParameterSpecification specification = new PostFilterParameterSpecification(params);

        Page<Post> page = postRepository.findAll(specification, pageable);

        page.stream().forEach(post -> {
            if (post.isPostExpired()) post.setActive(false);
            postRepository.save(post);
        });

        if (page.isEmpty()) throw new PostNotFoundException("There are no matching records..");

        return page;

    }

    @Override
    public Integer findAllActiveUserPostsCount(String emailAddress) {
        return postRepository.countAllByPostCreatorEmailAndActiveIsTrue(emailAddress);
    }

    @Override
    public Integer findAllUserPostsCount(String emailAddress) {
        return postRepository.countAllByPostCreatorEmail(emailAddress);
    }


    @Override
    public Page<Post> findAllActivePostsByPostCreator(String emailAddress, Pageable pageable) {

        Page<Post> page = postRepository.findAllActivePostsByPostCreatorEmailAndActiveIsTrue(emailAddress, pageable);

        page.stream().forEach(post -> {
            if (post.isPostExpired()) post.setActive(false);
            postRepository.save(post);
        });

        if (page.isEmpty()) throw new PostNotFoundException("You do not have any posts..");
        return page;
    }


    @Override
    public Page<Post> findAllPostsByPostCreator(String emailAddress, Pageable pageable) {

        Page<Post> page = postRepository.findAllByPostCreatorEmail(emailAddress, pageable);

        page.stream().forEach(post -> {
            if (post.isPostExpired()) post.setActive(false);
            postRepository.save(post);
        });

        if (page.isEmpty()) throw new PostNotFoundException("You do not have any posts");
        return page;
    }

    @Override
    public Integer findPostCountByBrand(String brand) {
        return postRepository.countAllByBrandAndActiveIsTrue(brand);
    }

    @Override
    public Integer findPostCountByProvince(String province) {
        return postRepository.countAllByProvinceAndActiveIsTrue(province);
    }

    // REFACTOR THIS
    @Override
    public Post updatePost(Post post, Long postId, String emailAddress, List<MultipartFile> postImageList) throws IOException {

        int postImageCount = 0;
        Post thePost = findPostById(postId);

        System.out.println("found post: " + thePost);
        if (postImageList != null) {
            postImageCount = postImageList.size() + fileUploadService.findPostImagesCountByPostId(thePost.getId(), emailAddress);

            if (postImageCount > 7) {
                throw new FileUploadException("Maximum of 7 images can be added per post");
            }
        }

        // Updating entity
        thePost.setTitle(post.getTitle());
        thePost.setDescription(post.getDescription());
        thePost.setBrand(post.getBrand());
        thePost.setModel(post.getModel());
        thePost.setActive(post.isActive());
        thePost.setKilometers(post.getKilometers());
        thePost.setColor(post.getColor());
        thePost.setCity(post.getCity());
        thePost.setPrice(post.getPrice());
        thePost.setBodyType(post.getBodyType());
        thePost.setProvince(post.getProvince());

        if (!(thePost.getPostCreatorEmail().equals(emailAddress))) {
            throw new PostNotFoundException("Post not found in your account");
        }

        postRepository.save(thePost);

        if(postImageList!=null) {
            fileUploadService.uploadPostImages(postImageList, postId, emailAddress);
        }


        return thePost;
    }


    @Override
    public Post createPost(Post post, String emailAddress) {
        User user = userRepository.findUserByEmailAddress(emailAddress);
        post.setPostCreatorEmail(emailAddress);
        post.setPostCreatorName(user.getFullName());
        post.setUser(user);
        post.setActive(true);

        if(postRepository.existsByTitle(post.getTitle())) {
            throw new PostAlreadyExistsException("Post with title " + post.getTitle() + " already exists");
        }


        userRepository.save(user);
        postRepository.save(post);
        return post;

    }


    public void deleteOneOrMultiplePosts(List<Long> postIdList, String emailAddress) {
        List<Post> posts = new ArrayList<>();
        List<PostImage> postImages = new ArrayList<>();
        for (int i = 0; i < postIdList.size(); i++) {
            posts.add(findPostById(postIdList.get(i), emailAddress));
            postImages.addAll(posts.get(i).getPostImages());


            if (!posts.get(i).getPostCreatorEmail().equals(emailAddress)) {
                throw new PostNotFoundException("Post " + posts.get(i).getTitle() + " not found in your account");
            }
        }

        fileUploadService.deletePostImages(postImages);
        postRepository.deleteAll(posts);
    }



}
