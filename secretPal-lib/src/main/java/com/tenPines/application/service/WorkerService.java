package com.tenPines.application.service;

import com.tenPines.model.Worker;
import com.tenPines.persistence.WorkerRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkerService {
    private final WorkerRepository workerRepository;
    private final UserService userService;

    public WorkerService(WorkerRepository workerRepository, UserService userService) {
        this.workerRepository = workerRepository;
        this.userService = userService;
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
        userService.deleteByWorker(aWorker);
        workerRepository.delete(aWorker);
    }

    public void removeAll() {
        workerRepository.deleteAll();
    }

    public Optional<Worker> retrieveWorkerByEmail(String email) {
        return workerRepository.findByeMail(email);
    }

    public static String errorWhenDoNotExistAWorkerWithThisEmail() {
        return "The Worker with this email does not exist";
    }

    public void changeIntention(Worker aWorker) {
        Worker worker = retrieveWorker(aWorker.getId());
        worker.changeParticipationIntention();
        workerRepository.save(worker);
    }

    public List<Worker> retrieveParticipants() {
        return workerRepository.findBywantsToParticipate(true);
    }

    public Worker retrieveWorker(Long to) {
        return workerRepository.findOne(to);
    }

    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }

    public Worker retrieveWorkerByFullname(String token) {
        return workerRepository.findByfullName(token);
    }

    public void updateGifUrlForWorker(Worker worker, String newGifUrl) {
        worker.setGifUrl(newGifUrl);
        workerRepository.save(worker);
    }
}
