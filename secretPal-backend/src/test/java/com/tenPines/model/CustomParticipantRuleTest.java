package com.tenPines.model;

import com.tenPines.builder.WorkerBuilder;
import com.tenPines.model.process.AssignmentException;
import com.tenPines.model.process.RelationEstablisher;

import static com.tenPines.model.process.AssignmentException.Reason.RECEIVER_NULL;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;


public class CustomParticipantRuleTest {

    private Worker participant_A;
    private Worker participant_B;
    private RelationEstablisher relationEstablisher;
    private WorkerBuilder workerBuilder = new WorkerBuilder();

    @Test
    public void When_there_is_no_participants_the_rule_should_give_an_error() {
        relationEstablisher = new RelationEstablisher(participant_A, participant_B);
        try {
            relationEstablisher.createRelation();
        } catch (AssignmentException e) {
            assertThat(e.getReason(), is(RECEIVER_NULL.toString()));
        }
    }

    @Test
    public void When_there_is_1_participant_the_rule_should_give_an_error() {
        participant_A = workerBuilder.build();
        relationEstablisher = new RelationEstablisher(participant_A, participant_B);
        try {
            relationEstablisher.createRelation();
        } catch (AssignmentException e) {
            assertThat(e.getReason(), is(RECEIVER_NULL.toString()));
        }
    }

    @Test
    public void When_there_is_2_participants_and_a_circular_rule() {

    }

}
