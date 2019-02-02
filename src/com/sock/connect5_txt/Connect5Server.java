/*
 * Connect5
 * Joe O'Regan
 * 31/01/2019
 */
package com.sock.connect5_txt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import java.net.InetAddress;


public class Connect5Server {
    private static int PORT = 8000;	// default port number
    
    public static void main(String[] args) throws Exception {
    	int portNumber = (args.length == 0) ? PORT : Integer.parseInt(args[0]);
        InetAddress inetAddress = InetAddress.getLocalHost();
    	
        try (ServerSocket listener = new ServerSocket(portNumber)) {
        	System.out.println("Connect5 Server Running\nOS: \t\t" + System.getProperty("os.name") + ", \nHost: \t\t" + inetAddress.getHostName() + ", \nIP Address: \t" + inetAddress.getHostAddress() + ", \nPort: \t\t" + portNumber);
            
            while (true) {
                Game game = new Game();
                game.initBoard();
                //game.showBoard();
                Game.Player player1 = game.new Player(listener.accept(), Game.PLAYER_1);
                Game.Player player2 = game.new Player(listener.accept(), Game.PLAYER_2);
                player1.setOpponent(player2);
                player2.setOpponent(player1);
                game.currentPlayer = player1;
                player1.start();
                player2.start();
            }
        }
    }
}

class Game {
	public final static int CONNECT = 5; //
	public final static int PLAYER_1 = 1; //
	public final static int PLAYER_2 = 2; //
	public final static int ROWS = 6; //
	public final static int COLS = 9; //	
	private int[][] board = new int[ROWS][COLS];	//
	public int[][] winBoard = new int[ROWS][COLS]; //
	private boolean gameOver; //
	public int[] fiveInARow = new int[10]; //
	
    Player currentPlayer;
    
