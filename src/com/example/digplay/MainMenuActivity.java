package com.example.digplay;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenuActivity extends Activity implements OnClickListener {
	
	private Button drawNewPlay;
	private Button createGameplan;
	private Button lookAtPlaybook;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);
        setButtons();
    }

    private void setButtons() {
		drawNewPlay = (Button) findViewById(R.id.maindrawplay);
		createGameplan = (Button)findViewById(R.id.maincreategameplan);
		lookAtPlaybook = (Button)findViewById(R.id.mainlookatplaybook);
		
		drawNewPlay.setOnClickListener(this);
		createGameplan.setOnClickListener(this);
		lookAtPlaybook.setOnClickListener(this);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

	public void onClick(View v) {
		int buttonPressed = v.getId();
		Intent intent = null;
		if(buttonPressed == drawNewPlay.getId())intent = new Intent(v.getContext(),FormationManagerActivity.class);
		else if(buttonPressed == createGameplan.getId())intent = new Intent(v.getContext(),GameplanManagerActivity.class);
		else intent = new Intent(v.getContext(),PlayViewActivity.class);
		setProgressBarIndeterminateVisibility(true);
		startActivity(intent);
	}

	private void runWaitDialog() {
		ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("Waiting...");
		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
		dialog.show();
	}
	@Override
	public boolean  onKeyDown(int keyCode, KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_BACK){
			
		}
		return false;
		//return super.onKeyDown(keyCode, event);
	}
}
