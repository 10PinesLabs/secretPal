package com.tenPines.restAPI;

import com.tenPines.SecretPalStarter;
import com.tenPines.application.service.WishListService;
import com.tenPines.application.service.WorkerService;
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
public class WishListController {

    @Autowired
    private WishListService wishListService;
    @Autowired
    private WorkerService workerService;

    @GetMapping("/")
    @ResponseBody
    public List<Wish> wishes() {
        return wishListService.retrieveAllWishes();
    }

    @GetMapping("/worker/{workerID}")
    @ResponseBody
    public List<Wish> getWorkersWishes(@PathVariable Long workerID) {
        Worker worker = workerService.retriveWorkerOrThrow(workerID);
        return wishListService.retrieveByWorker(worker);
    }

    @PostMapping("/")
    @ResponseBody
    public Wish save(@RequestBody Wish wish) {
        return wishListService.saveWish(wish);
    }

    @PostMapping("/{id}")
    public void updateWish(@PathVariable Long id, @RequestBody String gift) {
        Wish wish = wishListService.retrieveAWish(id);
        wish.setGift(gift);
        wishListService.updateWish(wish);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteWish(@PathVariable Long id) {
        Wish wish = wishListService.retrieveAWish(id);
        wishListService.deleteAWish(wish);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public RestfulException handleException(RestfulException e) {
        return e;
    }

}
