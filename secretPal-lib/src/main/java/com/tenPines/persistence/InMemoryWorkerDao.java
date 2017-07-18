package com.tenPines.persistence;

import com.tenPines.model.Worker;

import java.util.ArrayList;
import java.util.List;

public class InMemoryWorkerDao implements Repo<Worker> {

    protected static List<Worker> workers = new ArrayList<>();

    @Override
    public List<Worker> retrieveAll() {
        return workers;
    }

    @Override
    public Worker save(Worker worker) {
        workers.add(worker);
        return worker;
    }

    @Override
    public Worker refresh(Worker worker) {
        return worker;
    }

    @Override
    public void delete(Worker aWorker) {
        workers.remove(aWorker);
    }

    @Override
    public Worker findById(Long id) {
        return workers.get(id.intValue() - 1);
    }

    @Override
    public void update(Worker element) {

    }

    @Override
    public List<Worker> retrieveByCondition(String property, Object value) {
        return null;
    }

}
