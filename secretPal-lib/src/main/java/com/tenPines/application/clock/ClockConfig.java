package com.tenPines.application.clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;

@Configuration
public class ClockConfig {
    @Bean
    @Profile("test")
    public Clock fakeClock() {
        return new FakeClock(LocalDate.of(2018, 4, 1));
    }

    @Bean
    @Profile("!test")
    public Clock realClock() {
        return new RealClock();
    }
}