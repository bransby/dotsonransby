package com.example.digplay;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import com.businessclasses.Constants;
import com.businessclasses.Field;
import com.businessclasses.Location;
import com.businessclasses.Player;
import com.database.DatabaseHandler;
import com.database.DatabasePlayer;
import com.database.Formation;
import com.database.Play;
import com.database.RouteLocation;
import com.example.digplay.EditorActivity.DrawView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


public class SaveActivity extends Activity implements OnClickListener {
	private DatabaseHandler db;
	
	private String formationName;
	private String playName;
	private Field field;
	private byte[] fieldBitmap;
	
	private EditText enterPlayName;
	private Button submit;
	private Spinner playType;
	
	private TextView title;
	private TextView enterName;
	private TextView enterType;
	
	private Handler handler;
	private ProgressDialog dialog;
	
	private EditText input;
	private AlertDialog.Builder overwriteFormationBuilder;
	private AlertDialog.Builder confirmOverwriteFormationBuilder;
	
	private final static String TOO_MANY_PLAYS_MESSAGE= "You have reached the maximum number of plays you're " +
									"allowed to save in the free version of the app. "  +
									"Download the paid version to have unlimited plays. The " +
									"plays you already created will carryover for this device";
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.save);
	    
	    db = new DatabaseHandler(this);
	    
	    
	    field = EditorActivity.getField();
	    
	    runWaitDialog();
	    handler = new Handler();
	    setWidgets();
	    
	    input = new EditText(this);
	    
	    formationName = FormationManagerActivity.getFormationName();
	    
		if (formationName == null)
		{
			overwriteFormationBuilder = new AlertDialog.Builder(this);
		    
		    confirmOverwriteFormationBuilder = new AlertDialog.Builder(this);
		    
		    overwriteFormationBuilder.setTitle("Add Formation");
		    overwriteFormationBuilder.setMessage("Type in name of formation to add");
			overwriteFormationBuilder.setView(input);
			formationSavePrompt();
		}
	}
	
	@Override
	public void onBackPressed()
	{
		System.gc();
		super.onBackPressed();
	}

	private void formationSavePrompt()
	{   
		overwriteFormationBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{		
				formationName = input.getText().toString();
				
				ArrayList<String> formationNames = db.getAllFormationNames();
			    boolean formationNameExists = false;
			    for (int i = 0; i < formationNames.size(); i++)
			    {
			    	if (formationNames.get(i).equals(formationName))
			    	{
			    		formationNameExists = true;
			    		break;
			    	}
			    }

			    if (formationNameExists)
			    {
			    	confirmOverwriteFormationBuilder.setMessage("Formation \"" + formationName + "\" already exists. Do you wish to overwrite this formation?");
				    confirmOverwriteFormationBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() 
					{
						public void onClick(DialogInterface dialog, int which) 
						{
			    			convertFormationBitmapToByteArray();
			    			
							Formation form = new Formation(formationName, fieldBitmap);
							db.updateFormation(form);
							
							addFormationPlayersToDB();
						}
					});
				    confirmOverwriteFormationBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() 
					{
						public void onClick(DialogInterface dialog, int which) 
						{
							// the following 3 lines are necessary to avoid a memory leak.
							// we need to garbage collect the "old" EditText otherwise we
							// are using the same EditText as the View for multiple
							// formation builders, which is illegal in android.
							overwriteFormationBuilder.setView(null);
							input = new EditText(input.getContext());
							overwriteFormationBuilder.setView(input);
							
							// show the new formation builder asking if we want to overwrite
							// this formation. this allows the user to save the formation as
							// a different name, rather than the dialog going away permanently
							overwriteFormationBuilder.show();
						}
					});
				    confirmOverwriteFormationBuilder.show();
			    }
			    // formation does not exist, add to database
			    else
			    {
			    	convertFormationBitmapToByteArray();
	    			
					Formation form = new Formation(formationName, fieldBitmap);
					db.addFormation(form);
					
					addFormationPlayersToDB();
			    }
			}
		});
		overwriteFormationBuilder.setNegativeButton("I don't want to save this play's formation", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{
				
			}
		});
		
		overwriteFormationBuilder.show();
	}
	
	private void setWidgets() {
		Runnable runner = new Runnable(){
			public void run() {
				handler.post(new Runnable(){
					public void run(){
						setControls();
					    setText();
						stopWaitDialog();
					}
				});
			}
	    };
	    new Thread(runner).start();
	}

	private void setText() 
	{
		title = (TextView)findViewById(R.id.save_title);
		enterName = (TextView)findViewById(R.id.save_enter_name);
		enterType = (TextView)findViewById(R.id.save_enter_type);
		
		title.setTextColor(Color.WHITE);
		enterName.setTextColor(Color.WHITE);
		enterType.setTextColor(Color.WHITE);
	}

	private void setControls() 
	{
		enterPlayName = (EditText) findViewById(R.id.save_name);
		submit = (Button) findViewById(R.id.save_submit);
		playType = (Spinner) findViewById(R.id.save_play_type);
		
		submit.setOnClickListener(this);
		populateSpinner();
	}

	private void populateSpinner() 
	{
		ArrayList<String> playTypes = Constants.getPlayTypes();
		playTypes.remove(0);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,playTypes);
		playType.setAdapter(adapter);
	}

	public void onClick(final View v)
	{		
		if(Constants.FREE_VERSION){
			int numberOfSavedPlays = db.getPlayCount();
			if(numberOfSavedPlays >= Constants.NUMBER_OF_FREE_PLAYS_ALLOWED){
				tooManyPlaysDialog();
				return;
			}
		}
		playName = enterPlayName.getText().toString();
		ArrayList<String> playNames = db.getAllPlayNames();
	    boolean playNameExists = false;
	    for (int i = 0; i < playNames.size(); i++)
	    {
	    	if (playNames.get(i).equals(playName))
	    	{
	    		playNameExists = true;
	    		break;
	    	}
	    }
	    
	    if (playNameExists)
	    {
	    	AlertDialog.Builder playOverwrite = new AlertDialog.Builder(this);
	    	playOverwrite.setMessage("Play \"" + playName + "\" already exists. Do you wish to overwrite this play?");
	    	playOverwrite.setPositiveButton("Yes", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int which) 
				{
					// update this play
					convertRouteBitmapToByteArray();
					
					Play play = new Play(playName.toString(), formationName, playType.toString(), fieldBitmap);
					db.updatePlay(play);
					
					// remove the old Players from the database
					ArrayList<Integer> playerIds = db.removeAllPlayersFromPlay(playName);
					
					// remove all old route locations from the database for each player
					// that was removed
					db.removeAllRouteLocationsWithPlayerIds(playerIds);
	    			
					ArrayList<Integer> rowIds = addPlayPlayersToDB();
	    			
					addRouteLocationsToDB(rowIds);
					
					// need to recycle bitmaps to avoid memory leaks
					DrawView.recycleBitmaps();
	    			Intent intent = new Intent(v.getContext(), MainMenuActivity.class);
	    			// clear all activities, cannot have multiple editor screens open or we will
	    			// run out of memory
	    			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    			startActivity(intent);
				}
			});
	    	playOverwrite.setNegativeButton("No", new DialogInterface.OnClickListener() 
			{

				public void onClick(DialogInterface dialog, int which)
				{
					
				}
				
			});
	    	playOverwrite.show();
	    }
	    else
	    {
	    	AlertDialog.Builder playAdded = new AlertDialog.Builder(this);
	    	playAdded.setMessage("Play added");
	    	playAdded.setPositiveButton("OK", new DialogInterface.OnClickListener() 
	    	{
	    		public void onClick(DialogInterface dialog, int which) 
	    		{
	    			// add the play
	    			convertRouteBitmapToByteArray();
	    			
	    			Play play = new Play(playName.toString(), formationName, (String) playType.getSelectedItem(), fieldBitmap);
	    			db.addPlay(play);
					
	    			addPlayPlayersToDB();

	    			// need to recycle bitmaps to avoid memory leaks
	    			DrawView.recycleBitmaps();
	    			Intent intent = new Intent(v.getContext(), MainMenuActivity.class);
	    			// clear all activities, cannot have multiple editor screens open or we will
	    			// run out of memory
	    			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    			startActivity(intent);	
	    		}
	    	});
	    	playAdded.show();
	    }
	}
	
	private void tooManyPlaysDialog() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Your Title");	
		alertDialogBuilder
				.setMessage(TOO_MANY_PLAYS_MESSAGE)
				.setPositiveButton("",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						
					}
				  });
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
	}

	// convert the formation bitmap to a byte array
	private void convertFormationBitmapToByteArray()
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		
		EditorActivity.getFormationBitmap().compress(Bitmap.CompressFormat.JPEG, 100, stream);
		
		fieldBitmap = stream.toByteArray();
	}
	
	// convert the route bitmap to a byte array
	private void convertRouteBitmapToByteArray()
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		
		EditorActivity.getRouteBitmap().compress(Bitmap.CompressFormat.JPEG, 100, stream);
		
		fieldBitmap = stream.toByteArray();
	}
	
	// add the new formation players to the database
	private void addFormationPlayersToDB()
	{
		for (int i = 0; i < field.getAllPlayers().size(); i++)
		{
			Player fieldPlayer = field.getPlayer(i);
			Location playerLocation = fieldPlayer.getLocation();
			
			// play_name is null, see DatabasePlayer for reason
			DatabasePlayer databasePlayer = new DatabasePlayer(null, formationName, playerLocation.getX(), playerLocation.getY(), 
						fieldPlayer.getPosition().toString(), fieldPlayer.getRoute().toString(), fieldPlayer.getPath().toString());
			
			db.addPlayer(databasePlayer);
		}
	}

	//add the new players to the database
	// returns a list of player_id's added to the database
	private ArrayList<Integer> addPlayPlayersToDB()
	{
		ArrayList<Integer> player_ids = new ArrayList<Integer>();
		for (int i = 0; i < field.getAllPlayers().size(); i++)
		{
			Player fieldPlayer = field.getPlayer(i);
			Location playerLocation = fieldPlayer.getLocation();
			
			// formation_name is null, see DatabasePlayer for reason
			DatabasePlayer databasePlayer = new DatabasePlayer(playName, null, playerLocation.getX(), playerLocation.getY(), 
						fieldPlayer.getPosition().toString(), fieldPlayer.getRoute().toString(), fieldPlayer.getPath().toString());
			
			long rowid = db.addPlayer(databasePlayer); 
			player_ids.add(db.getPlayerIdOfRow(rowid));
		}
		return player_ids;
	}
	
	private void addRouteLocationsToDB(ArrayList<Integer> player_ids)
	{
		ArrayList<Player> allPlayers = field.getAllPlayers();
		for (int i = 0; i < allPlayers.size(); i++)
		{
			ArrayList<Location> routeLocations = field.getPlayer(i).getRouteLocations();
			for (int j = 0; j < routeLocations.size(); j++)
			{
				Location location = routeLocations.get(j);
				RouteLocation routeLocation = new RouteLocation(player_ids.get(i), location.getX(), location.getY());
				
				db.addRouteLocation(routeLocation);
			}
		}
	}
	
	private void runWaitDialog()
	{
		dialog = new ProgressDialog(this);
		dialog.setMessage("Loading...");
		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
		dialog.show();
	}
	
	private void stopWaitDialog()
	{
		dialog.dismiss();
	}
}
