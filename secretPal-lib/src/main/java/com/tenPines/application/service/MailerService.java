package com.tenPines.application.service;


import com.tenPines.application.clock.Clock;
import com.tenPines.builder.AssignationMessageBuilder;
import com.tenPines.mailer.PostOffice;
import com.tenPines.mailer.UnsentMessage;
import com.tenPines.model.EmailTemplate;
import com.tenPines.model.FriendRelation;
import com.tenPines.model.Message;
import com.tenPines.persistence.EmailTemplateRepository;
import com.tenPines.persistence.FailedMailsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MailerService {

    private final FailedMailsRepository failedMailsRepository;

    private final EmailTemplateRepository emailTemplateRepository;

    private final PostOffice postOffice;

    private final Clock clock;

    public MailerService(FailedMailsRepository failedMailsRepository, EmailTemplateRepository emailTemplateRepository, PostOffice postOffice, Clock clock) {
        this.failedMailsRepository = failedMailsRepository;
        this.emailTemplateRepository = emailTemplateRepository;
        this.postOffice = postOffice;
        this.clock = clock;
    }

    public EmailTemplate getEMailTemplate() {
        EmailTemplate emailTemplate = new EmailTemplate();
        if (!emailTemplateRepository.findAll().isEmpty())
            emailTemplate = emailTemplateRepository.findAll().get(0);
        return emailTemplate;
    }

    public EmailTemplate setEmailTemplate(EmailTemplate modifiedMail) {
        EmailTemplate mailerProperties = modifiedTemplate(modifiedMail);
        return mailerProperties;
    }

    private EmailTemplate modifiedTemplate(EmailTemplate modifiedMail) {
        EmailTemplate emailActual = getEMailTemplate();
        emailActual.modifiedTemplate(modifiedMail);
        saveTemplate(emailActual);
        return emailActual;
    }

    private void saveTemplate(EmailTemplate emailActual) {
        emailTemplateRepository.save(emailActual);
    }

    public List<UnsentMessage> retrieveAllFailedMails() {
        return failedMailsRepository.findAll();
    }

    public void resendMessageFailure(UnsentMessage unsentMessage) {
        failedMailsRepository.delete(unsentMessage.getId());
        postOffice.sendMessage(unsentMessage.toMessage());
    }

    public Optional<EmailTemplate> getTemplate() {
        return emailTemplateRepository.findFirstBy();
    }

    public void deleteTemplate() {
        emailTemplateRepository.deleteAll();
    }

    public void sendConfirmationMailFor(FriendRelation relation) {
        Message message = new AssignationMessageBuilder(this, clock).buildMessage(relation);
        postOffice.sendMessage(message);
    }
}
