package com.tenPines.model;

import com.tenPines.application.MailProperties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class Message {

    private String recipient;
    private String subject;
    private String plainTextBody;
    private String htmlBody;

    public Message(String recipient, String subject, String htmlBody, String plainTextBody) {
        this.recipient = recipient;
        this.subject = subject;
        this.htmlBody = htmlBody;
        this.plainTextBody = plainTextBody;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getSubject() {
        return subject;
    }

    public String getPlainTextBody() {
        return plainTextBody;
    }

    public String getHtmlBody() {
        return htmlBody;
    }

    public javax.mail.Message toJavax(MailProperties mailProperties) throws MessagingException {
        javax.mail.Message javaxMessage = new MimeMessage(authenticatedSession(mailProperties));
        javaxMessage.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(getRecipient()));
        javaxMessage.setSubject(getSubject());

        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(plainTextBody, "utf-8");
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(htmlBody, "text/html; charset=utf-8");

        MimeMultipart multiPart = new MimeMultipart("alternative");
        multiPart.addBodyPart(textPart);
        multiPart.addBodyPart(htmlPart);
        javaxMessage.setContent(multiPart);
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
