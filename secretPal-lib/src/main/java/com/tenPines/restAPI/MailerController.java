package com.tenPines.restAPI;

import com.tenPines.application.SystemPalFacade;
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

    @Autowired
    private SystemPalFacade system;

    @Autowired
    private MailerService mailerService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public EmailTemplate getMail() throws IOException {
        return system.getEMailTemplate();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public EmailTemplate setMail(@RequestBody EmailTemplate modifiedMail) throws IOException {
        return system.setEmailTemplate(modifiedMail);
    }

    @RequestMapping(value = "/failedMails", method = RequestMethod.GET)
    @ResponseBody
    public List<UnsentMessage> getFailedMail(){
        return mailerService.retrieveAllFailedMails();
    }

    @RequestMapping(value = "/resendMailsFailure", method = RequestMethod.POST)
    @ResponseBody
    public void resendMail(@RequestBody UnsentMessage unsentMessage) {
        system.resendMessageFailure(unsentMessage);
    }

    @RequestMapping(value = "/remind", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public void sendAllTodayReminders() {
        system.sendAllTodayReminders();
    }

}
