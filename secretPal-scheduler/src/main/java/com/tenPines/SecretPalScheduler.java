package com.tenPines;

import com.tenPines.application.ReminderSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SecretPalScheduler {
    private static final Logger log = LoggerFactory.getLogger(SecretPalScheduler.class);

    public static void main(String[] args) throws Exception {
        SpringApplication springApplication = new SpringApplication(SecretPalScheduler.class);
        springApplication.setWebEnvironment(false);
        springApplication.run(args).close();
    }

    @Bean
    public CommandLineRunner demo(ReminderSystem system) {
        return (args) -> {
            log.info(String.valueOf(system));
            //system.sendAllReminders();
        };
    }
}