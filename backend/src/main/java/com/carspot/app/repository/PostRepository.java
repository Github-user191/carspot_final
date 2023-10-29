package com.carspot.app.repository;

import com.carspot.app.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;

import java.util.List;


@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    Boolean existsByTitle(String title);
    Post findPostById(Long id);
    Post findPostByTitle(String title);

    Integer countPostsByPostCreatorEmailAndActiveIsTrue(String postCreatorEmail);
    Integer countPostsByPostCreatorEmail(String postCreatorEmail);


    @Query(value = "SELECT * FROM post p WHERE (p.brand LIKE :query OR p.model like :query OR p.description LIKE CONCAT('%', :query, '%') " +
            "OR p.title LIKE CONCAT('%', :query, '%')) AND p.active=true", nativeQuery = true)
    Page<Post> searchAllActivePosts(@Param("query")String query,Pageable pageable);

    Page<Post> findAllByIdIn(List<Long> postIdList,Pageable pageable);

    Integer countAllByPostCreatorEmail(String emailAddress);
    Integer countAllByPostCreatorEmailAndActiveIsTrue(String emailAddress);

    Integer countAllByBrandAndActiveIsTrue(String brand);
    Integer countAllByProvinceAndActiveIsTrue(String province);

    Page<Post> findAllActivePostsByPostCreatorEmailAndActiveIsTrue(String emailAddress, Pageable pageable);

    Page<Post> findAllByPostCreatorEmail(String emailAddress, Pageable pageable);

}

