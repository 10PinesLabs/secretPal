package com.tenPines.application.service;

import com.tenPines.builder.ReminderBuilder;
import com.tenPines.model.FriendRelation;
import com.tenPines.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReminderBuilderService {

    @Autowired
    ReplacerService replacerService;

    public Message buildAssignationMessage(FriendRelation friendRelation) {
        return new ReminderBuilder("assignation", replacerService).buildMessage(friendRelation);
    }

    public Message buildTwoWeekReminderMessage(FriendRelation friendRelation) {
        return new ReminderBuilder("two-week-reminder", replacerService).buildMessage(friendRelation);
    }

}
