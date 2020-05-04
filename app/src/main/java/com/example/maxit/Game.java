package com.example.maxit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Game extends AppCompatActivity {

    boolean playerTurn = true;

    int Nx = 3;
    int Ny = 4;

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

        initView(); // Initialize the GUI Components
        fillData(); // Insert The Data
        setDataAdapter(); // Set the Data Adapter

    }


    // Initialize the GUI Components
    private void initView()
    {
        gridview = (GridView) findViewById(R.id.gridView);
        gridview.setNumColumns(Nx);
//        gridview.setOnItemClickListener(this);
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
    }

//    @Override
//    public void onItemClick(final AdapterView<?> arg0, final View view, final int position, final long id)
//    {
//        String message = "Clicked : " + data.get(position).getTitle();
//        Toast.makeText(getApplicationContext(), message , Toast.LENGTH_SHORT).show();
//    }

}
