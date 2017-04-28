package com.tenPines.application.service.validator;

import com.tenPines.application.service.FriendRelationService;
import com.tenPines.application.service.validator.rule.*;
import com.tenPines.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FriendRelationValidator {

    @Autowired
    public FriendRelationService friendRelationService;
    public List<ValidFriendRelationRule> rules;

    public FriendRelationValidator(FriendRelationService friendRelationSv){
        friendRelationService = friendRelationSv;
        rules = new ArrayList<>();
        rules.add(new NotTheSamePersonRule());
        rules.add(new NotCircularRelationRule(friendRelationService));
        rules.add(new NotTheSameBirthdayRule());
        rules.add(new BirthdayIsNotTooCloseRule());
        rules.add(new NotPreviousToTheLastYearRule(friendRelationService));
    }

    public boolean validate(User giver, User receiver) {
        return rules.stream().allMatch(r -> r.evaluate(giver,receiver));
    }

    public boolean validateAll(List<User> validUsers){
        return validUsers.stream().allMatch(u -> validate(u, getNextUser(validUsers, u)));
    }

    private User getNextUser(List<User> validUsers, User u) {
        return validUsers.get(getIndexOfNextUser(validUsers, u));
    }

    private int getIndexOfNextUser(List<User> validUsers, User u) {
        return (validUsers.indexOf(u)+1) % validUsers.size();
    }

    public boolean validateHardRules(List<User> validUsers){
        deleteSoftRules();
        return validateAll(validUsers);
    }

    public void deleteSoftRules(){
        rules = this.rules.stream().filter(r -> !r.softRule()).collect(Collectors.toList());
    }
}
