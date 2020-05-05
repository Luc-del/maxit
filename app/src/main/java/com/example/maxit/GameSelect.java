package com.example.maxit;


import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameSelect extends AppCompatActivity {

    Button bot;
    Button local;
    Button online;
    Button size;

    private View.OnClickListener UNAVAILABLE = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), "Mode de jeu non disponible", Toast.LENGTH_SHORT).show();
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.gameselect);

        bot = findViewById(R.id.brainvsbot);
        local = findViewById(R.id.local);
        online = findViewById(R.id.online);
        size = findViewById(R.id.size);

        local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GameSelect.this, Game.class);
                Bundle b = new Bundle();
                b.putInt("Nx", 3);
                b.putInt("Ny", 4);
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
        });

        bot.setOnClickListener(UNAVAILABLE);
        online.setOnClickListener(UNAVAILABLE);
    }


}