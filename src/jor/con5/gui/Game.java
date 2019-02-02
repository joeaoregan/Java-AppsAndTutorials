/*
 * Joe O'Regan
 * 
 * Game.java
 * 08/01/2018
 * 
 * Connect5
 * Text and graphics based 5 in a row games
 */
package jor.con5.gui;

public class Game {
	public int[][] board, winBoard;
	private int player;
	private boolean gameOver;
	public int[] fiveInARow = new int[10];

	public boolean getGameOver() {
		return gameOver;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public int getPlayer() {
		return player;
	}

	public void setPlayer(int player) {
		this.player = player;
	}

	/*
	 * Game constructor
	 */
	public Game() {
		player = 1;
		board = new int[Var.ROWS][Var.COLS];
		winBoard = new int[Var.ROWS][Var.COLS];
		//setBoard(board);
		//setBoard(winBoard);
		setBoard(new int[][][] {board, winBoard});
	}

	/*
	 * Print the game board to the console window
	 */
	public void displayBoard(int[][] board) {
		for (int i = 1; i < 10; i++) {
			System.out.print(i + " ");
		}

		System.out.println();

		for (int row = 0; row < Var.ROWS; row++) {
			for (int col = 0; col < Var.COLS; col++) {
				System.out.print(board[row][col] + " ");
			}
			System.out.println();
		}
	}

	/*
	 * Set all board cells to 0
	 */
	/*
	public void setBoard(int[][] board) {
		for (int row = 0; row < Var.ROWS; row++) {
			for (int col = 0; col < Var.COLS; col++) {
				board[row][col] = 0;
			}
		}
	}
	*/
	public void setBoard(int[][][] board) {
		for (int i = 0; i < board.length; i++) {
			for (int row = 0; row < Var.ROWS; row++) {
				for (int col = 0; col < Var.COLS; col++) {
					board[i][row][col] = 0;
				}
			}
		}
	}

	/*
	 * Check for 5 in a row diagonal, horizontal, and vertical, and highlight the
	 * winning move if winner found
	 */
	public void checkWin() {
		for (int row = 0; row <= Var.ROWS - Var.CONNECT; row++) {
			// Diagonal /
			for (int col = Var.CONNECT - 1; col < Var.COLS; col++) {
				if (board[row][col] == player && board[row + 1][col - 1] == player && board[row + 2][col - 2] == player
						&& board[row + 3][col - 3] == player && board[row + 4][col - 4] == player) {
					gameOver = true;
					fiveInARow = new int[] { row, col, row + 1, col - 1, row + 2, col - 2, row + 3, col - 3, row + 4,
							col - 4 };
					show5InARow();
					break;
				}
			}

			// Diagonal \
			for (int col = 0; col <= Var.COLS - Var.CONNECT; col++) {
				if (board[row][col] == player && board[row + 1][col + 1] == player && board[row + 2][col + 2] == player
						&& board[row + 3][col + 3] == player && board[row + 4][col + 4] == player) {
					gameOver = true;
					fiveInARow = new int[] { row, col, row + 1, col + 1, row + 2, col + 2, row + 3, col + 3, row + 4,
							col + 4 };
					show5InARow();
					break;
				}
			}
		}

		// Check Rows
		for (int row = 0; row < Var.ROWS; row++) {
			for (int col = 0; col <= Var.COLS - Var.CONNECT; col++) {
				if (board[row][col] == player && board[row][col + 1] == player && board[row][col + 2] == player
						&& board[row][col + 3] == player && board[row][col + 4] == player) {
					gameOver = true;
					fiveInARow = new int[] { row, col, row, col + 1, row, col + 2, row, col + 3, row, col + 4 };
					show5InARow();
					break;
				}
			}
		}

		// Check Columns
		for (int row = 0; row <= Var.ROWS - Var.CONNECT; row++) {
			for (int col = 0; col < Var.COLS; col++) {
				if (board[row][col] == player && board[row + 1][col] == player && board[row + 2][col] == player
						&& board[row + 3][col] == player && board[row + 4][col] == player) {
					gameOver = true;
					fiveInARow = new int[] { row, col, row + 1, col, row + 2, col, row + 3, col, row + 4, col };
					show5InARow();
					break;
				}
			}
		}
	}

	/*
	 * If the column is not full, add the move to the game board
	 */
	public void checkCol(int col) {
		if (board[0][col] != 0) { // top row full
			System.out.println("\nERROR: Column " + col + " is full\n");
			player = (player == Var.PLAYER_1) ? Var.PLAYER_2 : Var.PLAYER_1;
		}

		for (int i = Var.ROWS - 1; i >= 0; i--) {
			if (board[i][col] == 0) {
				board[i][col] = player;
				break;
			}
		}
	}

	/*
	 * Highlight the winning 5 in a row
	 */
	public void show5InARow() {
		for (int i = 0; i < 10; i += 2) {
			winBoard[fiveInARow[i]][fiveInARow[i + 1]] = 1; // Highlight winning line
		}
	}
}