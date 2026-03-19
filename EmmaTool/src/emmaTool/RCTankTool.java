package emmaTool;
import java.util.Scanner;

public class RCTankTool {

	public static void main(String args[]) throws InterruptedException {
		Scanner userInput = new Scanner(System.in);
		boolean platesByHand = true;
		
		System.out.println("Welcome to RCTankTool!");		
		System.out.println("----------------------");
		System.out.println("Create your tank!");
		
		TankBuilder userTank = new TankBuilder();
		
		
		int choice;
		boolean tankActions = false;
		while(tankActions == false) {
			System.out.println("==================");
			System.out.println("What do you want to do?");
			System.out.println("1. Make a new tank");
			System.out.println("2. check tank info");
			System.out.println("3. check tank components");
			System.out.println("4. Toggle plates by hand");
			System.out.println("99. exit and throw away your progress");
			System.out.println("==================");
			choice = SillyTools.askForNumber(userInput, 4);
			
			switch(choice) {
			case 1:
				tankBuilder(userInput, userTank);
				break;
			case 2:
				userTank.displayStats();
				break;
			case 3:
				userTank.updateBreakdown(platesByHand);
				userTank.displayBreakdown();
				break;
			case 4: 
				platesByHand = SillyTools.invert(platesByHand);
				System.out.println("Still crafting plates by hand: " + platesByHand);
				break;
			case 99:
				tankActions = true;
				System.out.print("Shutting down");
				SillyTools.loadingBar(5, 100);
				break;
			}
			
		}// end of menu loop
		
		System.out.println("goodbye");
	}
	
	
	//the whole make a tank thing
	public static void tankBuilder(Scanner userInput, TankBuilder userTank) throws InterruptedException {
		boolean buildTankLoop = false;
		while(buildTankLoop == false) {
			//run the prompt for setting the dimension 
			dimensionWizard(userInput, userTank);
			
			//enter how many valves you want
			System.out.println();
			System.out.println("How many valves?");
			int tankValves = userInput.nextInt();
			userTank.setValves(tankValves);
			
			//pick your material
			System.out.print("loading avaliable materials...");
			SillyTools.loadingBar(8, 20);
			System.out.println("Pick a number:");
			System.out.println("1. Iron");
			System.out.println("2. Steel");
			System.out.println();
			
			int matNum = SillyTools.askForNumber(userInput, 2);
			
			switch (matNum) {
			case 1:
				userTank.setMaterial("iron");
				break;
			case 2:
				userTank.setMaterial("steel");
			}
			
			userTank.displayStats();
			System.out.println("Is this correct?");
			buildTankLoop = SillyTools.askYorN(userInput);
		}
	}
	
	
	// get user input to set the dimensions of the tank
	public static void dimensionWizard(Scanner userInput, TankBuilder tank) {
		boolean sizeConfirm = false;
		while(sizeConfirm == false) {
			//enter the three coords
			System.out.println("Enter tank width (x): ");
			int tankX = userInput.nextInt();
			tank.setX(tankX);		
			System.out.println("Enter tank length (y): ");
			int tankY = userInput.nextInt();
			tank.setY(tankY);		
			System.out.println("Enter tank height (z): ");
			int tankZ = userInput.nextInt();
			tank.setZ(tankZ);
			//confirm what you entered
			System.out.println("You have entered: " + tank.getX() + "x" + tank.getY() + "x" + tank.getZ() + ". Is this correct?");
			sizeConfirm = SillyTools.askYorN(userInput); // yay easy ask for yes or no
		}
	}
	
	
	
	
	
}
