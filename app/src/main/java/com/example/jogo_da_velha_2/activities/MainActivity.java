package com.example.jogo_da_velha_2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.jogo_da_velha_2.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        profile();
        play();
        ranking();
    }

    private void ranking() {
        Button ranking_btn = findViewById(R.id.rankingBtn);

        Intent i = new Intent(this, Ranking.class);

        ranking_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });
    }

    private void play() {
        Button play_btn = findViewById(R.id.playBtn);

        Intent i = new Intent(this, Loading.class);

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });
    }

    private void profile() {
        ImageView profile_pic = findViewById(R.id.profile_pic);
        LinearLayout user_info = findViewById(R.id.user_info);

        Intent i = new Intent(this, Profile.class);

        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });

        user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });
    }
}