	public boolean checkWin() {		
		for (int row = 0; row <= ROWS - CONNECT; row++) {
			// Diagonal /
			for (int col = CONNECT - 1; col < COLS; col++) {
				if (board[row][col] != 0 && board[row+1][col-1] == board[row][col] && board[row + 2][col - 2] == board[row][col]
						&& board[row + 3][col - 3] == board[row][col] && board[row + 4][col - 4] == board[row][col]) {
					gameOver = true;
					fiveInARow = new int[] { row, col, row + 1, col - 1, row + 2, col - 2, row + 3, col - 3, row + 4,
							col - 4 };
					break;
				}
			}

			// Diagonal \
			for (int col = 0; col <= COLS - CONNECT; col++) {
				if (board[row][col] != 0 && board[row + 1][col + 1] == board[row][col] && board[row + 2][col + 2] == board[row][col]
						&& board[row + 3][col + 3] == board[row][col] && board[row + 4][col + 4] == board[row][col]) {
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
				if (board[row][col] != 0 && board[row][col + 1] == board[row][col] && board[row][col + 2] == board[row][col]
						&& board[row][col + 3] == board[row][col] && board[row][col + 4] == board[row][col]) {
					gameOver = true;
					fiveInARow = new int[] { row, col, row, col + 1, row, col + 2, row, col + 3, row, col + 4 };
					break;
				}
			}
		}

		// Check Columns
		for (int row = 0; row <= ROWS - CONNECT; row++) {
			for (int col = 0; col < COLS; col++) {
				if (board[row][col] != 0 && board[row + 1][col] == board[row][col] && board[row + 2][col] == board[row][col]
						&& board[row + 3][col] == board[row][col] && board[row + 4][col] == board[row][col]) {
					gameOver = true;
					fiveInARow = new int[] { row, col, row + 1, col, row + 2, col, row + 3, col, row + 4, col };
					break;
				}
			}
		}
		if (gameOver) {
			show5InARow();
		}
		return gameOver;
	}
	
	public void show5InARow() {
		for (int i = 0; i < 10; i += 2) {
			winBoard[fiveInARow[i]][fiveInARow[i + 1]] = 3; // Highlight winning line
		}
		System.out.println("Winning Move");
		displayBoard(winBoard);
	}

    public boolean boardFull() {
    	for (int row = 0; row < ROWS; row++) {
    		for (int col = 0; col < COLS; col++) {
    			if (board[row][col] == 0)
    				return false;
    		}
    	}
        return true;
    }

	public boolean checkCol(int col) {
		System.out.println("Checking Column: " + col + " for Player " + currentPlayer.type);
		
		if (board[0][col] != 0) { // top row full
			System.out.println("\nERROR: Column " + col + " is full\n");
			return false;
		}
		return true;
	}
	
	public void setCol(int col) {
		for (int i = ROWS - 1; i >= 0; i--) {
			if (board[i][col] == 0) {
				board[i][col] = currentPlayer.type;
				break;
			}
		}
	}

	public void initBoard() {
    	for (int row = 0; row < ROWS; row++) {
    		for (int col = 0; col < COLS; col++) {
    			board[row][col] = 0;
    			winBoard[row][col] = 0;
    		}
    	}
    }
    
	public void showBoard() {
		displayBoard(board);
	}
	
	public void displayBoard(int[][] board) {
		for (int i = 1; i < 10; i++) {
			System.out.print(" " + i + " ");
		}

		System.out.println();

		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				System.out.print("["+board[row][col] + "]");
			}
			System.out.println();
		}
		//System.out.println("---------------------------");
		//System.out.println("|        CONNECT 5        |");
		//System.out.println("---------------------------");
	}
	
    public synchronized boolean legalMove(int column, Player player) {
    	if (column < 0 || column >= COLS) {
    		return false;					// value out of range
    	}
    	
    	// if it is the current player, 
        if (player == currentPlayer && checkCol(column)) {
            //brd[column] = currentPlayer; //
        	//System.out.println("Valid move - Set column");//////////////////////////////////////
        	setCol(column);
        	//System.out.println("Check Win Player " + currentPlayer.type);
        	//checkWin();        	
        	//System.out.println("Switch Players"); //////////////////////////////////////////
        	
            currentPlayer = currentPlayer.opponent;	//
            currentPlayer.otherPlayerMoved(column);
            
            return true;
        }
        
        return false;
    }

    class Player extends Thread {
        int type;	// PLAYER_1 or PLAYER_2
        Player opponent;	// opponent
        Socket socket;
        BufferedReader input;
        PrintWriter output;

        public Player(Socket socket, int type) {
            this.socket = socket;
            this.type = type;
            
            try {
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);
                output.println("WELCOME " + type);
                output.println("MESSAGE Waiting for opponent to connect");
            } catch (IOException e) {
                System.out.println("Player died: " + e);
            }
        }

        public void setOpponent(Player opponent) {
            this.opponent = opponent;
        }

        public void otherPlayerMoved(int location) {
        	//System.out.println("Other Player Moved");
            output.println("OPPONENT_MOVED " + location);
            output.println(checkWin() ? "DEFEAT" : boardFull() ? "TIE" : "CONTINUE");
        }

        public void run() {
            try {                
                output.println("MESSAGE All players connected");	// The thread is only started after everyone connects.

               // Game.this.showBoard();	// access outer class
                
                // Tell the first player that it is their turn.
                if (this.type == PLAYER_1) {
                    output.println("MESSAGE Player 1 Your move");
                }

                // Repeatedly get commands from the client and process them.
                while (true) {
                    String command = input.readLine();
                    
                    if (command.startsWith("MOVE")) {
                        int location = Integer.parseInt(command.substring(5));
                        
                        if (legalMove(location, this)) {
                        	//System.out.println("if legal move -- player " + currentPlayer.type);
                            output.println("VALID_MOVE");
                            output.println(checkWin() ? "VICTORY" : boardFull() ? "TIE" : "");
                        } else {
                            output.println("MESSAGE ?");
                        }
                    } else if (command.startsWith("QUIT")) {
                        return;
                    }
                }
            } catch (IOException e) {
                System.out.println("Player died: " + e);
            } finally {
                try {socket.close();} catch (IOException e) {}
            }
        }
    }
}
