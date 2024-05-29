package com.example.jogo_da_velha_2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jogo_da_velha_2.R;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class Profile extends AppCompatActivity {
    private TextView partidasJogadas;
    private TextView userName;
    private TextView winCount;
    private TextView ranking;
    private TextView amigos;
    private ImageView profilePic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ParseUser currentUser = ParseUser.getCurrentUser();
        String userId = currentUser.getObjectId();
        backBtn();
        getUserDetails(userId);
    }

    private void backBtn() {
        ImageButton voltar = findViewById(R.id.button);

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void getUserDetails(String objectId) {
        partidasJogadas = findViewById(R.id.partidasJogadas);
        userName = findViewById(R.id.user_name);
        winCount = findViewById(R.id.winCount);
        ranking = findViewById(R.id.ranking);
        amigos = findViewById(R.id.amigos);
        profilePic = findViewById(R.id.profile_pic);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");

        query.getInBackground(objectId, (object, e) -> {
            if (e == null) {
                if (object != null) {
                    userName.setText(object.getString("username"));
                    winCount.setText(String.valueOf(object.getInt("winCount")));
                    ranking.setText(String.valueOf(object.getInt("ranking")));
                    amigos.setText(String.valueOf(object.getInt("amigos")));
                    partidasJogadas.setText(String.valueOf(object.getInt("partidasJogadas")));

                    ParseFile profilePicture = object.getParseFile("profilePic");
                    if (profilePicture != null) {
                        profilePicture.getDataInBackground((data, e1) -> {
                            if (e1 == null) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                profilePic.setImageBitmap(bitmap);
                            } else {
                                Toast.makeText(Profile.this, "Erro ao carregar a foto: " + e1.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            } else {
                Toast.makeText(Profile.this, "Erro ao buscar o objeto: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}