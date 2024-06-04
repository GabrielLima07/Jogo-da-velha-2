package com.example.jogo_da_velha_2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.example.jogo_da_velha_2.activities.SignUp;


@RunWith(MockitoJUnitRunner.Silent.class)
public class TestSignUp {
    private SignUp signUpTestClass;

    @Before
    public void setUp() {
        this.signUpTestClass = Mockito.mock(SignUp.class);
    }

    @Test
    public void isValidEmailTest() {
        when(signUpTestClass.isValidEmail("teste@gmail.com")).thenReturn(true);

        String email = "teste@gmail.com";
        boolean result = signUpTestClass.isValidEmail(email);
        assertTrue(result);
    }

    @Test
    public void isNotValidEmailTest() {
        when(signUpTestClass.isValidEmail("email")).thenReturn(false);

        String notAnEmail = "email";
        boolean result = signUpTestClass.isValidEmail(notAnEmail);
        assertFalse(result);
    }
}
