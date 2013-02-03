package com.database;

public class Play 
{
	private String play_name;
	private String formation_name;
	private String type;
	private byte[] bitmap;
	
	public Play()
	{
		
	}
	
	public Play(String play_name, String formation_name, String type,
			byte[] bitmap)
	{
		this.play_name = play_name;
		this.formation_name = formation_name;
		this.type = type;
		this.bitmap = bitmap;
	}
	
	public String getPlayName()
	{
		return play_name;
	}
	
	public void setPlayName(String play_name)
	{
		this.play_name = play_name;
	}
	
	public String getFormationName()
	{
		return formation_name;
	}
	
	public void setFormationName(String formation_name)
	{
		this.formation_name = formation_name;
	}
	
	public String getType()
	{
		return type;
	}
	
	public void setType(String type)
	{
		this.type = type;
	}
	
	public byte[] getBitmap()
	{
		return bitmap;
	}
	
	public void setBitmap(byte[] bitmap)
	{
		this.bitmap = bitmap;
	}
	
	public String toString()
	{
		return "play_name: " + play_name + "\n"
				+ "formation_name: " + formation_name + "\n"
				+ "type: " + type;
	}
}
