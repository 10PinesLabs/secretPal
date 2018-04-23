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

    private AdminService adminService;

    public UserService(UserRepository userRepository, JWTGenerator jwtGenerator, AdminService adminService) {
        this.userRepository = userRepository;
        this.jwtGenerator = jwtGenerator;
        this.adminService = adminService;
    }

    public User save(User aUser) {
        return userRepository.save(aUser);
    }

    public void deleteByWorker(Worker worker) {
        assertIsNotAdmin(worker);
        userRepository.deleteByWorker(worker);
    }

    private void assertIsNotAdmin(Worker worker) {
        if(adminService.isAdmin(findByWorker(worker))){
            throw new RuntimeException("No se puede borrar a un administrador");
        }
    }

    public Optional<User> findByBackofficeId(Long backofficeId) {
        return userRepository.findByBackofficeId(backofficeId);
    }

    public User findByWorker(Worker worker) {
        return userRepository.findByWorker(worker);
    }

    public String getTokenForUser(User user) {
        return jwtGenerator.encodeJWTFromString(user.getId().toString());
    }

    public User getUserFromToken(String token) {
        return userRepository.findOne(Long.parseLong(jwtGenerator.decodeJWTToString(token)));
    }

}
