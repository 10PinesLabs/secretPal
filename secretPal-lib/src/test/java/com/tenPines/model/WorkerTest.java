package com.tenPines.model;

import com.tenPines.builder.WorkerBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class WorkerTest {

    private WorkerBuilder workerBuilder;

    @Before
    public void setUp() {
        this.workerBuilder = new WorkerBuilder();
    }

    @Test
    public void When_I_try_to_create_a_person_with_an_invalid_name_an_exception_is_raised() {
        try {
            this.workerBuilder.withFullName(null).build();
            fail("The exception was not raised");
        } catch (Exception e) {
            assertThat(e.getMessage(), is("Full Name is invalid"));
        }
    }

    @Test
    public void When_I_try_to_create_a_person_with_an_empty_name_an_exception_is_raised() {
        try {
            this.workerBuilder.withFullName("").build();
            fail("The exception was not raised");
        } catch (Exception e) {
            assertThat(e.getMessage(), is("Full Name is invalid"));
        }
    }

    @Test
    public void When_I_try_to_create_a_person_with_a_non_char_name_an_exception_is_raised() {
        try {
            this.workerBuilder.withFullName("123$_").build();
            fail("The exception was not raised");
        } catch (Exception e) {
            assertThat(e.getMessage(), is("Full Name is invalid"));
        }
    }


    @Test
    public void When_I_try_to_create_a_person_with_an_invalid_email_an_exception_is_raised() {
        try {
            this.workerBuilder.withEmail("invalidEmail").build();
            fail("The exception was not raised");
        } catch (Exception e) {
            assertThat(e.getMessage(), is("Email is invalid"));
        }
    }

    @Test
    public void When_I_try_to_create_a_person_with_a_last_name_that_contains_a_single_quote_on_it_should_not_raise_an_exception() throws Exception {
        Worker aWorker = workerBuilder.withFullName("Jason O'Connel").build();
        assertEquals(aWorker.getFullName(), "Jason O'Connel");
    }

    @Test
    public void When_I_try_to_create_a_person_he_should_not_want_to_participate_yet() throws Exception {
        Worker aWorker = workerBuilder.whoDoesentWantToParticipate().build();
        assertThat(aWorker.getWantsToParticipate(), is(false));
    }

//    @Test
//    public void when_I_receive_a_gift_I_want_to_mark_the_date_of_receipt(){
//        Worker aWorker = workerBuilder.withFullName("Jason O'Connel").build();
//        aWorker.markGiftAsReceived(),
//        assertThat(aWorker.getGiftDateReceived(),);
//    }
}
