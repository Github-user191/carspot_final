package com.carspot.app.controller;

import com.carspot.app.entity.ContactForm;
import com.carspot.app.entity.User;
import com.carspot.app.repository.ContactFormRepository;
import com.carspot.app.repository.UserRepository;
import com.carspot.app.service.ContactFormService;
import com.carspot.app.service.EmailService;
import com.carspot.app.service.impl.ContactFormServiceImpl;
import com.carspot.app.service.impl.EmailServiceImpl;
import com.carspot.app.service.impl.UserDetailsServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;

import static com.carspot.app.utils.TestUtils.mapToJson;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = ContactFormController.class)
class ContactFormControllerTest {

    @MockBean
    private ContactFormService contactFormService;


    @MockBean
    private UserDetailsServiceImpl userDetailsService;



    @Autowired
    private MockMvc mockMvc;

    private ContactForm contactFormOne, contactFormTwo;
    private User user;

    @BeforeEach
    void setUp() {

        user = new User(1, "admin", "admin@gmail.com", "+27339923823", "password");

        contactFormOne = new ContactForm(1L, "admin", "admin@gmail.com", "This is my first contact form message", "Urgent help", user);
        contactFormTwo = new ContactForm(2L, "admin", "admin@gmail.com", "This is my second contact form message", "Urgent help", user);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = "USER")
    void testCanCreateContactFormSuccessfully() throws Exception {
        given(contactFormService.createContactForm(any(ContactForm.class), any())).willReturn(contactFormOne);

        RequestBuilder builder = MockMvcRequestBuilders
                .post("/api/contact/send")
                .accept(MediaType.APPLICATION_JSON)
                .content(mapToJson(contactFormOne))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(builder)
                .andExpect(status().isCreated())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo(
                "Your query has been sent successfully. Please wait upto 2 days for a response"
        );
    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = "USER")
    void testCanGetUserContactForms() throws Exception {

        when(contactFormService.getAllByContactFormEmailAddress(anyString()))
                .thenReturn(List.of(contactFormOne, contactFormTwo));

        RequestBuilder builder = MockMvcRequestBuilders
                .get("/api/contact/all")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();


        assertEquals(user.getEmailAddress(), contactFormOne.getContactFormEmailAddress());
        assertEquals(user.getEmailAddress(), contactFormTwo.getContactFormEmailAddress());
        assertEquals(mapToJson(List.of(contactFormOne, contactFormTwo)), response);

    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = "USER")
    void willThrowIfUserHasNoContactForms() throws Exception {

        when(contactFormService.getAllByContactFormEmailAddress(anyString()))
                .thenReturn(Collections.EMPTY_LIST);

        RequestBuilder builder = MockMvcRequestBuilders
                .get("/api/contact/all")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder)
                .andExpect(status().isNotFound())
                .andReturn();

    }
}