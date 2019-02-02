/*
 * Connect5
 * Joe O'Regan
 * 02/02/2019
 */
package com.sock.connect5_txt_v2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Connect5Client {
	private int[][] board = new int[Game.ROWS][Game.COLS];
	private Scanner sc = new Scanner(System.in);
	private int playerIcon, opponentIcon, validMove = 0;
	private static int PORT = 8000;
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private static String os = null; // Display the operating system the client is running on

	public Connect5Client(String serverAddress, int port) throws Exception {
		socket = new Socket(serverAddress, port);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
	}

	public void play() throws Exception {
		String response;
		Game.init(new int[][][] { board });

		try {
			response = in.readLine();

			if (response.startsWith("WELCOME")) {
				int mark = Integer.parseInt(String.valueOf(response.charAt(8))); // Return PLAYER_1 or PLAYER_2
				playerIcon = (Integer.parseInt(String.valueOf(mark)) == Game.PLAYER_1) ? Game.PLAYER_1 : Game.PLAYER_2;
				opponentIcon = (playerIcon == Game.PLAYER_1) ? Game.PLAYER_2 : Game.PLAYER_1; // opponent opposite of
																								// player
				System.out.println("Connect 5 - Player " + mark);
			}

			while (true) {
				response = in.readLine();

				if (response.startsWith("VALID_MOVE")) {
					Game.setCol(validMove, playerIcon, board); // Player move
					Game.displayBoard(board);
					System.out.println("Valid move, please wait");
				} else if (response.startsWith("OPPONENT_MOVED")) {
					int loc = Integer.parseInt(response.substring(15));
					Game.setCol(loc, opponentIcon, board); // Opponent move
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

				if (response.startsWith("CONTINUE") || response.toLowerCase().contains("your move")
						|| response.toLowerCase().contains("?")) { // player move, or returned invalid move
					playerMove();
				}
			}

			out.println("QUIT");
		} finally {
			socket.close(); // close the socket
		}
	}

	private void playerMove() {
		Game.displayBoard(board);
		System.out.print("Select a column (0-8): ");
		int move = sc.nextInt();
		validMove = move;
		out.println("MOVE " + (move));
	}

	private boolean playAgain() {
		System.out.println("Want to play again? (y/n)");

		String playAgain = sc.next();
		if (playAgain.equalsIgnoreCase("y")) {
			return true;
		}

		return false;
	}

	public static String getOS() {
		if (os == null) {
			os = System.getProperty("os.name");
		}

		return os;
	}

	public static void main(String[] args) throws Exception {
		System.out.println("Connect 5 by Joe O'Regan running on " + getOS() + "");

		while (true) {
			String serverAddress = (args.length == 0) ? "localhost" : args[0];
			Connect5Client client = new Connect5Client(serverAddress,
					(args.length == 2) ? Integer.parseInt(args[1]) : PORT);
			client.play();

			if (!client.playAgain()) {
				break;
			}
		}
	}
}