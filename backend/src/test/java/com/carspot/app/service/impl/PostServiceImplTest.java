package com.carspot.app.service.impl;

import com.carspot.app.entity.*;
import com.carspot.app.exception.exceptions.PostAlreadyExistsException;
import com.carspot.app.exception.exceptions.PostNotFoundException;
import com.carspot.app.repository.PostRepository;
import com.carspot.app.repository.UserRepository;
import com.carspot.app.util.PostFilterParameterSpecification;
import com.carspot.app.util.PostFilterParameters;
import org.apache.tomcat.jni.Local;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @InjectMocks
    private PostServiceImpl postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FileUploadServiceImpl fileUploadService;

    private Post postOne, postTwo, postThree, duplicatePost;

    private User user;

    @BeforeEach
    void setUp() {

        user = new User(1, "admin", "admin@gmail.com", "+27339923823", "password");

        postOne = new Post(1L,
                "This is my first post on the website",
                "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaqueoluptas sit aspernatur aut odit aut fugit",
                "Toyota", "Supra", 120000D, "Red", 92000D, "Sedan", "Petrol", "Automatic",
                2019, "Province 1", "city 1");

        postTwo = new Post(2L,
                "This is my second post on the website",
                "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaqueoluptas sit aspernatur aut odit aut fugit",
                "BMW", "i35", 120000D, "Red", 92000D, "Sedan", "Petrol", "Automatic",
                2019, "Province 2", "city 2");

        postThree = new Post(3L,
                "This is my third post on the website",
                "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaqueoluptas sit aspernatur aut odit aut fugit",
                "Audi", "A3", 120000D, "Red", 92000D, "Sedan", "Petrol", "Automatic",
                2019, "Province 3", "city 3");


        duplicatePost = new Post(4L,
                "This is my first post on the website",
                "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaqueoluptas sit aspernatur aut odit aut fugit",
                "Toyota", "Supra", 120000D, "Red", 92000D, "Sedan", "Petrol", "Automatic",
                2019, "Province 1", "city 1");

        postOne.setExpiredAt(postOne.getCreatedAt().plusMonths(1));
        postOne.setPostCreatorEmail(user.getEmailAddress());

        postTwo.setExpiredAt(postTwo.getCreatedAt().plusMonths(1));
        postTwo.setPostCreatorEmail(user.getEmailAddress());

        postThree.setExpiredAt(postThree.getCreatedAt().plusMonths(1));
        postThree.setPostCreatorEmail(user.getEmailAddress());

        duplicatePost.setExpiredAt(duplicatePost.getCreatedAt().plusMonths(1));




        postService = new PostServiceImpl(postRepository, userRepository, fileUploadService);
    }

    @AfterEach
    void tearDown() {
        postRepository.deleteAll();

    }

    @Test
    public void canFindPostById() {
        when(postRepository.findPostById(1L)).thenReturn(postOne);

        Post thePost = postService.findPostById(1L);

        assertNotNull(thePost);
        assertEquals(1, thePost.getId());
        assertEquals("This is my first post on the website", thePost.getTitle());
    }

    @Test
    public void willThrowIfCantFindPostById() {


        long id = 9999L;

        when(postRepository.findPostById(id)).thenReturn(null);

        assertThrows(PostNotFoundException.class,
                () -> postService.findPostById(id), "Post with id " + id + " was not found");

        verify(postRepository, times(1)).findPostById(id);
    }

