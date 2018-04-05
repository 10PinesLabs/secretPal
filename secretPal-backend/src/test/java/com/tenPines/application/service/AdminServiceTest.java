package com.tenPines.application.service;

import com.tenPines.builder.WorkerBuilder;
import com.tenPines.integration.SpringBaseTest;
import com.tenPines.model.User;
import com.tenPines.persistence.AdminRepository;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class AdminServiceTest extends SpringBaseTest {

    private AdminRepository fakeAdminRepository;
    private AdminService adminService;
    private User anUser;

    @Before
    public void setUp(){
        anUser = new User(new WorkerBuilder().build(), null);
        fakeAdminRepository = new FakeAdminRepository();
        adminService = new AdminService(fakeAdminRepository);
    }

    @Test
    public void when_an_admin_service_is_created_it_has_no_admins(){
        assertThat(adminService.adminUsers(), empty());
    }

    @Test
    public void when_an_user_is_saved_it_is_added_to_the_admin_list(){
        adminService.save(anUser);
        assertThat(adminService.adminUsers(), hasItem(anUser));
    }

    @Test
    public void when_an_user_is_saved_it_becomes_an_admin(){
        adminService.save(anUser);
        assertTrue(adminService.isAdmin(anUser));
    }

    @Test
    public void when_an_user_is_not_saved_it_is_not_an_admin(){
        assertThat(adminService.isAdmin(anUser), is(false));
    }

    @Test
    public void when_more_than_one_user_are_saved_they_are_all_admins(){
        User anotherUser = new User(new WorkerBuilder().withFullName("Hans Landa").build(), null);
        adminService.save(anUser);
        adminService.save(anotherUser);
        assertTrue(adminService.isAdmin(anUser));
        assertTrue(adminService.isAdmin(anotherUser));
    }
}
