package com.carspot.app.service;

import com.carspot.app.entity.Post;
import com.carspot.app.entity.WatchlistPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface WatchlistPostService {
    Page<Post> findUserWatchListPosts(String emailAddress, Pageable pageable);
    WatchlistPost addWatchlistPost(Long postId, String emailAddress);
    void deleteMultipleWatchlistPosts(List<Long> watchlistPostIdList, String emailAddress);
    Integer  findUserWatchlistPostsCount(String emailAddress);
}
