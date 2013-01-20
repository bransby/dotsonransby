package com.businessclasses;

import java.util.ArrayList;

public class Field {
	private ArrayList<Player> _playersOnField;
	
	public Field getField() {
		return this;
	}

	public Field() {
		_playersOnField = new ArrayList<Player>();
	}
	
	public ArrayList<Player> getAllPlayers()
	{
		return _playersOnField;
	}
	
	public void addPlayer(Location l, Position p){
		Player player = new Player(l,p);
		_playersOnField.add(player);
	}

	//overloading method for DB use
	public void addPlayer(Location l, Position p, Route r, Path path){
		Player player = new Player(l,p,r, path);
		_playersOnField.add(player);
	}
	public void addPlayers(ArrayList<Player> newPlayers){
		this._playersOnField = newPlayers;
	}
	public void clearField(){
		_playersOnField.clear();
	}
	public void clearRouteLocations(){
		for(int i=0;i<_playersOnField.size();i++){
			_playersOnField.get(i).clearRouteLocations();
		}
	}
	public void clearRoutes(Route route)
	{
		for(int i=0;i<_playersOnField.size();i++){
			_playersOnField.get(i).changeRoute(route);
		}
	}
	public void clearPaths(Path path)
	{
		for (int i = 0; i < _playersOnField.size(); i++)
		{
			_playersOnField.get(i).changePath(path);
		}
	}
	public void removePlayer(int index)
	{
		_playersOnField.remove(index);
	}
	public boolean removePlayer(Player p){
		for(int i=0;i<_playersOnField.size();i++){
			if(_playersOnField.get(i).equals(p)){
				_playersOnField.remove(i);
				return true;
			}
		}
		return false;
	}
	public void changePlayerRoute(Player p,Route r){
		p.changeRoute(r);
	}
	public void removeAllPlayers(){
		_playersOnField.clear();
	}
	public Field clone(){
		try {
			return (Field)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
	public void flip(int width){
		for(int i = 0;i< _playersOnField.size();i++){
			Player thePlayer = _playersOnField.get(i);
			thePlayer.flipLocation(width);
			thePlayer.flipRouteLocations(width);
		}
	}
	public Player getPlayer(int num){
		return _playersOnField.get(num);
	}

}
