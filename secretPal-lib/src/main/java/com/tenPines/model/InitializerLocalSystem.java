package com.tenPines.model;


import com.tenPines.application.service.AdminService;
import com.tenPines.application.service.UserService;
import com.tenPines.application.service.WorkerService;

import java.time.LocalDate;

public class InitializerLocalSystem {
    public Worker kevin = new Worker("kevin", "kevin.varela@10pines.com", LocalDate.of(1993,11,26), true);
    public Worker aye = new Worker("aye", "ayelen.garcia@10pines.com", LocalDate.of(1992,12,26), true);
    public Worker joaco = new Worker("joaco", "joaquin.azcarate@10pines.com", LocalDate.of(1990,2,3), true);
    public User userKevin = User.newUser(kevin,"kevin","1234");
    private WorkerService workerService;
    private UserService userService;
    private AdminService adminService;

    public InitializerLocalSystem(WorkerService aWorkerService, UserService aUserService, AdminService adminService) {
        this.workerService = aWorkerService;
        this.userService = aUserService;
        this.adminService = adminService;
        this.init();
    }

    void init() {
        workerService.removeAll();
        workerService.save(kevin);
        workerService.save(aye);
        workerService.save(joaco);
        userService.save(userKevin);
        adminService.save(userKevin);
    }
}
