package com.tenPines.model;

import com.tenPines.application.service.FriendRelationService;
import com.tenPines.application.service.validator.FriendRelationValidator;
import com.tenPines.builder.UserFactory;
import com.tenPines.integration.SpringBaseTest;
import com.tenPines.persistence.UserRepository;
import com.tenPines.persistence.WorkerRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class FriendRelationValidatorTests extends SpringBaseTest{

    public FriendRelationValidator friendRelationValidator;
    @Autowired
    public FriendRelationService friendRelationService;
    @Autowired
    public UserRepository userRepository;
    @Autowired
    public WorkerRepository workerRepository;

    @Before
    public void setUp(){
        friendRelationValidator = new FriendRelationValidator(friendRelationService);
    }

    @Test
    public void whenAUserIsHimselfItShouldNotBeValid(){
       User user = (new UserFactory(userRepository, workerRepository)).createUser();
       assertThat(friendRelationValidator.validate(user,user)).isEqualTo(false);
    }

    @Test
    public void whenTheyWereBornTheSameDayItShouldNotBeValid(){
        User giver = (new UserFactory(userRepository, workerRepository)).createUser();
        User receiver = (new UserFactory(userRepository, workerRepository)).createUser();

        assertThat(friendRelationValidator.validate(giver,receiver)).isEqualTo(false);
    }

    @Test
    public void whenTheReceiverWillGiftMeItShouldNotBeValid(){
        User giver = (new UserFactory(userRepository, workerRepository)).createUser();
        giver.getWorker().setDateOfBirth(giver.getWorker().getDateOfBirth().plusWeeks(2).plusDays(1));
        workerRepository.save(giver.getWorker());
        User receiver = (new UserFactory(userRepository, workerRepository)).createUser();
        friendRelationService.create(giver.getWorker(), receiver.getWorker());
        assertThat(friendRelationValidator.validate(receiver,giver)).isEqualTo(false);
    }

    @Test
    public void whenTheReceiversBirthdayIsTooClosetItShouldNotBeValid(){
        User giver = (new UserFactory(userRepository, workerRepository)).createUser();
        User receiver = (new UserFactory(userRepository, workerRepository)).createUser();
        receiver.getWorker().setDateOfBirth(receiver.getWorker().getDateOfBirth().withYear(1990));
        assertThat(friendRelationValidator.validate(giver,receiver)).isEqualTo(false);
    }

    @Test
    public void whenTheReceiversBirthdayIsNotTooClosetItShouldBeValid(){
        User giver = (new UserFactory(userRepository, workerRepository)).createUser();
        User receiver = (new UserFactory(userRepository, workerRepository)).createUser();
        receiver.getWorker().setDateOfBirth(receiver.getWorker().getDateOfBirth().plusWeeks(2).plusDays(1));
        assertThat(friendRelationValidator.validate(giver,receiver)).isEqualTo(true);
    }

    @Test
    public void whenTheReceiverIsNotPreviousToTheLastYearItShouldNotBeValid(){
        User giver = (new UserFactory(userRepository, workerRepository)).createUser();
        User receiver = (new UserFactory(userRepository, workerRepository)).createUser();
        receiver.getWorker().setDateOfBirth(receiver.getWorker().getDateOfBirth().plusWeeks(2).plusDays(1));
        friendRelationService.create(giver.getWorker(), receiver.getWorker());
        FriendRelation friendRelation = friendRelationService.getByWorkerReceiver(receiver.getWorker());
        friendRelation.setCreationDate(friendRelation.getCreationDate().plusYears(-1));
        assertThat(friendRelationValidator.validate(giver,receiver)).isEqualTo(false);
    }

    @Test
    public void whenTheReceiverIsPreviousTheLastYearItShouldBeValid(){
        User giver = (new UserFactory(userRepository, workerRepository)).createUser();
        User receiver = (new UserFactory(userRepository, workerRepository)).createUser();
        receiver.getWorker().setDateOfBirth(receiver.getWorker().getDateOfBirth().plusWeeks(2).plusDays(1));
        friendRelationService.create(giver.getWorker(), receiver.getWorker());
        FriendRelation friendRelation = friendRelationService.getByWorkerReceiver(receiver.getWorker());
        friendRelation.setCreationDate(friendRelation.getCreationDate().plusYears(-2));
        friendRelationService.save(friendRelation);
        assertThat(friendRelationValidator.validate(giver,receiver)).isEqualTo(true);
    }
}
