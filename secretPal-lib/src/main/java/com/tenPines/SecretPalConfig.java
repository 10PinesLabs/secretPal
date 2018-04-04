package com.tenPines;

import com.tenPines.application.service.AdminService;
import com.tenPines.application.service.UserService;
import com.tenPines.application.service.WorkerService;
import com.tenPines.model.InitializerHerokuSystem;
import com.tenPines.model.InitializerLocalSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SecretPalConfig{

    private final WorkerService workerService;

    private final UserService userService;

    private final AdminService adminService;

    public SecretPalConfig(WorkerService workerService, UserService userService, AdminService adminService) {
        this.workerService = workerService;
        this.userService = userService;
        this.adminService = adminService;
    }

    @Bean
    @Profile("heroku")
    public InitializerHerokuSystem initializeHeroku(){
        return new InitializerHerokuSystem(workerService);
    }
}