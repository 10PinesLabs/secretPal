package com.tenPines.persistence;

import com.tenPines.application.service.validation.rule.CustomParticipantRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface CustomParticipantRuleRepository extends JpaRepository<CustomParticipantRule, Long> {

}
