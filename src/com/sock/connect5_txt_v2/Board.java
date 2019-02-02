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
		init(); // Initialise the board with all 0's after creating
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
				draw();
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

	public boolean win() {
		for (int r = 0; r <= Game.ROWS - Game.CONNECT; r++) {
			// Diagonal /
			for (int c = Game.CONNECT - 1; c < Game.COLS; c++) {
				if (winningLine(new int[] { r, c, r + 1, c - 1, r + 2, c - 2, r + 3, c - 3, r + 4, c - 4 })) {
					return true;
				}
			}

			// Diagonal \
			for (int col = 0; col <= Game.COLS - Game.CONNECT; col++) {
				if (winningLine(new int[] { r, col, r + 1, col + 1, r + 2, col + 2, r + 3, col + 3, r + 4, col + 4 })) {
					return true;
				}
			}
		}

		// Check Rows
		for (int row = 0; row < Game.ROWS; row++) {
			for (int col = 0; col <= Game.COLS - Game.CONNECT; col++) {
				if (winningLine(new int[] { row, col, row, col + 1, row, col + 2, row, col + 3, row, col + 4 })) {
					return true;
				}
			}
		}

		// Check Columns
		for (int row = 0; row <= Game.ROWS - Game.CONNECT; row++) {
			for (int col = 0; col < Game.COLS; col++) {
				if (winningLine(new int[] { row, col, row + 1, col, row + 2, col, row + 3, col, row + 4, col })) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean winningLine(int[] b) {
		if (board[b[0]][b[1]] != 0 && board[b[2]][b[3]] == board[b[0]][b[1]] && board[b[4]][b[5]] == board[b[0]][b[1]]
				&& board[b[6]][b[7]] == board[b[0]][b[1]] && board[b[8]][b[9]] == board[b[0]][b[1]]) {
			for (int i = 0; i < 10; i += 2) {
				board[b[i]][b[i + 1]] = 3; // Highlight winning line
			}

			System.out.println("Winning Move");
			draw();

			return true;
		}

		return false;
	}
}