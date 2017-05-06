package pacman;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.Timer;

public class Board extends JPanel implements KeyListener, ActionListener{

	private Timer gameTimer = new Timer(250, this);
	private Timer animateTimer = new Timer(0,this);

	private final ImageIcon WALL = new ImageIcon("images/StdWall.bmp");
	private final ImageIcon FOOD = new ImageIcon("images/StdFood.bmp");
	private final ImageIcon BLACK = new ImageIcon("images/Black.bmp");
	private final ImageIcon DOOR = new ImageIcon("images/Black.bmp");
	private final ImageIcon SKULL = new ImageIcon("images/Skull.bmp");
	private final ImageIcon CHERRY = new ImageIcon("images/cherry.bmp");
	private final ImageIcon BOMB= new ImageIcon("images/bomb.jpg");
	
	private JLabel[][] cell = new JLabel[25][27];
	private char[][] maze = new char[25][27];
	
	private PacMan pacMan;
	private Ghost[] ghost = new Ghost[3];

	private int pellets = 0;
	private int score = 0;
	private int eated = 0;
	private int cherry = 0;
	private int pStep;
	
	JLabel scoreLabel = new JLabel("Score: ");
	JLabel eat = new JLabel("Eat: ");
	JLabel cherrys = new JLabel("Cherry: ");
	
	public Board() throws IOException{
		setLayout(new GridLayout(25,27));

		setBackground(Color.BLACK);
		addKeyListener(this);

		pacMan = new PacMan(0,0,0,0,false, new ImageIcon("images/PacLeftClosed.bmp"));

		ghost[0] = new Ghost(0,0,0,0,false, new ImageIcon("images/Ghost1.bmp"));
		ghost[1] = new Ghost(0,0,0,0,false, new ImageIcon("images/Ghost2.bmp"));
		ghost[2] = new Ghost(0,0,0,0,false, new ImageIcon("images/Ghost3.bmp"));

		loadBoard();
	}

	private void loadBoard() throws IOException{
		int r = 0;

		Scanner input = new Scanner(new File("Maze"));

		while (input.hasNext()){

			maze[r] = input.nextLine().toCharArray();

			for(int c = 0; c < maze[r].length; c++){
				cell[r][c] = new JLabel();

				if(maze[r][c]=='W')
					cell[r][c].setIcon(WALL);
				
				else if(maze[r][c]=='F'){
					cell[r][c].setIcon(FOOD);
					pellets++;
				}
				
				else if(maze[r][c]=='C')
					cell[r][c].setIcon(CHERRY);
				else if (maze[r][c]=='B')
					cell[r][c].setIcon(BOMB);
				
				else if(maze[r][c]=='P'){
					cell[r][c].setIcon(pacMan.getImage());
					pacMan.setRow(r);
					pacMan.setColumn(c);
				}

				else if(maze[r][c]=='0'||maze[r][c]=='1'||maze[r][c]=='2')
				{
					int gNum = Character.getNumericValue(maze[r][c]);
					cell[r][c].setIcon(ghost[gNum].getImage());
					ghost[gNum].setRow(r);
					ghost[gNum].setColumn(c);
				}

				if(maze[r][c]=='D')
					cell[r][c].setIcon(DOOR);
				
				add(cell[r][c]);
			}
			r++;	
		}
		input.close();
	}

	public void keyPressed(KeyEvent key){
		if(gameTimer.isRunning()==false)
			gameTimer.start();
		
		if(pacMan.getDead()==false && score!=pellets){
			int direction = key.getKeyCode()-37;
			if(direction == 0 && maze[pacMan.getRow()][pacMan.getColumn()-1]!= 'W'){
				pacMan.setDirection(0);
			}
			else if(direction == 1 && maze[pacMan.getRow()-1][pacMan.getColumn()]!= 'W'){
				pacMan.setDirection(1);
			}
			else if(direction == 2 && maze[pacMan.getRow()][pacMan.getColumn()+1]!= 'W'){
				pacMan.setDirection(2);
			}
			else if(direction == 3 && maze[pacMan.getRow()+1][pacMan.getColumn()]!= 'W'){
				pacMan.setDirection(3);
			}
		}
	}

	public void keyReleased(KeyEvent key) {

	}
	
	public void keyTyped(KeyEvent key) {
	
	}

