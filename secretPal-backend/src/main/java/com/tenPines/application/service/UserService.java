package com.tenPines.application.service;

import com.tenPines.auth.JWTGenerator;
import com.tenPines.model.User;
import com.tenPines.model.Worker;
import com.tenPines.persistence.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final JWTGenerator jwtGenerator;

    public UserService(UserRepository userRepository, JWTGenerator jwtGenerator) {
        this.userRepository = userRepository;
        this.jwtGenerator = jwtGenerator;
    }

    public User save(User aUser) {
        return userRepository.save(aUser);
    }

    public void deleteByWorker(Worker worker) {
        userRepository.deleteByWorker(worker);
    }

    public Optional<User> findByBackofficeId(Long backofficeId) {
        return userRepository.findByBackofficeId(backofficeId);
    }

    public String getTokenForUser(User user) {
        return jwtGenerator.encodeJWTFromString(user.worker.getId().toString());
    }

    public User getUserFromToken(String token) {
        return userRepository.findOne(Long.parseLong(jwtGenerator.decodeJWTToString(token)));
    }

}
