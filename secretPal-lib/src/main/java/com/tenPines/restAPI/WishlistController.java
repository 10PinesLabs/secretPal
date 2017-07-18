package com.tenPines.restAPI;

import com.tenPines.application.SystemPalFacade;
import com.tenPines.model.Wish;
import com.tenPines.model.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/api/wishlist")
public class WishlistController {
    @Autowired
    private SystemPalFacade systemActual;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<Wish> wishes() {
        return systemActual.retrieveAllWishes();
    }

    @RequestMapping(value = "/worker/{workerID}", method = RequestMethod.GET)
    @ResponseBody
    public List<Wish> getWorkersWishes(@PathVariable Long workerID) {
        Worker worker = systemActual.retrieveAWorker(workerID);
        return systemActual.retrievallWishesForWorker(worker);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Wish save(@RequestBody Wish wish) throws IOException {
        return systemActual.saveWish(wish);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void updateWish(@PathVariable Long id, @RequestBody String gift) {
        Wish wish = systemActual.retrieveAWish(id);
        wish.setGift(gift);
        systemActual.updateWish(wish);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteWish(@PathVariable Long id) {
        Wish wish = systemActual.retrieveAWish(id);
        systemActual.deleteAWish(wish);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public RestfulException handleException(RestfulException e) {
        return e;
    }

}
