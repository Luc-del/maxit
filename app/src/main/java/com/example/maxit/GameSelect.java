package com.example.maxit;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import static com.example.maxit.ParametersBundleCreator.CreateBundle;

public class GameSelect extends Activity {

    Button campagne;
    Button fast_game;
    Button custom_game;
    Button online;


    private View.OnClickListener UNAVAILABLE = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), "Mode de jeu non disponible", Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener custom_game_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(GameSelect.this, CustomGameOptions.class);
            startActivity(intent);
            finish();
        }
    };

    private View.OnClickListener fast_game_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(GameSelect.this, Game.class);
            Bundle b = CreateBundle(5,5,true);
            intent.putExtras(b);
            startActivity(intent);
            finish();
        }
    };

//    private View.OnClickListener campagne = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Intent intent = new Intent(GameSelect.this, campagne.class);
//            startActivity(intent);
//            finish();
//        }
//    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.game_select);

        campagne = findViewById(R.id.campagne);
        fast_game = findViewById(R.id.fast_game);
        custom_game = findViewById(R.id.custom_game);
        online = findViewById(R.id.online);

        campagne.setOnClickListener(UNAVAILABLE);
        fast_game.setOnClickListener(fast_game_listener);
        custom_game.setOnClickListener(custom_game_listener);
        online.setOnClickListener(UNAVAILABLE);
    }


}