package game;

public enum TileType {
	GRASS(' '),
	WALL('#'),
	APPLE('o'),
	PEAR('p'),
	SNAKE('*');
	
	private char tileSymbol;
	
	TileType(char tileSymbol){
		this.tileSymbol = tileSymbol;
	}
	
	public char getTileSymbol() {
		return tileSymbol;
	}
}
