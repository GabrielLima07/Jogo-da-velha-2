package com.example.jogo_da_velha_2.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jogo_da_velha_2.R;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.List;

public class Ranking extends AppCompatActivity {

    private TextView[] textViewsRank = new TextView[5];
    private TextView[] textViewsNome = new TextView[5];
    private TextView[] textViewsPontuacao = new TextView[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        
        textViewsRank[0] = findViewById(R.id.textViewRank1);
        textViewsRank[1] = findViewById(R.id.textViewRank2);
        textViewsRank[2] = findViewById(R.id.textViewRank3);
        textViewsRank[3] = findViewById(R.id.textViewRank4);
        textViewsRank[4] = findViewById(R.id.textViewRank5);

        textViewsNome[0] = findViewById(R.id.textViewNome1);
        textViewsNome[1] = findViewById(R.id.textViewNome2);
        textViewsNome[2] = findViewById(R.id.textViewNome3);
        textViewsNome[3] = findViewById(R.id.textViewNome4);
        textViewsNome[4] = findViewById(R.id.textViewNome5);

        textViewsPontuacao[0] = findViewById(R.id.textViewPontuacao1);
        textViewsPontuacao[1] = findViewById(R.id.textViewPontuacao2);
        textViewsPontuacao[2] = findViewById(R.id.textViewPontuacao3);
        textViewsPontuacao[3] = findViewById(R.id.textViewPontuacao4);
        textViewsPontuacao[4] = findViewById(R.id.textViewPontuacao5);

        ImageButton button = findViewById(R.id.buttonBack);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Ranking.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
        carregarDadosRanking();
    }

    private void carregarDadosRanking() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Ranking");
        query.orderByAscending("placing"); // Ordena os resultados pela colocação
        query.setLimit(5); // Limita a consulta a 5 resultados
        query.findInBackground((objects, e) -> {
            if (e == null) {
                for (int i = 0; i < objects.size(); i++) {
                    ParseObject obj = objects.get(i);
                    // Atualizar os TextViews com os dados do ParseObject
                    switch (i) {
                        case 0:
                            textViewsRank[0].setText(obj.getString("placing") + "°");
                            textViewsNome[0].setText(obj.getString("user"));
                            textViewsPontuacao[0].setText(String.valueOf(obj.getBoolean("points")));
                            break;
                        case 1:
                            textViewsRank[1].setText(obj.getString("placing") + "°");
                            textViewsNome[1].setText(obj.getString("user"));
                            textViewsPontuacao[1].setText(String.valueOf(obj.getBoolean("points")));
                            break;
                        case 2:
                            textViewsRank[2].setText(obj.getString("placing") + "°");
                            textViewsNome[2].setText(obj.getString("user"));
                            textViewsPontuacao[2].setText(String.valueOf(obj.getBoolean("points")));
                            break;
                        case 3:
                            textViewsRank[3].setText(obj.getString("placing") + "°");
                            textViewsNome[3].setText(obj.getString("user"));
                            textViewsPontuacao[3].setText(String.valueOf(obj.getBoolean("points")));
                            break;
                        case 4:
                            textViewsRank[4].setText(obj.getInt("placing") + "°");
                            textViewsNome[4].setText(obj.getString("user"));
                            textViewsPontuacao[4].setText(String.valueOf(obj.getInt("points")));
                            break;
                    }
                }
            } else {
                Toast.makeText(Ranking.this, "Erro ao carregar dados: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

