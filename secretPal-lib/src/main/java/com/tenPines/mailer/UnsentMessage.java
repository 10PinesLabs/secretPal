package com.tenPines.mailer;

import com.tenPines.model.Message;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public class UnsentMessage{
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue
    private Long id;

    private String recipient;
    private String body;
    private String subject;

    private String error;

    private UnsentMessage() { }

    static UnsentMessage create(Message message, UnableToSendMessage error) {
        UnsentMessage instance = new UnsentMessage();
        if(error.toString().length() < 200)
            instance.setError(error.toString());
        else
            instance.setError(error.toString().substring(0,200));
        instance.setBody(message.getBody());
        instance.setSubject(message.getSubject());
        instance.setRecipient(message.getRecipient());
        return instance;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Message toMessage() {
        return new Message(getRecipient(), getSubject(), getBody());
    }
}
