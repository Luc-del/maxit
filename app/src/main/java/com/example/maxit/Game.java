package com.example.maxit;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
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
import java.util.HashMap;
import java.util.List;


import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.min;

public class Game extends Activity implements AdapterView.OnItemClickListener {

    int Nx;
    int Ny;

    boolean vsbot = false;
    boolean bot_begins;
    boolean rotate_text = false;

    int N_hidden_cells = 0;
    int N_neutral_cells = 0;

    boolean playerTurn = true;
    List<Integer> available_positions  = new ArrayList<>();
    int score1 = 0;
    int score2 = 0;

    int color_player1;
    int color_player2;
    String string_player1;
    String string_player2;
    String string_player1_wins;
    String string_player2_wins;

    TextView view_score1;
    TextView view_score2;
    TextView turn_info;

    GridView gridview;
    GridViewAdapter gridviewAdapter;
    ArrayList<Cell> data = new ArrayList<Cell>();

    double XFillRatio = 0.84;
    double YFillRatio = 0.72;

    Resources res;
    String res_hidden;
    String res_neutral;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.game);

        //Initialize data and gridview
        initParameters();
        initView();
        fillData();
        setDataAdapter();

        log("parameters","\nNx "+Nx+
                "\nNy "+Ny+
                "vsbot "+vsbot+
                "\nrotate_text "+rotate_text+
                "\nN_hidden_cells "+N_hidden_cells+
                "\nN_neutral_cells "+N_neutral_cells
        );
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
    //        Initialization       //
    //                             //
    /////////////////////////////////

    private void initParameters() {
        Bundle b = getIntent().getExtras();
        if(b != null) {
            Nx = b.getInt("Nx");
            Ny = b.getInt("Ny");
            vsbot = b.getBoolean("bot");
            rotate_text = b.getBoolean("rotate_text");
            N_hidden_cells = b.getInt("hidden_cells");
            N_neutral_cells = b.getInt("neutral_cells");
        }

        bot_begins = (new Random()).nextBoolean();

        res = getResources();
        res_hidden = res.getString(R.string.hidden_cells);
        res_neutral = res.getString(R.string.neutral_cells);

    }

    // Initialize the GUI Components
    private void initView()
    {
        gridview = (GridView) findViewById(R.id.gridView);
        gridview.setNumColumns(Nx);
        gridview.setOnItemClickListener(this);
    }

    // Instantiate cells
    private void fillData()
    {
        List<Integer> cell_values = random_distribution_values();
        HashMap<String,ArrayList<Integer>> special_cells = distribution_cells();

        for(int i = 0; i < Ny; i++) {
            for(int j = 0; j < Nx; j++) {
                int idx = j+i*Nx;
                int val = cell_values.get(idx);
                boolean ishidden = special_cells.get(res_hidden).contains(idx);
                boolean isneutral = special_cells.get(res_hidden).contains(idx);

                data.add(new Cell(val,ishidden,isneutral));
            }
        }
    }

    //Random distributions
    private ArrayList<Integer> linear_values() {
        ArrayList<Integer> values = new ArrayList<>();
        for(int i = 0; i < Ny; i++)
            for(int j = 0; j < Nx; j++)
                values.add(i*Nx+j);

        return values;
    }

    private ArrayList<Integer> random_distribution_values() {
        ArrayList<Integer> values = new ArrayList<>();
        for (int i = 0; i < Ny; i++) {
            for (int j = 0; j < Nx; j++) {
                int k = (int) (Nx * Ny * Math.random()) + 1;
                while (values.contains(k)) k = (int) (Nx * Ny * Math.random()) + 1;
                values.add(k);
            }
        }
        return values;
    }

    private HashMap<String,ArrayList<Integer>> distribution_cells() {
        HashMap<String,ArrayList<Integer>> indexes = new HashMap<>();
        indexes.put(res_hidden,  new ArrayList<Integer>());
        indexes.put(res_neutral,  new ArrayList<Integer>());

        while(indexes.get(res_hidden).size()<N_hidden_cells) {
            int k = (int) (Nx * Ny * Math.random()) + 1;
            if(!indexes.get(res_hidden).contains(k)) indexes.get(res_hidden).add(k);
        }

        while(indexes.get(res_neutral).size()<N_neutral_cells) {
            int k = (int) (Nx * Ny * Math.random()) + 1;
            if(!indexes.get(res_neutral).contains(k) && !indexes.get(res_hidden).contains(k)) indexes.get(res_neutral).add(k);
        }

        return indexes;
    }


    // Set the Data Adapter
    private void setDataAdapter()
    {
        gridviewAdapter = new GridViewAdapter(getApplicationContext(), R.layout.game, data);

        //Determine size of cells
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        int cell_height = (int)(YFillRatio*height/Ny);
        int cell_width = (int)(XFillRatio*width/Nx);
        //Check if we can put square cells
        if (Nx*cell_width < YFillRatio*height) cell_height = min(cell_width,cell_height);
        gridviewAdapter.cell_height = cell_height;

        gridview.setAdapter(gridviewAdapter);
    }

    private void set_turn_info(String str) {
        turn_info.setText(str);
    }

    private void displayViews() {

        view_score1 = findViewById(R.id.player1_score);
        view_score2 = findViewById(R.id.player2_score);

        color_player1 = res.getColor(R.color.player1);
        color_player2 = res.getColor(R.color.player2);

        string_player1 = res.getString(R.string.turn_player1);
        string_player2 = res.getString(R.string.turn_player2);

        string_player1_wins = res.getString(R.string.player1_wins);
        string_player2_wins = res.getString(R.string.player2_wins);

        //vs bot : order may be swapped in bot_first_play if bot begins
        if(vsbot) {
            if(bot_begins) {
                string_player1 = getResources().getString(R.string.turn_bot);
                string_player2 = res.getString(R.string.turn_playervsbot);

                string_player1_wins = res.getString(R.string.bot_wins);
                string_player2_wins = res.getString(R.string.playervsbot_wins);
            }
            else {
                string_player1 = res.getString(R.string.turn_playervsbot);
                string_player2 = res.getString(R.string.turn_bot);

                string_player1_wins = res.getString(R.string.playervsbot_wins);
                string_player2_wins = res.getString(R.string.bot_wins);
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

    private CellGrid getCell(int position) {
        return (CellGrid) gridview.getChildAt(position);
    }

    private void resetCellsColor() {
        for(int i = 0; i < available_positions.size(); i++) {
            getCell(available_positions.get(i)).setBackground(getResources().getDrawable(R.drawable.cellgrid));
        }
    }

    private void setPlayed(int position) {
        data.get(position).setPlayed();
        getCell(position).setBackground(getResources().getDrawable(R.drawable.cellgrid_played));
        getCell(position).setText(data.get(position).getDisplayValue());
    }

    //After each play, update list of available cells (highlight)
    private void setAvailableCells (int position) {

        available_positions.clear();
        available_positions = getPlayableCells(position,playerTurn);
        for(int i = 0; i < available_positions.size(); i++) getCell(available_positions.get(i)).setBackground(getResources().getDrawable(R.drawable.cellgrid_avail_ripple));
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

    private void rotateCells() {
        for (int p = 0; p < Nx * Ny; p++) getCell(p).setRotation(getCell(p).getRotation()+180);
    }

    private int guessHiddenValue() {
        ArrayList<Integer> remainingHiddenValues = new ArrayList<Integer>();
        for(int i = 0; i < data.size(); i++) {
            if (data.get(i).isHidden()) remainingHiddenValues.add(data.get(i).getValue());
        }

        int mean = 0;
        for(int i=0; i < remainingHiddenValues.size(); i++) {
            mean += remainingHiddenValues.get(i);
        }

        return mean/remainingHiddenValues.size();
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
            gridview.setEnabled(false);

            if (playerTurn) turn_info.setText(string_player2);
            else turn_info.setText(string_player1);

            //Play
            final boolean keep_playing = Play(position);

            if (rotate_text) rotateCells();

            new Thread(new Runnable() {
                public void run() {
                    if (keep_playing && vsbot) bot_play();
                    gridview.post(new Runnable() {
                        @Override
                        public void run() {
                            gridview.setEnabled(true);
                        }
                    });
                }
            }).start();


        }
        else toast(getResources().getString(R.string.unavailable_value)+" : "+data.get(position).getDisplayValue());
    }

    /////////////////////////////////
    //                             //
    //     Gameplay and Mechanics  //
    //                             //
    /////////////////////////////////

    public boolean Play(final int position) {

        final int value = data.get(position).getValue();
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

            //get cells value the opponent can play
            ArrayList<Integer> opponent_cells = getPlayableCells(position,opponentTurn);
            for(int k=0;k<opponent_cells.size();k++) {
                Cell tmpCell = data.get(opponent_cells.get(k));
                if (tmpCell.isHidden()) {
                    int guessedValue = guessHiddenValue();
                    opponent_cells.set(k,guessedValue);
                    log("bot"," guessing value "+guessedValue);
                }
                else opponent_cells.set(k,tmpCell.getValue());
            }

            //compute outcome
            log("bot"," opponent_cells "+Arrays.toString(opponent_cells.toArray()));
            int outcome = data.get(position).getValue();
            if(!opponent_cells.isEmpty()) outcome -= Collections.max(opponent_cells);

            log("bot"," Value "+data.get(position).getValue()+" outcome "+outcome);
            if (outcome >= max_points) {
                max_points = outcome;
                to_play = position;
            }
        }

        sleep(1000);
        final int bot_play = to_play;
        gridview.post(new Runnable() {
            @Override
            public void run() {
                Play(bot_play);
            }
        });
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
        Play(position);
    }

    private void End() {
        TextView end = ((TextView) findViewById(R.id.turn));
        if(score1>score2) {
            set_turn_info(string_player1_wins);
            end.setTextColor(color_player1);
        }
        else if(score1<score2) {
            set_turn_info(string_player2_wins);
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
