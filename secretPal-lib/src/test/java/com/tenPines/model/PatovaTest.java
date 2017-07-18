package com.tenPines.model;

import com.tenPines.integration.SpringBaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;


public class PatovaTest extends SpringBaseTest {

    @Autowired
    private SecurityGuard securityGuard;

    @Test
    public void when_i_authentic_with_a_invalid_credential_throw_error_of_autentication_and_i_cant_enter(){
        Credential invalidCredential = Credential.newCredential("kevin", "1234");
            try {
                securityGuard.enterWith(invalidCredential);
                assertTrue("nunca deberia llegar aca", false);
            } catch (RuntimeException ex) {
                assertThat(ex.getMessage()).isEqualTo(SecurityGuard.errorMessageWhenUserOrPasswordIsInvalid());
        }
    }
}
