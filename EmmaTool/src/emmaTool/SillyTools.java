package emmaTool;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * this is a class full of silly little static methods made by emma
 */
public class SillyTools {

	//this returns a list with each entry being the location of the target char within the string
	public static List<Integer> locateChars(char tc, String ts) {
		List<Integer> characterLocations = new ArrayList<Integer>();
		char current;
		//loops until there are no more characters in the String
		for(int i = 0; i < ts.length(); i++) {
			current = ts.charAt(i);
			//if the current char is the target char, add the index to the location list
			if(current == tc) {
				characterLocations.add(i);
			}			
		}
		//return the finished list 
		return characterLocations;
		
	}
	
	
	//prints the contents of an array
	public static void printStrList(List<String> stringList) {
		//give an error if the array you are trying to print has a length of 0
		if(stringList.size() != 0) {
			//iterates through array, prints each
			for(int i = 0; i < stringList.size();i++) {
				System.out.println(i + 1 + ". " + stringList.get(i));
			}
			
		} else {
			System.out.println("the list you attempted to read has a size of 0");
		}
		
	}
	
	//inverts a boolean. idk if i can do this some other way easily??
	public static boolean invert(boolean invBool) {
		
		if(invBool == true) {
			return false;
		} else 
		if(invBool == false) {
			return true;
		} else {
			System.out.println("how did I get here? invBool returning false");
			return false;
		}
		
	}
	
	//This prompts the user with a yes or no, and returns a boolean.
	public static boolean askYorN(Scanner userInput) {
		boolean validChoice = false;
		String userYN = null;
		//asks for a y or an n, loops you until you give a valid answer
		while(validChoice != true) {
			System.out.println("(y or n)");
			userYN = userInput.nextLine();			;			
			if(userYN.equalsIgnoreCase("y") || userYN.equalsIgnoreCase("n")) {
				System.out.println("you entered: " + userYN);
				validChoice = true;
			} else {
				System.out.println(userYN + " is not a valid answer.");
			}
		}
		
		//returns based on yes or no
		if(userYN.equalsIgnoreCase("y")) {
			return true;
		} else if(userYN.equalsIgnoreCase("n")) {
			return false;
		} else {
			System.out.println("how did you get here?");
			System.out.println("returning false");
			return false;
		}
	}
	
	//this prompts the user to input a number
	public static int askForNumber(Scanner userInput, int maxNumber) {
		boolean validChoice = false;
		int userNumber = 0;
		//asks for a number between 0 and maxNumber
		while(validChoice == false) {
			System.out.print("Enter number: ");
			//if it is a number
			userNumber = userInput.nextInt();				
				if((0 < userNumber && userNumber <= maxNumber) || userNumber == 99) {
					validChoice = true;
				} else {
					System.out.println("error, out of range");
					validChoice = false;
				}
			
		}
		userInput.nextLine();
		return userNumber;
	}
	
	
	//silly little loading bar
	public static void loadingBar(int dotTotal, long sleepTime) {
		
		int dotNum = 0;
		try {
			while(dotNum <= dotTotal) {
				System.out.print(".");
				Thread.sleep(sleepTime);
				dotNum++;
			}
		} catch(InterruptedException e) {
			System.out.println("lol exception , loading interrupted");
		}
		
	
	}
	
	
	
	
	
	
	
}
