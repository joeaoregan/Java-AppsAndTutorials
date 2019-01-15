/*
 * Joe O'Regan
 * 
 * Connect5GFX.java
 * 08/01/2018
 * 
 * Connect5
 * Graphics based 5 in a row game
 */
package com.jor.con5;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class Connect5GFX extends JFrame {

	private static final long serialVersionUID = 1L;
	public GamePanel pan;
	public Dimension dim;
	int locX, locY;

	public Connect5GFX() throws InterruptedException {
		dim = Toolkit.getDefaultToolkit().getScreenSize();
		locX = (int) dim.getWidth() * 4 / 12;
		locY = (int) dim.getHeight() * 2 / 12;
		pan = new GamePanel();
		this.setTitle("Joe O'Regan: Connect 5");
		this.setSize(Var.WIDTH, Var.HEIGHT);
		this.setLocation(locX, locY);
		this.add(pan);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setFocusable(true);
		this.setResizable(false);
	}

	public static void main(String[] args) throws InterruptedException {
		new Connect5GFX();
	}
}