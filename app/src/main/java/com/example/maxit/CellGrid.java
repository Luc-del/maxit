package com.example.maxit;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CellGrid extends TextView {

    public CellGrid(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CellGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CellGrid(Context context) {
        super(context);
        init();
    }

    private void init() {
        Resources res = getResources();
        setBackground(res.getDrawable(R.drawable.cellgrid));
//        setTextColor();
        setTypeface(null, Typeface.BOLD);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
