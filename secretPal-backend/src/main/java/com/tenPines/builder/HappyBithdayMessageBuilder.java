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

    private String assignationBodyText() {
        return "Feliz cumpleaños y que seas muy feliz!";
    }

    public Message buildMessage(Worker birthdaysWorker) {
        return new Message(
                mailProperties.getRecipients(),
                assignationSubject(birthdaysWorker),
                assignationBodyText());
    }
}
