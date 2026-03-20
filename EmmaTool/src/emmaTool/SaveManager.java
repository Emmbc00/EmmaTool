package emmaTool;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;



public class SaveManager {

	char HEADER_KEY = '#';// this is the character that signifies the beginning of the header
	String fileName = "emmasave.txt"; //name of the target file
	String templateName = "emmasave_template.txt"; //name of the template
	String fullSave; //this string will hold all of the text that was in the target file
	String template; // this string holds the text of the template file
	boolean moreInfo = true; //because why not, toggle if u want the console to spam you with info or not (kinda useless)
	List<String> headerNames = new ArrayList<String>(); //this holds the names of each header
	List<Integer> headerPos = new ArrayList<Integer>();//this holds positions for each header
	//these are going to hold the current selections
	List<String> selectedContents = new ArrayList<String>();
	int selectedHeader;
	
	
	
	/**
	 * without any options, the default SaveManager will be using 
	 * emmasave.txt as the main file and emmasave_template.txt as the template file.
	 * If the save is empty or blank on boot, it will create a new one and fill it from a template.
	 */
	public SaveManager() {
		storeSave();
		storeTemplate();
		if(fullSave.isBlank()) {
			resetSave();
		}
		storeHeaderNames();
	}
	
	/**
	 * @param constrFileName the name of the main file
	 * @param constrTempName the name of the template file 
	 */
	public SaveManager(String constrFileName, String constrTempName) {
		fileName = constrFileName;
		templateName = constrTempName;
		storeSave();
		storeTemplate();
		if(fullSave.isBlank()) {
			resetSave();
		}
		storeHeaderNames();
	}
	
	

	/**
	 * This method allows you to add items into a section.
	 * unfortunately this means that i may have to store the player name as an item lmao
	 * @param section - this is the target section you're trying to add to
	 * @param name - this is the name of the item you want to add
	 * @param amount - this is the amount of the item you want to add. Set to -1 if you don't want a quantity at all.
	 */
	public void addItem(int section, String name, int amount) {
		//create a buffer that we can modify
		StringBuilder textBuffer = new StringBuilder();		
		//first, we need to update the contents of fullSave, and load them into our buffer
		//thanks to the constructor header names/numbers should already be stored.
		storeSave();
		textBuffer.append(fullSave);
		//select the proper section
		selectSection(section);
		System.out.println(headerNames.get(section) + ": " + selectedContents.toString());
		//add the wanted item to the list. if -1, don't add amount.
		if(amount == -1) {
			selectedContents.add(name);
		} else {
			selectedContents.add((name + ":" + amount));
		}
		System.out.println(selectedContents.toString());
		//put the modified selected list into a string
		StringBuilder hold = new StringBuilder();
		//add this first one so the item doesn't end up on the same line as the header
		hold.append(System.lineSeparator());
		for(int i = 0; i < selectedContents.size(); i++) {
			hold.append(selectedContents.get(i) + System.lineSeparator());
		}
		//add one last line separator
		hold.append(System.lineSeparator());
		//replace the part of the text the section is from
		textBuffer.replace(headerPos.get(section) + (headerNames.get(section).length()),
				headerPos.get(section + 1), 
				hold.toString());
		System.out.println(textBuffer.toString());
		//push through to save file
		updateSaveFile(textBuffer);
	}	
	/**
	 * @param section - target section to add to
	 * @param name - name to add
	 * if you don't put an amount, this will make it not add a number after the entry.
	 */
	public void addItem(int section, String name) {
		addItem(section, name, -1);
	}
	
	//this replaces a target section with nothing!!! is this lazy or good practice lol
	public void clearSection(int section) {
		String empty = System.lineSeparator() + System.lineSeparator();
		storeSave();
		StringBuilder textBuffer = new StringBuilder();
		textBuffer.append(fullSave);
		textBuffer = replaceSection(section, empty, textBuffer);
		updateSaveFile(textBuffer);
	}
	
