package game;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Game {
	private static final int MOVES_UNTIL_NEW_FRUIT = 20;
	private static final int APPLE_PROBABILITY = 5;
	
	private Tile[][] field;
	private Tile fruit;
	private ArrayList<Tile> grassTiles;
	private boolean isDead;
	private boolean changingDirection;
	private JFrame frame;
	private KeyListener keyListener;
	private int snakeSpeed;
	private int grassCount;
	private int width;
	private int height;
	private int snakeMovesCounter;
	private Container ctr;
	private Thread movingThread;
	private boolean canMove;
	private Snake snake;
	private SnakeDirection direction;
	private Random random;
	
	public Game(Tile[][] field) {
		System.out.println("To start moving press any arrow key.");
		this.field = field;
		isDead = false;
		canMove = true;
		grassTiles = new ArrayList<Tile>();
		width = field[0].length;
		height = field.length;
		snakeSpeed = 250; //The lower the number, the faster the snake is.
		snakeMovesCounter = 0; //Required for the fruit respawn.
		
		frame = new JFrame("Snake game for Trading 212");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		
		initialize();
	}
	
	/**
	 * Initialization of some parameters and operations that require multiple lines of code.
	 * Done outside of the constructor for readability purpose.
	 */
	private void initialize() {
		ctr = frame.getContentPane();
		
		if(grassTiles.size() == 0) {
			//initialize grass
			for(int i=0; i<height; i++) {
				for(int j=0; j<width; j++) {
					if(field[i][j].getType().equals(TileType.GRASS))
						grassTiles.add(field[i][j]);
				}
			}
		}

		grassCount = grassTiles.size();
		if(grassCount <= 1) { //Make sure there is grass on the field.
			System.out.println("There is not enough grass on this field...");
			return;
		}
		
		//Draw the field
		for(int i=0; i<height; i++) {
			for(int j=0; j<width; j++) {
				JLabel tileLabel = new JLabel(field[i][j].getType().getTileSymbol() + "");
				tileLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
				frame.add(tileLabel);
			}
		}
		
		frame.setSize(width*20, height*20); // width = h*20; height = w*20
		GridLayout grid = new GridLayout(height, width);
		frame.setLayout(grid);
		//frame.setResizable(false);
		//frame.pack();

		frame.setAlwaysOnTop(true);
		frame.toFront();
		frame.requestFocus();
		frame.setVisible(true);
		
		
		//Snake Spawn
		spawnSnake();
		
		//Initialize Key listener
		keyListener = new KeyListener() {
			@Override
			public void keyPressed(KeyEvent evt) {
				switch (evt.getKeyCode()) {
				case 10: //Enter
					if(isDead)
						restartGame();
					break;
				
				case 37: // Left
					switchDirection(SnakeDirection.LEFT);
					break;

				case 38: // Up
					switchDirection(SnakeDirection.UP);
					break;

				case 39: // Right
					switchDirection(SnakeDirection.RIGHT);
					break;

				case 40: // Down
					switchDirection(SnakeDirection.DOWN);
					break;
				case 70: //Spawning fruits with 'F'
					spawnFruit();
					break;
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {}

			@Override
			public void keyTyped(KeyEvent arg0) {}
			
		};
		frame.addKeyListener(keyListener);
		
		if(movingThread != null) //If the moving thread was already started.
			movingThread.interrupt();
		
		movingThread = new Thread(
				new Runnable() {
					@Override
					public void run() {
						while(!isDead) {
							try {
								Thread.sleep(snakeSpeed);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}	
							moveSnake();
						}
					}					
				});
		movingThread.start();
	}
	
	/**
	 * Spawns the snake on a randomly chosen grass tile.
	 */
	private void spawnSnake() {		
		random = new Random();
		Tile snakeSpawn = grassTiles.get(random.nextInt(grassTiles.size()));
		changeTileType(snakeSpawn, TileType.SNAKE);
		snake = new Snake(snakeSpawn);
		random = null;
	}
	
	/**
	 * Restarts the game 
	 */
	private void restartGame() {
		isDead = false;
		direction = null;
		canMove = true;
		changingDirection = false;
		
		//Remove the fruit from the field
		if(fruit != null)
			changeTileType(fruit, TileType.GRASS);
		
		//Remove the snake from the field
		Tile[] snakeTiles = snake.getAllTiles();
		for(Tile tile : snakeTiles) {
			changeTileType(tile, TileType.GRASS);
		}
		
		//Reinitialize the game.
		ctr.removeAll();
		initialize();
	}

	/**
	 * Changes the current direction of the snake. If next direction is the same as or 
	 * opposite of the current direction the method returns without making changes.
	 * @param nextDirection The desired direction of the snake.
	 */
	private void switchDirection(SnakeDirection nextDirection) {
		if (changingDirection) //If the user has already made a change in the direction while the snake is in that state.
			return;
		
		switch (nextDirection){
		
		case LEFT:
			if(direction == SnakeDirection.LEFT || direction == SnakeDirection.RIGHT)
				return;
			direction = SnakeDirection.LEFT;
			changingDirection = true;
			break;
			
		case UP:
			if(direction == SnakeDirection.UP || direction == SnakeDirection.DOWN)
				return;
			
			direction = SnakeDirection.UP;
			changingDirection = true;
			break;
			
		case RIGHT:
			if(direction == SnakeDirection.RIGHT || direction == SnakeDirection.LEFT)
				return;
			
			direction = SnakeDirection.RIGHT;
			changingDirection = true;
			break;
			
		case DOWN:
			if(direction == SnakeDirection.DOWN || direction == SnakeDirection.UP)
				return;
			
			direction = SnakeDirection.DOWN;
			changingDirection = true;
			break;
		}
	}

	/**
	 * Reverses the movement of the snake.
	 */
	private void reverseMovement() {
		canMove = false;
		direction = snake.reverse(direction);
		canMove = true;
	}
	
	/**
	 * Moves the snake with 1 tile. The method also checks the type of the next tile
	 * and takes action accordingly.
	 * <ul>
	 * <li>If direction is Up - next tile is [x-1][y]; </li>
	 * <li>if direction is Down - next tile is [x+1][y]; </li>
	 * <li>if direction is Left - next tile is [x][y-1];</li>
	 * <li>if direction is Right - next tile is [x][y+1]; </li>
	 * </ul>
	 */
	private void moveSnake() {
		if(!canMove || direction == null)
			return;
		canMove = false;
		
		Tile snakeHead = snake.head();
	
		Tile nextTile = null;
		switch(direction) {
		case LEFT:
			nextTile = field[snakeHead.getX()][snakeHead.getY()-1];
			break;
			
		case UP:
			nextTile = field[snakeHead.getX()-1][snakeHead.getY()];
			break;
			
		case RIGHT:
			nextTile = field[snakeHead.getX()][snakeHead.getY()+1];
			break;
			
		case DOWN:
			nextTile = field[snakeHead.getX()+1][snakeHead.getY()];	
			break;
			
		}
		
		changingDirection = false;

		if(nextTile != null) {
			Tile tail = snake.tail().copy();

			switch(nextTile.getType()) {
			case APPLE:
				//If all tiles are filled. End game.
				if(grassCount == 0) { 
					win();
					break;
				}
				fruit = null;
			
				snake.moveSnake(nextTile.getX(), nextTile.getY());
				changeTileType(nextTile, TileType.SNAKE); //Change the next tile to a SNAKE tile. Put it as the 'head' of the snake afterwards.
				snake.grow(tail);
				spawnFruit();
				break;
				
			case GRASS:
				//Change the last (tail) tile to a GRASS tile.
				snake.moveSnake(nextTile.getX(), nextTile.getY()); //Move the snake to the next tile
				changeTileType(nextTile, TileType.SNAKE); //Change the next tile to a SNAKE tile.
				changeTileType(tail, TileType.GRASS); //Change the snake's tail to a GRASS tile
				
				snakeMovesCounter++;
				if(snakeMovesCounter > MOVES_UNTIL_NEW_FRUIT) {
					spawnFruit();
				}
				
				break;
			
			case PEAR:
				//In case a pear was spawned on the last free tile.
				canMove = false;
				if(grassCount == 0) {
					win();
					break;
				}
				snake.moveSnake(nextTile.getX(), nextTile.getY());
				changeTileType(nextTile, TileType.SNAKE); //Change the next tile to a SNAKE tile. Put it as the 'head' of the snake afterwards.
				changeTileType(field[tail.getX()][tail.getY()], TileType.GRASS);
				
				reverseMovement();
				fruit = null;
				spawnFruit();
				break;
				
			case SNAKE: //Collision
				System.out.println("You collided in a snake tile!");
				die();
				break;
				
			case WALL: //Collision
				System.out.println("You collided in a wall!");
				die();
				break;
			}
			canMove = true;
		}		
	}
	
	/**
	 * Changes the tile type of the given tile. The GUI is updated afterwards.
	 * @param tile The tile to get updated.
	 * @param newType The new tile type.
	 */
	private void changeTileType(Tile tile, TileType newType) {
		tile = field[tile.getX()][tile.getY()];
		if(tile.getType() == TileType.GRASS) {
			grassCount--;
			grassTiles.remove(tile);
		}
			
		if(newType == TileType.GRASS) {
			grassTiles.add(tile);
			grassCount++;
		}
		
		if(tile.getType() == TileType.SNAKE && (newType == TileType.APPLE || newType == TileType.PEAR)) {
			spawnFruit();
			return;
		}
		
		//Change the type of the tile and repaint it on the window.
		tile.setType(newType);
		JLabel jbt2 = (JLabel)ctr.getComponent(getTileId(tile));
		jbt2.setText(tile.getType().getTileSymbol() + "");
		frame.validate();
		frame.repaint();
	}
	
	/**
	 * Method for stopping the game on collision.
	 */
	private void die() {
		isDead = true;
		System.out.println("Game over! The length of the snake was " + snake.size() + ". "
				+ "You needed " + grassCount + " apples to beat the game. :( \n"
						+ "Press enter to start a new game.");
		
		if(movingThread != null)
			movingThread.interrupt();
	}
	
	/**
	 * Called when the snake fills out the whole field.
	 */
	private void win() {
		isDead = true;
		System.out.println("You won! The snake was " + snake.size() + " tiles long! \n"
				+ "Press enter to start a new game.");
		
		if(movingThread != null)
			movingThread.interrupt();
	}

	/**
	 * Gets the ID of a specific tile on the field grid.
	 * @param tile 
	 * @return the id of the tile on the field grid
	 */
	private int getTileId(Tile tile) {
		return tile.getX()*width + tile.getY();
	}
	
	/**
	 * Spawns a fruit on the game field. The location of the fruit is a random location currently covered with grass.
	 */
	private void spawnFruit() {
		//If this is the last available tile. Keep the fruit.
		snakeMovesCounter = 0;
		if(grassCount == 0)
			return;
		
		//Remove the old fruit
		if(fruit != null) {
			changeTileType(fruit, TileType.GRASS);
		}
		
		random = new Random();
		
		int fruitProbability = random.nextInt(6); //take a random number from 0 to 5 included 
		TileType nextFruitType;
		
		if(fruitProbability < APPLE_PROBABILITY) //Probability for an apple 5 times more than a pear.
			nextFruitType = TileType.APPLE; //Set next fruit type to apple
		else
			nextFruitType = TileType.PEAR; //Set next fruit type to pear		
			
		fruit = grassTiles.remove(random.nextInt(grassTiles.size())); 
		
		changeTileType(fruit, nextFruitType); //'Spawn' the new fruit	
		random = null; //Clear the random...
		Runtime.getRuntime().gc(); //Clear the random from the memory.
	}
	
}
