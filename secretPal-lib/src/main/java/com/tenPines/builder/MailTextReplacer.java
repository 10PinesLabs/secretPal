package com.tenPines.builder;

import com.tenPines.model.FriendRelation;

import java.util.function.Function;

public class MailTextReplacer {
    private String variableToReplace;
    private Function<FriendRelation, String> replacer;

    public MailTextReplacer(String variableToReplace, Function<FriendRelation, String> replacer) {
        this.variableToReplace = variableToReplace;
        this.replacer = replacer;
    }

    public String replaceFor(FriendRelation friendRelation) {
        return replacer.apply(friendRelation);
    }

    public boolean canReplace(String pattern) {
        return this.variableToReplace.equals(pattern);
    }
}
