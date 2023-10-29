package com.carspot.app.service;

import com.carspot.app.entity.Post;

import com.carspot.app.util.PostFilterParameters;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {
    Page<Post> findAllActivePosts(String query, Pageable pageable);
    Page<Post> filterActivePosts(PostFilterParameters params, Pageable pageable);
    Integer findAllActiveUserPostsCount(String emailAddress);
    Integer findAllUserPostsCount(String emailAddress);
    Integer findPostCountByBrand(String brand);
    Integer findPostCountByProvince(String province);

    Post findPostById(Long postId, String emailAddress);
    Post findPostById(Long postId);
    Page<Post> findAllActivePostsByPostCreator(String emailAddress,Pageable pageable);
    Page<Post> findAllPostsByPostCreator(String emailAddress,Pageable pageable);

    Post createPost(Post post, String emailAddress);

    Post updatePost(Post post, Long postId, String emailAddress,List<MultipartFile> postImageList) throws IOException;
    void deleteOneOrMultiplePosts(List<Long> postIdList, String emailAddress);



}
