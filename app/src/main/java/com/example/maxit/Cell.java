package com.example.maxit;

import android.widget.TextView;

public class Cell {

    static int counter;
    public int value;
    public boolean played = false;
    public int id;

    TextView view;

    public Cell(int x){
        counter++;
        value = x;
        id = counter;
    }

    public void setValue(int x) {
        value = x;
    }

    public int getValue() {
        return value;
    }

    public boolean isPlayed() {
        return played;
    }

    public void setPlayed() {
        played = true;
    }

    public void setView(TextView v) {
        view = v;
    }

    public TextView getView() {
        return view;
    }

    public void setBackgroundColor(int c) {
        view.setBackgroundColor(c);
    }

    public void setTextColor(int c) {
        view.setTextColor(c);
    }

}
