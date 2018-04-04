package com.tenPines.model;

import com.tenPines.builder.WorkerBuilder;
import com.tenPines.model.process.AssignmentException;
import com.tenPines.model.process.RelationEstablisher;
import org.junit.Before;
import org.junit.Test;

import static com.tenPines.model.process.AssignmentException.Reason.CANT_SELF_ASSIGN;
import static com.tenPines.model.process.AssignmentException.Reason.DOES_NOT_WANT_TO_PARTICIPATE;
import static com.tenPines.model.process.AssignmentException.Reason.RECEIVER_NULL;
import static com.tenPines.model.process.AssignmentException.Reason.GIVER_NULL;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class FriendRelationTest {

    private Worker aWorker;
    private Worker otherWorker;

    @Before
    public void setUp() {
        this.aWorker = new WorkerBuilder().build();
        this.otherWorker = new WorkerBuilder().build();
    }

    @Test
    public void When_I_try_to_create_a_participant_that_does_not_want_to_participate_an_exception_is_raised() {
        aWorker.setWantsToParticipate(false);
        RelationEstablisher relationEstablisher = new RelationEstablisher(aWorker, otherWorker);

        try {
            relationEstablisher.createRelation();
            fail("The exception was not raised");
        } catch (AssignmentException e) {
            assertThat(e.getReason(), is(DOES_NOT_WANT_TO_PARTICIPATE.toString()));
            assertThat(e.getDetails(), hasEntry("worker", aWorker));
        }
    }

    @Test
    public void When_I_try_to_create_a_participant_whose_secretpal_does_not_want_to_participate_an_exception_is_raised() {
        otherWorker.setWantsToParticipate(false);
        RelationEstablisher relationEstablisher = new RelationEstablisher(aWorker, otherWorker);

        try {
            relationEstablisher.createRelation();
            fail("The exception was not raised");
        } catch (AssignmentException e) {
            assertThat(e.getReason(), is(DOES_NOT_WANT_TO_PARTICIPATE.toString()));
            assertThat(e.getDetails(), hasEntry("worker", otherWorker));
        }
    }

    @Test
    public void When_I_try_to_create_a_participant_whose_secretpal_is_him_an_exception_is_raised() {
        RelationEstablisher relationEstablisher = new RelationEstablisher(aWorker, aWorker);

        try {
            relationEstablisher.createRelation();
            fail("The exception was not raised");
        } catch (AssignmentException e) {
            assertThat(e.getReason(), is(CANT_SELF_ASSIGN.toString()));
        }
    }


    @Test
    public void When_I_try_to_relate_two_willing_participants_all_is_ok() {
        RelationEstablisher relationEstablisher = new RelationEstablisher(aWorker, otherWorker);

        FriendRelation relation = relationEstablisher.createRelation();
        assertThat(relation, notNullValue());
    }

    @Test
    public void WhenHavingNullReceiverYouCannotEstablishAFriendRelation() {
        RelationEstablisher relationEstablisher = new RelationEstablisher(aWorker, null);

        try {
            relationEstablisher.createRelation();
            fail("The exception was not raised");
        } catch (AssignmentException e) {
            assertThat(e.getReason(), is(RECEIVER_NULL.toString()));
        }
    }

    @Test
    public void WhenHavingNullGiverYouCannotEstablishAFriendRelation() {
        RelationEstablisher relationEstablisher = new RelationEstablisher(null, otherWorker);

        try {
            relationEstablisher.createRelation();
            fail("The exception was not raised");
        } catch (AssignmentException e) {
            assertThat(e.getReason(), is(GIVER_NULL.toString()));
        }
    }

    @Test
    public void theGifterCanGiveHintsToTheReciever() {
        RelationEstablisher relationEstablisher = new RelationEstablisher(aWorker, otherWorker);

        FriendRelation relation = relationEstablisher.createRelation();
        String pista = "Tengo un cuchillo";
        relation.addHint(pista);

        assertThat(relation.hints(), hasItem(pista));
    }

    @Test
    public void theGifterStartsByGivingNoHintsToTheReciever() {
        RelationEstablisher relationEstablisher = new RelationEstablisher(aWorker, otherWorker);

        FriendRelation relation = relationEstablisher.createRelation();

        assertTrue(relation.hints().isEmpty());
    }

    @Test
    public void theGifterCanNotGiveMoreThan3HintsToTheReciever() {
        RelationEstablisher relationEstablisher = new RelationEstablisher(aWorker, otherWorker);

        FriendRelation relation = relationEstablisher.createRelation();

        relation.addHint("1");
        relation.addHint("2");
        relation.addHint("3");

        try {
            relation.addHint("cuarta pista");
            fail("The exception was not raised");
        } catch (RuntimeException e) {
            assertThat(e.getMessage(), is("Can not have more than 3 hints"));
        }
    }


    @Test
    public void theGifterCanDeleteAHint() {
        RelationEstablisher relationEstablisher = new RelationEstablisher(aWorker, otherWorker);

        FriendRelation relation = relationEstablisher.createRelation();
        relation.addHint("pista");
        relation.addHint("pista2");
        relation.removeHint("pista");

        assertThat(relation.hints(), hasSize(1));
        assertThat(relation.hints(), hasItem("pista2"));
        assertFalse(relation.hints().contains("pista"));
    }

    @Test
    public void theGifterCanEditAHint() {
        RelationEstablisher relationEstablisher = new RelationEstablisher(aWorker, otherWorker);

        FriendRelation relation = relationEstablisher.createRelation();
        relation.addHint("pista");
        relation.editHint("pista", "pista nueva");

        assertThat(relation.hints(), hasSize(1));
        assertThat(relation.hints(), hasItem("pista nueva"));
        assertFalse(relation.hints().contains("pista"));
    }


}
