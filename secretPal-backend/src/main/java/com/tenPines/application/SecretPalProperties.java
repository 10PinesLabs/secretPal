package com.tenPines.application;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "secretPal")
public class SecretPalProperties {
    private long reminderWeekPeriod;
    private long reminderMonthPeriod;
    private String allRecipientsMail;
    private String mailUser;
    private String mailPassword;

    public String getAllRecipientsMail() {
        return allRecipientsMail;
    }

    public void setAllRecipientsMail(String allRecipientsMail) {
        this.allRecipientsMail = allRecipientsMail;
    }


    public long getReminderWeekPeriod() {
        return reminderWeekPeriod;
    }

    public void setReminderWeekPeriod(long reminderWeekPeriod) {
        this.reminderWeekPeriod = reminderWeekPeriod;
    }

    public long getReminderMonthPeriod() {
        return reminderMonthPeriod;
    }

    public void setReminderMonthPeriod(long reminderMonthPeriod) {
        this.reminderMonthPeriod = reminderMonthPeriod;
    }

    public String getMailUser() {
        return mailUser;
    }

    public void setMailUser(String mailUser) {
        this.mailUser = mailUser;
    }

    public String getMailPassword() {
        return mailPassword;
    }

    public void setMailPassword(String mailPassword) {
        this.mailPassword = mailPassword;
    }
}
