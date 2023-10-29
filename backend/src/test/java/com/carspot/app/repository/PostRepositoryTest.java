package com.carspot.app.repository;

import com.carspot.app.entity.Post;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@DataJpaTest
@Slf4j
class PostRepositoryTest {

    private Logger logger;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
        postRepository.deleteAll();
    }

    @Test
    void canSearchAllActivePosts() {

        Post postOne = new Post(
                1L,
                "Test post title one woooooooooooo",
                "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. ",
                "Toyota", "Supra", 299999D, "Black", 15000D, "Sedan", "Petrol",
                "Manual", 2022, "Province 1", "City 1");

        Post postTwo = new Post(
                2L,
                "Test post title two woooooooooooo",
                "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. ",
                "Audi", "A3", 299999D, "Black", 15000D, "Sedan", "Petrol",
                "Manual", 1018, "Province 2", "City 2");

        postTwo.setActive(false);

        postRepository.saveAll(List.of(postOne, postTwo));

        String beforeSearch = postRepository.findAll().toString();

        System.out.println("beforeSearch : " + beforeSearch);

        Page<Post> c = postRepository.searchAllActivePosts("Audi", PageRequest.ofSize(10));

        System.out.println("res: " + c.getTotalElements());
        String afterSearch = postRepository.findAll().toString();
        System.out.println("afterSearch : " + beforeSearch);
    }
}