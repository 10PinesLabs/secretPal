package com.tenPines.builder;

import com.tenPines.model.Worker;
import com.tenPines.utils.PropertyParser;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class ReminderMonthsBirthdayAproachBuilder extends ReminderBuilder {

    private PropertyParser templateProperties;

    @Autowired
    public ReminderMonthsBirthdayAproachBuilder() {
        try {
            templateProperties = new PropertyParser("mailTemplate.properties");
        } catch (IOException e) {

        }
    }

    @Override
    protected String assignationSubject() {
        return "[SecretPal-Reminder] Se acerca el cumpleaños de tu pino!";
    }

    @Override
    protected String assignationBodyText(Worker birthdayWorker){
        return "Este año vas a ser el amigo invisible de: " + birthdayWorker.getFullName() + " que cumple el: " + birthday(birthdayWorker) + "\n" +
                "La idea sería tener su regalo para el Viernes después de su cumpleaños, y avisarnos.\n" +
                "Por cualquier cosa, no dudes en preguntarnos {admins}.\n" +
                "Saludos!";
    }

}
