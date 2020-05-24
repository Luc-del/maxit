package com.example.maxit;

import android.util.Log;
import android.widget.TextView;

public class Cell {

    static int counter = 0;
    public int value;
    public boolean played = false;
    public boolean hidden = false;
    public int id;

    public Cell(int x){
        value = x;
        id = counter;
        counter++;
    }

    public Cell(int x, boolean ishidden){
        value = x;
        id = counter;
        counter++;
        hidden = ishidden;
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

    public boolean isHidden() {
        return hidden;
    }
}
