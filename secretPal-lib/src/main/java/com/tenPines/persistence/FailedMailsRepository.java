package com.tenPines.persistence;

import com.tenPines.mailer.UnsentMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface FailedMailsRepository extends JpaRepository<UnsentMessage, Long> {

}