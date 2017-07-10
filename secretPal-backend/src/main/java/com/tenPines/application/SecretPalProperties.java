package com.tenPines.application;

import org.springframework.context.annotation.Configuration;

@Configuration
public class SecretPalProperties {
    private long reminderDayPeriod = 14L;
    private long reminderMonthPeriod = 2L;

    public long getReminderDayPeriod() {
        return reminderDayPeriod;
    }

    public void setReminderDayPeriod(long reminderDayPeriod) {
        this.reminderDayPeriod = reminderDayPeriod;
    }

    public long getReminderMonthPeriod() {
        return reminderMonthPeriod;
    }

    public void setReminderMonthPeriod(long reminderMonthPeriod) {
        this.reminderMonthPeriod = reminderMonthPeriod;
    }
}
