package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import game.MapFileReader;

@RunWith(Suite.class)
@SuiteClasses({})
public class MapFileReaderTest {
	
	/**
	 * Converts two dimensional char array to string. Eeach row of the char array is a new row on the string.
	 * @param arr - The char array to be converted
	 * @return String representing the two dimensional char array
	 */
	private String charArrToString(char[][] arr) {
		String str = "";
		for(char[] row : arr) {
			for (char cs : row) {
				str+= cs;
			}
			str += System.lineSeparator();
		}
		return str;
	}
	
	/**
	 * Checks if the 'MapFileReader.getMapData' method correctly reads and converts the map specified in the file.
	 */
	@Test
	public void ReadMapTest() {
		String expected = 
				"##############################\r\n" + 
				"#                            #\r\n" + 
				"#   ######                   #\r\n" + 
				"#        #                   #\r\n" + 
				"#        #                   #\r\n" + 
				"#        #                   #\r\n" + 
				"#                            #\r\n" + 
				"#                            #\r\n" + 
				"#                            #\r\n" + 
				"##############################\r\n";
		
		char[][] boundaries = null;
		try{
			boundaries = MapFileReader.getMapData("maps/ExampleMap.txt");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		String actual = charArrToString(boundaries);
		assertEquals(expected, actual);
	}
	
	/**
	 * The user input is different than expected (e.g. string instead of int)
	 */
	@Test
	public void BadInputParameterTest() {
		assertThrows(InputMismatchException.class, () -> {
			char[][]boundaries = MapFileReader.getMapData("maps/BadInputParameter.txt");
		});
	}
	
	
	@Test
	/**
	 * The user input is with smaller width value than the actual map width is.
	 */
	public void BadInputMapSizeTest() {
		assertThrows(InputMismatchException.class, () -> {
			char[][]boundaries = MapFileReader.getMapData("maps/BadInputMapSize.txt");
		});
	}
	
	/**
	 * The specified file is empty.
	 */
	@Test
	public void EmptyFileTest() {
		assertThrows(NoSuchElementException.class, () -> {
			char[][]boundaries = MapFileReader.getMapData("maps/Empty.txt");
		});
	}
	
	/**
	 * A file with that name could not be found.
	 */
	@Test
	public void FileNotFoundTest() {
		assertThrows(FileNotFoundException.class, () -> {
			char[][]boundaries = MapFileReader.getMapData("maps/NonExistentFile.txt");
		});
	}
}
