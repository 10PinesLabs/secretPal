package com.tenPines.persistence;

import com.tenPines.model.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Long> {

}