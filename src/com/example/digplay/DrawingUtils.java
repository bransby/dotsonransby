package com.example.digplay;

import com.businessclasses.Field;
import com.businessclasses.Location;
import com.businessclasses.Path;
import com.businessclasses.Player;
import com.businessclasses.Position;
import com.businessclasses.Route;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Align;

public class DrawingUtils {
	
	// final variables
	private static final int TEXT_WIDTH = 2;
	
	// class variables
	private static int[] x_grid;
	private static int[] y_grid;
	private static int lineOfScrimmageIndex;
	
	// provided by EditorActivity
	private static int LEFT_MARGIN;
	private static int RIGHT_MARGIN;
	private static int TOP_MARGIN;
	private static int BOTTOM_MARGIN;
	private static int TOP_ANDROID_BAR;
	private static float PIXELS_PER_YARD;
	private static int PLAYER_ICON_RADIUS;
	private static int FIELD_LINE_WIDTHS;
	private static float DENSITY;
	private static int TOUCH_SENSITIVITY;
	private static int BUTTON_Y_VALUE;
	private static int SCREEN_WIDTH;
	
	public static void setVariables(int PASSED_LEFT_MARGIN, int PASSED_RIGHT_MARGIN, int PASSED_TOP_MARGIN, int PASSED_BOTTOM_MARGIN, 
			int PASSED_TOP_ANDROID_BAR, float PASSED_PIXELS_PER_YARD, int PASSED_PLAYER_ICON_RADIUS, int PASSED_FIELD_LINE_WIDTHS, 
			float PASSED_DENSITY, int PASSED_TOUCH_SENSITIVITY, int PASSED_BUTTON_Y_VALUE, int PASSED_SCREEN_WIDTH)
	{
		LEFT_MARGIN = PASSED_LEFT_MARGIN;
		RIGHT_MARGIN = PASSED_RIGHT_MARGIN;
		TOP_MARGIN = PASSED_TOP_MARGIN;
		BOTTOM_MARGIN = PASSED_BOTTOM_MARGIN;
		TOP_ANDROID_BAR = PASSED_TOP_ANDROID_BAR;
		PIXELS_PER_YARD = PASSED_PIXELS_PER_YARD;
		PLAYER_ICON_RADIUS = PASSED_PLAYER_ICON_RADIUS;
		FIELD_LINE_WIDTHS = PASSED_FIELD_LINE_WIDTHS;
		DENSITY = PASSED_DENSITY;
		TOUCH_SENSITIVITY = PASSED_TOUCH_SENSITIVITY;
		BUTTON_Y_VALUE = PASSED_BUTTON_Y_VALUE;
		SCREEN_WIDTH = PASSED_SCREEN_WIDTH;
	}
	
	public static void initGrid()
	{
		/*
		 * x value initialization. The x value grid is placed at the center of screen.
		 * This makes a visually pleasing grid.
		 */
		int middleOfScreen = SCREEN_WIDTH/2;
		int numberBefore = (middleOfScreen - LEFT_MARGIN - PLAYER_ICON_RADIUS) / PLAYER_ICON_RADIUS;
		int sizeOfArray = (numberBefore*2)+1;
		x_grid = new int[sizeOfArray];
		x_grid[numberBefore] = middleOfScreen;
		int xValue = middleOfScreen;
		
		for (int i = numberBefore-1; i >= 0; i--)
		{
			xValue = xValue - PLAYER_ICON_RADIUS;
			x_grid[i] = xValue;
		}
		xValue = middleOfScreen;
		for (int i = numberBefore+1; i < sizeOfArray; i++)
		{
			xValue = xValue + PLAYER_ICON_RADIUS;
			x_grid[i] = xValue;
		}
		/*
		 * y value initialization. The y value grid is "centered" at the line of
		 * scrimmage. This makes a very visually pleasing grid.
		 */
		float twentyYards = PIXELS_PER_YARD*20;
		float lineOfScrimmageLineWidth = Math.round(6/DENSITY);
		
		int middleOfScreenWithoutActionBar = (int) (BOTTOM_MARGIN - twentyYards + lineOfScrimmageLineWidth);
		middleOfScreen = middleOfScreenWithoutActionBar + TOP_ANDROID_BAR;
		numberBefore = (middleOfScreenWithoutActionBar - TOP_MARGIN - PLAYER_ICON_RADIUS) / PLAYER_ICON_RADIUS;
		int numberAfter = (int) ((twentyYards - lineOfScrimmageLineWidth - PLAYER_ICON_RADIUS) / PLAYER_ICON_RADIUS);
		sizeOfArray = numberBefore + numberAfter + 1;
		y_grid = new int[sizeOfArray];
		y_grid[numberBefore] = middleOfScreen;
		lineOfScrimmageIndex = numberBefore;
		int yValue = middleOfScreen;
		for (int i = numberBefore-1; i >= 0; i--)
		{
			yValue = yValue - PLAYER_ICON_RADIUS;
			y_grid[i] = yValue;
		}
		yValue = middleOfScreen;
		for (int i = numberBefore+1; i < sizeOfArray; i++)
		{
			yValue = yValue + PLAYER_ICON_RADIUS;
			y_grid[i] = yValue;
		}
	}
	
