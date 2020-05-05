package com.example.maxit;

import java.util.ArrayList;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapter extends ArrayAdapter<Cell>
{
    Context mContext;
    int resourceId;
    ArrayList<Cell> data = new ArrayList<Cell>();

    public GridViewAdapter(Context context, int layoutResourceId, ArrayList<Cell> data)
    {
        super(context, layoutResourceId, data);
        this.mContext = context;
        this.resourceId = layoutResourceId;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        TextView v = new TextView(mContext);
        Cell cell = getItem(position);
        v.setText(Integer.toString(cell.getValue()));
        v.setGravity(Gravity.CENTER);
        cell.setView(v);

        return v;
    }
}