//package com.tenPines.model;
//
//import com.tenPines.builder.WorkerBuilder;
//import org.junit.Before;
//import org.junit.Test;
//
//import static org.junit.Assert.*;
//
//
//public class SecretPalEventTest {
//
////    private SecretPalEvent aSecretPalEvent;
//    private FriendRelation aFriendRelation;
//    private Worker aWorker;
//    private Worker otherWorker;
//
//    @Before
//    public void setUp() throws Exception {
//        aSecretPalEvent = new SecretPalEvent();
//        aWorker = new WorkerBuilder().build();
//        aWorker.changeParticipationIntention();
//        otherWorker = new WorkerBuilder().build();
//        otherWorker.changeParticipationIntention();
//        aFriendRelation = new FriendRelation(aWorker, otherWorker);
//    }
//
//    @Test
//    public void When_I_create_a_new_secret_pal_event_it_has_no_participants() {
//        eventHasNoParticipants();
//    }
//
//    @Test
//    public void When_I_add_a_participant_to_a_secret_pal_event_it_has_participants(){
//        aSecretPalEvent.registerParticipant(aFriendRelation);
//
//        eventHasParticipants();
//        amountOfParticipants(1);
//    }
//
//    @Test
//    public void When_I_add_two_participants_the_amount_of_participants_is_two() throws Exception {
//        FriendRelation otherFriendRelation = new FriendRelation(otherWorker, aWorker);
//
//        aSecretPalEvent.registerParticipant(aFriendRelation);
//        aSecretPalEvent.registerParticipant(otherFriendRelation);
//
//        amountOfParticipants(2);
//    }
//
//    private void eventHasParticipants(){
//        assertTrue(aSecretPalEvent.hasAnyParticipant());
//    }
//
//    private void eventHasNoParticipants(){
//        assertFalse(aSecretPalEvent.hasAnyParticipant());
//    }
//
//    private void amountOfParticipants(int amount){
//        assertEquals(aSecretPalEvent.amountOfParticipant(), amount);
//    }
//}
