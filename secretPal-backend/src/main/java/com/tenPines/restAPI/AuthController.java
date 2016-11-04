package com.tenPines.restAPI;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nimbusds.jose.JOSEException;
import com.tenPines.application.SecretPalSystem;
import com.tenPines.application.service.UserService;
import com.tenPines.application.service.WorkerService;
import com.tenPines.auth.GoogleAuth;
import com.tenPines.configuration.AdminProperties;
import com.tenPines.model.*;
import com.tenPines.utils.AuthUtils;
import com.tenPines.utils.Payload;
import com.tenPines.utils.PropertyParser;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.Properties;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private SecretPalSystem system;

    @Autowired
    private WorkerService workerService;

    @Autowired
    private UserService userService;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private SecurityGuard securityGuard;

    @RequestMapping(value = "/google", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Token> loginGoogle(@RequestBody Payload payload, final HttpServletRequest request) throws IOException, JOSEException {
        Properties googleAuthProperties = new PropertyParser("src/main/resources/gmailAPIAuth.properties");
        GoogleAuth googleAuth = new GoogleAuth(googleAuthProperties, HttpClientBuilder.create().build());
        String access_token = googleAuth.authUserWithPayload(payload);

        return validateWorker(request, googleAuth.getAuthUserEmail(access_token));
    }

    private ResponseEntity<Token> validateWorker(HttpServletRequest request, String workerEmail) throws JOSEException {
        try{
            Worker aUser = system.retrieveWorkerByEmail(workerEmail);
            Token token = AuthUtils.createToken(request.getRemoteHost(), aUser.geteMail());
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    @ResponseBody
    public Worker getAdmin() throws IOException {
        return workerService.retrieveWorkerByEmail(AdminProperties.getAdminEmail());
    }

    @RequestMapping(value = "/admin", method = RequestMethod.POST)
    @ResponseBody
    public void setAdmin(@RequestHeader(value = "Authorization") String header,
                         @RequestBody Worker newAdmin) throws ParseException, IOException, JOSEException {
        User user = User.newUser(system.retrieveWorkerByEmail(AuthUtils.tokenSubject(header)),"","");  //TODO: SOLUCIONAR

        if( user.isAdmin() ){
            AdminProperties.setAdmin(newAdmin);
        } else {
            throw new RuntimeException("This user is not an admin");
        }
    }

//    @RequestMapping(value = "/me", method = RequestMethod.GET)
//    @ResponseBody
//    public User retrieveLoggedWorker(@RequestHeader(value = "Authorization") String header) throws ParseException, JOSEException {
//        return new User(system.retrieveWorkerByEmail(AuthUtils.tokenSubject(header)));
//    }

    @RequestMapping(value = "/me", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public UserForFrontend retrieveLoggedWorker(@RequestHeader(value = "Authorization") String header) throws ParseException, JOSEException, IOException {
        User completeUser = userService.retrieveUserByUserName(header);
        return new UserForFrontend(completeUser.getId(),completeUser.getWorker(),completeUser.getUserName(), completeUser.isAdmin());
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

    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    SecurityToken loginWithInternalCredential(@RequestBody Credential credential){
        String token = securityGuard.enterWith(credential);
        return SecurityToken.createWith(token);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    void registerUserAndAsociateWithAWorker(@RequestBody NewUser newUser){
        registerService.registerUser(newUser);
    }
}
