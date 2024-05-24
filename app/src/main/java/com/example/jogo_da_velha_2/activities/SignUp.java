package com.example.jogo_da_velha_2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jogo_da_velha_2.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    private EditText suNickname;
    private EditText suEmail;
    private EditText suPassword;
    private EditText suPasswordConfirm;
    private Button signupbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        startViews();
        signUp();
    }

    private void signUp() {
        Intent i = new Intent(this, Login.class);

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    // Enviar os dados para o backend
                    sendSignUpRequest();
                    startActivity(i);
                }
            }
        });
    }

    private void startViews() {
        suNickname = findViewById(R.id.suNickname);
        suEmail = findViewById(R.id.suEmail);
        suPassword = findViewById(R.id.suPassword);
        suPasswordConfirm = findViewById(R.id.suPasswordConfirm);
        signupbtn = findViewById(R.id.signupbtn);
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean validateInputs() {
        String nickname = suNickname.getText().toString().trim();
        String email = suEmail.getText().toString().trim();
        String password = suPassword.getText().toString().trim();
        String passwordConfirm = suPasswordConfirm.getText().toString().trim();

        if (nickname.isEmpty()) {
            suNickname.setError("Nickname é obrigatório");
            suNickname.requestFocus();
            return false;
        }

        if (email.isEmpty() || !isValidEmail(email)) {
            suEmail.setError("Email é obrigatório e deve ser um email válido");
            suEmail.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            suPassword.setError("Senha é obrigatório");
            suPassword.requestFocus();
            return false;
        }

        if (!password.equals(passwordConfirm)) {
            suPasswordConfirm.setError("As senhas não são similares");
            suPasswordConfirm.requestFocus();
            return false;
        }

        return true;
    }

    //TODO: add request ao back
    private void sendSignUpRequest() {
        Toast toast = Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_LONG);
        toast.show();
    }
}