	public static int drawCreatePlayers(Field fieldForCreatePlayer, Canvas canvas, Paint paint)
	{
		int threeTimesIcon = PLAYER_ICON_RADIUS*3;
		int xValue = threeTimesIcon;
		
		// add players at bottom of screen, 75dp width between each of them
		Location playerLocQB = new Location(xValue, BUTTON_Y_VALUE);
		fieldForCreatePlayer.addPlayer(playerLocQB, Position.QB);
		
		xValue += threeTimesIcon;
		
		Location playerLocWR = new Location(xValue, BUTTON_Y_VALUE);
		fieldForCreatePlayer.addPlayer(playerLocWR, Position.WR);
		
		xValue += threeTimesIcon;
		
		Location playerLocRB = new Location(xValue, BUTTON_Y_VALUE);
		fieldForCreatePlayer.addPlayer(playerLocRB, Position.RB);
		
		xValue += threeTimesIcon;
		
		Location playerLocFB = new Location(xValue, BUTTON_Y_VALUE);
		fieldForCreatePlayer.addPlayer(playerLocFB, Position.FB);
		
		xValue += threeTimesIcon;
		
		Location playerLocTE = new Location(xValue, BUTTON_Y_VALUE);
		fieldForCreatePlayer.addPlayer(playerLocTE, Position.TE);
		
		xValue += threeTimesIcon;
		
		Location playerLocC = new Location(xValue, BUTTON_Y_VALUE);
		fieldForCreatePlayer.addPlayer(playerLocC, Position.C);
		
		xValue += threeTimesIcon;
		
		Location playerLocG = new Location(xValue, BUTTON_Y_VALUE);
		fieldForCreatePlayer.addPlayer(playerLocG, Position.G);
		
		xValue += threeTimesIcon;
		
		Location playerLocT = new Location(xValue, BUTTON_Y_VALUE);
		fieldForCreatePlayer.addPlayer(playerLocT, Position.T);
		
		xValue += PLAYER_ICON_RADIUS*2;

		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(TEXT_WIDTH);

		// y values are all the same, so just reuse one
		float eightPlayerY = playerLocQB.getY() - TOP_ANDROID_BAR;
		
		// for drawing the text
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(PLAYER_ICON_RADIUS);
		// descent and ascent are used for centering text vertically
		float height = (playerLocQB.getY()-TOP_ANDROID_BAR)-((paint.descent() + paint.ascent()) / 2);

		
		for (int i = 0; i < fieldForCreatePlayer.getAllPlayers().size(); i++)
		{
			paint.setColor(Color.WHITE);
			Player tempPlayer = fieldForCreatePlayer.getPlayer(i);
			float playerXLocation = tempPlayer.getLocation().getX();
			canvas.drawCircle(playerXLocation, eightPlayerY, PLAYER_ICON_RADIUS, paint);
			paint.setStyle(Paint.Style.STROKE);
			paint.setColor(Color.BLACK);
			canvas.drawCircle(playerXLocation, eightPlayerY, PLAYER_ICON_RADIUS, paint);
			paint.setStyle(Paint.Style.FILL);
			canvas.drawText(tempPlayer.getPosition().toString(), playerXLocation, height, paint);
		}
		return xValue;
	}
	
