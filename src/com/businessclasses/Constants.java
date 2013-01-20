package com.businessclasses;

import java.util.ArrayList;

public class Constants {
	public static ArrayList<String> getPlayTypes(){
		ArrayList<String> playTypes = new ArrayList<String>();
		playTypes.add("ALL PLAYS");
		playTypes.add("RUN");
		playTypes.add("PASS");
		return playTypes;
	}
	
	public static ArrayList<String>getFormations(){
		ArrayList<String> formations = new ArrayList<String>();
		formations.add("I");
		formations.add("Power I");
		formations.add("Ace");
		formations.add("4 Wide");
		formations.add("5 Wide");
		formations.add("Wishbone");
		formations.add("Strong I");
		formations.add("Weak I");
		formations.add("Wing T");
		formations.add("Pro");
		return formations;
	}
}
