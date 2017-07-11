package com.tenPines.builder;

import com.tenPines.application.service.MailerService;
import com.tenPines.model.Worker;
import com.tenPines.utils.PropertyParser;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Created by Aye on 01/11/16.
 */
public class ReminderWeeksBirthdayAproachBuilder extends ReminderBuilder {

    private PropertyParser templateProperties;

    @Autowired
    public ReminderWeeksBirthdayAproachBuilder(MailerService mailerService) {
        super(mailerService);
        try {
            templateProperties = new PropertyParser("mailTemplate.properties");
        } catch (IOException e) {

        }
    }

    @Override
    protected String assignationSubject() {
        return "[SecretPal-Reminder] Faltan solo 2 semanas!";
    }

    @Override
    protected String defaultBody(Worker birthdayWorker){
        return "Hola, este mail es para recordarte que el " + birthday(birthdayWorker) + " es el cumpleaños de " + birthdayWorker.getFullName() + ".\n" +
                "La idea sería tener su regalo para el Viernes después de su cumpleaños, y avisarnos.\n" +
                "Por cualquier cosa, no dudes en preguntarnos {admins}.\n" +
                "Saludos!";
    }

}

