/*
 * Connect5
 * Joe O'Regan
 * 31/01/2019
 */
package com.sock.connect5;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Connect5Client {
	public final static int PLAYER_1 = 1; //
	public final static int PLAYER_2 = 2; //
	public final static int ROWS = 6; //
	public final static int COLS = 9; //
	private int[][] boardNEW = new int[ROWS][COLS];	//
		
	Scanner sc = new Scanner(System.in);

	private int playerIcon;
	private int opponentIcon;

	private int currentSquare = 0;

    private static int PORT = 8000;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Connect5Client(String serverAddress, int port) throws Exception {
        socket = new Socket(serverAddress, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void play() throws Exception {
        String response;
        initBoard();
        
        try {
            response = in.readLine();
            
            if (response.startsWith("WELCOME")) {
                int mark = Integer.parseInt(String.valueOf(response.charAt(8)));	// Return PLAYER_1 or PLAYER_2                
                //System.out.println("test --------- " + String.valueOf(response.charAt(8)));                
                playerIcon = (Integer.parseInt(String.valueOf(mark)) == PLAYER_1) ? PLAYER_1 : PLAYER_2;
                //System.out.println("test player --------- " + playerIcon);
                opponentIcon = (playerIcon == PLAYER_1) ? PLAYER_2 : PLAYER_1;	// opponent opposite of player
               // System.out.println("test opponent --------- " + opponentIcon);
                System.out.println("Connect 5 - Player " + mark);
            }
            
            while (true) {
                response = in.readLine();
                
                if (response.startsWith("VALID_MOVE")) {
                    setCol(currentSquare, playerIcon);
                	drawBoard();          
                    System.out.println("Valid move, please wait");
                } else if (response.startsWith("OPPONENT_MOVED")) {
                    int loc = Integer.parseInt(response.substring(15));
                    setCol(loc, opponentIcon);
                    System.out.println("Opponent moved, your turn");   
                } else if (response.startsWith("VICTORY")) {
                    System.out.println("You win");
                    break;
                } else if (response.startsWith("DEFEAT")) {
                    System.out.println("You lose");
                    break;
                } else if (response.startsWith("TIE")) {
                    System.out.println("You tied");
                    break;
                } else if (response.startsWith("MESSAGE")) {
                    System.out.println(response.substring(8));
                }

                if (response.startsWith("CONTINUE") || response.toLowerCase().contains("your move") || response.toLowerCase().contains("?")) { // player move, or returned invalid move
                	playerMove();
                }
            }
            
            out.println("QUIT");
        } finally {
            socket.close();	// close the socket
        }
    }

	public void setCol(int col, int playerType) {
		for (int i = ROWS - 1; i >= 0; i--) {
			if (boardNEW[i][col] == 0) {
				boardNEW[i][col] = playerType;
				break;
			}
		}
	}
    
    private void playerMove() {
    	drawBoard();
        System.out.print("Select a column (0-8): ");
        int move = sc.nextInt();
        currentSquare = move;
        out.println("MOVE " + (move));
    }

    private boolean wantsToPlayAgain() {
    	System.out.println("Want to play again? (y/n)");
    	
    	String playAgain = sc.next();
    	if (playAgain.equalsIgnoreCase("y")) {
    		return true;
    	}
    	
    	return false;
    }
    
    private void initBoard() {
    	for (int row = 0; row < ROWS; row++) {
    		for (int col = 0; col < COLS; col++) {
    			boardNEW[row][col] = 0;
    		}
    	}
    }
    
    private void drawBoard() {
    	System.out.println();
    	
		for (int i = 0; i < 9; i++) {
			System.out.print(" " + i + " ");
		}

		System.out.println();

		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				System.out.print("["+boardNEW[row][col] + "]");
			}
			System.out.println();
		}
    }

    public static void main(String[] args) throws Exception {
        while (true) {
            String serverAddress = (args.length == 0) ? "localhost" : args[0];
            Connect5Client client = new Connect5Client(serverAddress, (args.length == 2) ? Integer.parseInt(args[1]) : PORT);            
            client.play();
            
            if (!client.wantsToPlayAgain()) {
                break;
            }
        }
    }
}