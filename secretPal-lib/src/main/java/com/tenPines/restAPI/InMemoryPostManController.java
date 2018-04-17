package com.tenPines.restAPI;

import com.tenPines.mailer.InMemoryPostMan;
import com.tenPines.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/postman")
@ConditionalOnExpression(InMemoryPostMan.USE_FAKE_MAILER_ENVIRONMENT)
public class InMemoryPostManController {
    @Autowired
    private InMemoryPostMan postMan;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
    @ResponseBody
    public List<Message> messages() {
        return postMan.messages;
    }
}


