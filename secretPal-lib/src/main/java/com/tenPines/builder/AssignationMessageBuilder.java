package com.tenPines.builder;

import com.tenPines.application.clock.Clock;
import com.tenPines.application.service.AdminService;
import com.tenPines.application.service.MailerService;
import com.tenPines.model.User;
import com.tenPines.model.Worker;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

public class AssignationMessageBuilder extends ReminderBuilder {

    private final AdminService adminService;
    private final Clock clock;

    @Autowired
    public AssignationMessageBuilder(MailerService mailerService, AdminService adminService, Clock clock) {
        super(mailerService);
        this.adminService = adminService;
        this.clock = clock;
    }

    @Override
    protected String assignationSubject() {
        return "[SecretPal-Reminder] Asignación Pino Invisible " + clock.now().getYear();
    }

    public String defaultHtmlBody(Worker birthdayWorker) {
        return String.join("<div style=\"text-align: center; width: 100%;\">",
                "<p>" + "Este año vas a ser el amigo invisible de: " + "</p>",
                "<img src=\"" + "images/dog_playing_drums.gif" + "\"/>",
                "<p>" + birthdayWorker.getFullName() + " <" + birthdayWorker.geteMail() + "> " + "que cumple el: " + birthday(birthdayWorker) + "</p>",
                "<br>",
                "<p>" + "La idea sería tener su regalo para el viernes después de su cumpleaños, y avisarnos." + "</p>",
                "<p>" + "Por cualquier cosa, no dudes en preguntar a " + admins() + ", o a esta misma dirección." + "</p>",
                "<br>",
                "<p>" + "PD: Por favor confirmá que leíste este mail respondiéndolo!" + "</p>",
                "<br>",
                "<p>" + "Abrazo!" + "</p>",
                "</div>");
    }

    private String admins() {
        return adminService.adminUsers().stream()
                .map(User::getWorker)
                .map(admin -> nameAndMail(admin))
                .collect(Collectors.joining(", "));
    }

    private String nameAndMail(Worker worker) {
        return worker.getName() + "<" + worker.geteMail() + ">";
    }

}
