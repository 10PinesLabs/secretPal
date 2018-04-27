package com.tenPines.builder;

import com.tenPines.application.service.ReplacerService;
import com.tenPines.files.TemplateReader;
import com.tenPines.model.FriendRelation;
import com.tenPines.model.Message;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.List;

public class ReminderBuilder {

    private String mailTemplate;
    private List<MailTextReplacer> replacers;
    private TemplateReader templateReader = new TemplateReader();

    public ReminderBuilder(String mailTemplate, ReplacerService replacerService) {
        this.mailTemplate = mailTemplate;
        this.replacers = replacerService.all();
    }

    public Message buildMessage(FriendRelation aFriendRelation) {
        return new Message(
                aFriendRelation.getGiftGiver().geteMail(),
                assignationSubject(aFriendRelation),
                htmlBodyText(aFriendRelation),
                plainBodyText(aFriendRelation)
        );
    }

    private  String assignationSubject(FriendRelation relation){
        return replaceMailVariables(templateReader.getContent(mailTemplate + "-subject.txt"), relation);
    }

    private String plainBodyText(FriendRelation relation) {
        return replaceMailVariables(templateReader.getContent(mailTemplate + "-text.txt"), relation);
    }

    private String htmlBodyText(FriendRelation relation) {
        return replaceMailVariables(templateReader.getContent(mailTemplate + "-html.html"), relation);
    }

    private String replaceMailVariables(String bodyText, FriendRelation friendRelation) {
        List<String> variablesToReplace = substringsBetween(bodyText, "${", "}");
        String res = variablesToReplace.stream().reduce(bodyText, (text, variable) -> {
                    MailTextReplacer appropiateReplacer = replacers.stream().filter(replacer -> replacer.canReplace(variable))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("${" + variable + "}" + " is not a valid variable"));
                    return StringUtils.replace(text, "${" + variable + "}", appropiateReplacer.replaceFor(friendRelation));
                }
        );
        return res;
    }

    private List<String> substringsBetween(String htmlText, String open, String close) {
        String[] plainTextFragments = StringUtils.substringsBetween(htmlText, open, close);
        if (plainTextFragments == null) {
            return Arrays.asList();
        }
        return Arrays.asList(plainTextFragments);
    }

}
