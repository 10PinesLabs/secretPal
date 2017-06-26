package com.tenPines.model;

import com.tenPines.application.service.FriendRelationService;
import com.tenPines.application.service.WorkerService;
import com.tenPines.application.service.validation.FriendRelationValidator;
import com.tenPines.application.service.validation.rule.AssignationRule;
import com.tenPines.application.service.validation.rule.CustomParticipantRule;
import com.tenPines.builder.WorkerBuilder;
import com.tenPines.integration.SpringBaseTest;
import com.tenPines.model.process.AssignmentException;
import com.tenPines.model.process.RelationEstablisher;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.tenPines.model.process.AssignmentException.Reason.RECEIVER_NULL;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


public class CustomParticipantRuleTest extends SpringBaseTest {

    private Worker giver;
    private Worker receiver;
    private RelationEstablisher relationEstablisher;

    @Autowired
    public WorkerService workerService;

    @Autowired
    public FriendRelationService friendRelationService;

    public FriendRelationValidator friendRelationValidator = new FriendRelationValidator(friendRelationService);

    @Test
    public void When_there_is_no_participants_the_rule_should_give_an_error() {
        relationEstablisher = new RelationEstablisher(giver, receiver);
        try {
            relationEstablisher.createRelation();
        } catch (AssignmentException e) {
            assertThat(e.getReason(), is(RECEIVER_NULL.toString()));
        }
    }

    @Test
    public void When_there_is_1_participant_the_rule_should_give_an_error() {
        Worker giver = new WorkerBuilder().build();
        relationEstablisher = new RelationEstablisher(giver, receiver);
        try {
            relationEstablisher.createRelation();
        } catch (AssignmentException e) {
            assertThat(e.getReason(), is(RECEIVER_NULL.toString()));
        }
    }

    @Test
    public void When_there_is_2_participants_and_a_soft_rule() {
        Worker giver = new WorkerBuilder().build();
        Worker receiver = new WorkerBuilder().build();
        workerService.save(giver);
        workerService.save(receiver);

        friendRelationService.create(giver, receiver);
        AssignationRule rule = new CustomParticipantRule(giver, receiver, false);

        assertTrue(rule.validate(giver, receiver));
    }
}
