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
	private int board[][] = new int[ROWS][COLS];

	public int[][] getBoard() {
		return board;
	}

	public void setBoard(int[][] board) {
		this.board = board;
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

	public static boolean checkWin(int[][] board) {
		int[][] winBoard = new int[ROWS][COLS]; //
		boolean gameOver = false;
		int[] fiveInARow = new int[10];

		for (int row = 0; row <= ROWS - CONNECT; row++) {
			// Diagonal /
			for (int col = CONNECT - 1; col < COLS; col++) {
				if (board[row][col] != 0 && board[row + 1][col - 1] == board[row][col]
						&& board[row + 2][col - 2] == board[row][col] && board[row + 3][col - 3] == board[row][col]
						&& board[row + 4][col - 4] == board[row][col]) {
					gameOver = true;
					fiveInARow = new int[] { row, col, row + 1, col - 1, row + 2, col - 2, row + 3, col - 3, row + 4,
							col - 4 };
					break;
				}
			}

			// Diagonal \
			for (int col = 0; col <= COLS - CONNECT; col++) {
				if (board[row][col] != 0 && board[row + 1][col + 1] == board[row][col]
						&& board[row + 2][col + 2] == board[row][col] && board[row + 3][col + 3] == board[row][col]
						&& board[row + 4][col + 4] == board[row][col]) {
					gameOver = true;
					fiveInARow = new int[] { row, col, row + 1, col + 1, row + 2, col + 2, row + 3, col + 3, row + 4,
							col + 4 };
					break;
				}
			}
		}

		// Check Rows
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col <= COLS - CONNECT; col++) {
				if (board[row][col] != 0 && board[row][col + 1] == board[row][col]
						&& board[row][col + 2] == board[row][col] && board[row][col + 3] == board[row][col]
						&& board[row][col + 4] == board[row][col]) {
					gameOver = true;
					fiveInARow = new int[] { row, col, row, col + 1, row, col + 2, row, col + 3, row, col + 4 };
					break;
				}
			}
		}

		// Check Columns
		for (int row = 0; row <= ROWS - CONNECT; row++) {
			for (int col = 0; col < COLS; col++) {
				if (board[row][col] != 0 && board[row + 1][col] == board[row][col]
						&& board[row + 2][col] == board[row][col] && board[row + 3][col] == board[row][col]
						&& board[row + 4][col] == board[row][col]) {
					gameOver = true;
					fiveInARow = new int[] { row, col, row + 1, col, row + 2, col, row + 3, col, row + 4, col };
					break;
				}
			}
		}

		if (gameOver) {
			show5InARow(winBoard, fiveInARow);
		}

		return gameOver;
	}

	public static void show5InARow(int[][] board, int[] fiveInARow) {
		for (int i = 0; i < 10; i += 2) {
			board[fiveInARow[i]][fiveInARow[i + 1]] = 3; // Highlight winning line
		}

		System.out.println("Winning Move");
		displayBoard(board);
	}

	public static boolean boardFull(int[][] board) {
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				if (board[row][col] == 0)
					return false;
			}
		}

		return true;
	}

	public static boolean checkCol(int col, int[][] board, int type) {
		System.out.println("Checking Column: " + col + " for Player " + type);

		if (board[0][col] != 0) { // top row full
			System.out.println("\nERROR: Column " + col + " is full\n");
			return false;
		}

		return true;
	}

	public static void setCol(int col, int type, int[][] board) {
		for (int i = ROWS - 1; i >= 0; i--) {
			if (board[i][col] == 0) {
				board[i][col] = type;
				break;
			}
		}
	}

	public void initBoard() {
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				this.board[row][col] = 0;
			}
		}
	}

	// public static void initBoard(int[][][] board) {
	public static void init(int[][][] board) {
		for (int i = 0; i < board.length; i++) {
			for (int row = 0; row < ROWS; row++) {
				for (int col = 0; col < COLS; col++) {
					board[i][row][col] = 0;
				}
			}
		}
	}

	public static void displayBoard(int[][] board) {
		System.out.println();

		for (int i = 0; i < COLS; i++) {
			System.out.print(" " + i + " ");
		}

		System.out.println();

		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				System.out.print("[" + board[row][col] + "]");
			}
			System.out.println();
		}
	}

	public synchronized boolean legalMove(int column, Player player, Game game) {
		if (column < 0 || column >= COLS) {
			return false; // value out of range
		}

		// if it is the current player,
		// if (player == game.getCurrentPlayer() && checkCol(column, player.getBoard(),
		// player.getType())) {
		// Game.setCol(column, currentPlayer.getType(), player.getBoard());
		if (player == game.getCurrentPlayer() && checkCol(column, this.board, player.getType())) {
			Game.setCol(column, currentPlayer.getType(), this.board);
			currentPlayer = currentPlayer.getOpponent();
			currentPlayer.otherPlayerMoved(column);

			return true;
		}

		return false;
	}
}