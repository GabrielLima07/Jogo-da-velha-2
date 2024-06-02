package com.example.jogo_da_velha_2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.jogo_da_velha_2.R;

public class Game extends AppCompatActivity {
    ImageButton seta;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        seta = findViewById(R.id.seta);
        Intent i = new Intent(this, MainActivity.class);
        seta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Game.this)
                        .setTitle("Sair da partida")
                        .setMessage("Tem certeza que quer sair da partida?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(i);
                            }
                        })
                        .setNegativeButton("Não", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }
}