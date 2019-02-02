package com.sock.connect5_txt_v2;

public class Board {
	private int board[][];

	public int[][] getBoard() {
		return board;
	}

	public void setBoard(int[][] board) {
		this.board = board;
	}

	public Board(int rows, int cols) {
		this.board = new int[rows][cols];
		
		init();	// Initialise the board with all 0's after creating
	}
	
	public void init() {
		for (int row = 0; row < Game.ROWS; row++) {
			for (int col = 0; col < Game.COLS; col++) {
				this.board[row][col] = 0;
			}
		}
	}
	
	public void setCol(int col, int type) {
		for (int i = Game.ROWS - 1; i >= 0; i--) {
			if (board[i][col] == 0) {
				board[i][col] = type;
				break;
			}
		}
	}
	
	public boolean checkCol(int col, int type) {
		System.out.println("Checking Column: " + col + " for Player " + type);
		if (topRowFull(col)) {
			return false;
		}
		
		return true;
	}
	
	public boolean topRowFull(int col) {
		if (board[0][col] != 0) { // if top row is not 0, col is full
			System.out.println("\nERROR: Column " + col + " is full\n");
			return true;
		}
		
		return false;
	}
	
	public boolean full() {
		for (int row = 0; row < Game.ROWS; row++) {
			for (int col = 0; col < Game.COLS; col++) {
				if (board[row][col] == 0)
					return false;
			}
		}

		return true;
	}
	
	public void draw() {
		System.out.println();

		for (int i = 0; i < Game.COLS; i++) {
			System.out.print(" " + i + " ");
		}

		System.out.println();

		for (int row = 0; row < Game.ROWS; row++) {
			for (int col = 0; col < Game.COLS; col++) {
				System.out.print("[" + board[row][col] + "]");
			}
			System.out.println();
		}
	}
	
	public void showWin(int[] fiveInARow) {
		for (int i = 0; i < 10; i += 2) {
			board[fiveInARow[i]][fiveInARow[i + 1]] = 3; // Highlight winning line
		}

		System.out.println("Winning Move");
		
		draw();
	}
	
	public boolean win() {
		boolean gameOver = false;
		int[] fiveInARow = new int[10];

		for (int row = 0; row <= Game.ROWS - Game.CONNECT; row++) {
			// Diagonal /
			for (int col = Game.CONNECT - 1; col < Game.COLS; col++) {
				if (board[row][col] != 0 
						&& board[row + 1][col - 1] == board[row][col]
						&& board[row + 2][col - 2] == board[row][col] 
						&& board[row + 3][col - 3] == board[row][col]
						&& board[row + 4][col - 4] == board[row][col]) {
					gameOver = true;
					fiveInARow = new int[] { row, col, row + 1, col - 1, row + 2, col - 2, row + 3, col - 3, row + 4, col - 4 };
					break;
				}
			}

			// Diagonal \
			for (int col = 0; col <= Game.COLS - Game.CONNECT; col++) {
				if (board[row][col] != 0 
						&& board[row + 1][col + 1] == board[row][col]
						&& board[row + 2][col + 2] == board[row][col] 
						&& board[row + 3][col + 3] == board[row][col]
						&& board[row + 4][col + 4] == board[row][col]) {
					gameOver = true;
					fiveInARow = new int[] { row, col, row + 1, col + 1, row + 2, col + 2, row + 3, col + 3, row + 4, col + 4 };
					break;
				}
			}
		}

		// Check Rows
		for (int row = 0; row < Game.ROWS; row++) {
			for (int col = 0; col <= Game.COLS - Game.CONNECT; col++) {
				if (board[row][col] != 0 
						&& board[row][col + 1] == board[row][col]
						&& board[row][col + 2] == board[row][col] 
						&& board[row][col + 3] == board[row][col]
						&& board[row][col + 4] == board[row][col]) {
					gameOver = true;
					fiveInARow = new int[] { row, col, row, col + 1, row, col + 2, row, col + 3, row, col + 4 };
					break;
				}
			}
		}

		// Check Columns
		for (int row = 0; row <= Game.ROWS - Game.CONNECT; row++) {
			for (int col = 0; col < Game.COLS; col++) {
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
			showWin(fiveInARow);
		}

		return gameOver;
	}
}