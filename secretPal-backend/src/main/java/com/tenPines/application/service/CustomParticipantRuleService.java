package com.tenPines.application.service;

import com.tenPines.application.clock.Clock;
import com.tenPines.application.service.validation.rule.*;
import com.tenPines.model.Worker;
import com.tenPines.persistence.CustomParticipantRuleRepository;
import com.tenPines.persistence.NotCircularRelationRuleRepository;
import com.tenPines.persistence.NotTooCloseBirthdayRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomParticipantRuleService {

    @Autowired
    private final Clock clock;
    @Autowired
    private final CustomParticipantRuleRepository customParticipantRuleRepository;
    @Autowired
    private final NotCircularRelationRuleRepository notCircularRelationRuleRepository;
    @Autowired
    private final NotTooCloseBirthdayRuleRepository notTooCloseBirthdayRuleRepository;
    @Autowired
    private final WorkerService workerService;

    public CustomParticipantRuleService(Clock clock, CustomParticipantRuleRepository customParticipantRuleRepository,
                                        NotCircularRelationRuleRepository notCircularRelationRuleRepository,
                                        NotTooCloseBirthdayRuleRepository notTooCloseBirthdayRuleRepository,
                                        WorkerService workerService) {
        this.clock = clock;
        this.customParticipantRuleRepository = customParticipantRuleRepository;
        this.notCircularRelationRuleRepository = notCircularRelationRuleRepository;
        this.notTooCloseBirthdayRuleRepository = notTooCloseBirthdayRuleRepository;
        this.workerService = workerService;
    }

    public CustomParticipantRule create(Worker from, Worker to, Boolean isActive) {
        return customParticipantRuleRepository.save(new CustomParticipantRule(from, to, isActive));
    }

    public void delete(Long id) {
        customParticipantRuleRepository.delete(id);
    }

    public List<CustomParticipantRule> getAllCustomRules() {
        return customParticipantRuleRepository.findAll();
    }

    public List<AssignationRule> getRules() {
        List<AssignationRule> assignationRules = new ArrayList<>();
            assignationRules.add(getCircularRule());
            assignationRules.add(getNotTooCloseBirthdayRule());
            assignationRules.add(new NotTheSamePersonRule());
            assignationRules.add(new BirthdayPassedRule(clock));

        return assignationRules;
    }

    public NotCircularRelationRule getCircularRule() {
        return notCircularRelationRuleRepository.findAll().get(0);
    }

    public NotTooCloseBirthdaysRule getNotTooCloseBirthdayRule() {
        return notTooCloseBirthdayRuleRepository.findAll().get(0);
    }

    public void updateCircularRule(NotCircularRelationRule rule) {
        notCircularRelationRuleRepository.deleteAll();
        notCircularRelationRuleRepository.save(rule);
    }

    public void updateRuleBirthday(NotTooCloseBirthdaysRule rule) {
        notTooCloseBirthdayRuleRepository.deleteAll();
        notTooCloseBirthdayRuleRepository.save(rule);
    }


}