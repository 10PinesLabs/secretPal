package com.tenPines.persistence;

import com.tenPines.application.service.validation.rule.NotTooCloseBirthdaysRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface NotTooCloseBirthdayRuleRepository extends JpaRepository<NotTooCloseBirthdaysRule, Long> {

}
