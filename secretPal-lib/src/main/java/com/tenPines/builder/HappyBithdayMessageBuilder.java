package com.tenPines.builder;

import com.tenPines.application.MailProperties;
import com.tenPines.application.service.DefaultGifService;
import com.tenPines.model.Message;
import com.tenPines.model.Worker;

public class HappyBithdayMessageBuilder {

    private DefaultGifService defaultGifService;
    private MailProperties mailProperties;

    public HappyBithdayMessageBuilder(MailProperties mailProperties, DefaultGifService defaultGifService) {
        this.mailProperties = mailProperties;
        this.defaultGifService = defaultGifService;
    }

    private String assignationSubject(Worker birthdayWorker) {
        return "Feliz cumpleaños " + birthdayWorker.getName();
    }

    private String plainAssignationBodyText() {
        return "Feliz cumpleaños y que seas muy feliz!";
    }

    private String htmlAssignationBodyText(Worker birthdayWorker) {
        return String.join("<div style=\"text-align: center; width: 100%;\">",
                "<h1>" + plainAssignationBodyText() + "</h1>",
                "<img src=\"" + gifUrlFor(birthdayWorker) + "\"/>",
                "</div>");
    }

    private String gifUrlFor(Worker birthdayWorker) {
        return birthdayWorker.getGifUrl().orElse(defaultGifService.get().getUrl());
    }

    public Message buildMessage(Worker birthdayWorker) {
        return new Message(
                mailProperties.getRecipients(),
                assignationSubject(birthdayWorker),
                htmlAssignationBodyText(birthdayWorker),
                plainAssignationBodyText());
    }
}