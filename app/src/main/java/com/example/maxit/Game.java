package com.example.maxit;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
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
import java.util.Collections;
import java.util.List;


import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Game extends Activity implements AdapterView.OnItemClickListener {

    int Nx;
    int Ny;
    boolean vsbot;
    boolean bot_begins;

    boolean playerTurn = true;
    List<Integer> available_positions  = new ArrayList<>();
    int score1 = 0;
    int score2 = 0;

    int color_player1;
    int color_player2;
    String string_player1;
    String string_player2;

    TextView view_score1;
    TextView view_score2;
    TextView turn_info;

    GridView gridview;
    GridViewAdapter gridviewAdapter;
    ArrayList<Cell> data = new ArrayList<Cell>();


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
            vsbot = b.getBoolean("bot");
        }

        bot_begins = (new Random()).nextBoolean();

        //Initialize data and gridview
        initView();
        fillData();
        setDataAdapter();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Set all cells as available
        for(int i = 0; i < Nx*Ny; i++) available_positions.add(i);

        //Check if bot begins
        gridview.post(new Runnable() {
            @Override
            public void run() {
                //Playing against bot : check who begins
                if(vsbot && bot_begins) bot_first_play();
            }
        });

        displayViews();
    }

    /////////////////////////////////
    //                             //
    //        Layout handler       //
    //                             //
    /////////////////////////////////

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
    }

    private void set_turn_info(String str) {
        turn_info.setText(str);
    }

    private void displayViews() {

        Resources res = getResources();
        view_score1 = findViewById(R.id.player1_score);
        view_score2 = findViewById(R.id.player2_score);

        color_player1 = res.getColor(R.color.player1);
        color_player2 = res.getColor(R.color.player2);

        string_player1 = res.getString(R.string.turn_player1);
        string_player2 = res.getString(R.string.turn_player2);

        //vs bot : order may be swapped in bot_first_play if bot begins
        if(vsbot) {
            if(bot_begins) {
                string_player2 = res.getString(R.string.turn_playervsbot);
                string_player1 = getResources().getString(R.string.turn_bot);
            }
            else {
                string_player1 = res.getString(R.string.turn_playervsbot);
                string_player2 = res.getString(R.string.turn_bot);
            }
        }

        turn_info = ((TextView) findViewById(R.id.turn));
        set_turn_info(string_player1);

    }


    /////////////////////////////////
    //                             //
    //       Game interactions     //
    //                             //
    /////////////////////////////////

    private TextView getCell(int position) {
        return (TextView) gridview.getChildAt(position);
    }

    private void resetCellsColor() {
        for(int i = 0; i < available_positions.size(); i++) {
            getCell(available_positions.get(i)).setBackground(getResources().getDrawable(R.drawable.cellgrid));
        }
    }

    private void setPlayed(int position) {
        data.get(position).setPlayed();
        getCell(position).setBackground(getResources().getDrawable(R.drawable.cellgrid_played));
    }

    //After each play, update list of avaible cells (highlight)
    private void setAvailableCells (int position) {

        available_positions.clear();
        available_positions = getPlayableCells(position,playerTurn);
        for(int i = 0; i < available_positions.size(); i++) getCell(available_positions.get(i)).setBackground(getResources().getDrawable(R.drawable.cellgrid_avail));
        log("avail",Arrays.toString(available_positions.toArray()));
    }

    private ArrayList<Integer> getPlayableCells(int position, boolean turn) {

        int ini_pos = position;
        ArrayList<Integer> cells = new ArrayList<Integer>();
        int boundary = Nx*Ny;
        int step  = Nx;

        //Ligne
        if (turn) position%=Nx;
        else {
            step = 1;
            boundary = Nx*(position/Nx+1);
            position = Nx*(position/Nx);
        }

        while (position<boundary) {
            Cell cell = data.get(position);
            if (!cell.isPlayed() && position!=ini_pos) cells.add(position);
            position+=step;
        }

        return cells;
    }



    /////////////////////////////////
    //                             //
    //            Utils            //
    //                             //
    /////////////////////////////////

    public void toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void log(String tag, String msg) {
        Log.d(tag, msg);
    }

    public void sleep(int ms) {
        try {
            Thread.sleep(ms);
        }
        catch(InterruptedException ex)  {
            Thread.currentThread().interrupt();
        }
    }


    /////////////////////////////////
    //                             //
    //      Player Action click    //
    //                             //
    /////////////////////////////////
    @Override
    public void onItemClick(final AdapterView<?> arg0, final View view, final int position, final long id)
    {
        log("touch","begin player turn");
        if (available_positions.contains(position)) {

            if (playerTurn)  turn_info.setText(string_player2);
            else turn_info.setText(string_player1);

            gridview.post(new Runnable() {
                @Override
                public void run() {
                    //Playing against bot : check who begins
                    if (Play(position) && vsbot) bot_play();
                }
            });
        }
        else toast(getResources().getString(R.string.unavailable_value)+" : "+data.get(position).getValue());
    }

    /////////////////////////////////
    //                             //
    //     Gameplay and Mechanics  //
    //                             //
    /////////////////////////////////

    public boolean Play(int position) {

        int value = data.get(position).getValue();
        if (playerTurn) {
            score1 += value;
            view_score1.setText(Integer.toString(score1));
            turn_info.setText(string_player2);
            getCell(position).setTextColor(color_player1);
        } else {
            score2 += value;
            view_score2.setText(Integer.toString(score2));
            turn_info.setText(string_player1);
            getCell(position).setTextColor(color_player2);
        }

        resetCellsColor();
        setAvailableCells(position);

        playerTurn = !playerTurn;
        setPlayed(position);

        if(available_positions.isEmpty()) {
            End();
            return false;
        }

        return true;
    }

    public void bot_play() {
        log("bot","bot playing");
        boolean opponentTurn = !playerTurn;
        //Play the value that maximize the points scored minus the max points the opponent can make on his next play

        int max_points = Integer.MIN_VALUE;
        int to_play = -1;
        for(int i = 0; i < available_positions.size(); i++)
        {
            int position = available_positions.get(i);

            ArrayList<Integer> opponent_cells = getPlayableCells(position,opponentTurn);
            for(int k=0;k<opponent_cells.size();k++) opponent_cells.set(k,data.get(opponent_cells.get(k)).getValue());
            log("bot"," opponent_cells "+Arrays.toString(opponent_cells.toArray()));
            int outcome = data.get(position).getValue()-Collections.max(opponent_cells);
            log("bot"," Value "+data.get(position).getValue()+" outcome "+outcome);
            if (outcome >= max_points) {
                max_points = outcome;
                to_play = position;
            }
        }
        sleep(1000);
        Play(to_play);
    }

    public void bot_first_play() {

        log("bot","Bot plays first");

        int max_value = 0;
        int position = 0;
        for(int i=0;i<data.size();i++) {
            if(data.get(i).getValue()>max_value) {
                max_value = data.get(i).getValue();
                position = i;
            }
        }
        sleep(1000);
        Play(position);
    }

    private void End() {
        TextView end = ((TextView) findViewById(R.id.turn));
        if(score1>score2) {
            set_turn_info(getResources().getString(R.string.player1_wins));
            end.setTextColor(color_player1);
        }
        else if(score1<score2) {
            set_turn_info(getResources().getString(R.string.player2_wins));
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

}
