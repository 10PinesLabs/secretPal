package com.tenPines.model;


import com.tenPines.application.service.UserService;
import com.tenPines.application.service.WorkerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class RegisterService {
    private WorkerService workerService;
    private UserService userService;

    public RegisterService(WorkerService workerService, UserService userService) {
        this.workerService = workerService;
        this.userService = userService;
    }

    public static String messageWhenUserNameHasAlreadyBeenUsed() {
        return "The user name has already been used";
    }

    public static String errorMessageWhenEmailInUse() {
        return "This email is now in use for other user";
    }

    public void registerUser(NewUser aNewUser) {
        validateIfUserNameHasBeenUsed(aNewUser.getUserName());
        validateIfExistAUserWithThisEmail(aNewUser.getEmail());
        Worker worker = workerService.retrieveWorkerByEmail(aNewUser.getEmail()).orElseThrow(() -> new RuntimeException(WorkerService.errorWhenDoNotExistAWorkerWithThisEmail()));
        User user = User.newUser(worker, aNewUser.getUserName(), aNewUser.getPassword());
        userService.save(user);
    }

    private void validateIfExistAUserWithThisEmail(String email) {
        if(emailIsAsociatedWithAUser(email)){
           throw new RuntimeException(RegisterService.errorMessageWhenEmailInUse());
        }
    }

    private boolean emailIsAsociatedWithAUser(String email) {
        List<User> users = userService.getAllUsers();
        Stream<Worker> workers = users.stream().map(user -> user.getWorker());
        return workers.anyMatch(worker -> worker.geteMail().equals(email));
    }

    private void validateIfUserNameHasBeenUsed(String userName) {
        if(!(userService.userNameAvailable(userName))){
            throw new RuntimeException(RegisterService.messageWhenUserNameHasAlreadyBeenUsed());
        }
    }
}
