package com.tenPines.application.service;

import com.tenPines.model.Worker;
import com.tenPines.persistence.WorkerRepository;
import org.springframework.core.NestedRuntimeException;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class WorkerService {
    private final WorkerRepository workerRepository;

    public WorkerService(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;
    }

    public Worker save(Worker newWorker) {
        return workerRepository.save(newWorker);
    }

    public List<Worker> getAllParticipants() {
        Specification<Worker> spec = Specifications.where((model, criteriaQuery, criteriaBuilder) ->
            criteriaBuilder.isTrue(model.get("wantsToParticipate"))
        );
        return workerRepository.findAll(spec);
    }

    public void remove(Worker aWorker) {
        //TODO: y debería sacar las relaciones asociadas; y arreglarlas de alguna manera
        workerRepository.delete(aWorker);
    }

    public void removeAll() {
        workerRepository.deleteAll();
    }

    public Optional<Worker> retrieveWorkerByEmail(String email) {
        return workerRepository.findByeMail(email).stream().findFirst();
    }

    public static String errorWhenDoNotExistAWorkerWithThisEmail() {
        return "The Worker with this email does not exist";
    }

    public void changeIntention(Worker aWorker) {
        Worker worker = retriveWorker(aWorker.getId());
        worker.changeParticipationIntention();
        workerRepository.save(worker);
    }

    public List<Worker> retrieveParticipants() {
        return workerRepository.findBywantsToParticipate(true);
    }

    public Worker retriveWorker(Long to) {
        return workerRepository.findOne(to);
    }

    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }

    public Worker retrieveWorkerByFullname(String token) { return workerRepository.findByfullName(token); }

}
