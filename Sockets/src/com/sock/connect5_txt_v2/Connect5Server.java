/*
 * Connect5
 * Joe O'Regan
 * 02/02/2019
 */
package com.sock.connect5_txt_v2;

import java.net.ServerSocket;
import java.net.InetAddress;

public class Connect5Server {
	private static int PORT = 8000; // default port number

	public static void main(String[] args) throws Exception {
		int portNumber = (args.length == 0) ? PORT : Integer.parseInt(args[0]);
		InetAddress inetAddress = InetAddress.getLocalHost();

		try (ServerSocket listener = new ServerSocket(portNumber)) {
			System.out.println("Connect5 Server Running\nOS: \t\t" + System.getProperty("os.name") + ", \nHost: \t\t"
					+ inetAddress.getHostName() + ", \nIP Address: \t" + inetAddress.getHostAddress() + ", \nPort: \t\t"
					+ portNumber);

			while (true) {
				Game game = new Game();
				
				Player player1 = new Player(listener.accept(), Game.PLAYER_1, game);
				Player player2 = new Player(listener.accept(), Game.PLAYER_2, game);
				
				player1.setOpponent(player2);
				player2.setOpponent(player1);
				
				game.setCurrentPlayer(player1);
				
				player1.start();
				player2.start();
			}
		}
	}
}
