package com.businessclasses;


import java.util.ArrayList;

import com.database.Play;
import com.example.digplay.PlayViewActivity;
import com.example.digplay.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayAdapter extends ArrayAdapter<Play>{
	
    Context context; 
    int layoutResourceId;    
    ArrayList<Play> plays = null;
    
    public PlayAdapter(Context context, int layoutResourceId, ArrayList<Play> plays) {
        super(context, layoutResourceId,plays);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.plays = plays;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ContactHolder holder = null;
        if(row == null){
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new ContactHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.topTitle = (TextView)row.findViewById(R.id.toptext);
            holder.bottomTitle = (TextView)row.findViewById(R.id.bottomtext);
            
            row.setTag(holder);
        }
        else{
            holder = (ContactHolder)row.getTag();
        }
        Play play = plays.get(position);
        if(play.getType().equals("RUN")){
        	holder.imgIcon.setImageResource(R.drawable.rush_pic);
        }else{
        	holder.imgIcon.setImageResource(R.drawable.pass_pic);
        }
        holder.topTitle.setText(play.getPlayName());
        holder.topTitle.setTextSize(22);
        holder.topTitle.setTypeface(null, Typeface.BOLD);
        
        holder.bottomTitle.setText(play.getFormationName());
        row.setBackgroundColor(Color.WHITE);
        
        PlayViewActivity.plays = plays;
              
        return row;
    }
    
    private static class ContactHolder{
        ImageView imgIcon;
        TextView topTitle;
        TextView bottomTitle;
    }
}
