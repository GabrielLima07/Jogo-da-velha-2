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

import android.widget.EditText;

import com.example.jogo_da_velha_2.activities.Login;

@RunWith(MockitoJUnitRunner.Silent.class)
public class TestLogin {
    private Login loginMock;

    @Before
    public void setUp() {
        this.loginMock = Mockito.mock(Login.class);
    }

    @Test
    public void testGetTextAndTrimEditText() {
        EditText editText = Mockito.mock(EditText.class);
        editText.setText("    Gabriel      ");
        when(loginMock.getTextAndTrimEditText(editText)).thenReturn("Gabriel");

        String trimmedText = "Gabriel";

        assertEquals(trimmedText, loginMock.getTextAndTrimEditText(editText));
    }

    @Test
    public void testValidateInputsTrue() {
        EditText nickname = Mockito.mock(EditText.class);
        EditText password = Mockito.mock(EditText.class);
        when(loginMock.validateInputs(nickname, password)).thenReturn(true);

        boolean result = loginMock.validateInputs(nickname, password);

        assertTrue(result);
    }

    @Test
    public void testValidateInputsFalse() {
        EditText nickname = Mockito.mock(EditText.class);
        EditText password = Mockito.mock(EditText.class);
        when(loginMock.validateInputs(nickname, password)).thenReturn(false);

        boolean result = loginMock.validateInputs(nickname, password);

        assertFalse(result);
    }
}
