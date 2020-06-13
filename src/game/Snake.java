package game;

import java.util.LinkedList;

public class Snake {
	private LinkedList<Tile> snakeTiles;
	private boolean isReversed;
	
	/**
	 * Constructor
	 * @param tile The tile that the snake should be spawned on.
	 */
	public Snake(Tile tile) {
		snakeTiles = new LinkedList<>();
		tile.setType(TileType.SNAKE);
		snakeTiles.add(tile.copy()); //Add copy so that any modifications on the tile will not modify the game field.
		isReversed = false;
	}
	
	/**
	 * Moves the head of the snake to the specified coordinates.
	 */
	public void moveSnake(int x, int y) {
		if(size() == 1) {
			head().setCoords(x, y);
			return;
		}
		if(!isReversed) {
			snakeTiles.addFirst(snakeTiles.removeLast());
			snakeTiles.getFirst().setCoords(x, y);
		} else {
			snakeTiles.addLast(snakeTiles.removeFirst());
			snakeTiles.getLast().setCoords(x, y);
		}

	}
	
	/**
	 * Grows the snake by adding a tile to the tail.
	 * The added tile is a copy of the tile on the game field, preventing 
	 * modification of the game field's tile coordinates.
	 * @param tile The tile to be added to the tail.
	 */
	public void grow(Tile tile) {
		if(isReversed)
			snakeTiles.addFirst(tile.copy()); 
		else
			snakeTiles.addLast(tile.copy());
	}
	
	/**
	 * @return The length of the snake 
	 */
	public int size() { return snakeTiles.size(); }
	
	/**
	 * Reverses the snake direction and returns the new direction
	 * @param currentDirection the current direction of the snake
	 * @return the new direction of the snake
	 */
	public SnakeDirection reverse(SnakeDirection currentDirection) {
		if(size() == 1) {
			//Snake is 1 tile long.
			isReversed = !isReversed;
			switch(currentDirection) {
			case LEFT:
				return SnakeDirection.RIGHT;
				
			case UP:
				return SnakeDirection.DOWN;
				
			case RIGHT:
				return SnakeDirection.LEFT;
				
			case DOWN:
				return SnakeDirection.UP;
			} 
		}
		
		SnakeDirection reversed = getReversedDirection();
		isReversed = !isReversed;
		return reversed;
	}
	
	/**
	 * @return The head of the snake.
	 */
	public Tile head() {
		if(!isReversed)
			return snakeTiles.getFirst();
		else
			return snakeTiles.getLast();
	}
	
	/**
	 * @return the tail of the snake
	 */
	public Tile tail() {
		if(!isReversed)
			return snakeTiles.getLast();
		else
			return snakeTiles.getFirst();
	}
	
	/**
	 * Gets the reversed direction by checking the last two tiles(the tail and the one before the tail).
	 * If the snake is with length equal to 1, the next direction is just the opposite direction of the current.
	 * @return the reversed direction of the snake
	 */
	public SnakeDirection getReversedDirection() {
		Tile tail;
		Tile secondToTail;
		
		if(!isReversed) {
			tail = snakeTiles.getLast();
			secondToTail = snakeTiles.get(size()-2);
		} else {
			tail = snakeTiles.getFirst();
			secondToTail = snakeTiles.get(1);
		}
		
		
		if(tail.getX() - secondToTail.getX() == 0) {
			//Moving on the Y axis (left or right)
			
			if(tail.getY() - secondToTail.getY() == 1) {
				//Currently moving left. Change to right.
				return SnakeDirection.RIGHT;
			} else {
				//Currently moving right. Change to left.
				return SnakeDirection.LEFT;
			}
			
		} else {
			//Moving on the X axis (up or down)
			if(tail.getX() - secondToTail.getX() == 1) {
				//Currently moving up. Change to down.
				return SnakeDirection.DOWN;
			} else {
				//Currently moving down. Change to up.
				return SnakeDirection.UP;
			}
		}
			
	}
	
	/**
	 * @return a Tile[] containing all snake tiles.
	 */
	public Tile[] getAllTiles() {
		Tile[] tiles = new Tile[size()];
		
		for(int i=0; i<tiles.length; i++) {
			tiles[i] = snakeTiles.get(i);
		}
		
		return tiles;
	}
	
	
}
