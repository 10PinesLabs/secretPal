package com.tenPines.persistence;

import com.tenPines.application.service.validation.rule.NotCircularRelationRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface NotCircularRelationRuleRepository extends JpaRepository<NotCircularRelationRule, Long> {

    Optional<NotCircularRelationRule> findFirstBy();
}
