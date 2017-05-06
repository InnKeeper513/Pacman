package pacman;

import java.io.*;

import javax.swing.*;

public class PacManGUI extends JFrame{
	
	public PacManGUI() throws IOException{
		
		Board board = new Board();
		setSize(600,600);
		setTitle("PacMan");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addKeyListener(board);
		add(board.scoreLabel);
		add(board.eat);
		add(board.cherrys);
		board.eat.setBounds(458,325,100,20);
		board.scoreLabel.setBounds(58,325,100,20);
		board.cherrys.setBounds(58,220,100,20);
		
		
		add(board);
		setVisible(true);
	}
	
	
}
