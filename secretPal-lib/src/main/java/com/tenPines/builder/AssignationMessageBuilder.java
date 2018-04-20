package com.tenPines.builder;

import com.tenPines.application.service.MailerService;
import com.tenPines.model.Worker;
import org.springframework.beans.factory.annotation.Autowired;

public class AssignationMessageBuilder extends ReminderBuilder {

    @Autowired
    public AssignationMessageBuilder(MailerService mailerService) {
        super(mailerService);
    }

    @Override
    protected String assignationSubject() {
        return "[SecretPal-Reminder] Asignación Pino Invisible 2018";
    }

    @Override
    public String defaultBody(Worker birthdayWorker){
        return "Este año vas a ser el amigo invisible de: " + birthdayWorker.getFullName() + " <" + birthdayWorker.geteMail() + "> " +
                "que cumple el: " + birthday(birthdayWorker) + "\n\n" +
                "La idea sería tener su regalo para el viernes después de su cumpleaños, y avisarnos.\n" +
                "Por cualquier cosa, no dudes en preguntarnos {admins}.\n\n" +
                "PD: Por favor confirmá que leíste este mail respondiéndolo!\n\n" +
                "Abrazo!";
    }

}
