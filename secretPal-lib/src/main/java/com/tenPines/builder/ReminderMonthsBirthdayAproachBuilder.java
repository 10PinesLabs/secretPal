package com.tenPines.builder;

import com.tenPines.application.service.MailerService;
import com.tenPines.model.Worker;
import org.springframework.beans.factory.annotation.Autowired;

public class ReminderMonthsBirthdayAproachBuilder extends ReminderBuilder {

    @Autowired
    public ReminderMonthsBirthdayAproachBuilder(MailerService mailerService) {
        super(mailerService);
    }

    @Override
    protected String assignationSubject() {
        return "[SecretPal-Reminder] Faltan 2 meses!";
    }

    @Override
    public String defaultHtmlBody(Worker birthdayWorker){
        return "<p>" + "Este año vas a ser el amigo invisible de: " + birthdayWorker.getFullName() + " que cumple el: " + birthday(birthdayWorker) + "\n" +
                "La idea sería tener su regalo para el Viernes después de su cumpleaños, y avisarnos.\n" +
                "Por cualquier cosa, no dudes en preguntarnos {admins}.\n" +
                "Saludos!" + "</p>";
    }

}
