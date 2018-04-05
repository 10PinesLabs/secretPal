package com.tenPines.application.service;

import com.tenPines.model.AdminProfile;
import com.tenPines.persistence.AdminRepository;

import java.util.ArrayList;
import java.util.List;

public class FakeAdminRepository implements AdminRepository {

    List<AdminProfile> adminProfiles = new ArrayList<>();

    @Override
    public List<AdminProfile> findAll() {
        return adminProfiles;
    }

    @Override
    public AdminProfile save(AdminProfile anAdminProfile){
        adminProfiles.add(anAdminProfile);
        return anAdminProfile;
    }

    @Override
    public void deleteAll() {
        adminProfiles.clear();
    }
}
