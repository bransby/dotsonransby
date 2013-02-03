package com.example.digplay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashScreenActivity extends Activity {
	
	private Thread splashThread;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);  
	    this.setContentView(R.layout.splash_screen);
	 // thread for displaying the SplashScreen
        splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized(this){
                            wait(3000);
                    }

                } catch(InterruptedException e) {
                	
                }
                finally{
                	goToMainMenu();
                }
            }
        
        };
        splashThread.start();
	}
	public void goToMainMenu(){
		Intent intent = new Intent(this,MainMenuActivity.class);
        startActivity(intent);
	}
}