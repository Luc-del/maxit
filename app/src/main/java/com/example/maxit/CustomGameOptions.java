package com.example.maxit;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import static com.example.maxit.ParametersBundleCreator.CreateBundle;

public class CustomGameOptions extends Activity {

    TextView vsbot_text;
    TextView vsplayer_text;

    androidx.appcompat.widget.SwitchCompat bot_selector;
    ViewFlipper flipper;

    HorizontalNumberPicker linespicker;
    HorizontalNumberPicker columnspicker;

    HorizontalNumberPicker hidden_cells;

    Button play;

    int Nx = 5;
    int Ny = 5;

    private View.OnClickListener launch_game = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.requestFocusFromTouch();

            Ny = linespicker.getValue();
            Nx = columnspicker.getValue();

            Intent intent = new Intent(CustomGameOptions.this, Game.class);

            boolean bot_selected = !bot_selector.isChecked();
            CheckBox rotate = findViewById(R.id.switch_text_orientation);
            boolean switch_text_rotation = !bot_selected && rotate.isChecked();
            int N_hidden_cells = hidden_cells.getValue();

            Bundle b = CreateBundle(Nx,Ny,bot_selected,switch_text_rotation,N_hidden_cells);
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

        vsbot_text = findViewById(R.id.vsbot_text);
        vsplayer_text = findViewById(R.id.vsplayer_text);
        bot_selector = findViewById(R.id.chose_bot);
        flipper = findViewById(R.id.view_flipper);
        linespicker = findViewById(R.id.linesPicker);
        columnspicker = findViewById(R.id.columnsPicker);
        hidden_cells = findViewById(R.id.hidden_cells);
        play = findViewById(R.id.launch_game);

        linespicker.setMinMaxValues(3,9);
        linespicker.setValue(Nx);

        columnspicker.setMinMaxValues(3,9);
        columnspicker.setValue(Ny);

        hidden_cells.setMinMaxValues(0,Nx*Ny);
        hidden_cells.setValue(0);

        linespicker.setCustomListener(new HorizontalNumberPicker.CustomListener() {
            @Override
            public void onValueChange() {
                Nx = linespicker.getValue();
                hidden_cells.setMaxValue(Nx*Ny);
            }
        });

        columnspicker.setCustomListener(new HorizontalNumberPicker.CustomListener() {
            @Override
            public void onValueChange() {
                Ny = columnspicker.getValue();
                hidden_cells.setMaxValue(Nx*Ny);
            }
        });


        vsbot_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bot_selector.isChecked()) bot_selector.performClick();
            }
        });

        vsplayer_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!bot_selector.isChecked()) bot_selector.performClick();
            }
        });


        bot_selector.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener()
        {
               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                   if(isChecked) SwitchRight();
                   else SwitchLeft();
               }
           });


        play.setOnClickListener(launch_game);
    }


    public void SwitchLeft() {
        flipper.setInAnimation(this, R.anim.slide_in_right);
        flipper.setOutAnimation(this, R.anim.slide_out_left);
        flipper.showPrevious();
    }

    public void SwitchRight() {
        flipper.setInAnimation(this, android.R.anim.slide_in_left);
        flipper.setOutAnimation(this, android.R.anim.slide_out_right);
        flipper.showNext();
    }


}