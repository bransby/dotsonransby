package com.businessclasses;

import java.util.ArrayList;

import com.database.Play;

public class Sort {
	public PlayAdapter sortPlaysByRunPass(PlayAdapter adapter,String playType){
		if(playType.equals("ALL PLAYS"))return adapter;
		int totalCount = adapter.getCount();
		for(int i = 0; i < totalCount; i++){
			Play play = adapter.getItem(i);
			if(!playType.equalsIgnoreCase(play.getType())){
				adapter.remove(play);
				i--; totalCount--;
			}
		}
		return adapter;
	}

	public PlayAdapter sortPlaysByPlaybook(PlayAdapter adapter, String playbook, ArrayList<String> listOfPlaysInGameplan) {
		if (playbook.equals("All Gameplans"))return adapter;
		int totalCount = adapter.getCount();
		for(int i = 0; i < totalCount; i++)
		{
			Play play = adapter.getItem(i);
			if(!listOfPlaysInGameplan.contains(play.getPlayName().toString())){
				adapter.remove(play);
				i--; totalCount--;
			}
		}
		return adapter;
	}
}