	public static void drawPlayers(boolean bitmap, Field field, Canvas canvas, 
			Paint paint, int playerIndex, int color)
	{
		int xOffset;
		int yOffset;
		if (bitmap)
		{
			xOffset = LEFT_MARGIN;
			yOffset = TOP_MARGIN + TOP_ANDROID_BAR;
		}
		else
		{
			xOffset = 0;
			yOffset = TOP_ANDROID_BAR;
		}
		int xpos = -1;
		int ypos = -1;
		float height = -1;
		
		// for drawing the text
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(PLAYER_ICON_RADIUS);
		paint.setStrokeWidth(TEXT_WIDTH);
		
		for (int i = 0; i < field.getAllPlayers().size(); i++)
		{
			Player tempPlayer = field.getAllPlayers().get(i);
			Location tempLocation = tempPlayer.getLocation();
			xpos = tempLocation.getX() - xOffset;
			ypos = tempLocation.getY() - yOffset;
			// this is the selected player
			if (playerIndex == i)
			{
				paint.setColor(color);
			}
			else
			{
				paint.setColor(Color.WHITE);
			}
			// draw the player again, but red this time
			canvas.drawCircle(xpos, ypos, PLAYER_ICON_RADIUS, paint);
			// descent and ascent are used for centering text vertically
			height = ypos-((paint.descent() + paint.ascent()) / 2);
			paint.setStyle(Paint.Style.STROKE);
			paint.setColor(Color.BLACK);
			canvas.drawCircle(xpos, ypos, PLAYER_ICON_RADIUS, paint);
			paint.setStyle(Paint.Style.FILL);
			canvas.drawText(tempPlayer.getPosition().toString(), xpos, height, paint);
		}
	}
	
	public static void drawArrow(Canvas canvas, Paint paint)
	{
		float temp = (int)(Math.cos(Math.PI/4)*PIXELS_PER_YARD)*2;
		canvas.drawLine(0, 0, -temp, temp, paint);
		canvas.drawLine(0, 0, temp, temp, paint);
	}
	
	public static void drawBlock(Canvas canvas, Paint paint)
	{
		float twoYards = PIXELS_PER_YARD*2;
		canvas.drawLine(0, 0, -twoYards, 0, paint);
		canvas.drawLine(0, 0, twoYards, 0, paint);
	}
	
	public static void drawEndOfRoute(Canvas canvas, Paint paint, int x, int y, float differenceAngle, Route route)
	{
		if (route == Route.ARROW)
		{
			canvas.save();
				canvas.translate(x, y);
				canvas.rotate(differenceAngle-90);
				drawArrow(canvas, paint);
			canvas.restore();
		}
		// draw block
		else
		{
			canvas.save();
				canvas.translate(x, y);
				canvas.rotate(differenceAngle-90);
				drawBlock(canvas, paint);
			canvas.restore();
		}
	}
	
	public static void drawRoutes(boolean bitmap, Field field, Canvas canvas, Paint paint)
	{
		int xOffset;
		int yOffset;
		if (bitmap)
		{
			xOffset = LEFT_MARGIN;
			yOffset = TOP_MARGIN + TOP_ANDROID_BAR;
		}
		else
		{
			xOffset = 0;
			yOffset = TOP_ANDROID_BAR;
		}
		
		paint.setColor(Color.YELLOW);
		paint.setStrokeWidth(FIELD_LINE_WIDTHS);
		
		for (int i = 0; i < field.getAllPlayers().size(); i++)
		{
			Player tempPlayer = field.getAllPlayers().get(i);
			// set x and y previous values to player's x/y values
			int previousX = tempPlayer.getLocation().getX() - xOffset;
			int previousY = tempPlayer.getLocation().getY() - yOffset;
			if (tempPlayer.getPath() == Path.DOTTED)
			{
				paint.setPathEffect(new DashPathEffect(new float[] {10/DENSITY, 10/DENSITY}, 0));
			}
			for (int j = 0; j < tempPlayer.getRouteLocations().size(); j++)
			{
				Location tempLocation = tempPlayer.getRouteLocations().get(j);
				int currentX = tempLocation.getX() - xOffset;
				int currentY = tempLocation.getY() - yOffset;
				canvas.drawLine(previousX, previousY, currentX, currentY, paint);
				if (j == tempPlayer.getRouteLocations().size()-1)
				{
					int deltaX = previousX - currentX;
					int deltaY = previousY - currentY;
					float differenceAngle = (float)(Math.atan2(deltaY, deltaX) * 180 / Math.PI);
					// solid line
					paint.setPathEffect(null);
					drawEndOfRoute(canvas, paint, currentX, currentY, differenceAngle, tempPlayer.getRoute());
				}
				previousX = currentX;
				previousY = currentY;
			}
			// solid line
			paint.setPathEffect(null);
		}
		// solid line
		paint.setPathEffect(null);
	}
	
