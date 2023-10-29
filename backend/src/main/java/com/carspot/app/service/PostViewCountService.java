package com.carspot.app.service;

public interface PostViewCountService {

    Long getPostViewCountByPostId(Long postId);

    void addPostViewByPostId(Long postId);

}
