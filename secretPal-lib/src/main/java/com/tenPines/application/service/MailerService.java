package com.tenPines.application.service;

import com.tenPines.mailer.PostOffice;
import com.tenPines.mailer.UnsentMessage;
import com.tenPines.model.FriendRelation;
import com.tenPines.model.Message;
import com.tenPines.persistence.FailedMailsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailerService {

    private final FailedMailsRepository failedMailsRepository;

    private final PostOffice postOffice;

    private final ReminderBuilderService reminderBuilderService;

    public MailerService(FailedMailsRepository failedMailsRepository, PostOffice postOffice, ReminderBuilderService reminderBuilderService) {
        this.failedMailsRepository = failedMailsRepository;
        this.postOffice = postOffice;
        this.reminderBuilderService = reminderBuilderService;
    }

    public List<UnsentMessage> retrieveAllFailedMails() {
        return failedMailsRepository.findAll();
    }

    public void resendMessageFailure(UnsentMessage unsentMessage) {
        failedMailsRepository.delete(unsentMessage.getId());
        postOffice.sendMessage(unsentMessage.toMessage());
    }

    public void sendConfirmationMailFor(FriendRelation relation) {
        Message message = reminderBuilderService.buildAssignationMessage(relation);
        postOffice.sendMessage(message);
    }

}
