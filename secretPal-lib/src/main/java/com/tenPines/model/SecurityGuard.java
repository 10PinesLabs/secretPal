package com.tenPines.model;


import com.tenPines.application.service.UserService;
import org.springframework.stereotype.Service;


@Service
public class SecurityGuard implements Patova {

    private final UserService userService;

    public SecurityGuard(UserService userService) {
        this.userService = userService;
    }

    static String errorMessageWhenUserOrPasswordIsInvalid() {
        return "The user do not exist or password is invalid";
    }

    @Override
    public String enterWith(Credential aCredential){
        if(notIsAValidUser(aCredential.getUserName()) || notIsAValidPasswordForThisUser(aCredential)){
            throw new RuntimeException(errorMessageWhenUserOrPasswordIsInvalid());
        }
        return aCredential.getUserName();
    }

    private boolean notIsAValidUser(String aUserName) {
        return userService.userNameAvailable(aUserName);
    }

    private boolean notIsAValidPasswordForThisUser(Credential aCredential) {
        return !(userService.validatePassword(aCredential.getUserName(),aCredential.getPassword()));
    }

}
