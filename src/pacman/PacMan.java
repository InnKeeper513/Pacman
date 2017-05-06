package pacman;

import javax.swing.*;

public class PacMan extends Mover {

	private final ImageIcon[][] pacManImage = new ImageIcon[4][2];
	
	public PacMan(int row, int column, int dRow, int dColumn, boolean isDead,
			ImageIcon image) {
		super(row, column, dRow, dColumn, isDead, image);
		
		pacManImage[0][0] = new ImageIcon("images/PacLeftClosed.bmp");
		pacManImage[0][1] = new ImageIcon("images/PacLeftOpen.bmp");
		pacManImage[1][0] = new ImageIcon("images/PacUpClosed.bmp");
		pacManImage[1][1] = new ImageIcon("images/PacUpOpen.bmp");
		pacManImage[2][0] = new ImageIcon("images/PacRightClosed.bmp");
		pacManImage[2][1] = new ImageIcon("images/PacRightOpen.bmp");
		pacManImage[3][0] = new ImageIcon("images/PacDownClosed.bmp");
		pacManImage[3][1] = new ImageIcon("images/PacDownOpen.bmp");
	}

	public ImageIcon[][] getPacManImage() {
		return pacManImage;
	}
}
