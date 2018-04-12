package com.tenPines.builder;

import com.tenPines.application.MailProperties;
import com.tenPines.model.Message;
import com.tenPines.model.Worker;

public class HappyBithdayMessageBuilder {

    private MailProperties mailProperties;

    public HappyBithdayMessageBuilder(MailProperties mailProperties) {
        this.mailProperties = mailProperties;
    }

    private String assignationSubject(Worker birthdayWorker) {
        return "Feliz cumpleaños " + birthdayWorker.getFullName();
    }

    private String plainAssignationBodyText() {
        return "Feliz cumpleaños y que seas muy feliz!";
    }

    private String htmlAssignationBodyText() {
        return String.join("<div style=\"text-align: center; width: 100%;\">",
                "<h1>" + plainAssignationBodyText() + "</h1>",
                "<img src=\"" + gifURL() + "\"/>",
                "</div>");
    }

    private String gifURL() {
        return "https://media.giphy.com/media/vFKqnCdLPNOKc/giphy.gif";
    }

    public Message buildMessage(Worker birthdaysWorker) {
        return new Message(
                mailProperties.getRecipients(),
                assignationSubject(birthdaysWorker),
                htmlAssignationBodyText(),
                plainAssignationBodyText());
    }
}
