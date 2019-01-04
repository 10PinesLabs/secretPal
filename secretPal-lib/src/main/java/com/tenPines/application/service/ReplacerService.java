package com.tenPines.application.service;

import com.tenPines.application.clock.Clock;
import com.tenPines.builder.MailTextReplacer;
import com.tenPines.model.User;
import com.tenPines.model.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class ReplacerService {

    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd 'de' MMMM", new Locale("es", "ES"));
    @Autowired
    private AdminService adminService;
    @Autowired
    private Clock clock;

    public List<MailTextReplacer> all(){
        return Arrays.asList(
                new MailTextReplacer("receiver.fullName", relation -> relation.getGiftReceiver().getFullName()),
                new MailTextReplacer("receiver.birthday", relation -> birthday(relation.getGiftReceiver())),
                new MailTextReplacer("admins", relation -> admins()),
                new MailTextReplacer("receiver.eMail", relation -> relation.getGiftReceiver().geteMail()),
                new MailTextReplacer("currentYear", relation -> String.valueOf(clock.now().getYear()))
        );
    }

    private String birthday(Worker birthdayWorker) {
        return dateFormat.format(birthdayWorker.getDateOfBirth());
    }

    private String admins() {
        return adminService.adminUsers().stream()
                .map(User::getWorker)
                .map(admin -> nameAndMail(admin))
                .collect(Collectors.joining(", "));
    }

    private String nameAndMail(Worker worker) {
        return worker.getName() + " &lt;" + worker.geteMail() + "&gt;";
    }

}
