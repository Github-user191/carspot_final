package com.carspot.app.service.impl;

import com.carspot.app.entity.Post;
import com.carspot.app.entity.User;
import com.carspot.app.entity.WatchlistPost;
import com.carspot.app.exception.exceptions.PostNotFoundException;
import com.carspot.app.repository.PostRepository;
import com.carspot.app.repository.UserRepository;
import com.carspot.app.repository.WatchlistPostRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WatchlistPostServiceImplTest {

    @InjectMocks
    private WatchlistPostServiceImpl watchlistPostService;

    @Mock
    private WatchlistPostRepository watchlistPostRepository;

    @Mock
    private PostServiceImpl postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    private Post postOne, postTwo;

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

        watchlistPostService = new WatchlistPostServiceImpl(watchlistPostRepository, userRepository, postRepository, postService);
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void canFindUserWatchlistPosts() {
        when(userRepository.findUserByEmailAddress(anyString())).thenReturn(user);

        List<WatchlistPost> postIdList = List.of(new WatchlistPost(1, user.getId()), new WatchlistPost(2,user.getId()));
        when(watchlistPostRepository.findAllByUserId(anyLong())).thenReturn(postIdList);

        when(postRepository.findAllByIdIn(postIdList.stream().map(p -> p.getPostId()).collect(Collectors.toList()), PageRequest.ofSize(10)))
        .thenReturn(new PageImpl<>(List.of(postOne, postTwo)));

        Page<Post> postPage  = watchlistPostService.findUserWatchListPosts("admin@gmail.com", PageRequest.ofSize(10));

        assertThat(postPage.getTotalElements()).isEqualTo(2);


    }

    @Test
    void canFindUserWatchlistPostsCount() {

        when(userRepository.findUserByEmailAddress(anyString())).thenReturn(user);
        List<WatchlistPost> watchlistPostList = List.of(new WatchlistPost(1, user.getId()), new WatchlistPost(2,user.getId()));

        when(watchlistPostRepository.countAllByUserId(user.getId())).thenReturn(watchlistPostList.size());
        Integer count = watchlistPostService.findUserWatchlistPostsCount("admin@gmail.com");

        assertThat(count).isGreaterThan(0);
    }

    @Test
    void canAddWatchlistPost() {

        when(userRepository.findUserByEmailAddress(anyString())).thenReturn(user);
        when(postService.findPostById(anyLong())).thenReturn(postTwo);

        WatchlistPost watchlistPost =  watchlistPostService.addWatchlistPost(postTwo.getId(), "admin@gmail.com");
        System.out.println(watchlistPost);

        assertThat(watchlistPost.getPostId()).isEqualTo(2);
        assertThat(watchlistPost.getUserId()).isEqualTo(1);
        verify(watchlistPostRepository).save(watchlistPost);



    }

    @Test
    void willThrowIfUserTriesToWatchlistTheirOwnPost() {
        when(userRepository.findUserByEmailAddress(anyString())).thenReturn(user);
        when(postService.findPostById(anyLong())).thenReturn(postTwo);

        postTwo.setPostCreatorEmail(user.getEmailAddress());

        assertThrows(PostNotFoundException.class, () -> watchlistPostService.addWatchlistPost(postTwo.getId(), "admin@gmail.com"),
                "You cannot watchlist your own post");
    }


//    @Test
//    void canFindWatchlistPostByPostId() {
//        WatchlistPost watchlistPost = new WatchlistPost();
//        watchlistPost.setUserId(user.getId());
//        watchlistPost.setPostId(postTwo.getId());
//
//        when(watchlistPostRepository.findFirstByPostIdAndUserId(postTwo.getId(), user.getId()))
//                .thenReturn(watchlistPost);
//
//        WatchlistPost foundWatchlistPost = watchlistPostService.
//    }

    @Test
    void canDeleteMultipleWatchlistPosts() {

        when(userRepository.findUserByEmailAddress(anyString())).thenReturn(user);

        List<Long> watchlistPostIdList = List.of(1L, 2L);
        List<WatchlistPost> posts = new ArrayList<>();

        WatchlistPost watchlistPostOne = new WatchlistPost(1, user.getId());
        WatchlistPost watchlistPostTwo = new WatchlistPost(2, user.getId());

        when(watchlistPostRepository.findFirstByPostIdAndUserId(1L, user.getId())).thenReturn(watchlistPostOne);
        when(watchlistPostRepository.findFirstByPostIdAndUserId(2L, user.getId())).thenReturn(watchlistPostTwo);

        posts.add(watchlistPostOne);
        posts.add(watchlistPostTwo);


        watchlistPostService.deleteMultipleWatchlistPosts(watchlistPostIdList, user.getEmailAddress());

        verify(watchlistPostRepository).deleteAll(posts);

    }

    @Test
    void willThrowIfWatchlistPostIsNullWhenDeletingMultipleWatchlistPosts() {
        when(userRepository.findUserByEmailAddress(anyString())).thenReturn(user);

        List<Long> watchlistPostIdList = List.of(1L);
        List<WatchlistPost> posts = new ArrayList<>();

        WatchlistPost watchlistPostOne = new WatchlistPost(1, user.getId());

        when(watchlistPostRepository.findFirstByPostIdAndUserId(watchlistPostIdList.get(0), user.getId())).thenReturn(null);

        posts.add(watchlistPostOne);


        assertThrows(PostNotFoundException.class, () -> watchlistPostService.deleteMultipleWatchlistPosts(watchlistPostIdList, user.getEmailAddress()),
                "Watchlist Post with id " + watchlistPostOne.getPostId() + " was not found");

        verify(watchlistPostRepository, never()).deleteAll(posts);

    }


}