/*
 * Connect5
 * Joe O'Regan
 * 02/02/2019
 */
package com.sock.connect5_txt_v2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class Player extends Thread {
	private int type; // PLAYER_1 or PLAYER_2
	private Player opponent; // opponent
	private Socket socket;
	private BufferedReader input;
	private PrintWriter output;
	private Game game;

	public Player getOpponent() {
		return opponent;
	}

	public void setOpponent(Player opponent) {
		this.opponent = opponent;
	}

	public int getType() {
		return type;
	}

	public Player(Socket socket, int type, Game game) {
		this.socket = socket;
		this.type = type;
		this.game = game;

		try {
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintWriter(socket.getOutputStream(), true);
			output.println("WELCOME " + type);
			output.println("MESSAGE Waiting for opponent to connect");
		} catch (IOException e) {
			System.out.println("Player died: " + e);
		}
	}

	public void otherPlayerMoved(int location) {
		System.out.println("Player " + ((type == Game.PLAYER_1) ? Game.PLAYER_2 : Game.PLAYER_1) + " Has Moved");
		output.println("OPPONENT_MOVED " + location);
		output.println((game.getBoard().win()) ? "DEFEAT" : game.getBoard().full() ? "TIE" : "CONTINUE");
	}

	public void run() {
		try {
			output.println("MESSAGE All players connected"); // The thread is only started after everyone connects.

			// Tell the first player that it is their turn.
			if (this.type == Game.PLAYER_1) {
				output.println("MESSAGE Player 1 Your move");
			}

			// Repeatedly get commands from the client and process them.
			while (true) {
				String command = input.readLine();

				if (command.startsWith("MOVE")) {
					int location = Integer.parseInt(command.substring(5));

					if (game.legalMove(location, this, game)) {
						output.println("VALID_MOVE");
						output.println((game.getBoard().win()) ? "VICTORY" : game.getBoard().full() ? "TIE" : "");
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
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
	}
}