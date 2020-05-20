package game;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class MapFileReader {
	
	/**
	 * Reads the specified file and returns a char[][] with the field in the file.
	 * @param fileDirectory The directory that should be read.
	 * @return Two-dimensional char array with the field from the specified file.
	 * @throws FileNotFoundException File was not found in the directory
	 * @throws InputMismatchException Bad field dimensions input (e.g. the specified height and width in the file do not match the 
	 * actual width and height of the field.
	 * @throws NoSuchElementException Empty text file.
	 * @throws IndexOutOfBoundsException Bad field inpud.
	 */
	public static char[][] getMapData(String fileDirectory) throws FileNotFoundException, InputMismatchException, NoSuchElementException, IndexOutOfBoundsException
	{
		Scanner scanner = new Scanner(new File(fileDirectory));
		int width = scanner.nextInt();
		int height = scanner.nextInt();
		int actualHeight = 0;
		scanner.nextLine(); //After 'nextInt()' the scanner adds '/n'. We use 'scanner.nextLine()' to skip this line.
		
		char[][] boundaries = new char[height][width]; //Initialize the two dimensional array
		
		while(scanner.hasNext()) {
			actualHeight++;
			
			if(actualHeight > height) {
				scanner.close();
				throw new InputMismatchException(); //Throw an exception if the 'input height' != 'actual height'
			}
			
			for(int i=0; i< boundaries.length; i++) {
				String line = scanner.nextLine();
				
				if(line != null && line.length() > width) {
					scanner.close();
					throw new InputMismatchException(); //Throw an exception if the 'input width' != 'actual width'
				}
				
				char[] lineChars = line.toCharArray();
				for (int j = 0; j < lineChars.length; j++) {
					boundaries[i][j] = lineChars[j];
				}
			}
		}
		scanner.close();
		return boundaries;	
	}
}
