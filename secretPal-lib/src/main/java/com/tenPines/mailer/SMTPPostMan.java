package com.tenPines.mailer;

import com.tenPines.application.MailProperties;
import com.tenPines.application.SecretPalProperties;
import com.tenPines.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.Transport;

@Component
class SMTPPostMan implements PostMan {

    private final MailProperties mailProperties;

    @Autowired
    public SMTPPostMan(MailProperties mailProperties) {
        this.mailProperties = mailProperties;
    }

    @Override
    public void sendMessage(Message message) {
        try {
            Transport.send(message.toJavax(mailProperties));
        } catch (MessagingException e) {
            throw new UnableToSendMessage(e);
        }
    }


}