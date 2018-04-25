package com.tenPines.requests;

import com.tenPines.application.SystemPalFacade;
import com.tenPines.application.service.AdminService;
import com.tenPines.application.service.UserService;
import com.tenPines.builder.WorkerBuilder;
import com.tenPines.integration.SpringBaseTest;
import com.tenPines.model.User;
import com.tenPines.model.Worker;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AdminsTest extends SpringBaseTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private SystemPalFacade systemFacade;
    @Autowired
    private UserService userService;
    @Autowired
    private AdminService adminService;
    @LocalServerPort
    private String port;


    @Test
    public void canAddANewAdmin() throws Exception {
        Worker aWorker = new WorkerBuilder().build();
        systemFacade.editWorker(aWorker);
        User aUser = userService.save(new User(aWorker, 1L));
        String url = "http://localhost:" + port + "/api/auth/admin";
        restTemplate.postForObject(url, aWorker, Worker.class);
        assertThat(adminService.isAdmin(aUser), is(true));
    }
}
