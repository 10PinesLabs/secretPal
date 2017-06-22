package com.tenPines.restAPI;


import com.tenPines.application.SystemPalFacade;
import com.tenPines.application.service.WorkerService;
import com.tenPines.model.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/api/customRule")
public class CustomParticipantRuleController {

    @Autowired
    private SystemPalFacade systemFacade;

    @Autowired
    private WorkerService workerService;

    @RequestMapping(value = "/{from}/{to}", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void createRule(@PathVariable Long from, @PathVariable Long to) {
        Worker workerFrom = workerService.retriveWorker(from);
        Worker workerTo = workerService.retriveWorker(to);
        systemFacade.createRule(workerFrom, workerTo);
    }

}
