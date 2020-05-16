package com.example.maxit;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameSelect extends Activity {

    Button bot;
    Button local;
    Button online;
    Button size;
    NumberPicker linespicker;
    NumberPicker columnspicker;

    int Nx = 5;
    int Ny = 5;

    private View.OnClickListener UNAVAILABLE = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), "Mode de jeu non disponible", Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener local_game = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(GameSelect.this, Game.class);
            Bundle b = new Bundle();
            b.putInt("Nx", Nx);
            b.putInt("Ny", Ny);
            boolean bot_selected = false;
            if(v.getId()==bot.getId()) bot_selected = true;
            b.putBoolean("bot",bot_selected);
            intent.putExtras(b);
            startActivity(intent);
            finish();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.gameselect);

        bot = findViewById(R.id.brainvsbot);
        local = findViewById(R.id.local);
        online = findViewById(R.id.online);
        size = findViewById(R.id.size);
        linespicker = findViewById(R.id.linesPicker);
        columnspicker = findViewById(R.id.columnsPicker);

        linespicker.setMaxValue(9);
        linespicker.setMinValue(3);
        linespicker.setValue(Nx);

        columnspicker.setMaxValue(9);
        columnspicker.setMinValue(3);
        columnspicker.setValue(Ny);

        bot.setOnClickListener(local_game);
        online.setOnClickListener(UNAVAILABLE);
        local.setOnClickListener(local_game);

        size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                linespicker.setVisibility(View.VISIBLE);
                linespicker.setClickable(true);

                columnspicker.setVisibility(View.VISIBLE);
                columnspicker.setClickable(true);

                linespicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker p, int i, int i1) {
                        Nx = p.getValue();
                    }
                });

                columnspicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker p, int i, int i1) {
                        Ny = p.getValue();
                    }
                });
            }
        });

    }


}