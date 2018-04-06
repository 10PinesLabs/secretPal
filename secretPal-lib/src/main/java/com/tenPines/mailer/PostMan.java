package com.tenPines.mailer;

import com.tenPines.model.Message;

public interface PostMan {
    void sendMessage(Message message);
}
