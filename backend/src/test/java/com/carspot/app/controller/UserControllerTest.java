package com.carspot.app.controller;

import com.carspot.app.dto.UserAndPostDTO;
import com.carspot.app.entity.Post;
import com.carspot.app.entity.User;
import com.carspot.app.payload.request.UpdateAccountDetailsRequest;
import com.carspot.app.repository.UserRepository;
import com.carspot.app.service.impl.ErrorValidationServiceImpl;
import com.carspot.app.service.impl.FileUploadServiceImpl;
import com.carspot.app.service.impl.UserDetailsServiceImpl;
import com.carspot.app.service.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static com.carspot.app.utils.TestUtils.mapToJson;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private UserServiceImpl userService;

    private UserAndPostDTO userAndPostDTOOne;
    private Post postOne, postTwo;
    private User user;
    private UpdateAccountDetailsRequest userRequest;


    @BeforeEach
    void setUp() {

        user = new User(1, "admin", "admin@gmail.com", "+27339923823", "password");

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

        userAndPostDTOOne = new UserAndPostDTO(1L, "admin", "admin@gmail.com","+27339923823",
                LocalDateTime.now(), null, List.of(postOne, postTwo));

        userRequest =  new UpdateAccountDetailsRequest(user.getId(), "updated user full name", user.getEmailAddress(), user.getMobileNumber());
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = "USER")
    void canGetUserAndPostInfo() throws Exception {


        when(userService.getUserAndPostInfo(anyString())).thenReturn(userAndPostDTOOne);

        RequestBuilder builder = MockMvcRequestBuilders
                .get("/api/user/info")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andReturn();
        assertThat(userAndPostDTOOne.getPosts().size()).isEqualTo(2);
    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = "USER")
    void canUpdateUserDetailsWithoutAvatar() throws Exception {


        MockMultipartFile updateRequestMultipart = new MockMultipartFile("user", "user",
                MediaType.APPLICATION_JSON_VALUE,
                mapToJson(userRequest).getBytes(StandardCharsets.UTF_8));

        RequestBuilder builder = MockMvcRequestBuilders
                .multipart("/api/user/update")
                .file(updateRequestMultipart)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("updated user full name"))
                .andDo(print());

        verify(userService).updateUser(any(), any());
    }


    @Test
    @WithMockUser(username = "admin", password = "password", roles = "USER")
    void canUpdateUserDetailsWithAvatar() throws Exception {
        MockMultipartFile avatarMultipart = new MockMultipartFile("avatar", "test.jpg", MediaType.MULTIPART_FORM_DATA_VALUE,
                new ClassPathResource("test.jpg").getInputStream());



        MockMultipartFile updateRequestMultipart = new MockMultipartFile("user", "user",
                MediaType.APPLICATION_JSON_VALUE,
                mapToJson(userRequest).getBytes(StandardCharsets.UTF_8));


        RequestBuilder builder = MockMvcRequestBuilders
                .multipart("/api/user/update")
                .file(updateRequestMultipart)
                .file(avatarMultipart)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("updated user full name"))
                .andDo(print());




        verify(userService).updateUser(any(), any());
    }
}