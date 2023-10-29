package com.carspot.app.controller;

import com.carspot.app.entity.ContactForm;
import com.carspot.app.payload.response.ApiResponse;
import com.carspot.app.service.ContactFormService;
import com.carspot.app.service.EmailService;
import com.carspot.app.service.impl.EmailServiceImpl;
import com.carspot.app.service.impl.ErrorValidationServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/contact")

public class ContactFormController {


    private final ContactFormService contactFormService;
    private final ErrorValidationServiceImpl errorValidationService;

    public ContactFormController(ContactFormService contactFormService, ErrorValidationServiceImpl errorValidationService) {
        this.contactFormService = contactFormService;
        this.errorValidationService = errorValidationService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> createContactForm(@Valid @RequestBody ContactForm contactForm, BindingResult result, Principal principal) {

        ResponseEntity<?> errorMap = errorValidationService.validationService(result);
        if(errorMap != null) return errorMap;

        contactFormService.createContactForm(contactForm, principal.getName());


        return new ResponseEntity<>("Your query has been sent successfully. Please wait upto 2 days for a response",
                HttpStatus.CREATED);
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAllContactFormsByEmailAddress(Principal principal) {
        List<ContactForm> contactFormList =  contactFormService.getAllByContactFormEmailAddress(principal.getName());

        if(contactFormList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>( contactFormList, HttpStatus.OK);
        }

    }
}
