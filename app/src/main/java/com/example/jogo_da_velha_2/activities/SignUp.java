package com.example.jogo_da_velha_2.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.jogo_da_velha_2.R;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


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

        loginActivityLink();
        startViews();
        signUp();
    }

    private void loginActivityLink() {
        Intent i = new Intent(SignUp.this, Login.class);
        TextView link = findViewById(R.id.loginActivityLink);
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });
    }

    private void signUp() {

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = suNickname.getText().toString().trim();
                String email = suEmail.getText().toString().trim();
                String password = suPassword.getText().toString().trim();

                if (validateInputs()) {
                    sendSignUpRequest(nickname, password, email);
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

    public boolean isValidEmail(String email) {
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

    private void sendSignUpRequest(String username, String password, String email) {
        ParseUser user = new ParseUser();

        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    showAlert("Cadastro realizado!", "Agora basta logar " + username +":)");
                } else {
                    ParseUser.logOut();
                    Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void showAlert(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(SignUp.this, Login.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

    public static class PopUpWin {
    }
}