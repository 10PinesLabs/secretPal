package com.tenPines.mailer;

import com.tenPines.model.Message;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.tenPines.mailer.InMemoryPostMan.USE_FAKE_MAILER_ENVIRONMENT;

@Component
@Primary
@ConditionalOnExpression(USE_FAKE_MAILER_ENVIRONMENT)
public class InMemoryPostMan implements PostMan {
    public static final String USE_FAKE_MAILER_ENVIRONMENT = "${use.fake.mailer:false}";

    public List<Message> messages = new ArrayList<>();

    @Override
    public void sendMessage(Message message) {
        messages.add(message);
    }

    List<Message> messagesTo(String to) {
        return messages.stream().filter(message -> message.getRecipient().contains(to)).collect(Collectors.toList());
    }

}
