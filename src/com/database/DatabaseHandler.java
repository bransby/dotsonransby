package com.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper
{
	private static final int DATABASE_VERSION = 3;
	
	private static final String DATABASE_NAME = "digPlay";

	private static final String TABLE_PLAYERS = "players";
	private static final String KEY_PLAYER_ID = "player_id";
	private static final String COLUMN_PLAY_NAME = "play_name";
	private static final String COLUMN_FORMATION_NAME = "formation_name";
	private static final String COLUMN_X = "x";
	private static final String COLUMN_Y = "y";
	private static final String COLUMN_POSITION = "position";
	private static final String COLUMN_ROUTE = "route";
	private static final String COLUMN_PATH = "path";
	
	private static final String TABLE_PLAYS = "plays";
	private static final String KEY_PLAY_NAME = "play_name";
	private static final String COLUMN_TYPE = "type";
	private static final String COLUMN_BITMAP = "bitmap";
	
	private static final String TABLE_ROUTE_LOCATIONS = "route_locations";
	private static final String KEY_ROUTE_ID = "route_id";
	private static final String COLUMN_PLAYER_ID = "player_id";
	
	private static final String TABLE_FORMATIONS = "formations";
	private static final String KEY_FORMATION_NAME = "formation_name";
	
	private static final String TABLE_GAMEPLANS = "gameplans";
	private static final String KEY_GAMEPLAN_NAME = "gameplan_name";
	
	private static final String TABLE_GAMEPLAN_PLAYS = "gameplan_plays";
	
	private static final String CREATE_PLAYERS = "CREATE TABLE " 
			+ TABLE_PLAYERS + "(" + KEY_PLAYER_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ COLUMN_PLAY_NAME + " TEXT," 
			+ COLUMN_FORMATION_NAME + " TEXT,"
			+ COLUMN_X + " INTEGER," + COLUMN_Y 
			+ " INTEGER," + COLUMN_POSITION + " TEXT," 
			+ COLUMN_ROUTE + " TEXT,"
			+ COLUMN_PATH + " TEXT)";
	
	private static final String CREATE_PLAYS = "CREATE TABLE "
			+ TABLE_PLAYS + "(" + KEY_PLAY_NAME
			+ " TEXT PRIMARY KEY," + COLUMN_FORMATION_NAME
			+ " TEXT," + COLUMN_TYPE + " TEXT," 
			+ COLUMN_BITMAP + " BLOB)";
	
	private static final String CREATE_ROUTE_LOCATIONS = "CREATE TABLE "
			+ TABLE_ROUTE_LOCATIONS + "(" + KEY_ROUTE_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ COLUMN_PLAYER_ID + " INTEGER,"
			+ COLUMN_X + " INTEGER," + COLUMN_Y + " INTEGER)";
	
	private static final String CREATE_FORMATIONS = "CREATE TABLE "
			+ TABLE_FORMATIONS + "(" + KEY_FORMATION_NAME
			+ " TEXT PRIMARY KEY," + COLUMN_BITMAP + " BLOB)";
	
	private static final String CREATE_GAMEPLANS = "CREATE TABLE "
			+ TABLE_GAMEPLANS + "(" + KEY_GAMEPLAN_NAME
			+ " TEXT PRIMARY KEY)";
	
	private static final String CREATE_GAMEPLAN_PLAYS = "CREATE TABLE "
			+ TABLE_GAMEPLAN_PLAYS + "(" + KEY_GAMEPLAN_NAME
			+ " TEXT," + KEY_PLAY_NAME
			+ " TEXT, PRIMARY KEY (" 
			+ KEY_GAMEPLAN_NAME + ", " + KEY_PLAY_NAME + "))";

	public DatabaseHandler(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) 
	{
		database.execSQL(CREATE_PLAYERS);
		database.execSQL(CREATE_PLAYS);
		database.execSQL(CREATE_ROUTE_LOCATIONS);
		database.execSQL(CREATE_FORMATIONS);
		database.execSQL(CREATE_GAMEPLANS);
		database.execSQL(CREATE_GAMEPLAN_PLAYS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTE_LOCATIONS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FORMATIONS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMEPLANS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMEPLAN_PLAYS);
		
		onCreate(db);
	}
	
	/*
	 * gets all players with formation_name
	 */
	public ArrayList<DatabasePlayer> getPlayersWithFormationName(String formation_name)
	{
		ArrayList<DatabasePlayer> playerList = new ArrayList<DatabasePlayer>();
		
	    // Select All Query
	    String selectQuery = "SELECT * FROM " + TABLE_PLAYERS + " WHERE " 
	    		+ COLUMN_FORMATION_NAME + " = \"" + formation_name + "\"";
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) 
	    {
	        do 
	        {
	            DatabasePlayer player = new DatabasePlayer();
	            player.setPlayerId(Integer.parseInt(cursor.getString(0)));
	            player.setPlayName(cursor.getString(1));
	            player.setFormationName(cursor.getString(2));
	            player.setX(Integer.parseInt(cursor.getString(3)));
	            player.setY(Integer.parseInt(cursor.getString(4)));
	            player.setPosition(cursor.getString(5));
	            player.setRoute(cursor.getString(6));
	            player.setPath(cursor.getString(7));
	            
	            playerList.add(player);
	        } 
	        while (cursor.moveToNext());
	    }
	    db.close();
	    return playerList;
	}
	
	/*
	 * removes all gameplan_plays
	 */
	public void removeAllGameplanPlaysWithName(String gameplan_name)
	{
		ArrayList<GameplanPlay> allGameplanPlays = getAllGameplanPlays();
		for (int i = 0; i < allGameplanPlays.size(); i++)
		{
			GameplanPlay temp = allGameplanPlays.get(i);
			if (temp.getGameplanName().equals(gameplan_name))
			{
				deleteGameplanPlay(temp.getGameplanName(), temp.getPlayName());
			}
		}
	}
	
	/*
	 * get all play names with gameplan name
	 */
	public ArrayList<String> getAllPlayNamesWithGameplan(String gameplan_name)
	{
		ArrayList<String> gameplanNames = new ArrayList<String>();
		
		String selectQuery = "SELECT " + KEY_PLAY_NAME + " FROM " + TABLE_GAMEPLAN_PLAYS
				+ " WHERE " + KEY_GAMEPLAN_NAME + " = \"" + gameplan_name + "\"";
		
		SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) 
	    {
	        do 
	        { 
	        	gameplanNames.add(cursor.getString(0));
	        } 
	        while (cursor.moveToNext());
	    }
	    db.close();
	    return gameplanNames;
	}
	
	/*
	 * get all formation names
	 */
	public ArrayList<String> getAllFormationNames()
	{
		ArrayList<String> formationNames = new ArrayList<String>();
		
	    String selectQuery = "SELECT " + KEY_FORMATION_NAME + " FROM " + TABLE_FORMATIONS;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) 
	    {
	        do 
	        { 
	        	formationNames.add(cursor.getString(0));
	        } 
	        while (cursor.moveToNext());
	    }
	    db.close();
	    return formationNames;
	}
	
	/*
	 * get all play names
	 */
	public ArrayList<String> getAllPlayNames()
	{
		ArrayList<String> playNames = new ArrayList<String>();
		
		// Select All Query
	    String selectQuery = "SELECT " + KEY_PLAY_NAME + " FROM " + TABLE_PLAYS;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) 
	    {
	        do 
	        { 
	        	playNames.add(cursor.getString(0));
	        } 
	        while (cursor.moveToNext());
	    }
	    db.close();
	    return playNames;
	}
	
	// removes all players with given play_name
	// returns a list of all player_ids of players
	// that were removed
	public ArrayList<Integer> removeAllPlayersFromPlay(String play_name)
	{
		ArrayList<Integer> player_ids = new ArrayList<Integer>();
		ArrayList<DatabasePlayer> allPlayers = getAllPlayers();
		for (int i = 0; i < allPlayers.size(); i++)
		{
			DatabasePlayer player = allPlayers.get(i);
			if (player.getPlayName() != null && player.getPlayName().equals(play_name))
			{
				player_ids.add(player.getPlayerId());
				deletePlayer(player.getPlayerId());
			}
		}
	    return player_ids;
	}
	
	// removes all players with given formation_name
	// returns a list of all player_ids of players
	// that were removed
	public ArrayList<Integer> removeAllPlayersFromFormation(String formation_name)
	{
		ArrayList<Integer> player_ids = new ArrayList<Integer>();
		ArrayList<DatabasePlayer> allPlayers = getAllPlayers();
		for (int i = 0; i < allPlayers.size(); i++)
		{
			DatabasePlayer player = allPlayers.get(i);
			if (player.getFormationName().equals(formation_name))
			{
				player_ids.add(player.getPlayerId());
				deletePlayer(player.getPlayerId());
			}
		}
	    return player_ids;
	}
	
	// removes all route locations for all player_ids in the ArrayList
	public void removeAllRouteLocationsWithPlayerIds(ArrayList<Integer> player_ids)
	{
		ArrayList<RouteLocation> routeLocations = getAllRouteLocations();
		
		for (int i = 0; i < routeLocations.size(); i++)
		{
			for (int j = 0; j < player_ids.size(); j++)
			{
				RouteLocation routeLocation = routeLocations.get(i);
				if (routeLocation.getPlayerId() == player_ids.get(j))
				{
					deleteRouteLocation(routeLocation.getRouteId());
				}
			}
		}
	}
	
	// get player_id of a row
	public int getPlayerIdOfRow(long rowid)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_PLAYERS, new String[] { KEY_PLAYER_ID},
				"rowid=?",
	            new String[] { String.valueOf(rowid) }, null, null, null, null);
		 if (cursor != null)
		 {
			 cursor.moveToFirst();
		 }
		 
		 db.close();
		 return Integer.parseInt(cursor.getString(0));
	}
	
	/*
	 * ADD SINGLE ROW TO TABLE
	 */
	public long addPlayer(DatabasePlayer player)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(COLUMN_PLAY_NAME, player.getPlayName());
		values.put(COLUMN_FORMATION_NAME, player.getFormationName());
		values.put(COLUMN_X, player.getX());
		values.put(COLUMN_Y, player.getY());
		values.put(COLUMN_POSITION, player.getPosition());
		values.put(COLUMN_ROUTE, player.getRoute());
		values.put(COLUMN_PATH, player.getPath());
		
		long rowId = db.insert(TABLE_PLAYERS, null, values);
		db.close();
		return rowId;
	}
	
	public void addPlay(Play play)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_PLAY_NAME, play.getPlayName());
		values.put(COLUMN_FORMATION_NAME, play.getFormationName());
		values.put(COLUMN_TYPE, play.getType());
		values.put(COLUMN_BITMAP, play.getBitmap());
		
		db.insert(TABLE_PLAYS, null, values);
		db.close();
	}
	
	public void addRouteLocation(RouteLocation routeLocation)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(COLUMN_PLAYER_ID, routeLocation.getPlayerId());
		values.put(COLUMN_X, routeLocation.getX());
		values.put(COLUMN_Y, routeLocation.getY());
		
		db.insert(TABLE_ROUTE_LOCATIONS, null, values);
		db.close();
	}
	
	public void addFormation(Formation formation)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_FORMATION_NAME, formation.getFormationName());
		values.put(COLUMN_BITMAP, formation.getBitmap());
		
		db.insert(TABLE_FORMATIONS, null, values);
		db.close();
	}
	
	public void addGameplan(Gameplan gameplan)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_GAMEPLAN_NAME, gameplan.getGameplanName());
		
		db.insert(TABLE_GAMEPLANS, null, values);
		db.close();
	}
	
	public void addGameplanPlay(GameplanPlay gameplanPlay)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_GAMEPLAN_NAME, gameplanPlay.getGameplanName());
		values.put(KEY_PLAY_NAME, gameplanPlay.getPlayName());
		
		db.insert(TABLE_GAMEPLAN_PLAYS, null, values);
		db.close();
	}
	
	/*
	 * GET SINGLE ROW OF TABLE
	 */
	public DatabasePlayer getPlayer(int id)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_PLAYERS, new String[] { KEY_PLAYER_ID,
	            COLUMN_PLAY_NAME, COLUMN_FORMATION_NAME, 
	            COLUMN_X, COLUMN_Y, COLUMN_POSITION, 
	            COLUMN_ROUTE, COLUMN_PATH }, KEY_PLAYER_ID + "=?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
		 if (cursor != null)
		 {
			 cursor.moveToFirst();
		 }
		 DatabasePlayer player = new DatabasePlayer(Integer.parseInt(cursor.getString(0)),
		            cursor.getString(0), cursor.getString(2), 
		            Integer.parseInt(cursor.getString(3)), 
		            Integer.parseInt(cursor.getString(4)),
		            cursor.getString(5), cursor.getString(6), cursor.getString(7));
		 
		 db.close();
		 return player;
	}
	
	public Play getPlay(String id)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_PLAYS, new String[] { KEY_PLAY_NAME,
				COLUMN_FORMATION_NAME, COLUMN_TYPE, COLUMN_BITMAP }, KEY_PLAY_NAME + "=?",
	            new String[] { id }, null, null, null, null);
		 if (cursor != null)
		 {
			 cursor.moveToFirst();
		 }
		 Play play = new Play(cursor.getString(0), cursor.getString(1), 
				 cursor.getString(2), cursor.getBlob(3));
		 db.close();
		 return play;
	}
	
	public RouteLocation getRouteLocation(int id)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_ROUTE_LOCATIONS, new String[] { KEY_ROUTE_ID,
	            COLUMN_PLAYER_ID, COLUMN_X, COLUMN_Y }, KEY_ROUTE_ID + "=?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
		 if (cursor != null)
		 {
			 cursor.moveToFirst();
		 }
		 RouteLocation routeLocation = new RouteLocation(
				 Integer.parseInt(cursor.getString(0)), 
				 Integer.parseInt(cursor.getString(1)), 
				 Integer.parseInt(cursor.getString(2)),
				 Integer.parseInt(cursor.getString(3)));
		 db.close();
		 return routeLocation;
	}
	
	public Formation getFormation(String id)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_FORMATIONS, new String[] { KEY_FORMATION_NAME, 
				COLUMN_BITMAP}, KEY_FORMATION_NAME + "=?", 
				new String[] { id }, null, null, null, null);
		 if (cursor != null)
		 {
			 cursor.moveToFirst();
		 }
		 Formation formation = new Formation(cursor.getString(0),
				 cursor.getBlob(1));
		 db.close();
		 return formation;
	}
	
	public Gameplan getGameplan(String id)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_GAMEPLANS, new String[] { KEY_GAMEPLAN_NAME},
				KEY_GAMEPLAN_NAME + "=?", new String[] { id }, null, null, null, null);
		 if (cursor != null)
		 {
			 cursor.moveToFirst();
		 }
		 Gameplan gameplan = new Gameplan(cursor.getString(0));
		 db.close();
		 return gameplan;
	}
	
	public GameplanPlay getGameplanPlay(String gameplan_name, String play_name)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_GAMEPLAN_PLAYS, new String[] { KEY_GAMEPLAN_NAME},
				KEY_GAMEPLAN_NAME + "=? AND " + KEY_PLAY_NAME + "=?", 
				new String[] { gameplan_name, play_name }, null, null, null, null);
		 if (cursor != null)
		 {
			 cursor.moveToFirst();
		 }
		 GameplanPlay gameplanPlay = new GameplanPlay(cursor.getString(0), cursor.getString(1));
		 db.close();
		 return gameplanPlay;
	}
	
	/*
	 * GET ALL ROWS OF TABLE
	 */
	public ArrayList<DatabasePlayer> getAllPlayers()
	{
		ArrayList<DatabasePlayer> playerList = new ArrayList<DatabasePlayer>();
		
	    // Select All Query
	    String selectQuery = "SELECT * FROM " + TABLE_PLAYERS;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) 
	    {
	        do 
	        {
	            DatabasePlayer player = new DatabasePlayer();
	            player.setPlayerId(Integer.parseInt(cursor.getString(0)));
	            player.setPlayName(cursor.getString(1));
	            player.setFormationName(cursor.getString(2));
	            player.setX(Integer.parseInt(cursor.getString(3)));
	            player.setY(Integer.parseInt(cursor.getString(4)));
	            player.setPosition(cursor.getString(5));
	            player.setRoute(cursor.getString(6));
	            player.setPath(cursor.getString(7));
	            
	            playerList.add(player);
	        } 
	        while (cursor.moveToNext());
	    }
	    db.close();
	    return playerList;
	}
	
	public ArrayList<Play> getAllPlays()
	{
		ArrayList<Play> playList = new ArrayList<Play>();
		
	    // Select All Query
	    String selectQuery = "SELECT * FROM " + TABLE_PLAYS;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) 
	    {
	        do 
	        {
	            Play play = new Play();
	            play.setPlayName(cursor.getString(0));
	            play.setFormationName(cursor.getString(1));
	            play.setType(cursor.getString(2));
	            play.setBitmap(cursor.getBlob(3));
	            
	            playList.add(play);
	        } 
	        while (cursor.moveToNext());
	    }
	    db.close();
	    return playList;
	}
	
	public ArrayList<RouteLocation> getAllRouteLocations()
	{
		ArrayList<RouteLocation> routeLocationList = new ArrayList<RouteLocation>();
		
	    // Select All Query
	    String selectQuery = "SELECT * FROM " + TABLE_ROUTE_LOCATIONS;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) 
	    {
	        do 
	        {
	        	RouteLocation routeLocation = new RouteLocation();
	        	routeLocation.setRouteId(Integer.parseInt(cursor.getString(0)));
	        	routeLocation.setPlayerId(Integer.parseInt(cursor.getString(1)));
	        	routeLocation.setX(Integer.parseInt(cursor.getString(2)));
	        	routeLocation.setY(Integer.parseInt(cursor.getString(3)));
	            
	            routeLocationList.add(routeLocation);
	        } 
	        while (cursor.moveToNext());
	    }
	    db.close();
	    return routeLocationList;
	}
	
	public ArrayList<Formation> getAllFormations()
	{
		ArrayList<Formation> formationList = new ArrayList<Formation>();
		
	    // Select All Query
	    String selectQuery = "SELECT * FROM " + TABLE_FORMATIONS;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) 
	    {
	        do 
	        {
	        	Formation formation = new Formation();
	        	formation.setFormationName(cursor.getString(0));
	        	formation.setBitmap(cursor.getBlob(1));
	            
	        	formationList.add(formation);
	        } 
	        while (cursor.moveToNext());
	    }
	    db.close();
	    return formationList;
	}
	
	public ArrayList<Gameplan> getAllGameplans()
	{
		ArrayList<Gameplan> gameplanList = new ArrayList<Gameplan>();
		
	    // Select All Query
	    String selectQuery = "SELECT * FROM " + TABLE_GAMEPLANS;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) 
	    {
	        do 
	        {
	        	Gameplan gameplan = new Gameplan();
	        	gameplan.setGameplanName(cursor.getString(0));
	            
	        	gameplanList.add(gameplan);
	        } 
	        while (cursor.moveToNext());
	    }
	    db.close();
	    return gameplanList;
	}
	
	public ArrayList<GameplanPlay> getAllGameplanPlays()
	{
		ArrayList<GameplanPlay> gameplanPlayList = new ArrayList<GameplanPlay>();
		
	    // Select All Query
	    String selectQuery = "SELECT * FROM " + TABLE_GAMEPLAN_PLAYS;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) 
	    {
	        do 
	        {
	        	GameplanPlay gameplanPlay = new GameplanPlay();
	        	gameplanPlay.setGameplanName(cursor.getString(0));
	        	gameplanPlay.setPlayName(cursor.getString(1));
	            
	        	gameplanPlayList.add(gameplanPlay);
	        } 
	        while (cursor.moveToNext());
	    }
	    db.close();
	    return gameplanPlayList;
	}
	
	/*
	 * GET COUNT OF ROWS IN TABLE
	 */
	public int getPlayerCount()
	{
		String countQuery = "SELECT * FROM " + TABLE_PLAYERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
 
        db.close();
        // return count
        return cursor.getCount();
	}
	
	public int getPlayCount()
	{
		String countQuery = "SELECT * FROM " + TABLE_PLAYS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
 
        db.close();
        // return count
        return cursor.getCount();
	}
	
	public int getRouteLocationCount()
	{
		String countQuery = "SELECT * FROM " + TABLE_ROUTE_LOCATIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
 
        db.close();
        // return count
        return cursor.getCount();
	}
	
	public int getFormationCount()
	{
		String countQuery = "SELECT * FROM " + TABLE_FORMATIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
 
        db.close();
        // return count
        return cursor.getCount();
	}
	
	public int getGameplanCount()
	{
		String countQuery = "SELECT * FROM " + TABLE_GAMEPLANS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
 
        db.close();
        // return count
        return cursor.getCount();
	}
	
	public int getGameplanPlayCount()
	{
		String countQuery = "SELECT * FROM " + TABLE_GAMEPLAN_PLAYS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
 
        db.close();
        // return count
        return cursor.getCount();
	}
	
	/*
	 * UPDATE SINGLE ROW IN TABLE
	 */
	public int updatePlayer(DatabasePlayer player)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    values.put(COLUMN_PLAY_NAME, player.getPlayName());
	    values.put(COLUMN_FORMATION_NAME, player.getFormationName());
	    values.put(COLUMN_X, player.getX());
	    values.put(COLUMN_Y, player.getY());
	    values.put(COLUMN_POSITION, player.getPosition());
	    values.put(COLUMN_ROUTE, player.getRoute());
	    values.put(COLUMN_PATH, player.getPath());
	 
	    int retVal = db.update(TABLE_PLAYERS, values, KEY_PLAYER_ID + " = ?",
	            new String[] { String.valueOf(player.getPlayerId()) });
	    db.close();
	    // updating row
	    return retVal;
	}
	
	public int updatePlay(Play play)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    values.put(KEY_PLAY_NAME, play.getPlayName());
	    values.put(COLUMN_FORMATION_NAME, play.getFormationName());
	    values.put(COLUMN_TYPE, play.getType());
	    values.put(COLUMN_BITMAP, play.getBitmap());
	 
	    int retVal = db.update(TABLE_PLAYS, values, KEY_PLAY_NAME + " = ?",
	            new String[] { play.getPlayName() });
	    db.close();
	    // updating row
	    return retVal;
	}
	
	public int updateRouteLocation(RouteLocation routeLocation)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    values.put(COLUMN_PLAYER_ID, routeLocation.getPlayerId());
	    values.put(COLUMN_X, routeLocation.getX());
	    values.put(COLUMN_Y, routeLocation.getY());
	 
	    int retVal = db.update(TABLE_ROUTE_LOCATIONS, values, KEY_ROUTE_ID + " = ?",
	            new String[] { String.valueOf(routeLocation.getRouteId()) });
	    db.close();
	    // updating row
	    return retVal;
	}
	
	public int updateFormation(Formation formation)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    values.put(KEY_FORMATION_NAME, formation.getFormationName());
	    values.put(COLUMN_BITMAP, formation.getBitmap());
	 
	    int retVal = db.update(TABLE_FORMATIONS, values, KEY_FORMATION_NAME + " = ?",
	            new String[] { String.valueOf(formation.getFormationName()) });
	    db.close();
	    // updating row
	    return retVal;
	}
	
	/*
	 * DELETE SINGLE ROW IN TABLE
	 */
	public void deletePlayer(int playerId)
	{
		SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(TABLE_PLAYERS, KEY_PLAYER_ID + " = ?",
	            new String[] { String.valueOf(playerId) });
	    db.close();
	}
	
	public void deletePlay(String play_name)
	{
		SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(TABLE_PLAYS, KEY_PLAY_NAME + " = ?",
	            new String[] { play_name });
	    db.close();
	}
	
	public void deleteRouteLocation(int routeId)
	{
		SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(TABLE_ROUTE_LOCATIONS, KEY_ROUTE_ID + " = ?",
	            new String[] { String.valueOf(routeId) });
	    db.close();
	}
	
	public void deleteFormation(String formation_name)
	{
		SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(TABLE_FORMATIONS, KEY_FORMATION_NAME + " = ?",
	            new String[] { formation_name });
	    db.close();
	}
	
	public void deleteGameplan(String gameplan_name)
	{
		SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(TABLE_GAMEPLANS, KEY_GAMEPLAN_NAME + " = ?",
	            new String[] { gameplan_name });
	    db.close();
	}
	
	public void deleteGameplanPlay(String gameplan_name, String play_name)
	{
		SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(TABLE_GAMEPLAN_PLAYS, 
	    		KEY_GAMEPLAN_NAME + " = ? AND " + KEY_PLAY_NAME + " = ?",
	            new String[] { gameplan_name, play_name });
	    db.close();
	}
}
