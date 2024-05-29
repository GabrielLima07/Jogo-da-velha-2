package com.example.jogo_da_velha_2.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.jogo_da_velha_2.R;
import com.parse.LogInCallback;
import com.parse.ParseUser;

import java.util.Objects;

public class Login extends AppCompatActivity {

    private EditText lsNickname;
    private EditText lsPassword;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signUpActivityLink();
        startViews();
        login();
    }

    private void signUpActivityLink() {
        Intent i = new Intent(Login.this, SignUp.class);
        TextView link = findViewById(R.id.signUpActivityLink);
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });
    }

    private void startViews() {
        lsNickname = findViewById(R.id.lsNickname);
        lsPassword = findViewById(R.id.lsPassword);
        loginBtn = findViewById(R.id.loginBtn);
    }

    private boolean validateInputs() {
        String nickanme = lsNickname.getText().toString().trim();
        String password = lsPassword.getText().toString().trim();

        if (nickanme.isEmpty()) {
            lsNickname.setError("Nickname é obrigatório");
            lsNickname.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            lsPassword.setError("Senha é obrigatório");
            lsPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void sendLoginRequest(String username, String password) {
        RelativeLayout layout = findViewById(R.id.lsLayout);
        ProgressBar progressBar = new ProgressBar (Login.this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(progressBar, params);
        progressBar.setVisibility(View.VISIBLE);

        ParseUser.logInInBackground(username, password, (parseUser, e) -> {
            progressBar.setVisibility(View.GONE);
            if (parseUser != null) {
                showAlert("Logado com sucesso", "Bem-vindo " + username + "!", parseUser);
            } else {
                ParseUser.logOut();
                Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void login() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = lsNickname.getText().toString().trim();
                String password = lsPassword.getText().toString().trim();

                if (validateInputs()) {
                    sendLoginRequest(nickname, password);
                }
            }
        });
    }

    private void showAlert(String title,String message, ParseUser parseUser){
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        intent.putExtra("objectId", parseUser.getObjectId());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
}