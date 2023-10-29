package com.carspot.app.service.impl;

import com.carspot.app.entity.ContactForm;
import com.carspot.app.entity.User;
import com.carspot.app.exception.exceptions.ReviewException;
import com.carspot.app.repository.ContactFormRepository;
import com.carspot.app.repository.UserRepository;
import com.carspot.app.service.ContactFormService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@AllArgsConstructor
@Service
public class ContactFormServiceImpl implements ContactFormService {


    private final UserRepository userRepository;
    private final ContactFormRepository contactFormRepository;


    @Override
    public List<ContactForm> getAllByContactFormEmailAddress(String emailAddress) {

        User user = userRepository.findUserByEmailAddress(emailAddress);

        if(user.getContactForms().isEmpty()) {
            throw new ReviewException("User has not created any contact forms yet.");
        }
        return contactFormRepository.getAllByContactFormEmailAddress(emailAddress);
    }

    @Override
    public ContactForm createContactForm(ContactForm contactForm, String emailAddress) {
        User user = userRepository.findUserByEmailAddress(emailAddress);

        contactForm.setUser(user);
        contactFormRepository.save(contactForm);
        return contactForm;
    }
}
