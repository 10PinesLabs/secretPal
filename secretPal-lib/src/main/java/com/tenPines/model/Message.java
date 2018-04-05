package com.tenPines.model;

import com.tenPines.application.MailProperties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Message {

    private String recipient;
    private String body;
    private String subject;


    public Message(String recipient, String subject, String body) {
        this.recipient = recipient;
        this.body = body;
        this.subject = subject;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public javax.mail.Message toJavax(MailProperties mailProperties) throws MessagingException {
        javax.mail.Message javaxMessage = new MimeMessage(authenticatedSession(mailProperties));
        javaxMessage.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(getRecipient()));
        javaxMessage.setSubject(getSubject());
        javaxMessage.setText(getBody());
        return javaxMessage;
    }

    private Session authenticatedSession(MailProperties mailProperties) {
        Properties props = mailProperties.getProperties();
        return Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailProperties.getUser(), mailProperties.getPassword());
                    }
                });
    }

}
