package com.carspot.app.repository;

import com.carspot.app.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, String> {

    PostImage findPostImageById(String id);


    @Query("SELECT COUNT(p.id) FROM PostImage p WHERE p.post.id = ?1")
    Integer countPostImagesByPostId(long id, String emailAddress);

}