	private void performMove(Mover mover){
		if(mover.getColumn()==0){
			mover.setColumn(25);
			cell[12][0].setIcon(BLACK);
		}
		
		else if(mover.getColumn()==26){
			mover.setColumn(1);
			cell[12][26].setIcon(BLACK);
		}
		if(maze[mover.getNextRow()][mover.getNextColumn()]!='W'){
			if(mover == pacMan)
				animateTimer.start();
			else
			{
				if(maze[mover.getRow()][mover.getColumn()]=='F')
					cell[mover.getRow()][mover.getColumn()].setIcon(FOOD);
				
				else
					cell[mover.getRow()][mover.getColumn()].setIcon(BLACK);
				mover.move();
				cell[mover.getRow()][mover.getColumn()].setIcon(mover.getImage());
			}
			if(collided())
				death();
		}
	}
	private boolean collided(){
		for(Ghost g: ghost)
			if (g.getRow()==pacMan.getRow()&&g.getColumn()==pacMan.getColumn())
				return true;
		return false;
	}
	private void death(){

		pacMan.setDead(true);
		stopGame();
		cell[pacMan.getRow()][pacMan.getColumn()].setIcon(SKULL);
	}

	private void stopGame(){
		if(pacMan.getDead()||score==pellets)
			gameTimer.stop();
	}

	private void moveGhosts(){
		for(Ghost g: ghost){
			int dir = 0;
			
				if (maze[g.getRow()][g.getColumn()]=='E')
					dir = 1;
				else if (maze[g.getRow()][g.getColumn()]=='G')
					dir = 1;
				else if (maze[g.getRow()][g.getColumn()]=='L')
					dir = 2;
				else if (maze[g.getRow()][g.getColumn()]=='R')
					dir = 0;
			
				else if(Math.abs(g.getDirection()-2)!=dir)
				dir = (int)(Math.random()*4);
			
			g.setDirection(dir);
			performMove(g);
		}
	}

	public void actionPerformed(ActionEvent e) {

		if(e.getSource()==gameTimer){
			performMove(pacMan);
			moveGhosts();
		}

		else if(e.getSource()==animateTimer){

			animatePacMan(pStep);
			pStep++;

			if(pStep == 3)
				pStep = 0;
		}
	}

	private void animatePacMan(int pStep){

		if(pStep == 0){

			cell[pacMan.getRow()][pacMan.getColumn()].setIcon(pacMan.getPacManImage()[pacMan.getDirection()][1]);
			animateTimer.setDelay(100);
		}

		else if(pStep == 1){

			cell[pacMan.getRow()][pacMan.getColumn()].setIcon(BLACK);
		}

		else if(pStep == 2){
			pacMan.move();

			if(maze[pacMan.getRow()][pacMan.getColumn()] == 'F'){
				score+= 5;
				scoreLabel.setText("Score: "+score );
				eated+=1;
				eat.setText("Eaten: " + eated);
				maze[pacMan.getRow()][pacMan.getColumn()]='E';
			}
			if(maze[pacMan.getRow()][pacMan.getColumn()] == 'C'){
				score+= 100;
				scoreLabel.setText("Score: "+score );
				cherry+=1;
				cherrys.setText("Cherry: "+cherry);
				maze[pacMan.getRow()][pacMan.getColumn()]='E';
			}
			if(maze[pacMan.getRow()][pacMan.getColumn()] == 'B'){
				score+= 100;
				if(maze[pacMan.getRow()][pacMan.getColumn()] == 'B'){
					score+= 100;
					cell[ghost[0].getRow()][ghost[0].getColumn()].setIcon(BLACK);
					cell[ghost[1].getRow()][ghost[1].getColumn()].setIcon(BLACK);
					cell[ghost[2].getRow()][ghost[2].getColumn()].setIcon(BLACK);
					
					ghost[1].setRow(13);
					ghost[1].setColumn(13);
					ghost[2].setRow(13);
					ghost[2].setColumn(13);
					ghost[0].setRow(13);
					ghost[0].setColumn(13);
					

					maze[pacMan.getRow()][pacMan.getColumn()]='E';
				}
				
				maze[pacMan.getRow()][pacMan.getColumn()]='E';
			}
			
			cell[pacMan.getRow()][pacMan.getColumn()].setIcon(pacMan.getPacManImage()[pacMan.getDirection()][0]);

			animateTimer.stop();
		}

	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
}
