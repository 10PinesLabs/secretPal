package com.tenPines.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

/**
 * Created by Aye on 09/11/16.
 */
@Entity
public class EmailTemplate {


    @Id
    @GeneratedValue
    private Long id;

    String subject;

    String bodyText;

    String dateOfBirth;

    String fullName;

    Boolean active;


    public EmailTemplate() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBodyText() {
        return bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }


    public void modifiedTemplate(EmailTemplate modifiedMail) {
        this.setActive(modifiedMail.getActive());
        this.setDateOfBirth(modifiedMail.getDateOfBirth());
        this.setFullName(modifiedMail.getFullName());
        this.setBodyText(modifiedMail.getBodyText());
        this.setSubject(modifiedMail.getSubject());
    }

}
