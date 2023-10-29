package com.carspot.app.service;

import com.carspot.app.entity.ContactForm;

import java.util.List;

public interface ContactFormService {
    List<ContactForm> getAllByContactFormEmailAddress(String emailAddress);
    ContactForm createContactForm(ContactForm contactForm, String emailAddress);
}
