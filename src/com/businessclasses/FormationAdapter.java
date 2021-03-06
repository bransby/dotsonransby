package com.businessclasses;

import java.util.ArrayList;

import com.database.Formation;
import com.example.digplay.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FormationAdapter extends ArrayAdapter<Formation>{
	
	Context context; 
    int layoutResourceId;    
    ArrayList<Formation> formations = null;
    
    public FormationAdapter(Context context, int layoutResourceId, ArrayList<Formation> formations) {
    	super(context, layoutResourceId,formations);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.formations = formations;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ContactHolder holder = null;
        if(row == null){
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new ContactHolder();
            holder.formationIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.formationName = (TextView)row.findViewById(R.id.toptext);
            
            row.setTag(holder);
        }
        else{
            holder = (ContactHolder)row.getTag();
        }
        Formation formation = formations.get(position);
        byte[] formationByteArray = formation.getBitmap();
        Bitmap bitmap = BitmapFactory.decodeByteArray(formationByteArray, 0, formationByteArray.length);
        holder.formationIcon.setImageBitmap(bitmap);
        holder.formationName.setText(formation.getFormationName());
        row.setBackgroundColor(Color.WHITE);
        return row;
    }
    
    private static class ContactHolder{
        ImageView formationIcon;
        TextView formationName;
    }
	
}
