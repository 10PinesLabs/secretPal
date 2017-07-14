package com.tenPines.persistence;

import com.tenPines.application.service.validation.rule.NotTooCloseBirthdaysRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface NotTooCloseBirthdayRuleRepository extends JpaRepository<NotTooCloseBirthdaysRule, Long> {

    Optional<NotTooCloseBirthdaysRule> findFirstBy();

}
