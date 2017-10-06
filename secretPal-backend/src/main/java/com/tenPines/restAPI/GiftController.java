package com.tenPines.restAPI;

import com.tenPines.application.service.GiftDefaultService;
import com.tenPines.application.service.WorkerService;
import com.tenPines.model.DefaultGift;
import com.tenPines.model.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/api/gifts")
public class GiftController {

    @Autowired
    private WorkerService workerService;
    @Autowired
    private GiftDefaultService giftDefaultService;

    @GetMapping(value = "/giftsDefault", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public DefaultGift giftDefaults() {
        List<DefaultGift> defaultGifts = giftDefaultService.getAll();
        if (defaultGifts.isEmpty()) {
            defaultGifts.add(DefaultGift.createGiftDfault("Nada", "$0"));
        }
        return defaultGifts.get(0);
    }

    @PostMapping(value = "/giftsDefault", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public void addGiftDefaults(@RequestBody DefaultGift defaultGift) {
        giftDefaultService.addGift(defaultGift);

    }

    @RequestMapping(value = "/confirmationGift/{id}", method = RequestMethod.PUT)
    public void updateGiftReceivedDate(@PathVariable(value = "id") Long id) {
        Worker workerToUpdate = workerService.retriveWorkerOrThrow(id);
        workerToUpdate.markGiftAsReceived();
        workerService.save(workerToUpdate);
    }
}
