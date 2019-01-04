package com.tenPines.restAPI;

import com.tenPines.application.ReminderSystem;
import com.tenPines.application.service.MailerService;
import com.tenPines.mailer.UnsentMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/mail")
public class MailerController {

    @Autowired
    private MailerService mailerService;

    @Autowired
    private ReminderSystem reminderSystem;

    @RequestMapping(value = "/failedMails", method = RequestMethod.GET)
    @ResponseBody
    public List<UnsentMessage> getFailedMail(){
        return mailerService.retrieveAllFailedMails();
    }

    @RequestMapping(value = "/resendMailsFailure", method = RequestMethod.POST)
    @ResponseBody
    public void resendMail(@RequestBody UnsentMessage unsentMessage) {
        mailerService.resendMessageFailure(unsentMessage);
    }

    @RequestMapping(value = "/remind", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public void sendAllTodayReminders() {
        reminderSystem.sendAllReminders();
    }

}
