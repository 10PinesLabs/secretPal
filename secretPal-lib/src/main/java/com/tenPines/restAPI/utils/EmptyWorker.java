package com.tenPines.restAPI.utils;

import java.time.LocalDate;

public class EmptyWorker extends com.tenPines.model.Worker {

    public EmptyWorker() {
        super("","", "", LocalDate.now(), true);
    }
}
