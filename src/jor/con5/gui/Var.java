/*
 * Joe O'Regan
 * 
 * Var.java
 * 09/01/2018
 * 
 * Connect5
 * Graphics based 5 in a row game
 */
package jor.con5.gui;

public class Var {
	// Text Game
	public final static int CONNECT = 5;

	public final static int PLAYER_1 = 1;
	public final static int PLAYER_2 = 2;

	public final static int ROWS = 6;
	public final static int COLS = 9;
	
	// Graphics Game
	public final static int EMPTY = 0;
	
	public final static int PADDING = 13;
	public final static int PADDING_TOP = 150;
	
	public final static int DISK_SPACING = 60;
	public final static int DISK_DIAMETER = 50;
	
	public final static int COL_Y = 135;
	public final static int COL_WIDTH = DISK_DIAMETER;
	public final static int COL_HEIGHT = PADDING + (DISK_SPACING * ROWS);
	
	
	public final static int BUTTON_Y = 50;
	public final static int BUTTON_WIDTH = 133;
	public final static int BUTTON_HEIGHT = 50;

	public final static int WIDTH = (DISK_SPACING * COLS) + (PADDING * 2);
	//public final static int WIDTH = 1280;
	public final static int HEIGHT = 720;
	
	public final static int DISK_OUTLINE = (DISK_SPACING - DISK_DIAMETER) / 2;
	
	public final static int TOP_TEXT_X = 125;
	public final static int BOTTOM_TEXT_X = 155;
	public final static int BOTTOM_TEXT_Y = PADDING_TOP + (DISK_SPACING * (ROWS + 1));
	public final static int BOTTOM_TEXT_SIZE = 28;
	public final static int TOP_TEXT_SIZE = 18;
}