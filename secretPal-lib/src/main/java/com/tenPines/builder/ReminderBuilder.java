package com.tenPines.builder;

import com.tenPines.application.service.MailerService;
import com.tenPines.model.FriendRelation;
import com.tenPines.model.Message;
import com.tenPines.model.Worker;
import org.apache.commons.lang.StringUtils;

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

    public Message buildMessage(FriendRelation aFriendRelation) {
        return new Message(
                aFriendRelation.getGiftGiver().geteMail(),
                assignationSubject(),
                htmlBodyText(aFriendRelation.getGiftReceiver()),
                plainBodyText(aFriendRelation.getGiftReceiver())
        );
    }

    protected abstract String assignationSubject();

    private String plainBodyText(Worker birthdayWorker) {
        return plainTextFrom(htmlBodyText(birthdayWorker));
    }

    private String plainTextFrom(String htmlText) {
        String[] plainTextFragments = StringUtils.substringsBetween(htmlText, "<p>", "</p>");
        if(plainTextFragments == null){
            throw new RuntimeException("The html message is missing <p> and </p>");
        }
        return String.join(" ", plainTextFragments);
    }

    protected String htmlBodyText(Worker birthdayWorker) {
        return mailerService.getTemplate()
                .map(emailTemplate -> replaceMailVariables(emailTemplate.getBodyText(), birthdayWorker))
                .orElse(defaultHtmlBody(birthdayWorker));
    }

    private String replaceMailVariables(String bodyText, Worker birthdayWorker) {
        String res = StringUtils.replace(bodyText, "${receiver.fullName}", birthdayWorker.getFullName());
        res = StringUtils.replace(res, "${receiver.dateOfBirth}", birthday(birthdayWorker));
        return res;
    }

    public abstract String defaultHtmlBody(Worker birthdayWorker);

    protected String birthday(Worker birthdayWorker) {
        return dateFormat.format(birthdayWorker.getDateOfBirth());
    }

}
