/*
 * Connect5
 * Joe O'Regan
 * 02/02/2019
 */
package com.sock.connect5_txt_v2;

public class Game {
	public final static int CONNECT = 5;
	public final static int PLAYER_1 = 1;
	public final static int PLAYER_2 = 2;
	public final static int ROWS = 6;
	public final static int COLS = 9;

	private Player currentPlayer;
	private Board board = new Board(ROWS, COLS);

	public Board getBoard() {
		return board;
	}

	public void switchCurrentPlayer(Player player) {
		this.currentPlayer = player.getOpponent();
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player player) {
		this.currentPlayer = player;
	}

	public synchronized boolean legalMove(int column, Player player, Game game) {
		// Check column number is in range
		if (column < 0 || column >= COLS) {
			return false; // value out of range
		}

		if (player == game.getCurrentPlayer() && board.checkCol(column, player.getType())) {
			board.setCol(column, currentPlayer.getType());
			currentPlayer = currentPlayer.getOpponent();
			currentPlayer.otherPlayerMoved(column);

			return true;
		}

		return false;
	}
}