package com.example.maxit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class Grid extends View {

    static private int Ny = 8;
    static private int Nx = 8;
    static private Grid instance = null;

    int height;
    int width;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public Grid(Context context, AttributeSet attrs){
        super(context, attrs);
        paint.setColor(Color.BLACK);
        instance = this;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        height = getHeight();
        width = getWidth();
        for (int i = 0; i < Nx+1; ++i) {
            canvas.drawLine(0, height / Nx*i , width, height / Nx*i, paint);
        }
        for (int i = 0; i < Ny+1; ++i) {
            canvas.drawLine(width / Ny*i, 0, width / Ny*i, height, paint);
        }
        super.onDraw(canvas);
    }

    static void setSize(int nx, int ny) {
        Nx=nx;
        Ny=ny;
    }

    void resize(int nx, int ny) {
        Nx=nx;
        Ny=ny;
    }


    static Grid get() {
        return instance;
    }



}