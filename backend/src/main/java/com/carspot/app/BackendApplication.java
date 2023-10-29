package com.carspot.app;

import com.carspot.app.config.AppProperties;
import com.carspot.app.service.impl.ErrorValidationServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;


@EnableConfigurationProperties({AppProperties.class})
@SpringBootApplication
//@EnableCaching
public class BackendApplication{

    @Bean
    public ErrorValidationServiceImpl errorValidationService() {
        return new ErrorValidationServiceImpl();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);



    }
}
