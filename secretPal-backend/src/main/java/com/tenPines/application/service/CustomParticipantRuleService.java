package com.tenPines.application.service;

import com.tenPines.application.service.validation.rule.CustomParticipantRule;
import com.tenPines.model.FriendRelation;
import com.tenPines.model.Worker;
import com.tenPines.model.process.AssignmentFunction;
import com.tenPines.model.process.RelationEstablisher;
import com.tenPines.persistence.CustomParticipantRuleRepository;
import com.tenPines.persistence.FriendRelationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomParticipantRuleService {
    private CustomParticipantRuleRepository customParticipantRuleRepository;
    private final WorkerService workerService;

    public CustomParticipantRuleService(CustomParticipantRuleRepository customParticipantRuleRepository, WorkerService workerService) {
        this.customParticipantRuleRepository = customParticipantRuleRepository;
        this.workerService = workerService;
    }

    public CustomParticipantRule create(Worker from, Worker to, Boolean isActive) {
        return customParticipantRuleRepository.save(new CustomParticipantRule(from, to, isActive));
    }

    public void delete(Long id) {
        customParticipantRuleRepository.delete(id);
    }

    public List<CustomParticipantRule> getAllRules() {
        return customParticipantRuleRepository.findAll();
    }
}