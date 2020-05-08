package com.example.maxit;

import android.app.Activity;
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

public class Game extends Activity implements AdapterView.OnItemClickListener {

    boolean playerTurn = false;
    List<Integer> available_positions  = new ArrayList<>();

    int score1 = 0;
    int score2 = 0;

    TextView view_score1;
    TextView view_score2;

    int Nx;
    int Ny;
    boolean vsbot;

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
            vsbot = b.getBoolean("bot");
        }

        view_score1 = findViewById(R.id.player1_score);
        view_score2 = findViewById(R.id.player2_score);
        color_player1 = getResources().getColor(R.color.player1);
        color_player2 = getResources().getColor(R.color.player2);


        initView();
        fillData();
        setDataAdapter();

        // Set all cells as available
        for(int i = 0; i < Nx*Ny; i++) available_positions.add(i);
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
    }


    private void resetCellsColor() {
        for(int i = 0; i < available_positions.size(); i++) {
            getCell(available_positions.get(i)).setBackground(getResources().getDrawable(R.drawable.cellgrid));
        }
    }

    private TextView getCell(int position) {
        return (TextView) gridview.getChildAt(position);
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
        if (turn) {
            step = 1;
            boundary = Nx*(position/Nx+1);
            position = Nx*(position/Nx);
        }
        else position%=Nx;

        while (position<boundary) {
            Cell cell = data.get(position);
            if (!cell.isPlayed() && position!=ini_pos) cells.add(position);
            position+=step;
        }

        return cells;
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
        log("touch","begin player turn");
        if (available_positions.contains(position)) {
            if (Play(position) && vsbot) bot_play();
        }
        else toast(getResources().getString(R.string.unavailable_value)+" : "+data.get(position).getValue());
    }

    public void toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void log(String tag, String msg) {
        Log.d(tag, msg);
    }

    public boolean Play(int position) {

        int value = data.get(position).getValue();
        if (playerTurn) {
            score1 += value;
            view_score1.setText(Integer.toString(score1));
            ((TextView) findViewById(R.id.turn)).setText(R.string.turn_player2);
            getCell(position).setTextColor(color_player1);
        } else {
            score2 += value;
            view_score2.setText(Integer.toString(score2));
            ((TextView) findViewById(R.id.turn)).setText(R.string.turn_player1);
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

        Play(to_play);
    }

}
