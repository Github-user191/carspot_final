package com.carspot.app.service.impl;


import com.carspot.app.entity.Post;
import com.carspot.app.entity.PostViewCount;
import com.carspot.app.repository.PostRepository;
import com.carspot.app.repository.PostViewRepository;
import com.carspot.app.service.PostService;
import com.carspot.app.service.PostViewCountService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.parameters.P;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostViewCountServiceImplTest {


    @InjectMocks
    private PostViewCountServiceImpl postViewCountService;

    @Mock
    private PostViewRepository postViewRepository;

    @Mock
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    private PostViewCount postViewCount;

    private Post post;


    @BeforeEach
    void setUp() {
        post = new Post(2L,
                "This is my first post on the website",
                "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaqueoluptas sit aspernatur aut odit aut fugit",
                "Toyota", "Supra", 120000D, "Red", 92000D, "Sedan", "Petrol", "Automatic",
                2019, "Province 1", "city 1");
        postViewCountService = new PostViewCountServiceImpl(postViewRepository, postService);

        postViewCount = new PostViewCount(1L, 500);

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void canGetPostViewCountByPostId() {

        when(postViewRepository.getPostViewCountByPostId(anyLong())).thenReturn(500L);

        Long viewCount = postViewCountService.getPostViewCountByPostId(1L);


        assertEquals(500, viewCount);
        verify(postViewRepository).getPostViewCountByPostId(anyLong());
    }

    @Test
    void canAddInitialPostViewByPostId() {

        when(postService.findPostById(anyLong())).thenReturn(post);
        when(postViewRepository.getPostViewCountByPostId(anyLong())).thenReturn(null);

        postViewCountService.addPostViewByPostId(post.getId());
        postViewCount.setViewCount(1);


        assertEquals(1, postViewCount.getViewCount());
        verify(postViewRepository).save(any());


    }

    @Test
    void canAddPostViewByPostId() {
        when(postService.findPostById(anyLong())).thenReturn(post);
        when(postViewRepository.getPostViewCountByPostId(anyLong())).thenReturn(postViewCount.getViewCount());

        postViewCountService.addPostViewByPostId(post.getId());
        postViewCount.setViewCount(postViewCount.getViewCount() + 1);


        assertEquals(501, postViewCount.getViewCount());
        verify(postViewRepository).save(any());
    }
}
