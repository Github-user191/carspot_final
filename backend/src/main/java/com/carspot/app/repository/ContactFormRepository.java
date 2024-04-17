package com.carspot.app.repository;

import com.carspot.app.entity.ContactForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactFormRepository extends CrudRepository<ContactForm, Long> {
    List<ContactForm> getAllByContactFormEmailAddress(String emailAddress);
}
