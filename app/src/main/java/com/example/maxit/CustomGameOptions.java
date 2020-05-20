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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomGameOptions extends Activity {

    RadioGroup bot;
    ViewFlipper flipper;

    HorizontalNumberPicker linespicker;
    HorizontalNumberPicker columnspicker;

    Button play;

    int Nx = 5;
    int Ny = 5;

    private View.OnClickListener launch_game = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Ny = linespicker.getValue();
            Nx = columnspicker.getValue();

            Intent intent = new Intent(CustomGameOptions.this, Game.class);
            boolean bot_selected = false;
            if(bot.getCheckedRadioButtonId()==R.id.playvsbot) bot_selected = true;
            Bundle b = Game.CreateBundle(Nx,Ny,bot_selected);
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

        setContentView(R.layout.custom_game_options);

        bot = findViewById(R.id.chose_bot);
        flipper = findViewById(R.id.view_flipper);
        linespicker = findViewById(R.id.linesPicker);
        columnspicker = findViewById(R.id.columnsPicker);
        play = findViewById(R.id.launch_game);


        linespicker.setMaxValue(9);
        linespicker.setMinValue(3);
        linespicker.setValue(Nx);

        columnspicker.setMaxValue(9);
        columnspicker.setMinValue(3);
        columnspicker.setValue(Ny);


        bot.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                flipper.showNext();
            }
        });

        play.setOnClickListener(launch_game);
    }


}