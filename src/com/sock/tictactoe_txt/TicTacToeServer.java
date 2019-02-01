/*
 * http://cs.lmu.edu/~ray/notes/javanetexamples/
 * 30/01/2019
 */
package com.sock.tictactoe_txt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import java.net.InetAddress;

/**
 * A server for a network multi-player tic tac toe game. Modified and extended from
 * the class presented in Deitel and Deitel "Java How to Program" book. I made a bunch
 * of enhancements and rewrote large sections of the code. The main change is instead of
 * passing *data* between the client and server, I made a TTTP (tic tac toe protocol)
 * which is totally plain text, so you can test the game with Telnet. The messages of
 * TTTP are:
 *
 *  Client -> Server           Server -> Client
 *  ----------------           ----------------
 *  MOVE <n>  (0 <= n <= 8)    WELCOME <char>  (char in {X, O})
 *  QUIT                       VALID_MOVE
 *                             OTHER_PLAYER_MOVED <n>
 *                             VICTORY
 *                             DEFEAT
 *                             TIE
 *                             MESSAGE <text>
 *
 * A second change is that it allows an unlimited number of pairs of players to play.
 */
public class TicTacToeServer {
    private static int PORT = 8000;
    
    public static void main(String[] args) throws Exception {
    	int portNumber = (args.length == 0) ? PORT : Integer.parseInt(args[0]);
        InetAddress inetAddress = InetAddress.getLocalHost();
    	
        try (ServerSocket listener = new ServerSocket(portNumber)) {
        	System.out.println("Host: " + inetAddress.getHostName() + ", IP Address: " + inetAddress.getHostAddress() + ", Port: " + portNumber);
            System.out.println("Tic Tac Toe Server is Running");
            while (true) {
                Game game = new Game();
                Game.Player playerX = game.new Player(listener.accept(), 'X');
                Game.Player playerO = game.new Player(listener.accept(), 'O');
                playerX.setOpponent(playerO);
                playerO.setOpponent(playerX);
                game.currentPlayer = playerX;
                playerX.start();
                playerO.start();
            }
        }
    }
}

class Game {

    /**
     * A board has nine squares. Each square is either unowned or it is owned by a
     * player. So we use a simple array of player references. If null, the corresponding
     * square is unowned, otherwise the array cell stores a reference to the player that
     * owns it.
     */
    private Player[] board = {
        null, null, null,
        null, null, null,
        null, null, null};

    Player currentPlayer;

    public boolean checkWin() {
        return
            (board[0] != null && board[0] == board[1] && board[0] == board[2])
          ||(board[3] != null && board[3] == board[4] && board[3] == board[5])
          ||(board[6] != null && board[6] == board[7] && board[6] == board[8])
          ||(board[0] != null && board[0] == board[3] && board[0] == board[6])
          ||(board[1] != null && board[1] == board[4] && board[1] == board[7])
          ||(board[2] != null && board[2] == board[5] && board[2] == board[8])
          ||(board[0] != null && board[0] == board[4] && board[0] == board[8])
          ||(board[2] != null && board[2] == board[4] && board[2] == board[6]);
    }

    public boolean boardFilledUp() {
        for (int i = 0; i < board.length; i++) {
            if (board[i] == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Called by the player threads when a player tries to make a move. This method
     * checks to see if the move is legal: that is, the player requesting the move must
     * be the current player and the square in which she is trying to move must not
     * already be occupied. If the move is legal the game state is updated (the square
     * is set and the next player becomes current) and the other player is notified of
     * the move so it can update its client.
     */
    public synchronized boolean legalMove(int location, Player player) {
        if (player == currentPlayer && board[location] == null) {
            board[location] = currentPlayer;
            currentPlayer = currentPlayer.opponent;
            currentPlayer.otherPlayerMoved(location);
            return true;
        }
        return false;
    }

    /**
     * A Player is identified by a character mark which is either 'X' or 'O'. For
     * communication with the client the player has a socket with its input and output
     * streams. Since only text is being communicated we use a reader and a writer.
     */
    class Player extends Thread {
        char mark;
        Player opponent;
        Socket socket;
        BufferedReader input;
        PrintWriter output;

        public Player(Socket socket, char mark) {
            this.socket = socket;
            this.mark = mark;
            try {
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);
                output.println("WELCOME " + mark);
                output.println("MESSAGE Waiting for opponent to connect");
            } catch (IOException e) {
                System.out.println("Player died: " + e);
            }
        }

        public void setOpponent(Player opponent) {
            this.opponent = opponent;
        }

        public void otherPlayerMoved(int location) {
            output.println("OPPONENT_MOVED " + location);
            output.println(checkWin() ? "DEFEAT" : boardFilledUp() ? "TIE" : "CONTINUE");
        }

        public void run() {
            try {
                // The thread is only started after everyone connects.
                output.println("MESSAGE All players connected");

                // Tell the first player that it is their turn.
                if (mark == 'X') {
                    output.println("MESSAGE Your move");
                }

                // Repeatedly get commands from the client and process them.
                while (true) {
                    String command = input.readLine();
                    if (command.startsWith("MOVE")) {
                        int location = Integer.parseInt(command.substring(5));
                        
                        if (legalMove(location, this)) {
                            output.println("VALID_MOVE");
                            output.println(checkWin() ? "VICTORY" : boardFilledUp() ? "TIE" : "");
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
