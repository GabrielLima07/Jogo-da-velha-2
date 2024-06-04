package com.example.jogo_da_velha_2.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jogo_da_velha_2.R;
import com.example.jogo_da_velha_2.activities.Login;
import com.example.jogo_da_velha_2.activities.MainActivity;
import com.example.jogo_da_velha_2.activities.Ranking;
import com.parse.ParseUser;

import java.util.Objects;

public class Menu extends Fragment {
    public Menu() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        finishFragement(view);
        ranking(view);

        return view;
    }

    private void finishFragement(View view) {
        ImageView closeBtn = view.findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .remove(Menu.this)
                        .commit();
            }
        });
    }

    private void ranking(View view) {
        TextView rankingBtn = view.findViewById(R.id.menuRanking);
        rankingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Ranking.class);
                startActivity(intent);
            }
        });
    }

}