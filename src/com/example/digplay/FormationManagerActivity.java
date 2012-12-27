package com.example.digplay;

import java.util.ArrayList;

import com.businessclasses.Constants;
import com.businessclasses.Field;
import com.businessclasses.Formation;
import com.businessclasses.FormationAdapter;
import com.database.DigPlayDB;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class FormationManagerActivity extends Activity implements OnItemClickListener, OnClickListener {
	private ListView formationsList;
	private ArrayList<Formation> formations;
	private Button addFormation;
	private TextView title;
	private ProgressDialog dialog;
	private Handler handler;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.formation_manager);
	    runWaitDialog();
	    handler = new Handler();
	    setWidgets();
	}
	private void setWidgets() {
		Runnable runner = new Runnable(){
			public void run() {
				handler.post(new Runnable(){
					public void run(){
						setListView();
						setButton();
						setTextView();
						stopWaitDialog();
					}
				});
			}
	    };
	    new Thread(runner).start();
		
	}
	private void setTextView() {
		title = (TextView)findViewById(R.id.fm_title);
		title.setTextColor(Color.WHITE);
	}
	private void setButton() {
		addFormation = (Button)findViewById(R.id.fm_add_formation);
		addFormation.setOnClickListener(this);
	}
	private void setListView() {
		formationsList = (ListView)findViewById(R.id.formations_list);
		formations = new ArrayList<Formation>();
		formations = DigPlayDB.getInstance(getBaseContext()).getFormations();
		if(formations.isEmpty()){
			Field f = new Field();
			Formation emptyFormation = new Formation("There are no saved formations",f);
			formations.add(emptyFormation);
		}
		FormationAdapter adapter = new FormationAdapter(this,R.layout.formation_listview_item_row,formations);
		formationsList.setAdapter(adapter);
		formationsList.setOnItemClickListener(this);
	}
	public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
		Formation selectedFormation = (Formation)adapter.getItemAtPosition(position);
		Intent intent = new Intent(v.getContext(),EditorActivity.class);
		intent.putExtra("Formation", selectedFormation);
		startActivity(intent);
	}
	public void onClick(View v) {
		Intent intent  = new Intent(v.getContext(),EditorActivity.class);
		startActivity(intent);
	}
	private void runWaitDialog() {
		dialog = new ProgressDialog(this);
		dialog.setMessage("Loading formations...");
		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
		dialog.show();
	}
	private void stopWaitDialog(){
		dialog.dismiss();
	}
}
