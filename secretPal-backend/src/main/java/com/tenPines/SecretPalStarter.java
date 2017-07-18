package com.tenPines;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class SecretPalStarter extends SpringBootServletInitializer {

    public static void main(String[] args) throws Exception {
        SpringApplication springApplication = new SpringApplication(SecretPalStarter.class);
        springApplication.setWebEnvironment(false);
        springApplication.run(args);
    }
}