package com.example.digplay;

import java.util.ArrayList;

import com.database.DatabaseHandler;
import com.database.Gameplan;
import com.database.GameplanPlay;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class GameplanManagerActivity extends Activity implements OnItemClickListener, OnClickListener, OnItemSelectedListener{
	private DatabaseHandler db;
	private ListView gameplanLV;
	private ListView playbookLV;
	private ArrayList<String> playbookPlays = new ArrayList<String>();
	private ArrayList<String> gameplanPlays = new ArrayList<String>();
	private ArrayList<String> gameplans = new ArrayList<String>();
	private Button delete;
	private Button deleteGameplan;
	private boolean deletePressed;
	private TextView gpHeader;
	private TextView pbHeader;
	private TextView gmTitle;
	private int positionSelected;
	private Context deleteContext;
	private Context gameplanDeleteContext;
	private Context gameplanAddContext;
	private Button createNewGameplan;
	private Spinner gameplansSpinner;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.gameplan_manager);
	    
	    db = new DatabaseHandler(this);
	    
	    setTexts();
	    
		ArrayList<com.database.Play> listOfPlays = db.getAllPlays();
		for (int i = 0; i < listOfPlays.size(); i++)
		{
			playbookPlays.add(listOfPlays.get(i).getPlayName());
		}
		
		ArrayList<com.database.Gameplan> listOfGameplans = db.getAllGameplans();
		for (int i = 0; i < listOfGameplans.size(); i++)
		{
			gameplans.add(listOfGameplans.get(i).getGameplanName());
		}
		
	    setListViews();
	    setButton();
	    setSpinner();
	    deletePressed = false;
	    startNotification();
	}
	
	private void setSpinner() {
		gameplansSpinner = (Spinner)findViewById(R.id.gm_spinner);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,gameplans);
		gameplansSpinner.setAdapter(adapter);
		gameplansSpinner.setOnItemSelectedListener(this);
	}

	private void setButton() {
		delete = (Button) findViewById(R.id.gm_delete);
		deleteGameplan = (Button)findViewById(R.id.gm_delete_gameplan);
		createNewGameplan = (Button)findViewById(R.id.gm_create_gp);
		createNewGameplan.setOnClickListener(this);
		deleteGameplan.setOnClickListener(this);
	    delete.setOnClickListener(this);
	    delete.setBackgroundColor(Color.LTGRAY);
	}
	private void startNotification() {
		Toast message = Toast.makeText(this, "Tap plays in playbook list to add to gameplan", Toast.LENGTH_LONG);
		message.show();
	}
	private void setTexts() {
		gpHeader = (TextView)findViewById(R.id.gp_manager_gp_text);
		pbHeader = (TextView)findViewById(R.id.gp_manager_pb_text);
		gmTitle = (TextView)findViewById(R.id.gm_title);
		gpHeader.setTextColor(Color.WHITE);
		pbHeader.setTextColor(Color.WHITE);
		gmTitle.setTextColor(Color.WHITE);
	}
	private void setListViews() {
		gameplanLV = (ListView) findViewById(R.id.gm_gameplan);
		playbookLV = (ListView) findViewById(R.id.gm_playbook);
		ArrayAdapter<String> playbookAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,playbookPlays);  
		ArrayAdapter<String> gameplanAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,gameplanPlays); 
		playbookLV.setAdapter(playbookAdapter);
		gameplanLV.setAdapter(gameplanAdapter);
		playbookLV.setOnItemClickListener(this);
		gameplanLV.setOnItemClickListener(this);
		playbookLV.setBackgroundColor(Color.WHITE);
		gameplanLV.setBackgroundColor(Color.WHITE);
	}
	
	public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
		
		boolean isGamePlanClicked = false;
		if(adapter.getId() == gameplanLV.getId())
		{
			isGamePlanClicked = true;
		}
		
		if(!deletePressed)
		{
			String playSelected = (String) adapter.getItemAtPosition(position);
			if(!gameplanPlays.contains(playSelected))
			{
				if (!gameplansSpinner.getAdapter().isEmpty())
				{
					gameplanPlays.add(playSelected);
					resetGameplanList();
					Toast.makeText(this, "Play has been added to the gameplan",Toast.LENGTH_SHORT).show();

					//store to database
					int index = gameplansSpinner.getSelectedItemPosition();
					if(index >= 0)
					{
						GameplanPlay gameplanPlay = new GameplanPlay(gameplansSpinner.getAdapter().getItem(index).toString(), playSelected);
						db.addGameplanPlay(gameplanPlay);
					}
				}
			}
			else
			{
				Builder alert = new AlertDialog.Builder(this);
				alert.setTitle("Caution");
				alert.setMessage("This play has already been added to the game plan");
				alert.setNeutralButton("Close", null);
				alert.show();
			}
		}
		else if(isGamePlanClicked)
		{
			positionSelected = position;
			deleteContext = this;
			verifyDelete();
		}
	}
	private void verifyDelete() 
	{
		Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Caution");
		alert.setMessage("Are you sure you want to delete this play from the gameplan?");
		alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which)
			{
				int index = gameplansSpinner.getSelectedItemPosition();
				String gameplanName = gameplansSpinner.getAdapter().getItem(index).toString();
				String removedPlayName = gameplanPlays.remove(positionSelected);
				db.deleteGameplanPlay(gameplanName, removedPlayName);
					
				ArrayAdapter<String> thisAdapter = new ArrayAdapter<String>(deleteContext, android.R.layout.simple_list_item_1,gameplanPlays);
				gameplanLV.setAdapter(thisAdapter);
			}
		});
		alert.setNegativeButton("Cancel", null);
		alert.show();
	}
	public void onClick(View v) {		
		if(v.getId() == delete.getId()){
			if(!deletePressed){
				delete.setBackgroundColor(Color.YELLOW);
				delete.setText("Tap play to delete");
				deletePressed = true;
			}else{
				delete.setBackgroundColor(Color.LTGRAY);
				delete.setText("Click to enable deleting");
				deletePressed = false;
			}
		}else if(v.getId() == createNewGameplan.getId()){
			gameplanAddContext = this;
			popupForName();
		}else if(v.getId() == deleteGameplan.getId()){
			gameplanDeleteContext = this;
			gameplanDeleteVerify();
		}
	}
	private void gameplanDeleteVerify() 
	{
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Caution");
		alert.setMessage("You have selected to delete the entire gameplan. Are you sure you want to do this? It will be deleted from the database.");
		alert.setPositiveButton("Yes. Delete", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int whichButton) 
			{
			
			if(gameplansSpinner.getChildCount() > 0)
			{
				int index = gameplansSpinner.getSelectedItemPosition();
				String gameplanName = gameplansSpinner.getAdapter().getItem(index).toString();
				db.deleteGameplan(gameplanName);
				db.removeAllGameplanPlaysWithName(gameplanName);
				gameplans.remove(gameplans.indexOf(gameplansSpinner.getAdapter().getItem(index).toString()));
				
				ArrayAdapter<String>adapter = new ArrayAdapter<String>(gameplanDeleteContext,android.R.layout.simple_list_item_1,gameplans);
				gameplansSpinner.setAdapter(adapter);

				gameplanPlays.clear();
				resetGameplanList();
			}
		}
		});
		alert.setNegativeButton("Whoops. No don't delete",null);
		alert.show();
	}

	private void popupForName() 
	{
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Add Gameplan");
		alert.setMessage("Type in name of gameplan to add");
		final EditText input = new EditText(this);
		alert.setView(input);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{		
				String newName = input.getText().toString();

				Gameplan gameplan = new Gameplan(newName);
				db.addGameplan(gameplan);
				
				gameplans.add(newName);
				ArrayAdapter<String>adapter = new ArrayAdapter<String>(gameplanAddContext,android.R.layout.simple_list_item_1,gameplans);
				gameplansSpinner.setAdapter(adapter);
				
				resetGameplanList();
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int whichButton) 
			{
				
			}
		});

		alert.show();
	}

	public void onItemSelected(AdapterView<?> adapter, View v, int position,long arg3)
	{
		int index = gameplansSpinner.getSelectedItemPosition();
		String gameplanSelected = gameplansSpinner.getAdapter().getItem(index).toString();		

		gameplanPlays = db.getAllPlayNamesWithGameplan(gameplanSelected);

		resetGameplanList();

		Toast toast = Toast.makeText(this, gameplanSelected, Toast.LENGTH_LONG);
		toast.show();
	}

	public void onNothingSelected(AdapterView<?> arg0) 
	{
		
	}
	private void resetGameplanList(){
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,gameplanPlays);
		gameplanLV.setAdapter(adapter);
	}

}
