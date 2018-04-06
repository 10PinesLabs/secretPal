package com.tenPines.mailer;

import com.tenPines.model.Message;
import com.tenPines.persistence.FailedMailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FailProofPostOffice implements PostOffice {
    private static final Logger log = LoggerFactory.getLogger(FailProofPostOffice.class);

    private PostMan postMan;
    private final FailedMailsRepository failedMails;

    public FailProofPostOffice(PostMan postMan, FailedMailsRepository failedMails) {
        this.postMan = postMan;
        this.failedMails = failedMails;
    }

    @Override
    public void sendMessage(Message message) {
        log.info("Mail [{}] to [{}]", message.getSubject(), message.getRecipient());
        try {
            postMan.sendMessage(message);
            log.info("OK");
        } catch (UnableToSendMessage e) {
            UnsentMessage unsentMessage = UnsentMessage.create(message, e);
            log.error("Failed");
            failedMails.save(unsentMessage);
        }
    }

    @Override
    public List<UnsentMessage> getFailedMessages() {
        return failedMails.findAll();
    }
}
