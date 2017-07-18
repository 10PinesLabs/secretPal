package com.tenPines.mailer;

import javax.mail.MessagingException;

public class UnableToSendMessage extends RuntimeException {
    private MessagingException originalException;

    UnableToSendMessage(MessagingException e) {
        this.originalException = e;
    }

    UnableToSendMessage() {
    }

    @Override
    public String toString() {
        return originalException.toString();
    }
}
