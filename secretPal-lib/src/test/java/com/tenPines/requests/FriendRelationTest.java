package com.tenPines.requests;

import com.tenPines.application.SystemPalFacade;
import com.tenPines.builder.WorkerBuilder;
import com.tenPines.integration.SpringBaseTest;
import com.tenPines.model.Hint;
import com.tenPines.model.Worker;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;


public class FriendRelationTest extends SpringBaseTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private SystemPalFacade systemFacade;
    @LocalServerPort
    private String port;


    @Test
    public void whenAddingANewHintItReturnsTheHintWithId() throws Exception {
        Worker gifter = new WorkerBuilder().build();
        Worker receiver = new WorkerBuilder().build();
        systemFacade.editWorker(gifter);
        systemFacade.editWorker(receiver);
        systemFacade.createRelation(gifter, receiver);


        String url = "http://localhost:" + port + "/api/friendRelation/hintsFrom/" + gifter.getId();
        Hint hint = restTemplate.postForObject(url, new Hint("Pista"), Hint.class);
        assertThat(hint.getId(), notNullValue());
    }
}
