package com.tenPines.application;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "secretPal")
public class SecretPalProperties {
    public String allRecipientsMail;
    private long reminderWeekPeriod;
    private long reminderMonthPeriod;

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
}
