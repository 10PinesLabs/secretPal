package com.tenPines.application.service;

import com.tenPines.model.Worker;
import com.tenPines.persistence.WorkerRepository;
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

    public List<Worker> getAllGiverParticipants() {
        return workerRepository.findByWantsToParticipate_WantsToGive(true);
    }

    public List<Worker> getAllReceiverParticipants() {
        return workerRepository.findByWantsToParticipate_WantsToReceive(true);
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

    public void changeIntention(Worker aWorker) {
        Worker worker = retrieveWorker(aWorker.getId());
        worker.changeParticipationIntention();
        workerRepository.save(worker);
    }

    public List<Worker> retrieveParticipants() {
        return workerRepository.findByWantsToParticipate_WantsToGive(true);
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
