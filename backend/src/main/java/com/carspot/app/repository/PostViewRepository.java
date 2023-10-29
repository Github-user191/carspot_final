package com.carspot.app.repository;

import com.carspot.app.entity.PostViewCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostViewRepository extends JpaRepository<PostViewCount, Long> {

    @Query(value = "SELECT view_count FROM post_view_count p WHERE p.post_id = ?;", nativeQuery = true)
    Long getPostViewCountByPostId(Long postId);
}
