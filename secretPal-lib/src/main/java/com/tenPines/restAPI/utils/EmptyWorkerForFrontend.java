package com.tenPines.restAPI.utils;

import com.tenPines.model.ParticipationConfig;

import java.time.LocalDate;
import java.time.Month;

public class EmptyWorkerForFrontend extends com.tenPines.model.Worker {

    public EmptyWorkerForFrontend() {
        super("xxxxxxxxxxxx","xxxx", "xxxxxxxxxx@xxxxx.com", LocalDate.of(2112, Month.DECEMBER,21), new ParticipationConfig(false,false,false));
    }
}
