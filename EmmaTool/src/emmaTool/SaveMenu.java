package emmaTool;
import java.util.Scanner;

public class SaveMenu {
	
	
	
	
	
	public static void main(String[] args) {
		Scanner userInput = new Scanner(System.in);
		SaveManager emmaSave = new SaveManager();
		boolean exitMenu = false;
		
		while(exitMenu == false) {
			mainMenu();
			int chosenOption = SillyTools.askForNumber(userInput, 8);
			//begin options
			switch(chosenOption) {
			case 1:
				//reset save
				if(emmaSave.resetSave()) {
					System.out.println("Save successful. New save named " + emmaSave.fileName);
				} else {
					System.out.println("either you said no or something broke");
				}
				
				break;
			case 2:
				//System.out.println("does file exist? " + emmaSave.doesFileExist());
				break;
			case 3: // return contents of section
				
				System.out.println("current options are: ");
				SillyTools.printStrList(emmaSave.headerNames);
				int sectionChoice = SillyTools.askForNumber(userInput, emmaSave.getHeaderAmount() - 1);
				SillyTools.printStrList(emmaSave.readSection(sectionChoice - 1));
				
				break;
			case 4: //read save
				emmaSave.storeSave();
				System.out.println(emmaSave.getFileName());
				System.out.println(emmaSave.getContents());
				break;
			case 5:
				emmaSave.storeTemplate();
				System.out.println(emmaSave.getTemplateName());
				System.out.println(emmaSave.getTemplate());
				break;
			case 6:
				
				emmaSave.addItem(1, "copper", 10);
				
				break;
			case 7:
				emmaSave.addItem(0, "emma");
				
				break;
			case 8:			// THIS DOESN"T WORK for some reason but im gonna sleep	
				changeName(emmaSave, userInput);				
				break;
			case 99:
				System.out.println("goodbye");
				exitMenu = true;
				break;
			}//end options
		}//end menu loop
	}
	
	//menu text
	public static void mainMenu() {
		System.out.println("--------------------------------");
		System.out.println("Welcome to the emma save manager");
		System.out.println("--------Choose an option--------");
		System.out.println("1. Reset your current save");
		System.out.println("2. Check save status");
		System.out.println("3. View the contents of a save file section");
		System.out.println("4. Read Save");
		System.out.println("5. Read Template");
		System.out.println("6. debug");
		System.out.println("7. debug 2");
		System.out.println("8. Set player name");
		System.out.println("99. Exit");
		
	}

	public static void changeName(SaveManager m, Scanner s) {
		//get old name
		String tempName = m.getPlayerName();
		boolean ch = true;
		//get permissions
		if(tempName.isBlank()) {
			System.out.println("No previous name found.");
		} else {
			System.out.println("Your current name is " + tempName + ". Would you like to change it?");
			if(!(SillyTools.askYorN(s))) {
				ch = false;
			}
		}
		//actually do it 
		if(ch) {
			m.clearSection(0);
			
			System.out.print("Enter new name: ");
			tempName = s.nextLine();
			System.out.println();
			m.addItem(0, tempName);
			System.out.println("New name is " + m.getPlayerName());
			
		} else {
			System.out.println("You decided not to change your name.");
		}
		
		
	}
	
}