//    @Test
//    public void willThrowIfPostIsExpired() {
//        when(postRepository.findPostById(1L)).thenReturn(postOne);
//        postOne.setActive(false);
//
//        assertThrows(PostNotFoundException.class,
//                () -> postService.findPostById(1L), "Post has expired");
//
//
//        verify(postRepository, atLeastOnce()).findPostById(1L);
//    }


    @Test
    public void canGetAllPostsThatAreActive() {

        PageRequest pageRequest = PageRequest.of(0, 5);

        when(postRepository.searchAllActivePosts("second post", pageRequest))
                .thenReturn(new PageImpl<>(List.of(postTwo)));


        Page<Post> posts = postService.findAllActivePosts("second post",  pageRequest);

        assertTrue(postTwo.isActive());
        assertThat(posts.getTotalElements()).isEqualTo(1);
    }


    @Test
    public void canFilterActivePosts() {
        PostFilterParameters params = new PostFilterParameters(null,
                null, List.of("Audi"), 0, 120000);
        Pageable pagingSort = PageRequest.of(0, 10,
                Sort.by("price").ascending());



        when(postRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(postThree)));
        Page<Post> posts = postService.filterActivePosts(params, pagingSort);


        assertThat(posts.getTotalElements()).isGreaterThan(0);
    }


    @Test
    void testCanCreatePostAndUploadImages() {
        MockMultipartFile multipartFile1 = new MockMultipartFile("image", "test.jpg",
                "image/jpg", "Some bytes".getBytes());
        MockMultipartFile multipartFile2 = new MockMultipartFile("image", "test.jpg",
                "image/jpg", "Some bytes".getBytes());

        List<MultipartFile> postImageList = List.of(multipartFile1, multipartFile2);

        when(userRepository.findUserByEmailAddress(anyString())).thenReturn(user);

        Post createdPost = postService.createPost(postOne, "admin@gmail.com");

        PostImage postImage1 = new PostImage("44", "EEE", createdPost);
        PostImage postImage2 = new PostImage("44", "EEE", createdPost);

        createdPost.setPostImages(List.of(postImage1, postImage2));


        assertNotNull(createdPost.getPostImages());
        assertThat(createdPost.getPostImages().size()).isGreaterThan(0);
        assertTrue(createdPost.isActive());
        assertEquals("admin@gmail.com", createdPost.getPostCreatorEmail());
        verify(postRepository).save(any(Post.class));

    }

    @Test
    void willThrowIfPostTitleIsDuplicateWhenCreatingPost() {
        when(userRepository.findUserByEmailAddress(anyString())).thenReturn(user);

        when(postRepository.existsByTitle(anyString())).thenReturn(true);

        assertThrows(PostAlreadyExistsException.class,
                () -> postService.createPost(postOne, "admin@gmail.com"),
                "Post with title This is my first post on the website already exists");

    }

    @Test
    void testCanUpdatePost() throws IOException {
        when(postRepository.findPostById(anyLong())).thenReturn(postOne);

        postOne.setTitle("This is an updated title");

        Post updatedPost = postService.updatePost(postOne, 1L, "admin@gmail.com", null);

        assertEquals(updatedPost.getTitle(), "This is an updated title");
        verify(postRepository).save(any());
    }

    @Test
    void testCanUpdatePostAndAddPostImages() throws IOException {
        MockMultipartFile multipartFile1 = new MockMultipartFile("image", "test.jpg", "image/jpg", new ClassPathResource("test.jpg").getInputStream());
        MockMultipartFile multipartFile2 = new MockMultipartFile("image", "test2.jpg", "image/jpg", new ClassPathResource("test.jpg").getInputStream());

        List<MultipartFile> postImageList = List.of(multipartFile1, multipartFile2);

        when(postRepository.findPostById(anyLong())).thenReturn(postOne);

        PostImage postImage1 = new PostImage("44", "EEE", postOne);
        PostImage postImage2 = new PostImage("44", "EEE", postOne);

        postOne.setTitle("This is an updated title");
        postOne.setPostImages(List.of(postImage1, postImage2));

        Post updatedPost = postService.updatePost(postOne, 1L, "admin@gmail.com", postImageList);

        assertEquals("This is an updated title",updatedPost.getTitle());
        assertThat(updatedPost.getPostImages().size()).isGreaterThan(0);
    }

    @Test
    void willThrowIfDifferentUserTriesToUpdatePost() {
        when(postRepository.findPostById(anyLong())).thenReturn(postOne);

        assertThrows(PostNotFoundException.class, () -> postService.updatePost(postOne, 1L, "fake@gmail.com", null),
                "Post not found in your account");

        verify(postRepository, never()).save(any(Post.class));

    }


    @Test
    void testCanDeleteOneOrMultiplePosts() {

        List<Post> postList = List.of(postOne, postTwo);

        when(postRepository.findPostById(1L)).thenReturn(postOne);
        when(postRepository.findPostById(2L)).thenReturn(postTwo);

        doNothing().when(postRepository).deleteAll(postList);

        postService.deleteOneOrMultiplePosts(postList.stream().map(p -> p.getId()).collect(Collectors.toList()), "admin@gmail.com");

        verify(postRepository).deleteAll(postList);

    }


    @Test
    void willThrowIfDifferentUserTriesToDeletePost() {

        List<Post> postList = List.of(postOne);

        when(postRepository.findPostById(1L)).thenReturn(postOne);


        assertThrows(PostNotFoundException.class,
                () -> postService.deleteOneOrMultiplePosts(postList.stream().map(p -> p.getId()).collect(Collectors.toList()), "fake@gmail.com"),
                "Post " + postOne.getTitle() + " not found in your account");

        verify(postRepository, never()).deleteAll(postList);

    }


    @Test
    void canFindAllActiveUserPostsCount() {
        user.setPosts(List.of(postOne, postTwo));

        when(postRepository.countAllByPostCreatorEmailAndActiveIsTrue(anyString())).thenReturn(2);

        postService.findAllActiveUserPostsCount("admin@gmail.com");

        assertThat(user.getPosts().size()).isEqualTo(2);
        assertTrue(user.getPosts().get(0).isActive());
        assertTrue(user.getPosts().get(1).isActive());
    }

    @Test
    void canFindAllUserPostsCount() {
        user.setPosts(List.of(postOne, postTwo));

        when(postRepository.countAllByPostCreatorEmail(anyString())).thenReturn(2);

        postService.findAllUserPostsCount("admin@gmail.com");

        assertThat(user.getPosts().size()).isEqualTo(2);
        assertTrue(user.getPosts().get(0).isActive());
        assertTrue(user.getPosts().get(1).isActive());
    }

    @Test
    void canFindAllActivePostsByPostCreator() {

        List<Post> posts = List.of(postOne, postTwo);
        user.setPosts(posts);

        when(postRepository.findAllActivePostsByPostCreatorEmailAndActiveIsTrue(anyString(), any())).thenReturn(new PageImpl<>(posts));

        Page<Post> postsPage = postService.findAllActivePostsByPostCreator("admin@gmail.com", PageRequest.ofSize(10));

        assertThat(postsPage.getTotalElements()).isEqualTo(2);
        assertTrue(user.getPosts().get(0).isActive());
        assertTrue(user.getPosts().get(1).isActive());
    }

    @Test
    void willThrowIfCannotFindAllActivePostsByPostCreator() {

        List<Post> posts = Collections.EMPTY_LIST;
        user.setPosts(posts);

        when(postRepository.findAllActivePostsByPostCreatorEmailAndActiveIsTrue(anyString(), any())).thenReturn(new PageImpl<>(posts));

        assertThrows(PostNotFoundException.class, () -> postService.findAllActivePostsByPostCreator("admin@gmail.com", PageRequest.ofSize(10)),
                "You do not have any posts..");

    }

    @Test
    void canFindAllPostsByPostCreator() {
        List<Post> posts = List.of(postOne, postTwo);
        user.setPosts(posts);

        when(postRepository.findAllByPostCreatorEmail(anyString(), any())).thenReturn(new PageImpl<>(posts));

        Page<Post> postsPage = postService.findAllPostsByPostCreator("admin@gmail.com", PageRequest.ofSize(10));

        assertThat(user.getPosts().get(0).getPostCreatorEmail()).isEqualTo(user.getEmailAddress());
        assertThat(user.getPosts().get(1).getPostCreatorEmail()).isEqualTo(user.getEmailAddress());
        assertThat(postsPage.getTotalElements()).isEqualTo(2);
        assertTrue(user.getPosts().get(0).isActive());
        assertTrue(user.getPosts().get(1).isActive());
    }

    @Test
    void willThrowIfCannotFindAllPostsByPostCreator() {
        List<Post> posts = Collections.EMPTY_LIST;
        user.setPosts(posts);

        when(postRepository.findAllByPostCreatorEmail(anyString(), any())).thenReturn(new PageImpl<>(posts));

        assertThrows(PostNotFoundException.class, () -> postService.findAllPostsByPostCreator("admin@gmail.com", PageRequest.ofSize(10)),
                "You do not have any posts..");
    }


    @Test
    void canFindActivePostCountByBrand() {
        when(postRepository.countAllByBrandAndActiveIsTrue("Audi")).thenReturn(1);
        assertThat(postService.findPostCountByBrand("Audi")).isEqualTo(1);
    }

    @Test
    void canFindActivePostCountByProvince() {
        when(postRepository.countAllByProvinceAndActiveIsTrue("Province 1")).thenReturn(2);
        assertThat(postService.findPostCountByProvince("Province 1")).isEqualTo(2);
    }





//    @Test
//    void willThrowIfPostIsDuplicate() {
//
//        when(userRepository.findUserByEmailAddress("admin@gmail.com")).thenReturn(user);
//
//        when(postRepository.save(postOne)).thenReturn(postOne);
//        Post p1 = postService.createPost(postOne, "admin@gmail.com", null);
//
//        when(userRepository.findUserByEmailAddress("admin@gmail.com")).thenReturn(user);
//        when(postRepository.save(duplicatePost)).thenReturn(duplicatePost);
//        Post p2 = postService.createPost(duplicatePost, "admin@gmail.com", null);
//
//
//        System.out.println(p1);
//        System.out.println(p2);
//        assertThat(p1.getTitle()).isEqualTo(p2.getTitle());
//
//    }

}