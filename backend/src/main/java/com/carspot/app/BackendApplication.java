package com.carspot.app;

import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.carspot.app.config.AppProperties;
import com.carspot.app.entity.User;
import com.carspot.app.repository.UserRepository;
import com.carspot.app.service.impl.ErrorValidationServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;


@EnableConfigurationProperties({AppProperties.class})
@SpringBootApplication
//@EnableCaching
public class BackendApplication implements CommandLineRunner {

    @Bean
    public ErrorValidationServiceImpl errorValidationService() {
        return new ErrorValidationServiceImpl();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoderSeed() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private UserRepository userRepository;


    @Override
    public void run(String... args) throws Exception {


        var user1 = userRepository.findById(1L);
        var user2 = userRepository.findById(2L);
        var user3 = userRepository.findById(3L);

        if(user1.isPresent() && user2.isPresent() && user3.isPresent()) {
            System.out.println("IN RUNNER");
            user1.get().setPassword(passwordEncoderSeed().encode("password!"));
            user1.get().setEmailVerified(true);
            user2.get().setPassword(passwordEncoderSeed().encode("password!"));
            user2.get().setEmailVerified(true);
            user3.get().setPassword(passwordEncoderSeed().encode("password!"));
            user3.get().setEmailVerified(true);

            userRepository.saveAll(List.of(user1.get(), user2.get(), user3.get()));
        }


    }

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);

    }
}
