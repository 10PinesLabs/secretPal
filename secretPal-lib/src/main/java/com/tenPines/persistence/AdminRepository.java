package com.tenPines.persistence;

import com.tenPines.model.AdminProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public interface AdminRepository extends JpaRepository<AdminProfile, Long> {
}
