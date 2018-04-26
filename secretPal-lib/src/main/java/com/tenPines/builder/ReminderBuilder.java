package com.tenPines.builder;

import com.tenPines.application.service.MailerService;
import com.tenPines.model.FriendRelation;
import com.tenPines.model.Message;
import com.tenPines.model.Worker;
import org.apache.commons.lang.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by Aye on 01/11/16.
 */
public abstract class ReminderBuilder {

    public List<MailTextReplacer> replacers = Arrays.asList(
            new MailTextReplacer("receiver.fullName", relation -> relation.getGiftReceiver().getFullName()),
            new MailTextReplacer("receiver.dateOfBirth", relation -> birthday(relation.getGiftReceiver()))
    );

    public MailerService mailerService;

    public DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd 'de' MMMM", new Locale("es", "ES"));

    protected ReminderBuilder(MailerService mailerService) {
        this.mailerService = mailerService;
    }

    public Message buildMessage(FriendRelation aFriendRelation) {
        return new Message(
                aFriendRelation.getGiftGiver().geteMail(),
                assignationSubject(),
                htmlBodyText(aFriendRelation),
                plainBodyText(aFriendRelation)
        );
    }

    protected abstract String assignationSubject();

    private String plainBodyText(FriendRelation relation) {
        return plainTextFrom(htmlBodyText(relation));
    }

    private String plainTextFrom(String htmlText) {
        List<String> plainTextFragments = substringsBetween(htmlText, "<p>", "</p>");
        assertThereIsPlainText(plainTextFragments);
        return String.join(" ", plainTextFragments);
    }

    private List<String> substringsBetween(String htmlText, String open, String close) {
        String[] plainTextFragments = StringUtils.substringsBetween(htmlText, open, close);
        if (plainTextFragments == null) {
            return Arrays.asList();
        }
        return Arrays.asList(plainTextFragments);
    }

    private void assertThereIsPlainText(List<String> plainTextFragments) {
        if (plainTextFragments.isEmpty()){
            throw new RuntimeException("The html message is missing <p> and </p>");
        }
    }

    protected String htmlBodyText(FriendRelation relation) {
        return mailerService.getTemplate()
                .map(emailTemplate -> replaceMailVariables(emailTemplate.getBodyText(), relation))
                .orElse(defaultHtmlBody(relation.getGiftReceiver()));
    }

    private String replaceMailVariables(String bodyText, FriendRelation friendRelation) {
        List<String> variablesToReplace = substringsBetween(bodyText, "${", "}");
        String res = variablesToReplace.stream().reduce(bodyText, (text, variable) -> {
                    MailTextReplacer appropiateReplacer = replacers.stream().filter(replacer -> replacer.canReplace(variable))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("${" + variable + "}" + " is not a valid variable"));
                    return StringUtils.replace(bodyText, "${" + variable + "}", appropiateReplacer.replaceFor(friendRelation));
                }
        );
        return res;
    }

    public abstract String defaultHtmlBody(Worker birthdayWorker);

    protected String birthday(Worker birthdayWorker) {
        return dateFormat.format(birthdayWorker.getDateOfBirth());
    }

}
