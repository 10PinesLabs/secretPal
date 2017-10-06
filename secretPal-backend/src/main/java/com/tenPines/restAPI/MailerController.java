package com.tenPines.restAPI;

import com.tenPines.application.ReminderSystem;
import com.tenPines.application.service.MailerService;
import com.tenPines.mailer.UnsentMessage;
import com.tenPines.model.EmailTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/api/mail")
public class MailerController {
    @Autowired private MailerService mailerService;
    @Autowired private ReminderSystem reminderSystem;

    @GetMapping("/")
    @ResponseBody
    public EmailTemplate getMail() throws IOException {
        return mailerService.getEMailTemplate();
    }

    @PostMapping("/")
    @ResponseBody
    public EmailTemplate setMail(@RequestBody EmailTemplate modifiedMail) throws IOException {
        return mailerService.setEmailTemplate(modifiedMail);
    }

    @GetMapping("/failedMails")
    @ResponseBody
    public List<UnsentMessage> getFailedMail(){
        return mailerService.retrieveAllFailedMails();}

    @GetMapping("/resendMailsFailure")
    @ResponseBody
    public void resendMail(@RequestBody UnsentMessage unsentMessage) {
        mailerService.resendMessageFailure(unsentMessage);
    }

    @PutMapping("/remind")
    @ResponseStatus(HttpStatus.OK)
    public void sendAllTodayReminders() {
        reminderSystem.sendAllReminders();
    }
}
