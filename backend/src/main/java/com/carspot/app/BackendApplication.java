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

        if(userRepository.findAll().isEmpty()) {
            User user1 = new User(0, "John Doe", "johndoe@gmail.com", "07229933322", passwordEncoderSeed().encode("password!"));
            User user2 = new User(0, "Anne Michael", "annemichael@gmail.com", "06229933322", passwordEncoderSeed().encode("password!"));
            User user3 = new User(0, "Sarah Young", "sarahyoung@gmail.com", "08221933312", passwordEncoderSeed().encode("password!"));

            user1.setEmailVerified(true);
            user2.setEmailVerified(true);
            user3.setEmailVerified(true);

            userRepository.saveAll(List.of(user1,user2,user3));
        }


    }

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);

        String vaultUrl = "https://carspotkv.vault.azure.net/";



    }
}
