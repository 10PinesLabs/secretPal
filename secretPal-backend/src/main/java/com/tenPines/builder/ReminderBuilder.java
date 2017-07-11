package com.tenPines.builder;

import com.tenPines.application.service.MailerService;
import com.tenPines.model.FriendRelation;
import com.tenPines.model.Message;
import com.tenPines.model.Worker;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Created by Aye on 01/11/16.
 */
public abstract class ReminderBuilder {

    public MailerService mailerService;

    public DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd 'de' MMMM", new Locale("es", "ES"));

    protected ReminderBuilder(MailerService mailerService) {
        this.mailerService = mailerService;
    }

    public Message buildMessage(FriendRelation aFriendRelation) throws IOException {
        return new Message(
                aFriendRelation.getGiftGiver().geteMail(),
                assignationSubject(),
                assignationBodyText(aFriendRelation.getGiftReceiver())
        );
    }


    protected abstract String assignationSubject() throws IOException;

    protected String assignationBodyText(Worker birthdayWorker) throws IOException {
        return mailerService.getTemplate()
                .map(emailTemplate -> replaceMailVariables(emailTemplate.getBodyText(), birthdayWorker))
                .orElse(defaultBody(birthdayWorker));
    }

    private String replaceMailVariables(String bodyText, Worker birthdayWorker) {
        String res = StringUtils.replace(bodyText, "${receiver.fullName}", birthdayWorker.getFullName());
        res = StringUtils.replace(res, "${receiver.dateOfBirth}", birthday(birthdayWorker));
        return res;
    }

    protected abstract String defaultBody(Worker birthdayWorker);

    protected String birthday(Worker birthdayWorker) {
        return dateFormat.format(birthdayWorker.getDateOfBirth());
    }

}
