package com.tenPines.restAPI;

import com.tenPines.application.SystemPalFacade;
import com.tenPines.application.service.WorkerService;
import com.tenPines.application.service.validation.rule.CustomParticipantRule;
import com.tenPines.application.service.validation.rule.NotCircularRelationRule;
import com.tenPines.application.service.validation.rule.NotTooCloseBirthdaysRule;
import com.tenPines.model.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/api/customParticipantRule")
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
        systemFacade.createRule(workerFrom, workerTo, false);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long id) throws IOException {
        systemFacade.deleteRule(id);
    }

    @RequestMapping(value = "/customRules", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<CustomParticipantRule> rules() {
        List<CustomParticipantRule> rules = systemFacade.getAllCustomRules();
        return rules;
    }

    @RequestMapping(value = "/notCircularRule", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public NotCircularRelationRule notCircularRule() {
        NotCircularRelationRule circularRule = systemFacade.getCircularRule();
        return circularRule;
    }

    @RequestMapping(value = "/notTooCloseBirthdayRule", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public NotTooCloseBirthdaysRule notTooCloseBirthdayRule() {
        NotTooCloseBirthdaysRule birthdayRule = systemFacade.getNotTooCloseBirthdayRule();
        return birthdayRule;
    }
}
