package com.tenPines.application;

import org.springframework.context.annotation.Configuration;

@Configuration
public class SecretPalProperties {
    private long reminderWeekPeriod = 2L;
    private long reminderMonthPeriod = 2L;

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
