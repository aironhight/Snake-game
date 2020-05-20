package game;

import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Launcher {
		
	public static void main(String[] args) {
		boolean mapLoaded = false;
		
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Please enter field file path and press enter: ");
		char[][] boundaries = null;
		
		while(!mapLoaded) {
			String mapNameInput = keyboard.nextLine();
			
			try {
				//boundaries = MapFileReader.getMapData("maps/ExampleMap.txt");
				boundaries = MapFileReader.getMapData(mapNameInput);
				
			} catch (FileNotFoundException e) {
				//File not found.
				System.out.println("ERROR: File not found. Please enter field file path again:");
			} catch (InputMismatchException inp) {
				//Wrong input format.
				System.out.println("ERROR: Wrong input formatting. Please check the field file and enter field file path again:");
			} catch (NoSuchElementException nse) {
				//Empty txt file.
				System.out.println("ERROR: Empty field file or bad field dimension input. Please enter a field file path again:");
			} catch (IndexOutOfBoundsException iob) {
				//Mismatching input width/height and actual
				System.out.println("ERROR: Bad field file inputs. Please check the field file and enter field file path again:");
			} finally {
				if(boundaries != null) {
					mapLoaded = true;
					keyboard.close();
				}
					
			}
		}
		if(!mapHasValidBoundaries(boundaries)) {
			System.out.println("Invalid map boundaries. Check the boundaries, restart the program and try again.");
			return;
		}
		
		Tile[][] field = new Tile[boundaries.length][boundaries[0].length];
		
		//Convert the char array to a tile array
		for(int i=0; i<boundaries.length; i++) {
			for(int j=0; j<boundaries[0].length; j++)
			{
				TileType type = null;
				switch(boundaries[i][j]) {
				
				case ' ':
					type = TileType.GRASS;
					break;
				case '#':
					type = TileType.WALL;
					break;
				default:
					System.out.println("The file contains forbidden symbols. Program terminated!");
					return;
				}
				
				field[i][j] = new Tile(i, j, type);
			}
		}
		//Start the game!
		Game g = new Game(field);
	}
	
	/**
	 * Checks if the field is surrounded by walls
	 * @param field the field to be examined.
	 * @return true if the map has valid boundaries
	 */
	private static boolean mapHasValidBoundaries(char[][] field) {
		//Check the left and right boundaries.
		for(int i=0; i< field.length; i++) {
			if(field[i][0] != '#' || (field[i][field[0].length-1] != '#'))
				return false;
		}
		
		//Check the top and bottom boundaries.
		for(int i=0; i< field[0].length; i++) {
			if(field[0][i] != '#' || (field[field.length-1][i] != '#'))
				return false;
		}
		
		return true;
	}
}
