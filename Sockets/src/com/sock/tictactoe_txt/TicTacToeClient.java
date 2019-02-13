/*
 * Joe O'Regan
 * 31/01/2019
 */
package com.sock.tictactoe_txt;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TicTacToeClient {
	Scanner sc = new Scanner(System.in);

	private char icon;
	private char opponentIcon;

	private char[] board = new char[9];
	private int currentSquare = 0;

    private static int PORT = 8000;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public TicTacToeClient(String serverAddress, int port) throws Exception {
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
                char mark = response.charAt(8);
                icon = (mark == 'X') ? 'X' : 'O';
                opponentIcon = (mark == 'X') ? 'O' : 'X';
                System.out.println("Tic Tac Toe - Player " + mark);
            }
            
            while (true) {
                response = in.readLine();
                
                if (response.startsWith("VALID_MOVE")) {
                    board[currentSquare] = icon;
                	drawBoard();          
                    System.out.println("Valid move, please wait");
                } else if (response.startsWith("OPPONENT_MOVED")) {
                    int loc = Integer.parseInt(response.substring(15));
                    board[loc] = opponentIcon;                    
                    System.out.println("Opponent moved, your turn");                    
                    //playerMove();
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
    
    
    private void playerMove() {
    	drawBoard();
        System.out.print("Enter your move (0-8): ");
        //char move = sc.next().charAt(0);
        int move = sc.nextInt();
        currentSquare = move;
        out.println("MOVE " + move);
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
    	for (int i = 0; i < 9; i++) {
    		board[i] = Character.forDigit(i, 10);
    	}
    }
    
    private void drawBoard() {
    	System.out.println();
    	for (int i = 0; i < 9; i++) {
    		if ((i+1) % 3 == 0) {
    			System.out.println(board[i]);
    			if (i < 8)
    				System.out.println("_________");
    		} else {
    			System.out.print(board[i] + " | ");
    		}
    	}
    }

    public static void main(String[] args) throws Exception {
        while (true) {
        	//System.out.println("test: " + args[0]);
            //String serverAddress = (args.length == 0) ? "localhost" : args[1];
            String serverAddress = (args.length == 0) ? "localhost" : args[0];
            //TicTacToeClient client = new TicTacToeClient(serverAddress, PORT);  
            TicTacToeClient client = new TicTacToeClient(serverAddress, (args.length == 2) ? Integer.parseInt(args[1]) : PORT);            
            client.play();
            
            if (!client.wantsToPlayAgain()) {
                break;
            }
        }
    }
}