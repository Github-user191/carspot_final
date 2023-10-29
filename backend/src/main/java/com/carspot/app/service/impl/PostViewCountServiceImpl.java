package com.carspot.app.service.impl;

import com.carspot.app.entity.Post;
import com.carspot.app.entity.PostViewCount;
import com.carspot.app.exception.exceptions.PostNotFoundException;
import com.carspot.app.repository.PostRepository;
import com.carspot.app.repository.PostViewRepository;
import com.carspot.app.service.PostService;
import com.carspot.app.service.PostViewCountService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostViewCountServiceImpl implements PostViewCountService {

    private final PostViewRepository postViewRepository;
    private final PostService postService;

    public PostViewCountServiceImpl(PostViewRepository postViewRepository, PostService postService) {
        this.postViewRepository = postViewRepository;
        this.postService = postService;
    }

    @Override
    public Long getPostViewCountByPostId(Long postId) {
        Long count =  postViewRepository.getPostViewCountByPostId(postId);

        if(count == null) {
            return 0L;
        }
        return count;
    }

    @Override
    public void addPostViewByPostId(Long postId) {
        Post post = postService.findPostById(postId);

        Long viewCount = postViewRepository.getPostViewCountByPostId(postId);
        PostViewCount postViewCount = null;

        if(viewCount == null) {
            postViewCount = new PostViewCount(post.getId(), 1);
        } else {
            postViewCount = new PostViewCount(post.getId(),viewCount + 1);
        }

        postViewRepository.save(postViewCount);
    }
}
