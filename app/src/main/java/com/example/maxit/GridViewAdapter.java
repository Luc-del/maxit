package com.example.maxit;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
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
        Log.d("GridViewAdapter", "Getting view "+position);
        TextView v;
        if(convertView==null) {
            Log.d("GridViewAdapter", "null convertView");
            v = new TextView(mContext);
        }
        else v = (TextView) convertView;

        Cell cell = getItem(position);
        v.setText(Integer.toString(cell.getValue()));
        v.setGravity(Gravity.CENTER);
        cell.setView(v);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(200,100);
        v.setLayoutParams(lp);
        Log.d("GridViewAdapter", "Id "+cell.id +" value "+cell.value);

        return v;
    }
}