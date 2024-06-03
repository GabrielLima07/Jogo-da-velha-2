package com.example.jogo_da_velha_2.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jogo_da_velha_2.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private ShapeableImageView profilePic;
    private TextView username;
    private TextView winCount;
    private String newMatchId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        profile();
        play();
        ranking();
        logout();
        startViews();
        getUserDetails(getIntent().getStringExtra("objectId"));
    }

    private void logout() {
        Button logout = findViewById(R.id.logoutBtn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOutInBackground(e -> {
                    if (e == null)
                       showAlert("Logout realizado", "Até a próxima...");
                });
            }
        });
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

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findAndJoinMatch();
            }
        });
    }

    private void startLoadingActivity() {
        Intent i = new Intent(MainActivity.this, Loading.class);
        i.putExtra("matchId", newMatchId);
        startActivity(i);
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

    private void showAlert(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(MainActivity.this, Login.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

    private void startViews() {
        profilePic = findViewById(R.id.profile_pic);
        username = findViewById(R.id.username);
        winCount = findViewById(R.id.win_count);
    }

    private void getUserDetails(String objectId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");

        query.getInBackground(objectId, (object, e) -> {
            if (e == null) {
                if (object != null) {
                    username.setText(object.getString("username"));
                    winCount.setText(String.valueOf(object.getInt("winCount")));

                    ParseFile profilePicture = object.getParseFile("profilePic");
                    if (profilePicture != null) {
                        profilePicture.getDataInBackground((data, e1) -> {
                            if (e1 == null) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                profilePic.setImageBitmap(bitmap);
                            } else {
                                Toast.makeText(MainActivity.this, "Erro ao carregar a foto: " + e1.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            } else {
                Toast.makeText(MainActivity.this, "Erro ao buscar o objeto: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void createNewMatch() {
        ParseObject table = new ParseObject("Table");
        table.put("boardState", new ArrayList<>(Collections.nCopies(25, "")));

        table.saveInBackground(e -> {
            if (e == null) {
                ParseObject match = new ParseObject("Match");
                match.put("player_x", ParseUser.getCurrentUser());
                match.put("state", false);
                match.put("table", table);
                table.put("match", match);

                match.saveInBackground(e1 -> {
                    if (e1 == null) {
                        Log.d("Match", "Match criada");
                        newMatchId = match.getObjectId();
                        startLoadingActivity();
                    } else {
                        Log.d("Match", "Match não criada!");
                        e1.printStackTrace();
                    }
                });
            } else {
                e.printStackTrace();
            }
        });
    }

    private void findAndJoinMatch() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Match");
        query.whereDoesNotExist("player_o");
        query.whereEqualTo("state", false);
        Log.d("Checkpoint", "findAndJoinMatch");
        query.getFirstInBackground((match, e) -> {
            if (e == null) {
                if (match != null) {
                    Log.d("Match", "Entrando na match");
                    joinMatch(match.getObjectId());
                } else {
                    Log.d("Match", "Criando match");
                    createNewMatch();
                }
            } else {
                Log.d("Match", "Criando match");
                createNewMatch();
                e.printStackTrace();
            }
        });
    }

    private void joinMatch(String matchId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Match");

        query.getInBackground(matchId, (match, e) -> {
            if (e == null) {
                if (match != null && match.getParseUser("player_o") == null) {
                    match.put("player_o", ParseUser.getCurrentUser());
                    match.put("state", true);

                    match.saveInBackground(e1 -> {
                        if (e1 == null) {
                            Log.d("Match", "player_o adicionado");
                            newMatchId = matchId;
                            startLoadingActivity();
                        } else {
                            Log.d("Match", "player_o não adicionado");
                            e1.printStackTrace();
                        }
                    });
                } else {
                    Log.e("Match", "Partida não encontrada ou já possui um segundo jogador.");
                }
            } else {
                e.printStackTrace();
            }
        });
    }

}