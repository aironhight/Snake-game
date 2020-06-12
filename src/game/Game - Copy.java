//package game;
//
//import java.awt.Color;
//import java.awt.Container;
//import java.awt.GridLayout;
//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Random;
//
//import javax.swing.BorderFactory;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//
//import com.sun.glass.ui.Window.State;
//
//
//public class Game {
//	private Tile[][] field;
//	private Tile fruit;
//	private ArrayList<Tile> snakeTiles;
//	private ArrayList<Tile> grassTiles;
//	private boolean isDead;
//	private boolean leftDirection, rightDirection, upDirection, downDirection, changingDirection;
//	private JFrame frame;
//	private KeyListener keyListener;
//	private int snakeSpeed;
//	private int grassCount;
//	private int snakeLength;
//	private int width;
//	private int height;
//	private int snakeMovesCounter;
//	private Container ctr;
//	private Thread movingThread;
//	private boolean canMove;
//	private Snake snake;
//	
//	public Game(Tile[][] field) {
//		System.out.println("To start moving press any arrow key.");
//		this.field = field;
//		isDead = false;
//		leftDirection = false;
//		rightDirection = false;
//		upDirection = false;
//		downDirection = false;
//		canMove = true;
//		snakeTiles = new ArrayList<Tile>(1); 
//		grassTiles = new ArrayList<Tile>();
//		width = field[0].length;
//		height = field.length;
//		snakeLength=1;
//		snakeSpeed = 250; //The lower the number, the faster the snake is.
//		snakeMovesCounter = 0; //Required for the fruit respawn.
//		
//		frame = new JFrame("Snake game for Trading 212");
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		
//		ctr = frame.getContentPane();
//		
//		//initialize grass
//		for(int i=0; i<height; i++) {
//			for(int j=0; j<width; j++) {
//				if(field[i][j].getType().equals(TileType.GRASS))
//					grassTiles.add(field[i][j]);
//			}
//		}
//		
//		grassCount = grassTiles.size();
//		if(grassCount <= 1) { //Make sure there is grass on the field.
//			System.out.println("There is not enough grass on this field...");
//			return;
//		}
//		
//		//Take a "spawn point for the snake"
//		snake = new Snake(grassTiles.remove(0))
//		
//		initialize();
//		movingThread.start();
//	}
//	
//	/**
//	 * Initialization of some parameters and operations that require multiple lines of code.
//	 * Done outside of the constructor for readability purpose.
//	 */
//	private void initialize() {
//		//Draw the field
//		for(int i=0; i<height; i++) {
//			for(int j=0; j<width; j++) {
//				JLabel tileLabel = new JLabel(field[i][j].getType().getTileSymbol() + "");
//				tileLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
//				frame.add(tileLabel);
//			}
//		}
//		
//		frame.setSize(width*20, height*20); // width = h*20; height = w*20
//		GridLayout grid = new GridLayout(height, width);
//		frame.setLayout(grid);
//		//frame.setResizable(false);
//		//frame.pack();
//
//		frame.setAlwaysOnTop(true);
//		frame.toFront();
//		frame.requestFocus();
//		frame.setVisible(true);
//		
//		//Initialize threads
//		movingThread = new Thread(
//				new Runnable() {
//					@Override
//					public void run() {
//						while(!isDead) {
//							try {
//								Thread.sleep(snakeSpeed);
//							} catch (InterruptedException e) {
//								e.printStackTrace();
//							}	
//							moveSnake();
//						}
//					}					
//				});
//				
//		//Initialize Key listnere
//		keyListener = new KeyListener() {
//			@Override
//			public void keyPressed(KeyEvent evt) {
//
//				switch (evt.getKeyCode()) {
//				case 37: // Left
//					switchDirection("left");
//					break;
//
//				case 38: // Up
//					switchDirection("up");
//					break;
//
//				case 39: // Right
//					switchDirection("right");
//					break;
//
//				case 40: // Down
//					switchDirection("down");
//					break;
//				case 70: //Spawning fruits with 'F'
//					spawnFruit();
//					break;
//				}
//			}
//
//			@Override
//			public void keyReleased(KeyEvent arg0) {}
//
//			@Override
//			public void keyTyped(KeyEvent arg0) {}
//			
//		};
//		frame.addKeyListener(keyListener);
//	}
//
//	/**
//	 * Changes the current direction of the snake. If next direction is the same as or 
//	 * opposite of the current direction the method returns without making changes.
//	 * @param nextDirection The desired direction of the snake.
//	 */
//	private void switchDirection(String nextDirection) {
//		if (changingDirection) //If the user has already made a change in the direction while the snake is in that state.
//			return;
//		
//		switch (nextDirection){
//		
//		case "left":
//			if(leftDirection || rightDirection) 
//				return;
//			
//			upDirection = false;
//			downDirection = false;
//			leftDirection = true;
//			changingDirection = true;
//			break;
//			
//		case "up":
//			if(upDirection || downDirection)
//				return;
//			
//			leftDirection = false;
//			rightDirection = false;
//			upDirection = true;
//			changingDirection = true;
//			break;
//			
//		case "right":
//			if(leftDirection || rightDirection)
//				return;
//			
//			upDirection = false;
//			downDirection = false;
//			rightDirection = true;
//			changingDirection = true;
//			break;
//			
//		case "down":
//			if(upDirection || downDirection)
//				return;
//			
//			leftDirection = false;
//			rightDirection = false;
//			downDirection = true;
//			changingDirection = true;
//			break;
//		}
//	}
//
//	/**
//	 * Reverses the movement of the snake. If the snake is more than 1 tile long the direction
//	 * of the snake is retrieved by the position of the last 2 snake tiles(the tail).
//	 */
//	private void reverseMovement() {
//		
//		if(snakeLength == 1) {
//			//Snake is 1 tile long.
//			Collections.reverse(snakeTiles);
//			if(leftDirection) {
//				leftDirection = false;
//				rightDirection = true;
//			} else if(upDirection) {
//				upDirection = false;
//				downDirection = true;
//			}
//			else if(rightDirection) {
//				rightDirection = false;
//				leftDirection = true;
//			}
//			else if(downDirection) {
//				downDirection = false;
//				upDirection = true;
//			}
//		} else {
//			//Snake is longer than 1 tile... Take the last 2 tiles and see the direction from them.
//			canMove = false;
//			Tile tail = snakeTiles.get(snakeLength-1);
//			Tile secondToTail = snakeTiles.get(snakeLength-2);
//			
//			if(tail.getX() - secondToTail.getX() == 0) {
//				//Moving on the Y axis (left or right)
//				leftDirection = false; upDirection = false; rightDirection = false; downDirection = false;
//				
//				if(tail.getY() - secondToTail.getY() == 1) {
//					//Currently moving left. Change to right.
//					leftDirection = false; upDirection = false; downDirection = false;
//					rightDirection = true;
//				} else {
//					//Currently moving right. Change to left.
//					upDirection = false; rightDirection = false; downDirection = false;
//					leftDirection = true;
//				}
//			} else {
//				//Moving on the X axis (up or down)
//				if(tail.getX() - secondToTail.getX() == 1) {
//					//Currently moving up. Change to down.
//					leftDirection = false; upDirection = false; rightDirection = false;
//					downDirection = true;
//				} else {
//					//Currently moving down. Change to up.
//					leftDirection = false;rightDirection = false; downDirection = false;
//					upDirection = true;
//				}
//			}
//			Collections.reverse(snakeTiles); //Reverse the snake body.
//			canMove = true;
//		}
//		
//	}
//	
//	/**
//	 * Moves the snake with 1 tile. The method also checks the type of the next tile
//	 * and takes action accordingly.
//	 * <ul>
//	 * <li>If direction is Up - next tile is [x-1][y]; </li>
//	 * <li>if direction is Down - next tile is [x+1][y]; </li>
//	 * <li>if direction is Left - next tile is [x][y-1];</li>
//	 * <li>if direction is Right - next tile is [x][y+1]; </li>
//	 * </ul>
//	 */
//	private void moveSnake() {
//
//		if(!canMove)
//			return;
//		
//		Tile snakeHead = snakeTiles.get(0);
//		Tile nextTile = null;
//		changingDirection = false;
//		
//		if(leftDirection) {
//			nextTile = field[snakeHead.getX()][snakeHead.getY()-1];
//		} 
//		else if(upDirection) {
//			nextTile = field[snakeHead.getX()-1][snakeHead.getY()];
//		}
//		else if(rightDirection) {
//			nextTile = field[snakeHead.getX()][snakeHead.getY()+1];
//		}
//		else if(downDirection) {
//			nextTile = field[snakeHead.getX()+1][snakeHead.getY()];	
//		}
//		
//		if(nextTile != null) {
//			switch(nextTile.getType()) {
//			case APPLE:
//				//If all tiles are filled. End game.
//				if(grassCount == 0) { 
//					win();
//					break;
//				}
//				//Add a snake tile in front of the 'head'. Keep the body
//				changeTileType(nextTile, TileType.SNAKE);
//				snakeTiles.add(0, nextTile);
//				snakeLength++;
//				break;
//				
//			case GRASS:
//				//Change the last (tail) tile to a GRASS tile.
//				changeTileType(snakeTiles.remove(snakeLength-1), TileType.GRASS); 
//				changeTileType(nextTile, TileType.SNAKE); //Change the next tile to a SNAKE tile. Put it as the 'head' of the snake afterwards.
//				snakeTiles.add(0, nextTile);
//				snakeMovesCounter++;
//				if(snakeMovesCounter > 9) {
//					spawnFruit();
//					snakeMovesCounter = 0;
//				}
//				break;
//			
//			case PEAR:
//				//In case a pear was spawned on the last free tile.
//				if(grassCount == 0) {
//					isDead = true;
//					System.out.println("YOU WON!");
//				}
//				changeTileType(nextTile, TileType.GRASS);
//				reverseMovement();
//				break;
//				
//			case SNAKE: //Collision
//				die();
//				break;
//				
//			case WALL: //Collision
//				die();
//				break;
//			}
//			
//		}		
//	}
//	
//	/**
//	 * Changes the tile type of the given tile. The GUI is updated afterwards.
//	 * @param tile The tile to get updated.
//	 * @param newType The new tile type.
//	 */
//	private void changeTileType(Tile tile, TileType newType) {
//		if(tile.getType() == TileType.GRASS && newType != TileType.GRASS) {
//			grassCount--;
//		}
//			
//		if(tile.getType() != TileType.GRASS && newType == TileType.GRASS) {
//			grassTiles.add(tile);
//			grassCount++;
//		}
//		
//		if(tile.getType() == TileType.SNAKE && (newType == TileType.APPLE || newType == TileType.PEAR)) {
//			spawnFruit();
//			return;
//		}
//		
//		tile.setType(newType);
//		JLabel jbt2 = (JLabel)ctr.getComponent(getTileId(tile));
//		jbt2.setText(newType.getTileSymbol() + "");
//		frame.validate();
//		frame.repaint();
//	}
//	
//	/**
//	 * Method for stopping the game on collision.
//	 */
//	private void die() {
//		isDead = true;
//		System.out.println("Game over! The length of the snake was " + snakeLength + ". "
//				+ "You needed " + grassCount + " apples to beat the game. :(");
//	}
//	
//	/**
//	 * Called when the snake fills out the whole field.
//	 */
//	private void win() {
//		isDead = true;
//		System.out.println("You won! The snake was " + snakeLength + " tiles long!");
//	}
//
//	/**
//	 * Gets the ID of a specific tile on the field grid.
//	 * @param tile 
//	 * @return the id of the tile on the field grid
//	 */
//	private int getTileId(Tile tile) {
//		return tile.getX()*width + tile.getY();
//	}
//	
//	/**
//	 * Spawns a fruit on the game field. The location of the fruit is a random location currently covered with grass.
//	 */
//	private void spawnFruit() {
//		//If this is the last available tile. Keep the fruit.
//		if(grassCount == 0)
//			return;
//		
//		//Remove the old fruit
//		if(fruit != null) {
//			changeTileType(fruit, TileType.GRASS);
//		}
//		
//		Random random = new Random();
//		
//		int fruitProbability = random.nextInt(6); //take a random number from 0 to 5 included 
//		TileType nextFruitType;
//		
//		if(fruitProbability < 5) //Probability for an apple 5 times more than a pear.
//			nextFruitType = TileType.APPLE; //Set next fruit type to apple
//		else
//			nextFruitType = TileType.PEAR; //Set next fruit type to pear		
//			
//		fruit = grassTiles.remove(random.nextInt(grassTiles.size())); 
//		
//		changeTileType(fruit, nextFruitType); //'Spawn' the new fruit	
//	}
//	
//	
//}
