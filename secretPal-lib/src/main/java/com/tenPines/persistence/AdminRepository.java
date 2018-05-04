package com.tenPines.persistence;

import com.tenPines.model.AdminProfile;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface AdminRepository {
    List<AdminProfile> findAll();
    AdminProfile save(AdminProfile anAdminProfile);
    void deleteAll();
}
