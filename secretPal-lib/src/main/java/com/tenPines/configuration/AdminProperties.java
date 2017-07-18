package com.tenPines.configuration;

import com.tenPines.application.service.AdminService;
import com.tenPines.model.Worker;

import java.util.Optional;

public class AdminProperties {

    public Optional<Worker> findAdminWorker(AdminService adminService) {
        return adminService.findAdminWorker();
    }

}
