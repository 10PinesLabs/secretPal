package com.tenPines.application.service;

import com.tenPines.model.User;
import com.tenPines.model.Worker;
import com.tenPines.persistence.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User aUser) {
        return userRepository.save(aUser);
    }

    public User retrieveUserByUserName(String userName) {
        return userRepository.findByUserName(userName).stream().findFirst().orElseThrow(
                () -> new RuntimeException("The user does not exist")
        );
    }

    public boolean userNameAvailable(String userName) {
        return userRepository.findByUserName(userName).isEmpty();
    }

    public boolean validatePassword(String userName, String password) {
        return userRepository.findByUserName(userName).stream().findFirst().get().getPassword().equals(password);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteByWorker(Worker worker) {
        userRepository.deleteByWorker(worker);
    }

    public User retrieveByWorker(Worker worker) {
        return userRepository.findByWorker(worker);
    }
}
