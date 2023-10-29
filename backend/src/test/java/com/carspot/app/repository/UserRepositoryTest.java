package com.carspot.app.repository;

import com.carspot.app.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@DataJpaTest
class UserRepositoryTest {


    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }


    @Test
    void canFindUserThatIsVerifiedByEmailAddress() {
        String emailAddress = "admin@gmail.com";

        User user = new User(1, "admin", emailAddress, "+27678130371", "password");
        user.setEmailVerified(true);
        userRepository.save(user);

        boolean isValid = userRepository.findEmailVerifiedByEmailAddress(emailAddress);

        assertTrue(isValid);
    }

    @Test
    void cannotFindUserThatIsVerifiedByEmailAddress() {
        String emailAddress = "admin@gmail.com";

        User user = new User(1, "admin", emailAddress, "+27678130371", "password");
        user.setEmailVerified(false);
        userRepository.save(user);

        boolean isValid = userRepository.findEmailVerifiedByEmailAddress(emailAddress);

        assertFalse(isValid);
    }
}