/*package com.tenPines.persistence;

import com.tenPines.application.service.FriendRelationService;
import com.tenPines.application.service.WorkerService;
import com.tenPines.application.service.validation.rule.NotCircularRelationRule;
import com.tenPines.builder.WorkerBuilder;
import com.tenPines.model.Worker;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class NotCircularRelationRuleTest {

    @Autowired
    private FriendRelationRepository friendRelationRepository;
    @Autowired
    private NotCircularRelationRuleRepository notCircularRelationRuleRepository;
    @Autowired
    private WorkerService workerService;
    @Autowired
    private FriendRelationService friendRelationService = new FriendRelationService(friendRelationRepository, workerService);

    private Worker worker;
    private Worker creator;

    @Before
    public void setUP() {
        worker = new WorkerBuilder().build();
        creator = new WorkerBuilder().build();
        workerService.save(worker);
        workerService.save(creator);
    }
*//*    @Test
    public void When_i_add_a_circular_rule_it_should_be_in_the_repository(){

        NotCircularRelationRule notCircularRelationRule = new NotCircularRelationRule(friendRelationService);
        notCircularRelationRuleRepository.save(notCircularRelationRule);

        assertThat(notCircularRelationRuleRepository.findAll(), hasSize(1));

    }*//*
}*/
