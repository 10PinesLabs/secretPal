package com.tenPines.mailer;

import com.tenPines.model.Message;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.Transport;

@Component
@Profile("!test")
class SMTPPostMan implements PostMan {
    @Override
    public void sendMessage(Message message) {
        try {
            Transport.send(message.toJavax());
        } catch (MessagingException e) {
            throw new UnableToSendMessage(e);
        }
    }


}