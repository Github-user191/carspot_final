package com.carspot.app.service.impl;

import com.carspot.app.entity.ContactForm;
import com.carspot.app.entity.User;
import com.carspot.app.exception.exceptions.ReviewException;
import com.carspot.app.repository.ContactFormRepository;
import com.carspot.app.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContactFormServiceImplTest {

    @InjectMocks
    private ContactFormServiceImpl contactFormService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ContactFormRepository contactFormRepository;

    private ContactForm contactFormOne,contactFormTwo;

    private User user;

    @BeforeEach
    void setUp() {
        contactFormService = new ContactFormServiceImpl(userRepository, contactFormRepository);
        contactFormOne = new ContactForm(1L, "admin", "admin@gmail.com", "This is my first contact form message", "Urgent help", user);
        contactFormTwo = new ContactForm(2L, "admin", "admin@gmail.com", "This is my second contact form message", "Urgent help", user);
        user = new User(1, "admin", "admin@gmail.com", "+27339923823", "password");
        contactFormOne = new ContactForm();
    }

    @AfterEach
    void tearDown() {
    }



    @Test
    void canCreateContactForm() {
        when(contactFormRepository.save(any(ContactForm.class))).thenReturn(contactFormOne);

        contactFormService.createContactForm(contactFormOne, "admin@gmail.com");
        user.setContactForms(List.of(contactFormOne));

        assertThat(user.getContactForms()).isNotNull();
        verify(contactFormRepository).save(any(ContactForm.class));
    }

    @Test
    void canGetAllContactFormsByEmailAddress() {


        when(userRepository.findUserByEmailAddress(anyString())).thenReturn(user);
        user.setContactForms(List.of(contactFormOne, contactFormTwo));
        when(contactFormRepository.getAllByContactFormEmailAddress(anyString())).thenReturn(List.of(contactFormOne, contactFormTwo));

        List<ContactForm> contactFormList = contactFormService.getAllByContactFormEmailAddress("admin@gmail.com");

        assertThat(contactFormList.size()).isGreaterThan(0);
    }


    @Test
    void willThrowIfUserHasNoContactForms() {

        when(userRepository.findUserByEmailAddress(anyString())).thenReturn(user);
        assertThrows(ReviewException.class, () ->  contactFormService.getAllByContactFormEmailAddress("admin@gmail.com"),
                "User has not sent any contact forms yet.");
    }
}