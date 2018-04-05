package com.tenPines.application.clock;

import java.time.LocalDate;

public class FakeClock implements Clock {
    private LocalDate lies;

    public FakeClock(LocalDate lies) {
        this.lies = lies;
    }

    @Override
    public LocalDate now() {
        return lies;
    }

    public void setTime(LocalDate lies) {
        this.lies = lies;
    }
}
