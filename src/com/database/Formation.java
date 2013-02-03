package com.database;

public class Formation
{
	private String formation_name;
	private byte[] bitmap;
	
	public Formation()
	{
		
	}
	
	public Formation(String formation_name, byte[] bitmap)
	{
		this.formation_name = formation_name;
		this.bitmap = bitmap;
	}
	
	public String getFormationName()
	{
		return formation_name;
	}
	
	public void setFormationName(String formation_name)
	{
		this.formation_name = formation_name;
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
		return "formation_name: " + formation_name;
	}
}
