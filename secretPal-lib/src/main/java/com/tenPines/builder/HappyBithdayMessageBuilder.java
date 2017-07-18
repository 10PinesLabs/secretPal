package com.tenPines.builder;


import com.tenPines.application.SecretPalProperties;
import com.tenPines.model.Message;
import com.tenPines.model.Worker;
import com.tenPines.utils.PropertyParser;

public class HappyBithdayMessageBuilder {

    private PropertyParser templateProperties;
    private SecretPalProperties secretPalProperties;

    public HappyBithdayMessageBuilder(SecretPalProperties secretPalProperties) {
        this.secretPalProperties = secretPalProperties;
    }

    private String assignationSubject(Worker birthdayWorker) {
        return "Feliz cumpleaños " + birthdayWorker.getFullName();
    }

    private String assignationBodyText() {
        return "Feliz cumpleaños y que seas muy feliz!";
    }

    public Message buildMessage(Worker birthdaysWorker) {
        return new Message(
                secretPalProperties.getAllRecipientsMail(),
                assignationSubject(birthdaysWorker),
                assignationBodyText()
        );
    }
}
