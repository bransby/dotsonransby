package com.database;

public class Gameplan 
{
	private String gameplan_name;
	
	public Gameplan()
	{
		
	}
	
	public Gameplan(String gameplan_name)
	{
		this.gameplan_name = gameplan_name;
	}
	
	public String getGameplanName()
	{
		return gameplan_name;
	}
	
	public void setGameplanName(String gameplan_name)
	{
		this.gameplan_name = gameplan_name;
	}
	
	public String toString()
	{
		return "gameplan_name: " + gameplan_name;
	}
}
