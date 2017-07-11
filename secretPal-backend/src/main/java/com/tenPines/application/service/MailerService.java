package com.tenPines.application.service;


import com.tenPines.mailer.PostOffice;
import com.tenPines.mailer.UnsentMessage;
import com.tenPines.model.EmailTemplate;
import com.tenPines.persistence.EmailTemplateRepository;
import com.tenPines.persistence.FailedMailsRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class MailerService {

    private final FailedMailsRepository failedMailsRepository;

    private final EmailTemplateRepository emailTemplateRepository;

    private final PostOffice postOffice;

    public MailerService(FailedMailsRepository failedMailsRepository, EmailTemplateRepository emailTemplateRepository, PostOffice postOffice) {
        this.failedMailsRepository = failedMailsRepository;
        this.emailTemplateRepository = emailTemplateRepository;
        this.postOffice = postOffice;
    }

    public EmailTemplate getEMailTemplate() throws IOException {
        EmailTemplate emailTemplate = new EmailTemplate();
        if (!emailTemplateRepository.findAll().isEmpty())
            emailTemplate = emailTemplateRepository.findAll().get(0);
        return emailTemplate;

    }


    public EmailTemplate setEmailTemplate(EmailTemplate modifiedMail) throws IOException {
        EmailTemplate mailerProperties = ModifiedTemplate(modifiedMail);
        return mailerProperties;
    }

    private EmailTemplate ModifiedTemplate(EmailTemplate modifiedMail) throws IOException {
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
}
