package com.tenPines.model;

import com.tenPines.builder.WorkerBuilder;
import com.tenPines.model.process.AssignmentException;
import com.tenPines.model.process.RelationEstablisher;
import org.junit.Before;
import org.junit.Test;

import static com.tenPines.model.process.AssignmentException.Reason.*;
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
        Hint tengo_un_cuchillo = new Hint("Tengo un cuchillo");
        relation.addHint(tengo_un_cuchillo);

        assertThat(relation.getHints(), hasItem(tengo_un_cuchillo));
    }

    @Test
    public void theGifterStartsByGivingNoHintsToTheReciever() {
        RelationEstablisher relationEstablisher = new RelationEstablisher(aWorker, otherWorker);

        FriendRelation relation = relationEstablisher.createRelation();

        assertTrue(relation.getHints().isEmpty());
    }

    @Test
    public void theGifterCanNotGiveMoreThan3HintsToTheReciever() {
        RelationEstablisher relationEstablisher = new RelationEstablisher(aWorker, otherWorker);

        FriendRelation relation = relationEstablisher.createRelation();

        relation.addHint(new Hint("1"));
        relation.addHint(new Hint("2"));
        relation.addHint(new Hint("3"));

        try {
            relation.addHint(new Hint("cuarta pista"));
            fail("The exception was not raised");
        } catch (RuntimeException e) {
            assertThat(e.getMessage(), is("Can not have more than 3 hints"));
        }
    }


    @Test
    public void theGifterCanDeleteAHint() {
        RelationEstablisher relationEstablisher = new RelationEstablisher(aWorker, otherWorker);

        FriendRelation relation = relationEstablisher.createRelation();
        Hint pista = new Hint("pista");
        relation.addHint(pista);
        Hint pista2 = new Hint("pista2");
        relation.addHint(pista2);
        relation.removeHint(pista);

        assertThat(relation.getHints(), hasSize(1));
        assertThat(relation.getHints(), hasItem(pista2));
        assertFalse(relation.getHints().contains(pista));
    }

    @Test
    public void theGifterCanEditAHint() {
        RelationEstablisher relationEstablisher = new RelationEstablisher(aWorker, otherWorker);

        FriendRelation relation = relationEstablisher.createRelation();
        Hint pista = new Hint("pista");
        relation.addHint(pista);
        Hint pista_nueva = new Hint("pista nueva");
        relation.editHint(pista, pista_nueva);

        assertThat(relation.getHints(), hasSize(1));
        assertThat(relation.getHints(), hasItem(pista_nueva));
        assertThat(relation.getHints(), not(hasItem(pista)));
    }

    @Test
    public void aNewRelationHas3RemainingGuessAttempts(){
        RelationEstablisher relationEstablisher = new RelationEstablisher(aWorker, otherWorker);
        assertThat(relationEstablisher.createRelation().getAmountOfGuessAttempts(), is(0));
    }

    @Test
    public void aNewRelationHasNotBeenGuessed(){
        RelationEstablisher relationEstablisher = new RelationEstablisher(aWorker, otherWorker);
        assertThat(relationEstablisher.createRelation().isGuessed(), is(false));
    }

    @Test
    public void aRelationIsGuessedIfTheGivenNameIsTheSameAsTheGiftGiverFullName(){
        RelationEstablisher relationEstablisher = new RelationEstablisher(aWorker, otherWorker);
        FriendRelation relation = relationEstablisher.createRelation();

        relation.guessGiftGiver(aWorker.getFullName());

        assertThat(relation.isGuessed(), is(true));
    }

    @Test
    public void whenTheGivenNameIsDifferentFromTheGiftGiverFullNameAGuessAttemptIsLost(){
        RelationEstablisher relationEstablisher = new RelationEstablisher(aWorker, otherWorker);
        FriendRelation relation = relationEstablisher.createRelation();

        relation.guessGiftGiver("Some Incorrect Full Name");

        assertThat(relation.getAmountOfGuessAttempts(), is(1));
    }

    @Test
    public void theGifterCanNotGuessAnymoreAfter3FailedAttempts() {
        RelationEstablisher relationEstablisher = new RelationEstablisher(aWorker, otherWorker);
        FriendRelation relation = relationEstablisher.createRelation();

        relation.guessGiftGiver("Some Incorrect Full Name");
        relation.guessGiftGiver("Some Incorrect Full Name");
        relation.guessGiftGiver("Some Incorrect Full Name");

        try {
            relation.guessGiftGiver(aWorker.getFullName());
            fail("The exception was not raised");
        } catch (RuntimeException e) {
            assertThat(relation.getAmountOfGuessAttempts(), is(3));
            assertThat(e.getMessage(), is("Can not have more than 3 failed guess attempts"));
        }
    }

    @Test
    public void theGifterCanNotGuessIfTheGiftGiverHasAlreadyBeenGuessed() {
        RelationEstablisher relationEstablisher = new RelationEstablisher(aWorker, otherWorker);
        FriendRelation relation = relationEstablisher.createRelation();

        relation.guessGiftGiver(aWorker.getFullName());

        try {
            relation.guessGiftGiver(aWorker.getFullName());
            fail("The exception was not raised");
        } catch (RuntimeException e) {
            assertThat(e.getMessage(), is("The gift giver was already guessed"));
        }
    }

    @Test
    public void theGuessesAreRecorded(){
        RelationEstablisher relationEstablisher = new RelationEstablisher(aWorker, otherWorker);
        FriendRelation relation = relationEstablisher.createRelation();

        String anotherWorker = new WorkerBuilder().build().getFullName();
        String oneMoreWorker = new WorkerBuilder().build().getFullName();
        relation.guessGiftGiver(anotherWorker);
        relation.guessGiftGiver(oneMoreWorker);
        assertThat(relation.getGuessAttempts(), hasItem(anotherWorker));
        assertThat(relation.getGuessAttempts(), hasItem(oneMoreWorker));
        assertThat(relation.getAmountOfGuessAttempts(), is(2));
    }

    @Test
    public void aNewRelationIsNotImmutableByDefault(){
        RelationEstablisher relationEstablisher = new RelationEstablisher(aWorker, otherWorker);
        FriendRelation relation = relationEstablisher.createRelation();

        assertThat(relation.isImmutable(), is(false));
    }

    @Test
    public void aRelationCanBeMadeImmutable(){
        RelationEstablisher relationEstablisher = new RelationEstablisher(aWorker, otherWorker);
        FriendRelation relation = relationEstablisher.createRelation();

        relation.makeImmutable();

        assertThat(relation.isImmutable(), is(true));
    }

}
