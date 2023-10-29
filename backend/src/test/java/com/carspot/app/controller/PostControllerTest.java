package com.carspot.app.controller;

import static com.carspot.app.utils.PostUtils.*;
import static com.carspot.app.utils.TestUtils.mapToJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.carspot.app.entity.Post;
import com.carspot.app.entity.User;
import com.carspot.app.repository.UserRepository;
import com.carspot.app.service.PostViewCountService;
import com.carspot.app.service.impl.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;


@WebMvcTest(value = PostController.class)
class PostControllerTest {

    @MockBean
    private ErrorValidationServiceImpl errorValidationService;

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private PostServiceImpl postService;

    @MockBean
    private FileUploadServiceImpl fileUploadService;
    @MockBean
    private WatchlistPostServiceImpl watchlistPostService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PostViewCountService postViewCountService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    private User user;

    private Post postOne, postTwo, postThree;

    @BeforeEach
    void setUp() {
        user = new User(1, "admin", "admin@gmail.com", "+27678130371", "password");
        postOne = new Post(1L,
                "This is my first post on the website",
                "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaqueoluptas sit aspernatur aut odit aut fugit",
                "Toyota", "Supra", 120000D, "Red", 92000D, "Sedan", "Petrol", "Automatic",
                2019, "Province 1", "city 1");

        postTwo = new Post(2L,
                "This is my second post on the website",
                "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaqueoluptas sit aspernatur aut odit aut fugit",
                "BMW", "i35", 120000D, "Red", 92000D, "Sedan", "Petrol", "Automatic",
                2019, "Province 2", "city 2");

        postThree = new Post(3L,
                "This is my third post on the website",
                "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaqueoluptas sit aspernatur aut odit aut fugit",
                "Audi", "A3", 120000D, "Red", 92000D, "Sedan", "Petrol", "Automatic",
                2019, "Province 3", "city 3");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = "USER")
    void shouldCreatePostWithoutPostImages() throws Exception {
        MockMultipartFile postMultipart = new MockMultipartFile("post", "post",
                MediaType.APPLICATION_JSON_VALUE,
                mapToJson(postOne).getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/api/post/create")
                        .file(postMultipart)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(print());

        verify(postService, times(1)).createPost(any(), anyString());

    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = "USER")
    void shouldCreatePostWithPostImages() throws Exception {

        //InputStream inputStream1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("test.jpg");
        MockMultipartFile postImageMultipart = new MockMultipartFile("image", "test.jpg", MediaType.IMAGE_JPEG_VALUE,
                new ClassPathResource("test.jpg").getInputStream());

        System.out.println(postImageMultipart.getOriginalFilename());



        MockMultipartFile postMultipart = new MockMultipartFile("post", "post",
                MediaType.APPLICATION_JSON_VALUE,
                mapToJson(postOne).getBytes(StandardCharsets.UTF_8));



        mockMvc.perform(multipart("/api/post/create")
                        .file(postMultipart)
                        .file(postImageMultipart)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(print());

        verify(postService, times(1)).createPost(any(), anyString());

    }


    @Test
    void shouldUpdatePost() {

    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = "USER")
    void shouldGetPostById() throws Exception {

        when(postService.findPostById(anyLong())).thenReturn(postOne);


        RequestBuilder builder = MockMvcRequestBuilders
                .get("/api/post/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value(postOne.getTitle()))
                .andExpect(jsonPath("$.description").value(postOne.getDescription()))
                .andExpect(jsonPath("$.active").value(postOne.isActive()))
                .andDo(print());


        verify(postService).findPostById(anyLong());
    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = "USER")
    void willThrowIfPostDoesNotExistWhenGettingPostById() throws Exception {

        long postId = 999L;
        when(postService.findPostById(anyLong())).thenReturn(null);

        RequestBuilder builder = MockMvcRequestBuilders
                .get("/api/post/{postId}", postId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder)
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = "USER")
    void shouldGetAllActivePosts() throws Exception {

        when(postService.findAllActivePosts(any(), any())).thenReturn(new PageImpl<>(List.of(postOne, postTwo, postThree)));
        postOne.setActive(false);

        RequestBuilder builder = MockMvcRequestBuilders
                .get("/api/posts/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("query", "Audi");

        MvcResult result = mockMvc.perform(builder)
                .andExpect(jsonPath("$.totalItems").value(3))
                .andReturn();

        System.out.println("result: " + result.getResponse().getContentAsString());

    }


    @Test
    @WithMockUser(username = "admin", password = "password", roles = "USER")
    void shouldGetActiveUserPostsByEmailAddress() throws Exception {

    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = "USER")
    void shouldGetPostCountByBrand() throws Exception {

    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = "USER")
    void shouldGetPostCountByProvince() throws Exception {

    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = "USER")
    void shouldGetActiveUserPosts() throws Exception {

    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = "USER")
    void shouldGetUserPosts() throws Exception {

    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = "USER")
    void shouldDeleteOneOrMultiplePosts() throws Exception {

        List<Long> postIdList = List.of(postOne.getId(), postTwo.getId());

        RequestBuilder builder = MockMvcRequestBuilders
                .delete("/api/posts/{postIdList}", postIdList)
                .accept(MediaType.APPLICATION_JSON);
    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = "USER")
    void shouldGetUserPostCount() throws Exception {

    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = "USER")
    void shouldGetUserWatchlistPosts() throws Exception {

    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = "USER")
    void shouldAddWatchlistPost() throws Exception {

    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = "USER")
    void shouldDeleteOneOrMultipleWatchlistPosts() throws Exception {

    }


}