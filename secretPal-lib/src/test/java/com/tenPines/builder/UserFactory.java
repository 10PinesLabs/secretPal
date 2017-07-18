package com.tenPines.builder;

import com.tenPines.model.User;
import com.tenPines.model.Worker;

public class UserFactory {
    public static User newUser(){
        Worker worker = new WorkerBuilder().withFullName("Test").withEmail("test@test.com").build();
        User user = User.newUser(worker, "Test", "111");
        return user;
    }
}