	/** 
	 * @param section - section you want replaced
	 * @param contents - string you want it replaced with
	 * @param buffer - the target StringBuilder you're replacing the text inside
	 */
	public StringBuilder replaceSection(int section, String contents, StringBuilder buffer) {
		buffer.replace((headerPos.get(section) + (headerNames.get(section).length())),
				headerPos.get(section + 1), 
				contents);
		System.out.println(buffer.toString());
		return buffer;
	}
	
	//makes the content of the txt be the desired text
	public void updateSaveFile (String desiredText) {
		
		try(BufferedWriter tempWriter = new BufferedWriter(new FileWriter(fileName))) {
			tempWriter.write(desiredText);			
			if(moreInfo) {
				System.out.println("txt file text updated");	
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//you can even do it with a string builder if u want !! fuck buffer.toString()
	public void updateSaveFile(StringBuilder desText) {
		updateSaveFile(desText.toString());
	}
	
	//everything above this is gonna be about writing to the file
	
	
	
	/**
	 * this finds and records the name of each header
	 */
	public void storeHeaderNames(){
		//first, make sure we have the correct position numbers
		storeHeaderPositions();
		Scanner hFinder = new Scanner(fullSave);		
		//i'm going to assume the first category will always be at 0
		headerNames.add(hFinder.nextLine());		
		// this is going to loop the number of times there are headers (minus 1 for zero)
		for(int i = 0; i < headerPos.size() - 1; i++) {			
			//this sets the scanner to advance one char at a time
			hFinder.useDelimiter("");
			if(moreInfo) {
				System.out.println("target " + headerPos.get(i+1));
			}
			//this skips the last name entered, + 2 for each new line char? i think?
			int skippedChars = 0;
			skippedChars = (headerNames.get(i).length()) + 2;
			
			//this figures out how far to move the scanner
			int tillNext = (headerPos.get(i + 1) - headerPos.get(i) - skippedChars);
			if(moreInfo) {
				System.out.println("moving " + tillNext);
			}
			//the actual loop that moves the scanner
			SillyTools.moveScanner(hFinder, tillNext);
		/*	for(int in = 0; in < tillNext; in++) {
				System.out.print(in);
				hFinder.next();
			}			
	*/		//once the scanner is there, it reads the next line
			headerNames.add(hFinder.nextLine());			
		}		
		if(moreInfo) {
			System.out.println(headerNames.toString());
		}		
		hFinder.close();		
	}
	
	//this reads the position of the headers, and stores them internally
	public void storeHeaderPositions() {
		storeSave();				
		headerPos = SillyTools.locateChars(HEADER_KEY, fullSave);		
		
	}
	
	
	/**
	 * @param int sectionNumber - the number of the desired category within headerNames
	 * @return List<String> - a list made up of the contents of said section
	 */
	public List<String> readSection(int sectionNumber) {
		storeSave();
		Scanner findName = new Scanner(fullSave);
		List<String> contents = new ArrayList<String>();
		String currentLine;		
		//brings the scanner to the appropriate section
		findName.findWithinHorizon(headerNames.get(sectionNumber), 0);
		//skips the section name itself (not sure if needed)
		findName.nextLine();
		//the while loop updates currentLine, checks if it's empty, and continues if it is not
		while(!(currentLine = findName.nextLine()).isEmpty()) {
			contents.add(currentLine);
		}		
		//close scanner
		findName.close();
		return contents;	
	}
	
	//do these count as getters and setters
	public int getHeaderAmount() {
		return headerNames.size();
	}
	public String getSaveName() {
		
		return readSection(0).toString();
		
	}
	public void selectSection(int sectionNumber) {
		selectedHeader = sectionNumber;
		selectedContents = readSection(sectionNumber);
	}
	public String getPlayerName() {
		if(readSection(0).isEmpty()) {
			return "empty";
		}
		return readSection(0).get(0);
	}
	
	//-------------------- everything above this is gonna be about reading the file, below is basic file management ig?
	
	//if there is an existing save, gives the user a yes/no prompt. If yes/there is no save, creates a new one from the template
	public boolean resetSave() {
		Scanner yn = new Scanner(System.in);
		storeSave();
		//checking for an existing save
		if(fullSave.isBlank() != true) {
			System.out.println("You have an existing save file. Delete it?");
			if(SillyTools.askYorN(yn)) {
				deleteSave(fileName);
			} else {
				System.out.println("ok we will not delete");
				return false;
			}
		} else {//if the save doesn't exist
			System.out.println("No save data exists.");
		}
		//end of checking save state
		//save creation		
		System.out.println("Creating save file named " + fileName);
		createSave();
		System.out.println("Filling it with data from a file named " + templateName);
		storeTemplate();
		fillFromString(template);
		
		return true;
	}
	
	//replaces the contents of target file with the contents of the string
	public void fillFromString(String newInfo) {

		try(BufferedWriter tempWriter = new BufferedWriter(new FileWriter(fileName)) ){			
			tempWriter.write(newInfo);
			storeSave();
			if(moreInfo) {
				System.out.println(fileName + " filled with the contents of given string");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
		
	
	//creates a new save file
	public void createSave(){
		File newSave = new File(fileName);
		try {// there might be a failure making a file ig
			if(newSave.createNewFile()) {
				System.out.println("file successfully created");
			}
		} catch (IOException e) {
			System.out.println("failure making new save. maybe one already exists?");
		}
	}
	
	//deletes a file with targetName
	public void deleteSave(String targetName) {
		File target = new File(targetName);
		if(target.delete()) {
			System.out.println("deleted!");
		} else {
			System.out.println("failed?");
		}
	}
	
	/**
	 * This looks at the save file, reads all of the data on it and saves it to fullSave as a String
	 */
	public void storeSave() {
		//ok uhh this isn't really copied but I did look at an example for help
		try(BufferedReader bufferedSave = new BufferedReader(new FileReader(fileName))) {
			StringBuilder sbSave = new StringBuilder();
			String line = bufferedSave.readLine();
			//iterate through lines until the current line is empty. add each line to the StringBuilder as you go.
			while (line != null) {
				sbSave.append(line);
				sbSave.append(System.lineSeparator());
				line = bufferedSave.readLine();
			}
			//save to fullSave
			fullSave = sbSave.toString();
			if(moreInfo) {
				System.out.println("fullSave updated with current save file data from file named " + fileName);
			}
		} catch (FileNotFoundException e) {	 //for FileReader		
			e.printStackTrace();
		} catch (IOException e) { //for BufferedReader
			e.printStackTrace();
		} 

	}
	
	/**
	 * This reads the template file, and syncs it with the internal template
	 */
	public void storeTemplate() {
		try(BufferedReader bufferedTemplate = new BufferedReader(new FileReader(templateName))){
			StringBuilder tempSave = new StringBuilder();
			String line = bufferedTemplate.readLine();
			//this iterates through each line of the template, and adds them to the StringBuilder
			while (line != null) {
				tempSave.append(line);
				tempSave.append(System.lineSeparator());
				line = bufferedTemplate.readLine();
			}
			//the StringBuilder is saved as a string
			template = tempSave.toString();
			if(moreInfo = true) {
				System.out.println("template saved from template file named " + templateName);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//getters and setters	
	//file name
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fn) {
		fileName = fn;
	}
	//template name
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String tn) {
		templateName = tn;
	}
	//contents of save
	public String getContents() {
		return fullSave;
	}
	//contents of template
	public String getTemplate() {
		return template;
	}
	//more info during loads get/set/toggle
	public boolean getMoreInfo() {
		return moreInfo;
	}
	public void setMoreInfo(boolean info) {
		moreInfo = info;
	}
	public void toggleMoreInfo() {
		moreInfo = SillyTools.invert(moreInfo);
	}
	
	
	
	
	
	
	
	
	
	
}
