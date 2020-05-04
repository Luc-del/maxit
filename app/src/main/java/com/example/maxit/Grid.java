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

    Canvas canvas;

    int height;
    int width;

    int Dx;
    int Dy;

    int [][] values;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public Grid(Context context, AttributeSet attrs){
        super(context, attrs);
        paint.setColor(Color.BLACK);
        instance = this;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.canvas=canvas;
        setDistances();
        for (int i = 0; i < Nx+1; ++i) {
            canvas.drawLine(0, Dx*i , width, Dx*i, paint);
        }
        for (int i = 0; i < Ny+1; ++i) {
            canvas.drawLine(Dy*i, 0, Dy*i, height, paint);
        }
        super.onDraw(canvas);
        fill();
    }

    public void setDistances() {
        height = getHeight();
        width = getWidth();
        Dx = height/Nx;
        Dy = width/Ny;
    }

    static void setSize(int nx, int ny) {
        Nx=nx;
        Ny=ny;
        instance.setDistances();
    }

    static Grid get() {
        return instance;
    }

    private float getX(int i) {
        return (float) ((float)Dx*(i+0.5));
    }

    private float getY(int j) {
        return (float) ((float)Dy*(j+0.5));
    }

    private float centerY(String str) {
        return paint.measureText(str)/2;
    }

    private float centerX() {
        return (paint.descent() + paint.ascent()) / 2;
    }

    private void fill(String str, int i, int j) {
        canvas.drawText(str, getY(j)-centerY(str), getX(i)-centerX(), paint);
    }

    public void fill() {

        values = new int[Ny][Nx];

        double relation = Math.sqrt(canvas.getWidth() * canvas.getHeight());
        relation = relation / 250;
        paint.setTextSize((float) (10 * relation));

        for(int i = 0; i < Ny; i++)
        {
            for(int j = 0; j < Nx; j++) {
                int k = i+Ny*j;
                fill(Integer.toString(k),j,i);
                values[j][i] = k;
            }
        }

    }


}