	public static void drawField(boolean bitmap, Canvas canvas, Paint paint)
	{
		int adjustedLeftMargin;
		int adjustedRightMargin;
		int adjustedTopMargin;
		int adjustedBottomMargin;
		if (bitmap)
		{
			adjustedLeftMargin = 0;
			adjustedRightMargin = RIGHT_MARGIN - LEFT_MARGIN;
			adjustedTopMargin = 0;
			adjustedBottomMargin = BOTTOM_MARGIN - TOP_MARGIN;
		}
		else
		{
			adjustedLeftMargin = LEFT_MARGIN;
			adjustedRightMargin = RIGHT_MARGIN;
			adjustedTopMargin = TOP_MARGIN;
			adjustedBottomMargin = BOTTOM_MARGIN;
		}
		// green color
		paint.setColor(0xFF007900);
		
		// draw the field
		canvas.drawRect(adjustedLeftMargin, adjustedTopMargin, adjustedRightMargin, adjustedBottomMargin, paint);
		
		// 2 = number of pixels between out of bounds and hash mark
		// 18 = length of the hash mark
		drawHashLines(canvas, paint);
		
		// PIXELS_PER_YARD * 5, because we are drawing 5 yard lines
		drawFiveYardLines(canvas, paint);
		
		// blue color
		paint.setColor(0xFF000080);
		
		// draw line of scrimmage
		paint.setStrokeWidth(Math.round(6/DENSITY));
		
		// want to draw line of scrimmage at 20 yard line
		float twentyYards = PIXELS_PER_YARD * 20;
		int lineOfScrimmageYValue = (int) (adjustedBottomMargin - twentyYards + Math.round(FIELD_LINE_WIDTHS/2));
		canvas.drawLine(adjustedLeftMargin, lineOfScrimmageYValue, adjustedRightMargin, lineOfScrimmageYValue, paint);
		
		// fill = fill enclosed shapes with the color, like a circle with the middle one color
		paint.setStyle(Paint.Style.FILL);
	}
	
	public static void drawHashLines(Canvas canvas, Paint paint)
	{
		int outOfBoundsSpacing = Math.round(2/DENSITY);
		int hashLength = Math.round(18/DENSITY);
		int halfHashLength = Math.round(hashLength/2);
		
		paint.setColor(Color.WHITE);
		// draw a stroke, not a line
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(FIELD_LINE_WIDTHS);
		
		float middleHashOffset = SCREEN_WIDTH * 0.375f;
		for (int i = 0; i < 45; i++)
		{
			if (!(i % 5 == 0))
			{
				float temp = BOTTOM_MARGIN - (PIXELS_PER_YARD*i) + Math.round(FIELD_LINE_WIDTHS/2);
				float leftMarginPlusOutOfBounds = LEFT_MARGIN + outOfBoundsSpacing;
				float rightMarginPlusHashLength = RIGHT_MARGIN - middleHashOffset;
				float leftMarginPlusHashLength = LEFT_MARGIN + middleHashOffset;
				canvas.drawLine(leftMarginPlusOutOfBounds, temp, 
						leftMarginPlusOutOfBounds + hashLength, temp, paint);
				canvas.drawLine(RIGHT_MARGIN - hashLength, temp, RIGHT_MARGIN - outOfBoundsSpacing, temp, paint);
				canvas.drawLine(leftMarginPlusHashLength - halfHashLength, temp, 
						leftMarginPlusHashLength + halfHashLength, temp, paint);
				canvas.drawLine(rightMarginPlusHashLength - halfHashLength, temp, 
						rightMarginPlusHashLength + halfHashLength, temp, paint);
			}
		}
	}
	
