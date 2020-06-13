package game;

public class Tile {
	private int x;
	private int y;
	private TileType type;
	
	public Tile(int x, int y, TileType type) {
		this.x = x;
		this.y = y;
		this.type = type;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public TileType getType() {
		return type;
	}

	public void setType(TileType type) {
		this.type = type;
	}
	
	public void setCoords(int x, int y) {
		this.x = x;
		this.y = y;
	}
	/**
	 * Returns a new object with the same parameters as the current.
	 */
	public Tile copy() {
		return new Tile(this.x, this.y, this.type);
	}
	
	public String toString() {
		return String.format("%s   |  X:%d Y:%d", type, x, y);
	}
}
