package com.tenPines.persistence;

import com.tenPines.application.service.validation.rule.NotCircularRelationRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface NotCircularRelationRuleRepository extends JpaRepository<NotCircularRelationRule, Long> {

}
