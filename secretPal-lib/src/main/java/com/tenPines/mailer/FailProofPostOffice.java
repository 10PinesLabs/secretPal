package com.tenPines.mailer;

import com.tenPines.model.Message;
import com.tenPines.persistence.FailedMailsRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FailProofPostOffice implements PostOffice {

    private PostMan postMan;
    private final FailedMailsRepository failedMails;

    public FailProofPostOffice(PostMan postMan, FailedMailsRepository failedMails) {
        this.postMan = postMan;
        this.failedMails = failedMails;
    }

    @Override
    public void sendMessage(Message message) {
        try {
            postMan.sendMessage(message);
        } catch (UnableToSendMessage e) {
            UnsentMessage unsentMessage = UnsentMessage.create(message, e);
            failedMails.save(unsentMessage);
        }
    }

    @Override
    public List<UnsentMessage> getFailedMessages() {
        return failedMails.findAll();
    }

    @Scheduled(fixedDelay = 86400000) //86400000 = 1 dia
    public void resendFailedMessages() {
        failedMails.findAll().stream().forEach(
                (unSentMessage) -> {
                    failedMails.delete(unSentMessage);
                    sendMessage(unSentMessage.toMessage());
                }
        );
    }
}
