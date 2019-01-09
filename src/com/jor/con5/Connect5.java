/*
 * Joe O'Regan
 * 09/01/2018
 * Connect5
 * Text based 5 in row game
 */
package com.jor.con5;

import java.util.Scanner;

public class Connect5 {

	private static Game game;
	private static Scanner sc = new Scanner(System.in);
	private static int turns;
	private static char continueGame;
	private static int lastPlayer;

	public Connect5() {
		game = new Game();
		continueGame = 'y';
	}

	public static void init() {
		turns = 0;
		game.setBoard(game.board);
		game.setBoard(game.winBoard);
		game.setPlayer(1);
		game.setGameOver(false);
		lastPlayer = 2;
	}

	public static void main(String[] args) throws InterruptedException {
		new Connect5();

		while (Character.toLowerCase(continueGame) != 'n') {
			System.out.println("CONNECT 5 by Joe O'Regan\n");

			init();

			while (!game.getGameOver()) {
				if (!game.getGameOver()) {
					if (game.getPlayer() != lastPlayer) {
						System.out.println("Turn: " + ++turns);
						lastPlayer = game.getPlayer();
					} else {
						System.out.println("Turn: " + turns + ": Please Try Again!!!");
					}

					game.displayBoard(game.board);
					System.out.println("\nPlayer " + game.getPlayer() + ": Select a column from 1 to 9");
					int column = sc.nextInt();

					game.checkCol(column - 1);

					if (turns >= 9) {
						game.checkWin(); // Can't have 5 in a row until the 9th turn
					}

					if (!game.getGameOver()) {
						game.setPlayer((game.getPlayer() == Var.PLAYER_1) ? Var.PLAYER_2 : Var.PLAYER_1);
					}
				}
			}

			System.out.println("\nGame Over!!! Player " + game.getPlayer() + " is the winner\n");
			System.out.println("The Winning Line Was:");
			game.displayBoard(game.winBoard);

			System.out.println("\nContinue Game? (y/n)");
			continueGame = sc.next().charAt(0);
		}

		System.out.println("Thank you for playing Connect5");
	}
}