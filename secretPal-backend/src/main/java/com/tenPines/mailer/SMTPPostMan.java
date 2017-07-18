package com.tenPines.mailer;

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

    @Autowired
    public SMTPPostMan(SecretPalProperties properties) {
        this.properties = properties;
    }

    @Override
    public void sendMessage(Message message) {
        try {
            Transport.send(message.toJavax(properties.getMailUser(), properties.getMailUser()));
        } catch (MessagingException e) {
            throw new UnableToSendMessage(e);
        }
    }


}