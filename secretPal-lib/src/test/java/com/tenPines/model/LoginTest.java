package com.tenPines.model;


import com.tenPines.application.service.UserService;
import com.tenPines.application.service.WorkerService;
import com.tenPines.integration.SpringBaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class LoginTest extends SpringBaseTest{

    @Autowired
    Patova securityGuard;

    @Autowired
    UserService userService;

    @Autowired
    WorkerService workerService;

    public void setUp(UserService userService, WorkerService workerService) {
        Worker kevin = new Worker("kevin", "kevin.varela@10pines.com", LocalDate.of(1993, 4, 6), true);
        User userKevin = User.newUser(kevin, "kevin", "1234");
        workerService.save(kevin);
        userService.save(userKevin);
    }

    @Test
    public void whenILoginInTheSystemShouldReturnATokenWithMyUserName(){
        setUp(userService, workerService);
        Credential credential = Credential.newCredential("kevin","1234");

        assertThat(securityGuard.enterWith(credential)).isEqualTo(credential.getUserName());
    }

    @Test
    public void withATokenRetriveCorrectUserAndUserWithoutPassword(){
        setUp(userService, workerService);
        Credential credential = Credential.newCredential("kevin","1234");
        String token = securityGuard.enterWith(credential);

        assertTrue(userService.retrieveUserByUserName(token).getUserName().equals("kevin"));
        assertTrue(userService.retrieveUserByUserName(token).getPassword().equals("1234"));
    }
}
