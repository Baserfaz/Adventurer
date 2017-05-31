package com.adventurer.main;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Window extends Canvas {
	
	private static final long serialVersionUID = 351245801233048538L;
	
	public Window(int width, int height, String title, Game game) {
		
		// create a new frame
		JFrame frame = new JFrame(title);
		
		// set the dimensions of the frame
		frame.setPreferredSize(new Dimension(width, height));
		frame.setMinimumSize(new Dimension(width, height));
		frame.setMaximumSize(new Dimension(width, height));
		
		// set closing operation
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// set if the frame is resizeable
		frame.setResizable(false);
		
		// set the location
		frame.setLocationRelativeTo(null);
		
		// add our game to the frame
		frame.add(game);
		
		// pack the frame
		frame.pack();
		
		// set the frame to be visible.
		frame.setVisible(true);
		
		// start the game
		game.Start();
	}
}
