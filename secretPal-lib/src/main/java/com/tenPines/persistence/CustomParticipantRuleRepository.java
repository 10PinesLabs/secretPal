package com.tenPines.persistence;

import com.tenPines.application.service.validation.rule.CustomParticipantRule;
import com.tenPines.model.FriendRelation;
import com.tenPines.model.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface CustomParticipantRuleRepository extends JpaRepository<CustomParticipantRule, Long> {

}