	public static void drawFiveYardLines(Canvas canvas, Paint paint)
	{
		paint.setColor(Color.WHITE);
		// draw a stroke, not a line
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(FIELD_LINE_WIDTHS);
		
		float pixelsPerFiveYards = PIXELS_PER_YARD*5;
		for (int i = 0; i < 9; i++)
		{
			float temp = BOTTOM_MARGIN - pixelsPerFiveYards + Math.round(FIELD_LINE_WIDTHS/2) - i*pixelsPerFiveYards;
			canvas.drawLine(LEFT_MARGIN, temp, RIGHT_MARGIN, temp, paint);
		}
	}
	
	public static void actionMove(Field field, int playerIndex, int x, int y)
	{
		if (playerIndex != -1)
		{
			Player thisPlayer =  field.getPlayer(playerIndex);
			Location thisLocation = thisPlayer.getLocation();
			int deltaX = x - thisLocation.getX();
			int deltaY = y - thisLocation.getY();
			for (int i = 0; i < thisPlayer.getRouteLocations().size(); i++)
			{
				Location routeLocation = thisPlayer.getRouteLocations().get(i);
				routeLocation.changeLocation(routeLocation.getX() + deltaX, routeLocation.getY() + deltaY);
			}
			field.getPlayer(playerIndex).setLocation(new Location(x, y));
		}
	}
	
	public static int binarySearchGridX(int xValue, int minIndex, int maxIndex)
	{
		int midpoint = (minIndex + maxIndex) /2;

		if (x_grid[midpoint] > xValue)
		{
			if (x_grid[midpoint-1] < xValue)
			{
				int halfDifference = (x_grid[midpoint] - x_grid[midpoint-1])/2;
				if (xValue + halfDifference >= x_grid[midpoint])
				{
					return midpoint;
				}
				else
				{
					return midpoint-1;
				}
			}
			else
			{
				return binarySearchGridX(xValue, minIndex, midpoint-1);
			}
		}
		else if (x_grid[midpoint] < xValue)
		{
			if (x_grid[midpoint+1] > xValue)
			{
				int halfDifference = (x_grid[midpoint+1] - x_grid[midpoint])/2;
				if (xValue + halfDifference >= x_grid[midpoint+1])
				{
					return midpoint+1;
				}
				else
				{
					return midpoint;
				}
			}
			else
			{
				return binarySearchGridX(xValue, midpoint+1, maxIndex);
			}
		}
		else
		{
			return midpoint;
		}
	}
	
	public static int binarySearchGridY(int yValue, int minIndex, int maxIndex)
	{
		int midpoint = (minIndex + maxIndex) /2;

		if (y_grid[midpoint] > yValue)
		{
			if (y_grid[midpoint-1] < yValue)
			{
				int halfDifference = (y_grid[midpoint] - y_grid[midpoint-1])/2;
				if (yValue + halfDifference >= y_grid[midpoint])
				{
					return midpoint;
				}
				else
				{
					return midpoint-1;
				}
			}
			else
			{
				return binarySearchGridY(yValue, minIndex, midpoint-1);
			}
		}
		else if (y_grid[midpoint] < yValue)
		{
			if (y_grid[midpoint+1] > yValue)
			{
				int halfDifference = (y_grid[midpoint+1] - y_grid[midpoint])/2;
				if (yValue + halfDifference >= y_grid[midpoint+1])
				{
					return midpoint+1;
				}
				else
				{
					return midpoint;
				}
			}
			else
			{
				return binarySearchGridY(yValue, midpoint+1, maxIndex);
			}
		}
		else
		{
			return midpoint;
		}
	}
	
