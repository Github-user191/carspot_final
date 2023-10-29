package com.carspot.app.controller;

import com.carspot.app.dto.UserReviewAndAvatarDTO;
import com.carspot.app.entity.Review;
import com.carspot.app.entity.User;
import com.carspot.app.entity.UserAvatar;
import com.carspot.app.repository.UserRepository;
import com.carspot.app.service.impl.ReviewServiceImpl;
import com.carspot.app.service.impl.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.carspot.app.utils.TestUtils.mapToJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewServiceImpl reviewService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;


    private Review reviewOne;
    private UserReviewAndAvatarDTO reviewAndAvatarDTOOne, reviewAndAvatarDTOTwo;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1, "admin", "admin@gmail.com", "+27339923823", "password");
        reviewOne = new Review(1L, "admin", "admin@gmail.com", "This is my first review of the website!");
        reviewAndAvatarDTOOne = new UserReviewAndAvatarDTO(1L,"user", "user@gmail.com", LocalDateTime.now(),
                "This is my first review of the website!", new UserAvatar("44", "33", user));
        reviewAndAvatarDTOTwo = new UserReviewAndAvatarDTO(2L,"user", "user@gmail.com", LocalDateTime.now(),
                "This is my second review of the website!", new UserAvatar("44", "33", user));
    }


    @AfterEach
    void tearDown() {
    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = "USER")
    void shouldCreateReview() throws Exception {

        RequestBuilder builder = MockMvcRequestBuilders
                .post("/api/review/create")
                .accept(MediaType.APPLICATION_JSON)
                .content(mapToJson(reviewOne))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.review").value("This is my first review of the website!"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = "USER")
    void shouldReturnReviewByEmailAddress() throws Exception {
        when(reviewService.getUserReviewByEmailAddress(anyString())).thenReturn(reviewOne);

        RequestBuilder builder = MockMvcRequestBuilders
                .get("/api/review")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reviewOne.getId()))
                .andExpect(jsonPath("$.review").value(reviewOne.getReview()))
                .andReturn();


    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = "USER")
    void shouldGetAllUserReviews() throws Exception {
        List<UserReviewAndAvatarDTO> reviewList = List.of(reviewAndAvatarDTOOne, reviewAndAvatarDTOTwo);

        when(reviewService.getAllUserReviewsAndUserAvatarDTO(any())).thenReturn(new PageImpl<>(reviewList));

        RequestBuilder builder = MockMvcRequestBuilders
                .get("/api/review/all")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalItems").value(2))
                .andDo(print());
    }


    @Test
    @WithMockUser(username = "admin", password = "password", roles = "USER")
    void shouldDeleteUserReviewById() throws Exception {

        RequestBuilder builder = MockMvcRequestBuilders
                .delete("/api/review/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result =  mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        assertEquals("Review with id 1 was deleted successfully!", response);
    }



}