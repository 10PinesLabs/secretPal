package com.tenPines.restAPI;

import com.tenPines.application.SystemPalFacade;
import com.tenPines.application.service.AdminService;
import com.tenPines.application.service.UserService;
import com.tenPines.application.service.WorkerService;
import com.tenPines.auth.BackofficeValidator;
import com.tenPines.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Arrays;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private WorkerService workerService;

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private SystemPalFacade systemFacade;

    @Autowired
    private BackofficeValidator backofficeValidator;

    @GetMapping(value = "/callback", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String backofficeCallback(
            @RequestParam("uid") Long uid,
            @RequestParam("email") String email,
            @RequestParam("username") String username,
            @RequestParam("full_name") String fullName,
            @RequestParam("root") Boolean root,
            @RequestParam("hmac") String hmac) throws Exception {

        if (!backofficeValidator.isFromBackoffice(uid, email, username, fullName, root, hmac))
            return "Falló la validación, el backoffice envió una firma incorrecta";

        User loggedUser = userService.findByBackofficeId(uid)
                .orElseGet(() -> createUser(uid, fullName, email));

        String token = userService.getTokenForUser(loggedUser);

        return String.join("\n",
                "<!DOCTYPE html>",
                "<html lang=\"en\">",
                "<head>",
                "    <meta charset=\"UTF-8\">",
                "    <title>Logueandote</title>",
                "</head>",
                "<body>",
                "    <h2>Redirigiendo...</h2>",
                "    <script>",
                "        debugger;",
                "        /* Store the token into localStorage */",
                "        window.localStorage.setItem('token', '" + token + "');",
                "        /* Redirect to the actual application */",
                "        window.location.href = '/';",
                "    </script>",
                "</body>",
                "</html >");
    }


    private User createUser(Long backofficeId, String fullName, String email) {
        Worker worker = workerService.retrieveWorkerByEmail(email)
                .orElseThrow(() -> new RuntimeException("Tu mail no esta registrado en el sistema, molestá a un admin"));

        worker.setFullName(fullName);
        workerService.save(worker);

        return userService.save(new User(worker, backofficeId));
    }

    @RequestMapping(value = "/me", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public UserForFrontend retrieveLoggedWorker(@RequestHeader("Authorization") String token) throws IOException {
        User user = userService.getUserFromToken(token);
        return new UserForFrontend(user, adminService.isAdmin(user));
    }

    @RequestMapping(value = "/giftsDefault", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public DefaultGift giftDefaults() {
        return systemFacade.retrieveTheLastDefaultGift();
    }

    @RequestMapping(value = "/giftsDefault", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public void addGiftDefaults(@RequestBody DefaultGift defaultGift) {
        systemFacade.addGiftDefaults(defaultGift);
    }

    @RequestMapping(value = "/confirmationGift/{id}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public void updateGiftReceivedDate(@PathVariable(value = "id") Long id) {
        Worker workerToUpdate = workerService.retriveWorker(id);
        workerToUpdate.markGiftAsReceived();
        workerService.save(workerToUpdate);
    }
}