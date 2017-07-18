package com.tenPines.model;


import com.tenPines.application.service.AdminService;
import com.tenPines.application.service.UserService;
import com.tenPines.application.service.WorkerService;
import com.tenPines.configuration.AdminProperties;
import com.tenPines.integration.SpringBaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

public class InitializerTest extends SpringBaseTest {

    @Autowired
    private WorkerService workerService;
    @Autowired
    private UserService userService;
    @Autowired
    private AdminService adminService;


    @Test
    public void when_i_initialize_the_system_the_list_of_workers_must_be_charged(){
        InitializerLocalSystem initializerLocalSystem = new InitializerLocalSystem(workerService, userService, adminService);

        assertThat(workerService.getAllParticipants(), is(not(empty())));
    }

    @Test
    public void when_initialize_the_system_twice_the_list_of_workers_must_be_charged_once() throws IOException {
        InitializerLocalSystem initializerLocalSystem = new InitializerLocalSystem(workerService, userService, adminService);

        assertThat(new AdminProperties().findAdminWorker(adminService), is(not(nullValue())));
    }

}
