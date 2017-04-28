package com.tenPines.builder;

import com.tenPines.model.User;
import com.tenPines.model.Worker;
import com.tenPines.persistence.UserRepository;
import com.tenPines.persistence.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

public class UserFactory {

    public UserRepository userRepository;
    public WorkerRepository workerRepository;

    public UserFactory(UserRepository userRepo, WorkerRepository workerRepo){
        userRepository = userRepo;
        workerRepository = workerRepo;
    }

    public User createUser(){
        Worker worker = new WorkerBuilder().withFullName("Test").withEmail("test@test.com").withBirthDayDate(LocalDate.now().withYear(1999)).build();
        workerRepository.save(worker);
        User user = User.newUser(worker, "Test", "111");
        user = userRepository.save(user);
        return user;
    }

    public static User newUserFromWorker(Worker w){
        return User.newUser(w, w.getFullName(), "1234");
    }
}
