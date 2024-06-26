package com.example.jogo_da_velha_2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.jogo_da_velha_2.R;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Objects;

public class Loading extends AppCompatActivity {
    private boolean hasGameStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        backBtn();
        startCheckingMatchStatus();
    }

    private void backBtn() {
        ImageButton voltar = findViewById(R.id.button3);
        Button cancelar = findViewById(R.id.button4);

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hasGameStarted = true;
        
                Intent intent = new Intent(Loading.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    private void startCheckingMatchStatus() {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (!hasGameStarted) {
                    checkMatchState();
                    handler.postDelayed(this, 5000);
                }
            }
        };
        handler.post(runnable);
    }

    private void checkMatchState() {
        if (hasGameStarted) {
            return;
        }

        String matchId = getCurrentMatchId();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Match");
        query.getInBackground(matchId, (match, e) -> {
            if (e == null) {
                if (match != null && match.getBoolean("state")) {
                    hasGameStarted = true;
                    ParseUser playerX = match.getParseUser("player_x");
                    ParseUser playerO = match.getParseUser("player_o");
                    String tableId = match.getParseObject("table").getObjectId();
                    navigateToGame(matchId, playerX.getObjectId(), playerO.getObjectId(), tableId);
                }
            } else {
                e.printStackTrace();
            }
        });
    }

    private void navigateToGame(String matchId, String playerXId, String playerOId, String tableId) {
        Intent intent = new Intent(Loading.this, Game.class);
        intent.putExtra("matchId", matchId);
        intent.putExtra("playerXId", playerXId);
        intent.putExtra("playerOId", playerOId);
        intent.putExtra("tableId", tableId);
        startActivity(intent);
        finish();
    }

    private String getCurrentMatchId() {
        return getIntent().getStringExtra("matchId");
    }
}