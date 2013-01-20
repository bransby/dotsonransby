package com.database;

public class GameplanPlays 
{
	private String gameplan_name;
	private String play_name;
	
	public GameplanPlays(String gameplan_name, String play_name)
	{
		this.gameplan_name = gameplan_name;
		this.play_name = play_name;
	}
	
	public String getGameplanName()
	{
		return gameplan_name;
	}
	
	public void setGameplanName(String gameplan_name)
	{
		this.gameplan_name = gameplan_name;
	}
	
	public String getPlayName()
	{
		return play_name;
	}
	
	public void setPlayName(String play_name)
	{
		this.play_name = play_name;
	}
	
	public String toString()
	{
		return "gameplan_name: " + gameplan_name + "\n"
				+ "play_name: " + play_name;
	}

}
