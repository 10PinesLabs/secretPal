package com.tenPines.model;

import com.tenPines.application.service.UserService;
import com.tenPines.application.service.WorkerService;
import com.tenPines.integration.SpringBaseTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;


public class RegisterTest extends SpringBaseTest {

    @Autowired
    public RegisterService registerService;
    @Autowired
    private WorkerService workerService;
    @Autowired
    private UserService userService;
    private NewUser newUser1;
    private NewUser newUser2;

    @Before
    public void setUp(){
        Worker kevin = new Worker("kevin", "kevin.varela@10pines.com", LocalDate.of(1993, 4, 6), true);
        Worker ayelen = new Worker("ayelen", "ayelen.garcia@10pines.com", LocalDate.of(1992, 12, 6), true);
        User userKevin = User.newUser(kevin, "kevin", "1234");
        workerService.save(kevin);
        workerService.save(ayelen);
        userService.save(userKevin);
    }

    @Test
    public void when_i_register_my_account_with_username_and_password_not_will_be_empty(){

        try {
            NewUser newUser = NewUser.createANewUser("","", "kevin.varela@10pines.com");
            fail("The exception was not raised");
        } catch (Exception e) {
            assertThat(e.getMessage(), is("Any field is null, please check your input data"));
        }
    }

    @Test
    public void whenAnEmailHasBeenUsedByAUserNothingShouldBeRegister(){
        newUser1 = NewUser.createANewUser("pepe22","1234","kevin.varela@10pines.com");
        try {
            registerService.registerUser(newUser1);
            fail();
        } catch (RuntimeException ex) {
            assertThat(ex.getMessage()).isEqualTo(RegisterService.errorMessageWhenEmailInUse());
        }
    }

    @Test
    public void whenAnEmailHasntBeenByAUserShouldBeRegisterWithThisEmail(){
        newUser2 = NewUser.createANewUser("pepe11","1234","ayelen.garcia@10pines.com");
        registerService.registerUser(newUser2);
        assertThat(userService.retrieveUserByUserName(newUser2.getUserName())).isNotNull();
    }

    @Test
    public void withAValidEmailAndAUsernameThatWasNotUsedToLetMeCreate(){
        newUser2 = NewUser.createANewUser("usuarioSinUso","1234","ayelen.garcia@10pines.com");
        registerService.registerUser(newUser2);
        assertThat(userService.retrieveUserByUserName(newUser2.getUserName())).isNotNull();
    }

    @Test
    public void withAValidEmailAndAUsernameThatWasUsedWillNotBeCreateAndThrowExistentUserException(){
        newUser1 = NewUser.createANewUser("kevin","1234","ayelen.garcia@10pines.com");
        try {
            registerService.registerUser(newUser1);
            fail();
        } catch (RuntimeException ex) {
            assertThat(ex.getMessage()).isEqualTo(RegisterService.messageWhenUserNameHasAlreadyBeenUsed());
        }
    }

    @Test
    public void whenIAmWantRegisterWithEmailThatAreNotAssociatedWithAnyWorkedMustThrowException(){
        newUser1 = NewUser.createANewUser("carlos22","1234","cosme.fulanito@gmail.com");
        try {
            registerService.registerUser(newUser1);
            fail();
        } catch (RuntimeException ex) {
            assertThat(ex.getMessage()).isEqualTo(WorkerService.errorWhenDoNotExistAWorkerWithThisEmail());
        }
    }
}
