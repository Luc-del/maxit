package com.example.maxit;

import android.util.Log;
import android.widget.TextView;

public class Cell {

    static int counter = 0;
    static String hiddenValue = "?";

    public int value;
    public boolean played = false;
    public boolean hidden = false;
    public boolean neutral = false;
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

    public Cell(int x, boolean ishidden, boolean isneutral){
        value = x;
        id = counter;
        counter++;
        hidden = ishidden;
        neutral = isneutral;
    }

    public void setValue(int x) {
        value = x;
    }

    public int getValue() {
        return value;
    }

    public String getDisplayValue() {
        if (isHidden()) return hiddenValue;
        else return Integer.toString(getValue());
    }

    public boolean isPlayed() {
        return played;
    }

    public void setPlayed() {
        played = true;
        hidden = false;
    }

    public boolean isHidden() {
        return hidden;
    }
}
