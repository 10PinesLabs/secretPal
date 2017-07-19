package com.tenPines.mailer;

import com.tenPines.application.MailProperties;
import com.tenPines.application.SecretPalProperties;
import com.tenPines.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.Transport;

@Component
@Profile("!test")
class SMTPPostMan implements PostMan {

    private final SecretPalProperties properties;
    private final MailProperties mailProperties;

    @Autowired
    public SMTPPostMan(SecretPalProperties properties, MailProperties mailProperties) {
        this.properties = properties;
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