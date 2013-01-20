package com.database;

public class RouteLocation 
{
	private int route_id;
	private int player_id;
	private int x;
	private int y;
	
	public RouteLocation()
	{
		
	}
	
	public RouteLocation(int route_id, int player_id, int x, int y)
	{
		this.route_id = route_id;
		this.player_id = player_id;
		this.x = x;
		this.y = y;
	}
	
	public RouteLocation(int player_id, int x, int y)
	{
		this.player_id = player_id;
		this.x = x;
		this.y = y;
	}
	
	public int getRouteId()
	{
		return route_id;
	}
	
	public void setRouteId(int route_id)
	{
		this.route_id = route_id;
	}
	
	public int getPlayerId()
	{
		return player_id;
	}
	
	public void setPlayerId(int player_id)
	{
		this.player_id = player_id;
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
	
	public String toString()
	{
		return "route_id: " + route_id + "\n"
				+ "player_id: " + player_id + "\n"
				+ "x: " + x + "\n"
				+ "y: " + y;
	}
}
