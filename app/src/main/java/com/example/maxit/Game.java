package com.example.maxit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;


import java.util.ArrayList;

public class Game extends Activity implements AdapterView.OnItemClickListener {

    boolean playerTurn = true;
    List<Integer> available_positions  = new ArrayList<>();

    int score1 = 0;
    int score2 = 0;

    TextView view_score1;
    TextView view_score2;

    int Nx = 8;
    int Ny = 8;

    int color_avail;
    int color_played;
    int color_player1;
    int color_player2;

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

        Bundle b = getIntent().getExtras();
        if(b != null) {
            Nx = b.getInt("Nx");
            Ny = b.getInt("Ny");
        }

        view_score1 = findViewById(R.id.player1_score);
        view_score2 = findViewById(R.id.player2_score);
        color_avail = getResources().getColor(R.color.available_cell);
        color_played = getResources().getColor(R.color.played_cell);
        color_player1 = getResources().getColor(R.color.player1);
        color_player2 = getResources().getColor(R.color.player2);


        initView();
        fillData();
        setDataAdapter();

        // Set all cells as available
        for(int i = 0; i < Nx*Ny; i++)
        {
            available_positions.add(i);
        }
    }

    // Initialize the GUI Components
    private void initView()
    {
        gridview = (GridView) findViewById(R.id.gridView);
        gridview.setNumColumns(Nx);
        gridview.setOnItemClickListener(this);
    }

    // Insert data
    private void fillData()
    {
        List<Integer> given = new ArrayList<>();
        for(int i = 0; i < Ny; i++)
        {
            for(int j = 0; j < Nx; j++) {
//                int k = i*Nx+j;
                int k =(int)(Nx*Ny*Math.random())+1;
                while(given.contains(k)) k =(int)(Nx*Ny*Math.random())+1;
                data.add(new Cell(k));
                given.add(k);
            }
        }
    }

    // Set the Data Adapter
    private void setDataAdapter()
    {
        gridviewAdapter = new GridViewAdapter(getApplicationContext(), R.layout.game, data);
        gridview.setAdapter(gridviewAdapter);
        gridview.setVerticalSpacing(100);
    }


    private void resetCellsColor() {
        for(int i = 0; i < available_positions.size(); i++) {
            getCell(available_positions.get(i)).setBackgroundColor(0);
        }
    }

    private TextView getCell(int position) {
        return (TextView) gridview.getChildAt(position);
    }

    //After each play, update list of avaible cells (highlight)
    private void setAvailableCells (int position) {

        available_positions.clear();

        int boundary = Nx*Ny;
        int step  = Nx;
        //Ligne
        if (playerTurn) {
            step = 1;
            boundary = Nx*(position/Nx+1);
            position = Nx*(position/Nx);
        }
        else position%=Nx;

        while (position<boundary) {
            if (!data.get(position).isPlayed()) {
                available_positions.add(position);
                getCell(position).setBackgroundColor(color_avail);
            }
            position+=step;
        }
        log("avail",Arrays.toString(available_positions.toArray()));
    }

    private void End() {

        TextView end = ((TextView) findViewById(R.id.turn));
        if(score1>score2) {
            end.setText(R.string.player1_wins);
            end.setTextColor(color_player1);
        }
        else if(score1<score2) {
            end.setText(R.string.player2_wins);
            end.setTextColor(color_player2);
        }
        else end.setText(R.string.draw);

        //Make grid disappear
        gridview.setVisibility(View.INVISIBLE);

        // Make button appear
        Button play_again = findViewById(R.id.play_again);
        play_again.setVisibility(View.VISIBLE);
        play_again.setClickable(true);

        play_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });

    }

    @Override
    public void onItemClick(final AdapterView<?> arg0, final View view, final int position, final long id)
    {
        int v = data.get(position).getValue();
        Log.d("Position clicked ", " "+position);

        if (available_positions.contains(position)) {

            if (playerTurn) {
                score1 += v;
                view_score1.setText(Integer.toString(score1));
                ((TextView) findViewById(R.id.turn)).setText(R.string.turn_player2);
                getCell(position).setTextColor(color_player1);
            } else {
                score2 += v;
                view_score2.setText(Integer.toString(score2));
                ((TextView) findViewById(R.id.turn)).setText(R.string.turn_player1);
                getCell(position).setTextColor(color_player2);
            }

            playerTurn = !playerTurn;
            data.get(position).setPlayed();

            resetCellsColor();
            setAvailableCells(position);
            getCell(position).setBackgroundColor(color_played);
        }
        else toast("Unavailable value : "+v);

        // end game
        if(available_positions.isEmpty()) End();;
    }

    public void toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void log(String tag, String msg) {
        Log.d(tag, msg);
    }


}
