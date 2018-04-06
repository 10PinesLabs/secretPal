package com.tenPines.application.clock;

import java.time.LocalDate;

public class RealClock implements Clock {

    @Override
    public LocalDate now() {
        return LocalDate.now();
    }
}
