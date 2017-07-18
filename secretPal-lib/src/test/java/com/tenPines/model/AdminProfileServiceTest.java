package com.tenPines.model;

import com.tenPines.application.service.AdminService;
import com.tenPines.application.service.UserService;
import com.tenPines.application.service.WorkerService;
import com.tenPines.builder.WorkerBuilder;
import com.tenPines.integration.SpringBaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;


public class AdminProfileServiceTest extends SpringBaseTest {

    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;
    @Autowired
    private WorkerService workerService;

    @Test
    public void whenISaveAnAdminItShouldBeSaved(){
        Worker worker = workerService.save(new WorkerBuilder().withFullName("Tasty MacUserton").withEmail("test@test.com").build());
        User user = userService.save(User.newUser(worker, "test", "123456789"));

        adminService.save(userService.retrieveUserByUserName(user.userName));
        assertThat(adminService.findAdminWorker().get(), is(instanceOf(Worker.class)));
    }

    @Test
    public void whenIDoNotSaveAnAdminItShouldNotBeSaved(){
        assertThat(adminService.findAdminWorker().isPresent(), is(false) );
    }
}