	// boolean[0] = added more than 11 players
	// boolean[1] = player added on other side of line of scrimmage
	// boolean[2] = player put on top of another player
	public static boolean[] actionUp(Field field, int playerIndex, int x, int y, boolean moreThanElevenPlayers, boolean movePlayer,
			boolean addingPlayer)
	{
		boolean retVal[] = {moreThanElevenPlayers, false, false};
		if (playerIndex != -1)
		{
			if (movePlayer || addingPlayer)
			{
				// is player outside of the field?
				if (x < LEFT_MARGIN + PLAYER_ICON_RADIUS || x > RIGHT_MARGIN - PLAYER_ICON_RADIUS 
						|| y < TOP_MARGIN + TOP_ANDROID_BAR + PLAYER_ICON_RADIUS 
						|| y > BOTTOM_MARGIN + TOP_ANDROID_BAR - PLAYER_ICON_RADIUS)
				{
					field.getAllPlayers().remove(playerIndex);
					retVal[0] = false;
					// disable route possibilities
					EditorActivity.setPlayerIndex(-1);
				}
				else
				{
					Player tempPlayer = field.getAllPlayers().get(playerIndex);
					
					int playerX = tempPlayer.getLocation().getX();
					int playerY = tempPlayer.getLocation().getY();
					
					int tempXLocation;
					int tempYLocation;
					
					if (playerX < x_grid[0])
					{
						tempXLocation = x_grid[0];
					}
					else if (playerX > x_grid[x_grid.length-1])
					{
						tempXLocation = x_grid[x_grid.length-1];
					}
					else
					{
						int x_index = binarySearchGridX(playerX, 0, x_grid.length-1);
						tempXLocation = x_grid[x_index];
					}
					
					if (playerY < y_grid[0])
					{
						tempYLocation = y_grid[0];
					}
					else if (playerY > y_grid[y_grid.length-1])
					{
						tempYLocation = y_grid[y_grid.length-1];
					}
					else
					{
						int y_index = binarySearchGridY(playerY, 0, y_grid.length-1);
						tempYLocation = y_grid[y_index];
					}
					
					if (tempYLocation <= y_grid[lineOfScrimmageIndex])
					{
						retVal[1] = true;
					}
					for (int i = 0; i < field.getAllPlayers().size(); i++)
					{
						if (playerIndex != i)
						{
							Location playerLoc = field.getPlayer(i).getLocation();
							
							playerX = playerLoc.getX();
							playerY = playerLoc.getY();
							
							if ((tempXLocation + PLAYER_ICON_RADIUS == playerX || tempXLocation == playerX || tempXLocation - PLAYER_ICON_RADIUS == playerX) &&
									(tempYLocation + PLAYER_ICON_RADIUS == playerY || tempYLocation == playerY || tempYLocation - PLAYER_ICON_RADIUS == playerY))
							{
								retVal[2] = true;
								break;
							}
						}
					}
					Location tempLocation = new Location(tempXLocation, tempYLocation);
					tempPlayer.setLocation(tempLocation);
					EditorActivity.setPlayerIndex(playerIndex);
				}
			}
			else
			{
				EditorActivity.setPlayerIndex(playerIndex);
			}
		}
		else
		{
			EditorActivity.setPlayerIndex(-1);
		}
		return retVal;
	}
	
