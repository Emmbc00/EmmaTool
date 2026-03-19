package emmaTool;

import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) throws InterruptedException {
		Scanner userInput = new Scanner(System.in);	
		boolean exitGame = false;
		int choice = 0;

		while(exitGame == false) {
			//print the menu
			System.out.println("-----programs-----");
			System.out.println("1. RCTankTool");
			System.out.println("2. Save Data");
			System.out.println("99. Exit program");
			System.out.println("------------------");
			
			choice = SillyTools.askForNumber(userInput, 2);
			//what happens for each option
			switch(choice) {
			case 1:
				SillyTools.loadingBar(18, 75);
				System.out.println("launching RCTankTool");
				System.out.println();
				System.out.println();
				System.out.println();
				RCTankTool.main(args);
				break;
			case 2:
				
			case 99:
				System.out.print("shutting down");
				SillyTools.loadingBar(6, 50);
				exitGame = true;
				break;
			}
			
		} //end menu loop
		
		System.out.println(".");
		System.out.println(".");
		System.out.println("end");
	}
	
	
	
	
	
}
