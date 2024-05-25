package com.example.jogo_da_velha_2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jogo_da_velha_2.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class Login extends AppCompatActivity {

    private EditText lsEmail;
    private EditText lsPassword;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        startViews();
        login();
    }

    private void startViews() {
        lsEmail = findViewById(R.id.lsEmail);
        lsPassword = findViewById(R.id.lsPassword);
        loginBtn = findViewById(R.id.loginBtn);
    }

    private boolean validateInputs() {
        String email = lsEmail.getText().toString().trim();
        String password = lsPassword.getText().toString().trim();

        if (email.isEmpty()) {
            lsEmail.setError("Email é obrigatório");
            lsEmail.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            lsPassword.setError("Senha é obrigatório");
            lsPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void sendLoginRequest(String email, String password) {

    }

    private void login() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = lsEmail.getText().toString().trim();
                String password = lsPassword.getText().toString().trim();

                if (validateInputs()) {
                    sendLoginRequest(email, password);
                }
            }
        });
    }
}