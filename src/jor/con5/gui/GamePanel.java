/*
 * Joe O'Regan
 * 
 * GamePanel.java
 * 08/01/2018
 * 
 * Connect5
 * Text and graphics based 5 in a row games
 */
package jor.con5.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;

	private boolean[] highlightCol = new boolean[Var.COLS];
	private Rectangle[] colRect = new Rectangle[Var.COLS];

	public int x, y, mouseX, mouseY;
	public Graphics graphics;
	public static Rectangle rectButton1, rectButton2;
	private boolean stop, gameStarted, hover1, hover2, win;

	private static int clickedX, clickedY;
	private int player;

	public Game game;

	public GamePanel() throws InterruptedException {
		this.setSize(Var.WIDTH, Var.HEIGHT);
		this.setVisible(true);

		game = new Game();
		rectButton1 = new Rectangle(150, Var.BUTTON_Y, Var.BUTTON_WIDTH, Var.BUTTON_HEIGHT);
		rectButton2 = new Rectangle(295, Var.BUTTON_Y, Var.BUTTON_WIDTH, Var.BUTTON_HEIGHT);

		for (int i = 0; i < Var.COLS; i++) {
			colRect[i] = new Rectangle(Var.PADDING + (Var.DISK_SPACING * i), Var.COL_Y, Var.COL_WIDTH, Var.COL_HEIGHT);
		}

		stop = false;
		gameStarted = false;
		hover1 = false;
		hover2 = false;
		
		this.setFocusable(true);
		this.requestFocus();
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, Var.WIDTH, Var.HEIGHT);

		// Board
		g.setColor(Color.blue);
		g.fillRect(Var.PADDING - 3, Var.COL_Y, Var.DISK_SPACING * Var.COLS,
				(Var.DISK_SPACING * Var.ROWS) + Var.PADDING_TOP - Var.COL_Y);
		
		// highlight column
		for (int i = 0; i < Var.COLS; i++) {
			g.setColor((!highlightCol[i]) ? Color.blue : (!gameStarted) ? Color.blue : (game.getGameOver()) ? Color.blue : Color.red);
			g.fillRect(colRect[i].x, colRect[i].y, colRect[i].width, colRect[i].height);
		}

		for (int x = 0; x < Var.COLS; x++) {
			for (int y = 0; y < Var.ROWS; y++) {
				g.setColor(Color.BLUE);

				if (game.getGameOver()) {
					g.setColor(game.winBoard[y][x] == Var.EMPTY ? Color.BLUE : Color.green);
				}
				
				g.fillOval(Var.PADDING + ((x) * Var.DISK_SPACING) - Var.DISK_OUTLINE,
						150 + ((y) * Var.DISK_SPACING) - Var.DISK_OUTLINE, Var.DISK_SPACING, Var.DISK_SPACING);

				g.setColor((game.board[y][x] == Var.EMPTY) ? Color.gray : (game.board[y][x] == Var.PLAYER_1) ? Color.yellow : Color.red); // Draw disc or empty

				g.fillOval(Var.PADDING + ((x) * Var.DISK_SPACING), Var.PADDING_TOP + ((y) * Var.DISK_SPACING),
						Var.DISK_DIAMETER, Var.DISK_DIAMETER);
			}
		}

		g.setColor((!hover1) ? Color.lightGray : Color.cyan);
		g.fillRect(rectButton1.x, rectButton1.y, rectButton1.width, rectButton1.height);
		g.setColor((!hover2) ? Color.lightGray : Color.cyan);
		g.fillRect(rectButton2.x, rectButton2.y, rectButton2.width, rectButton2.height);

		g.setFont(new Font("Arial", Font.BOLD, Var.TOP_TEXT_SIZE));
		g.setColor(Color.white);
		g.drawString("Connect 5 Java Implementation", 150, 30);

		g.setFont(new Font("Arial", Font.BOLD, Var.TOP_TEXT_SIZE));
		g.drawString(" New Game ", rectButton1.x + 15, 82);
		g.drawString(" Exit Game ", rectButton2.x + 15, 82);

		if (gameStarted) {
			if (game.getGameOver()) {
				g.setFont(new Font("Arial", Font.BOLD, Var.TOP_TEXT_SIZE));
				stop = true;

				if (game.getPlayer() == Var.PLAYER_1) {
					g.setColor(Color.yellow);
					g.drawString("     Player 2 Wins!", 200, Var.TOP_TEXT_X);
					g.setFont(new Font("Arial", Font.BOLD, Var.BOTTOM_TEXT_SIZE));
					g.drawString("     GAME OVER!", Var.BOTTOM_TEXT_X, Var.BOTTOM_TEXT_Y);
				}

				if (game.getPlayer() == Var.PLAYER_2) {
					g.setColor(Color.red);
					g.drawString("     Player 1 Wins!", 200, Var.TOP_TEXT_X);
					g.setFont(new Font("Arial", Font.BOLD, Var.BOTTOM_TEXT_SIZE));
					g.drawString("     GAME OVER!", Var.BOTTOM_TEXT_X, Var.BOTTOM_TEXT_Y);
				}

			} else if (game.getPlayer() == Var.PLAYER_1) {
				g.setFont(new Font("Arial", Font.BOLD, Var.TOP_TEXT_SIZE));
				g.setColor(Color.red);
				g.drawString("        Player 1 To Move", 170, Var.TOP_TEXT_X);
				g.setFont(new Font("Arial", Font.BOLD, Var.BOTTOM_TEXT_SIZE));
				g.drawString("     Red to Move", Var.BOTTOM_TEXT_X, Var.BOTTOM_TEXT_Y);
			} else if (game.getPlayer() == Var.PLAYER_2) {
				g.setFont(new Font("Arial", Font.BOLD, Var.TOP_TEXT_SIZE));
				g.setColor(Color.yellow);
				g.drawString("        Player 2 To Move ", 170, Var.TOP_TEXT_X);
				g.setFont(new Font("Arial", Font.BOLD, Var.BOTTOM_TEXT_SIZE));
				g.drawString("   Yellow to Move", Var.BOTTOM_TEXT_X, Var.BOTTOM_TEXT_Y);
			}
		}

		g.dispose();
	}

	@Override
	public void mouseClicked(MouseEvent e) {		
		clickedX = e.getX();
		clickedY = e.getY();

		if (stop == false) {
			if (gameStarted == true) {
				for (int i = 0; i < Var.COLS; i++) {
					if (clickedX > colRect[i].x && clickedX < colRect[i].x + colRect[i].width && clickedY > colRect[i].y
							&& clickedY < colRect[i].y + colRect[i].height) {
						x = 13 + (i * 44);
						y = 238;
						player = (game.getPlayer() == Var.PLAYER_1) ? Var.PLAYER_2 : Var.PLAYER_1;
						game.setPlayer(player);
						game.checkCol(i);
						break;
					}
				}

				repaint();
				game.checkWin();
			}
		}

		if (clickedX > rectButton1.x && clickedX < rectButton1.x + rectButton1.width && clickedY > rectButton1.y
				&& clickedY < rectButton1.y + rectButton1.height) {
			gameStarted = true;

			if (gameStarted || stop) {
				stop = false;

				for (int row = 0; row < Var.ROWS; row++) {
					for (int col = 0; col < Var.COLS; col++) {
						game.board[row][col] = 0;
					}

					this.win = false;
					game.setGameOver(this.win);
					game.setBoard(new int[][][] {game.winBoard});

					repaint();
				}
			}
		}

		if (clickedX > rectButton2.x && clickedX < rectButton2.x + rectButton2.width && clickedY > rectButton2.y
				&& clickedY < rectButton2.y + rectButton2.height) {
			System.exit(1);
		}
	}

	@Override
	public void mouseMoved(MouseEvent em) {
		mouseX = em.getX();
		mouseY = em.getY();

		if (mouseX > rectButton1.x && mouseX < rectButton1.x + rectButton1.width && mouseY > rectButton1.y
				&& mouseY < rectButton1.y + rectButton1.height) {
			hover1 = true;
			repaint();
		} else {
			hover1 = false;
			repaint();
		}
		
		if (mouseX > rectButton2.x && mouseX < rectButton2.x + rectButton2.width && mouseY > rectButton2.y
				&& mouseY < rectButton2.y + rectButton2.height) {
			hover2 = true;
			repaint();
		} else {
			hover2 = false;
			repaint();
		}

		// Highlight column
		for (int i = 0; i < Var.COLS; i++) {
			if (mouseX > colRect[i].x && mouseX < colRect[i].x + colRect[i].width && mouseY > colRect[i].y
					&& mouseY < colRect[i].y + colRect[i].height) {
				highlightCol[i] = true;
				repaint();
				break;
			} else {
				highlightCol[i] = false;
				repaint();
				continue;
			}
		}
	}

	public void draw(Graphics g) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
	}
}