package com.tenPines.restAPI;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tenPines.application.SystemPalFacade;
import com.tenPines.application.service.AdminService;
import com.tenPines.application.service.UserService;
import com.tenPines.application.service.WorkerService;
import com.tenPines.configuration.AdminProperties;
import com.tenPines.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private SystemPalFacade system;

    @Autowired
    private WorkerService workerService;

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private SystemPalFacade systemFacade;

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    @ResponseBody
    public Worker getAdmin() {
        return new AdminProperties().findAdminWorker(adminService).orElseThrow(() -> new RuntimeException("No existe un administrador paa este sistema"));
    }

    @RequestMapping(value = "/me", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public UserForFrontend retrieveLoggedWorker(@RequestHeader(value = "Authorization") String header) throws IOException {
        User completeUser = userService.retrieveUserByUserName(header);
        return new UserForFrontend(completeUser.getId(),completeUser.getWorker(),completeUser.getUserName(), adminService.isAdmin(completeUser));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public SecurityToken loginWithInternalCredential(@RequestBody Credential credential){
        return systemFacade.loginWithInternalCredential(credential);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public void registerUserAndAsociateWithAWorker(@RequestBody NewUser form){
        systemFacade.registerUserAndAsociateWithAWorker(form);
    }

    @RequestMapping(value="/giftsDefault", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public DefaultGift giftDefaults() {
        DefaultGift defaultGift =systemFacade.retrieveTheLastDefaultGift();
        return defaultGift;
    }

    @RequestMapping(value="/giftsDefault", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public void addGiftDefaults(@RequestBody DefaultGift defaultGift){
        systemFacade.addGiftDefaults(defaultGift);
    }

    @RequestMapping(value="/confirmationGift/{id}", method= RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public void updateGiftReceivedDate(@PathVariable(value="id") Long id){
        Worker workerToUpdate = workerService.retriveWorker(id);
        workerToUpdate.markGiftAsReceived();
        workerService.save(workerToUpdate);
    }

    public static class Token {
        String token;

        public Token(@JsonProperty("token") String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }
    }
}