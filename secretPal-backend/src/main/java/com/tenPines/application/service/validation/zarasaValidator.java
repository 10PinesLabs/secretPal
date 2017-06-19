package com.tenPines.application.service.validation;

import com.tenPines.application.service.FriendRelationService;
import com.tenPines.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

//TODO: Eliminar esta clase cuando tenga el validator de Eze.
public class zarasaValidator {

    @Autowired
    public FriendRelationService friendRelationService;
    public List<AsssignationRule> rules;

    public boolean validate(User giver, User receiver) {
        return rules.stream().allMatch(r -> r.validate(giver.getWorker(), receiver.getWorker()));
    }
}
