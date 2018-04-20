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

    @Override
    public String defaultBody(Worker birthdayWorker){
        return "Este año vas a ser el amigo invisible de: " + birthdayWorker.getFullName() + " <" + birthdayWorker.geteMail() + "> " +
                "que cumple el: " + birthday(birthdayWorker) + ".\n\n" +
                "La idea sería tener su regalo para el viernes después de su cumpleaños, y avisarnos.\n" +
                "Por cualquier cosa, no dudes en preguntar a " + admins() + ", o a esta misma dirección.\n\n" +
                "PD: Por favor confirmá que leíste este mail respondiéndolo!\n\n" +
                "Abrazo!";
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
