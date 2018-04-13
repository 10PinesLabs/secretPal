package com.tenPines.restAPI;

import com.tenPines.application.SystemPalFacade;
import com.tenPines.application.service.AdminService;
import com.tenPines.application.service.UserService;
import com.tenPines.application.service.WorkerService;
import com.tenPines.model.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@Controller
@RequestMapping("/api/worker")
public class WorkerController {

    @Autowired
    private SystemPalFacade system;
    @Autowired
    private WorkerService workerService;
    @Autowired
    private SystemPalFacade systemFacade;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
    @ResponseBody
    public List<Worker> workers() {
        List<Worker> workers =system.getAllWorkers();
        return workers;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public Worker save(@RequestBody @Valid Worker aWorker) {
        return workerService.save(aWorker);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        Worker worker = system.retrieveAWorker(id);
        userService.deleteByWorker(worker);
        system.deleteAWorker(worker);
    }

    @RequestMapping(value = "/intention", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public void changeIntention(@RequestBody Worker aWorker){
        system.changeIntention(aWorker);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public RestfulException handleException(RestfulException e) {
        return e;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void updateWorker(@RequestBody Worker worker) {
        systemFacade.editWorker(worker);
    }

    @RequestMapping(value = "/gif", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public void updateGifUrlForWorker(@PathVariable Long workerID, @RequestBody URL newGifUrl){
        Worker worker = workerService.retrieveWorker(workerID);
        workerService.updateGifUrlForWorker(worker, newGifUrl);
    }

}


