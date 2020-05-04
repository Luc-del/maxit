package com.example.maxit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Game extends AppCompatActivity implements AdapterView.OnItemClickListener {

    boolean playerTurn = true;

    int score1 = 0;
    int score2 = 0;

    TextView view_score1;
    TextView view_score2;

    int Nx = 8;
    int Ny = 8;

    GridView gridview;
    GridViewAdapter gridviewAdapter;
    ArrayList<Cell> data = new ArrayList<Cell>();

    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.game);

        view_score1 = findViewById(R.id.player1_score);
        view_score2 = findViewById(R.id.player2_score);

        initView();
        fillData();
        setDataAdapter();

    }


    // Initialize the GUI Components
    private void initView()
    {
        gridview = (GridView) findViewById(R.id.gridView);
        gridview.setNumColumns(Nx);
        gridview.setOnItemClickListener(this);
    }

    // Insert The Data
    private void fillData()
    {
        for(int i = 0; i < Ny; i++)
        {
            for(int j = 0; j < Nx; j++) {
                int k = i*Nx+j;
                data.add(new Cell(k));
            }
        }
    }

    // Set the Data Adapter
    private void setDataAdapter()
    {
        gridviewAdapter = new GridViewAdapter(getApplicationContext(), R.layout.game, data);
        gridview.setAdapter(gridviewAdapter);
        gridview.setVerticalSpacing(100);
//        gridview.setStretchMode(GridView.NO_STRETCH);
    }

    @Override
    public void onItemClick(final AdapterView<?> arg0, final View view, final int position, final long id)
    {
        int v = data.get(position).getValue();
        String message = "Clicked : " + v;
        Toast.makeText(getApplicationContext(), message , Toast.LENGTH_SHORT).show();

        if (playerTurn) {
            score1 += v;
            view_score1.setText(Integer.toString(score1));
            ((TextView)findViewById(R.id.turn)).setText(R.string.turn_player2);
        }
        else {
            score2 += v;
            view_score2.setText(Integer.toString(score2));
            ((TextView)findViewById(R.id.turn)).setText(R.string.turn_player1);
        }

        playerTurn = !playerTurn;


    }

}
