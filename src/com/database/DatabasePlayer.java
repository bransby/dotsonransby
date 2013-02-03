package com.database;

public class DatabasePlayer 
{
	private int player_id;
	private String play_name; // used for plays only, do not use for formations.
							  // if this is a formation, this field is null.
							  // this is used as an identifier that this player
							  // belongs to a play, not a formation,
	private String formation_name; // used for formations only, do not use for plays.
								   // if this is a play, this field is null.
								   // this is used as an identifier that this player
	  							    // belongs to a formation, not a play,
	private int x;
	private int y;
	private String position;
	private String route;
	private String path;
	
	public DatabasePlayer()
	{
		
	}
	
	// used for adding a player (no player_id, because it's autoincremented
		public DatabasePlayer(String play_name, String formation_name,
				int x, int y, String position, String route, String path)
		{
			this.play_name = play_name;
			this.formation_name = formation_name;
			this.x = x;
			this.y = y;
			this.position = position;
			this.route = route;
			this.path = path;
		}
		
	// used for returning a player
	public DatabasePlayer(int player_id, String play_name, String formation_name,
			int x, int y, String position, String route, String path)
	{
		this.player_id = player_id;
		this.play_name = play_name;
		this.formation_name = formation_name;
		this.x = x;
		this.y = y;
		this.position = position;
		this.route = route;
		this.path = path;
	}
	
	public int getPlayerId()
	{
		return player_id;
	}
	
	public void setPlayerId(int player_id)
	{
		this.player_id = player_id;
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
	
	public int getX()
	{
		return x;
	}
	
	public void setX(int x)
	{
		this.x = x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}
	
	public String getPosition()
	{
		return position;
	}
	
	public void setPosition(String position)
	{
		this.position = position;
	}
	
	public String getRoute()
	{
		return route;
	}
	
	public void setRoute(String route)
	{
		this.route = route;
	}
	
	public String getPath()
	{
		return path;
	}
	
	public void setPath(String path)
	{
		this.path = path;
	}
	
	public String toString()
	{
		return "player_id: " + player_id + "\n"
				+ "play_name: " + play_name + "\n"
				+ "formation_name: " + formation_name + "\n"
				+ "x: " + x + "\n"
				+ "y: " + y + "\n"
				+ "position: " + position + "\n"
				+ "route: " + route + "\n"
				+ "path: " + path;
	}
}
