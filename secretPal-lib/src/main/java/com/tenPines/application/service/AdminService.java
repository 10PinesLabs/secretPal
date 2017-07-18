package com.tenPines.application.service;

import com.tenPines.model.AdminProfile;
import com.tenPines.model.User;
import com.tenPines.model.Worker;
import com.tenPines.persistence.AdminRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final WorkerService workerService;

    public AdminService(WorkerService workerService, AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
        this.workerService = workerService;
    }

    public User save(User aUser) {
        this.adminRepository.save(AdminProfile.newAdmin(aUser));
        return aUser;
    }

    public Optional<Worker> findAdminWorker(){
        return workerService.retrieveWorkerByEmail(findAdminUser().orElse(nullUser()).geteMail());
    }

    public Optional<User> findAdminUser() {
        Optional<AdminProfile> adminProfile = adminRepository.findAll().stream().findFirst();
        return adminProfile.map(a->a.user);
    }

    public User nullUser(){
        return User.newUser(new Worker("Testy MacUserton", "someone@mail.com", LocalDate.now(),true),"","");
    }

    public boolean isAdmin(User user) {
        return findAdminUser().equals(Optional.of(user));
    }
}
