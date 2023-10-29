package com.carspot.app.repository;

import com.carspot.app.entity.WatchlistPost;
import com.carspot.app.entity.WatchlistPostPk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchlistPostRepository extends JpaRepository<WatchlistPost, WatchlistPostPk> {
    WatchlistPost findFirstByPostIdAndUserId(Long postId, Long userId);
    List<WatchlistPost> findAllByUserId(Long userId);
    Integer countAllByUserId(Long userId);



}