	// boolean[0] = added more than 11 players
	// boolean[1] = adding a new player
	// boolean[2] = clicking on path button
	// boolean[3] = clicking on route button
	public static boolean[] actionDown(Field field, Field fieldForCreatePlayer, int x, int y, 
			int playerIndex, Route route, Path path, int BUTTON_X_VALUE)
	{
		int playerXPos = -1;
		int playerYPos = -1;
		boolean[] retVal = {false, false, false, false};
		
		double playerIndexDistance = Float.MAX_VALUE;
		
		boolean staticPlayerSelected = false; 
		// loop for selecting bottom 8 players
		for (int i = 0; i < fieldForCreatePlayer.getAllPlayers().size(); i++)
		{
			playerXPos = fieldForCreatePlayer.getAllPlayers().get(i).getLocation().getX();
			playerYPos = fieldForCreatePlayer.getAllPlayers().get(i).getLocation().getY();
			// calculate distance between user click and this player
			double dist = Math.sqrt(((playerXPos-x)*(playerXPos-x)) 
					+ ((playerYPos-y)*(playerYPos-y)));
			if (dist < TOUCH_SENSITIVITY)
			{
				// this player has been selected
				Player temp = fieldForCreatePlayer.getAllPlayers().get(i);
				field.addPlayer(temp.getLocation(), temp.getPosition(), route, path); // add to field
				playerIndex = field.getAllPlayers().size()-1; // this player is the last 
				// player to be added to field
				staticPlayerSelected = true; // flag to say that one of the 8 players has been selected
				retVal[1] = true;
			}
		}
		if (!staticPlayerSelected)
		{
			boolean hasBeenSet = false;
			// check if one of the players has been selected
			for (int i = 0; i < field.getAllPlayers().size(); i++)
			{
				playerXPos = field.getAllPlayers().get(i).getLocation().getX();
				playerYPos = field.getAllPlayers().get(i).getLocation().getY();
				// calculate distance between user click and this player
				double distance = Math.sqrt(((playerXPos-x)*(playerXPos-x)) 
						+ ((playerYPos-y)*(playerYPos-y)));
				if (distance < TOUCH_SENSITIVITY)
				{
					if (distance < playerIndexDistance)
					{
						playerIndexDistance = distance;
						// this player has been selected
						playerIndex = i;
						// routes can be changed for this player
						hasBeenSet = true;
					}
				}
			}
			// if not selected reset player index
			if (!hasBeenSet)
			{
				int doubleLineWidths = FIELD_LINE_WIDTHS * 2;
				int quadLineWidths = doubleLineWidths * 2;
				int buttonLength = 5 * PLAYER_ICON_RADIUS + quadLineWidths;
				
				float buttonLowerValue = BUTTON_Y_VALUE - PLAYER_ICON_RADIUS - doubleLineWidths;
				float buttonUpperValue = BUTTON_Y_VALUE + PLAYER_ICON_RADIUS + doubleLineWidths;
				float secondButtonStartPos = BUTTON_X_VALUE + buttonLength + PLAYER_ICON_RADIUS;
				if (x >= BUTTON_X_VALUE && x <= (BUTTON_X_VALUE + buttonLength) && y >= buttonLowerValue && y <= buttonUpperValue)
				{
					retVal[2] = true;
				}
				else if (x >= secondButtonStartPos && x <= (secondButtonStartPos + buttonLength) 
						&& y >= buttonLowerValue && y <= buttonUpperValue)
				{
					retVal[3] = true;
				}
				else
				{
					EditorActivity.setPlayerIndex(-1);
				}
			}
			else
			{
				EditorActivity.setPlayerIndex(playerIndex);
			}
		}
		else
		{
			EditorActivity.setPlayerIndex(playerIndex);
			if (field.getAllPlayers().size() > 11)
			{
				retVal[0] = true;
			}
		}
		return retVal;
	}
	
	public static void drawButtons(Canvas canvas, Paint paint, Route route, Path path, int BUTTON_X_VALUE)
	{
		paint.setStrokeWidth(FIELD_LINE_WIDTHS);
		float value = BUTTON_Y_VALUE-TOP_ANDROID_BAR;
		// gray for buttons
		paint.setColor(0xE7E7E7FF);
		
		int doubleLineWidths = FIELD_LINE_WIDTHS * 2;
		int quadLineWidths = doubleLineWidths * 2;
		int fiveIconRadius = 5 * PLAYER_ICON_RADIUS;
	 
		float buttonHeightOffset = PLAYER_ICON_RADIUS + doubleLineWidths;
		float lineLength = fiveIconRadius + doubleLineWidths;
		float buttonLength = fiveIconRadius + quadLineWidths;

		float secondButtonStartPos = BUTTON_X_VALUE + buttonLength + PLAYER_ICON_RADIUS;
		canvas.drawRect(BUTTON_X_VALUE, value + buttonHeightOffset, 
				BUTTON_X_VALUE + buttonLength, value - buttonHeightOffset, paint);
		canvas.drawRect(secondButtonStartPos, value + buttonHeightOffset, 
				secondButtonStartPos + buttonLength, value - buttonHeightOffset, paint);
		paint.setColor(Color.BLACK);
		if (path == Path.DOTTED)
		{
			paint.setPathEffect(new DashPathEffect(new float[] {10/DENSITY, 10/DENSITY}, 0));
		}
		canvas.drawLine(BUTTON_X_VALUE + doubleLineWidths, value, BUTTON_X_VALUE + lineLength, value, paint);
		paint.setPathEffect(null);
		canvas.drawLine(secondButtonStartPos + doubleLineWidths, value, secondButtonStartPos + lineLength, value, paint);
		drawEndOfRoute(canvas, paint, (int)(secondButtonStartPos + lineLength), (int)value, 180, route);
	}
}
