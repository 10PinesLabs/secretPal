package com.tenPines.restAPI.utils;

import java.time.LocalDate;

public class EmptyWorkerForFrontend extends com.tenPines.model.Worker {

    public EmptyWorkerForFrontend() {
        super("","", "", LocalDate.now(), true);
    }
}
