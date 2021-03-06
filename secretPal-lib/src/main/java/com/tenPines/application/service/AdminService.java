package com.tenPines.application.service;

import com.tenPines.model.AdminProfile;
import com.tenPines.model.User;
import com.tenPines.persistence.AdminRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {
    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public User save(User aUser) {
        this.adminRepository.save(AdminProfile.newAdmin(aUser));
        return aUser;
    }

    public List<User> adminUsers() {
        return adminRepository.findAll().stream().map(AdminProfile::getUser).collect(Collectors.toList());
    }

    public boolean isAdmin(User user) {
        return adminUsers().stream().map(u->u.getId()).collect(Collectors.toList()).contains(user.getId());
    }
}
