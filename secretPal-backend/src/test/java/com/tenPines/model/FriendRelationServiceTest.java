package com.tenPines.model;

import com.tenPines.application.service.FriendRelationService;
import com.tenPines.builder.UserFactory;
import com.tenPines.integration.SpringBaseTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import com.tenPines.persistence.UserRepository;
import com.tenPines.persistence.WorkerRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class FriendRelationServiceTest extends SpringBaseTest {

    @Autowired
    private FriendRelationService friendRelationService;
    private List<User> users;
    @Autowired
    UserRepository userRepository;
    @Autowired
    WorkerRepository workerRepository;

    @Before
    public void setUp(){
        users = new ArrayList();
    }

    @Test
    public void WhenIShuffleThreeValidUsersItCreatesThreeRelations(){
        addThreeValidUsers();
        assertThat(friendRelationService.shuffle(users)).hasSize(3);
    }

    @Test
    public void WhenIShuffleFourUsersOrderedInAWayItWillBreakItReshuflesAndCreatesAllRelations(){
        addThreeValidUsers();
        users.add(users.get(2));
        assertThat(friendRelationService.shuffle(users)).hasSize(4);
    }

    //SOFT RULES

    @Test
    public void WhenIShuffleThreeValidUsersAndOneInvalidBySameBirthdayRuleItCreatesAllRelations(){
        addThreeValidUsers();
        User forth = (new UserFactory(userRepository, workerRepository)).createUser();
        forth.getWorker().setDateOfBirth(users.get(0).getWorker().getDateOfBirth());
        users.add(forth);
        assertThat(friendRelationService.shuffle(users)).hasSize(4);
    }

    @Test
    public void WhenIShuffleThreeValidUsersAndOneInvalidByCircularRelationRuleItCreatesAllRelations(){
        addThreeValidUsers();
        users.add(users.get(1));
        assertThat(friendRelationService.shuffle(users)).hasSize(4);
    }

    @Test
    public void WhenIShuffleThreeValidUsersAndOneInvalidByNotPreviousToTheLastYearRuleItCreatesAllRelations(){
        addThreeValidUsers();
        FriendRelation friendRelation = friendRelationService.create(users.get(0).getWorker(),users.get(1).getWorker());
        friendRelation.setCreationDate(friendRelation.getCreationDate().withYear(-1));
        friendRelationService.save(friendRelation);
        assertThat(friendRelationService.shuffle(users)).hasSize(3);
    }

    //HARD RULES

    @Test
    public void WhenIShuffleThreeValidUsersAndOneInvalidBySamePersonRuleItThrowsException(){
        User user = (new UserFactory(userRepository, workerRepository)).createUser();
        user.getWorker().setDateOfBirth(user.getWorker().getDateOfBirth().plusWeeks(2).plusDays(1));
        users.add(user);
        users.add(user);
        users.add(user);
        try {
            friendRelationService.shuffle(users);
            fail("Should have thrown an exception");
        } catch (RuntimeException e){
            assertThat(e.getMessage()).isEqualTo("Cannot shuffle those users");
        }
    }

    @Test
    public void WhenIShuffleThreeValidUsersAndOneInvalidByBirthdayTooCloseRuleItThrowsException(){
        addThreeValidUsers();
        User forth = (new UserFactory(userRepository, workerRepository)).createUser();
        forth.getWorker().setDateOfBirth(forth.getWorker().getDateOfBirth().plusWeeks(1));
        users.add(forth);
        try {
            friendRelationService.shuffle(users);
            fail("Should have thrown an exception");
        } catch (RuntimeException e){
            assertThat(e.getMessage()).isEqualTo("Cannot shuffle those users");
        }
    }

    //AUXILIARY METHODS

    private void addThreeValidUsers() {
        User first = (new UserFactory(userRepository, workerRepository)).createUser();
        first.getWorker().setDateOfBirth(first.getWorker().getDateOfBirth().plusWeeks(2).plusDays(1));
        User second = (new UserFactory(userRepository, workerRepository)).createUser();
        second.getWorker().setDateOfBirth(second.getWorker().getDateOfBirth().plusWeeks(2).plusDays(2));
        User third = (new UserFactory(userRepository, workerRepository)).createUser();
        third.getWorker().setDateOfBirth(third.getWorker().getDateOfBirth().plusWeeks(2).plusDays(3));
        users.add(first);
        users.add(second);
        users.add(third);
    }
}

