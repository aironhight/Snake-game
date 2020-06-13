package Arrays;

import java.util.Scanner;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import jdk.internal.org.objectweb.asm.util.CheckFieldAdapter;
import sun.reflect.generics.tree.ArrayTypeSignature;

public class Ex1to5 {
	
	private static boolean arraysAreEqual(Object[] firstArray, Object[] secondArray) {
		if(firstArray.length != secondArray.length) {
			System.out.println("The two arrays are not equal");
			return false;
		}
		for (int i = 0; i < firstArray.length; i++) {
			if(!firstArray[i].equals(secondArray[i])) {
				System.out.println("The two arrays are not equal");
				return false;
			}
		}
		return true;
	}
	
	public static void main(String[] args) {
		//Exercise 1
		int[] arr = new int[20];
		for(int i=0; i<arr.length; i++)
			arr[i] = i*5;
		
		//System.out.println(java.util.Arrays.toString(arr));
		
		
		//Exercise 2
		/*
		System.out.println("Enter array 1 elements. Please split the elements by ','");
		Scanner keyboard = new Scanner(System.in);
		String first = keyboard.nextLine();
		System.out.println("Enter array 2 elements. Please split the elements by ','");
		String second = keyboard.nextLine();
		
		String[] firstArray = first.split(",");
		String[] secondArray = second.split(",");
		
		System.out.println("The arrays are equal:" + arraysAreEqual(firstArray, secondArray));
		*/
		
		
		//Exercise 4
		/*
		int[] ar = {2, 1, 1, 2, 3, 3, 2, 2, 2, 1};
		System.out.println(java.util.Arrays.toString(longestRow(ar))); */
		
		//Exercise 5
		int[] ar =  {3, 2, 3, 4, 2, 2, 4};
		System.out.println(java.util.Arrays.toString(longestRowAscending(ar)));
		
		
	}
	
	private static int[] longestRow(int[] arr) {
		int longestStreak = 1;
		int longestStreakNumber = arr[0];
		int currentNumber = arr[0];
		int currentStreak = 0;
		
		for(int i=1; i<arr.length; i++) {
			if(arr[i] == currentNumber) {
				currentStreak++;
				if(currentStreak > longestStreak) {
					longestStreak = currentStreak;
					longestStreakNumber = currentNumber;
				}
			} else {
				currentNumber = arr[i];
				currentStreak = 1;
			}
		}
		
		int[] toReturn = new int[longestStreak];
		for (int i = 0; i < toReturn.length; i++) {
			toReturn[i] = longestStreakNumber;
		}
		return toReturn;
	}
	
	private static int[] longestRowAscending(int[] arr) {
		int longestStreak = 1;
		int longestStreakNumber = arr[0];
		int currentNumber = arr[0];
		int currentStreak = 0;
		
		for(int i=1; i<arr.length; i++) {
			if(arr[i] == (currentNumber+1)) {
				currentStreak++;
				if(currentStreak > longestStreak) {
					currentNumber++;
					longestStreak = currentStreak;
					longestStreakNumber = currentNumber+1;
				}
			} else {
				currentNumber = arr[i];
				currentStreak = 1;
			}
		}
		
		int[] toReturn = new int[longestStreak];
		for (int i = 0; i < toReturn.length; i++) {
			toReturn[i] = longestStreakNumber-(longestStreak-i);
		}
		return toReturn;
	}
	
}
