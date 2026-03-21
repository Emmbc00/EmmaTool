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
	String storedSave; //this string will hold all of the text that was in the target file
	String template; // this string holds the text of the template file
	boolean moreInfo = true; //because why not, toggle if u want the console to spam you with info or not (kinda useless)
	ArrayList<String> headerNames = new ArrayList<String>(); //this holds the names of each header
	List<Integer> headerPos = new ArrayList<Integer>();//this holds positions for each header
	//here are the holders for each inventory section
	ArrayList<String> playernameConts = new ArrayList<String>();
	ArrayList<String> inventoryConts = new ArrayList<String>();
	ArrayList<String> notsureyetConts = new ArrayList<String>();
	ArrayList<String> mapConts = new ArrayList<String>();
	ArrayList<String> endsaveConts = new ArrayList<String>();
	//this is a list of all the lists
	ArrayList<ArrayList<String>> masterList = new ArrayList<ArrayList<String>>();
	
	
	//these are going to hold the current selections
	List<String> selectedContents = new ArrayList<String>();
	int selectedHeader;
	
	
	
	/**
	 * without any options, the default SaveManager will be using 
	 * emmasave.txt as the main file and emmasave_template.txt as the template file.
	 * If the save is empty or blank on boot, it will create a new one and fill it from a template.
	 */
	public SaveManager() {				
		startupUpdates();
		
	}
	
	/**
	 * @param constrFileName the name of the main file
	 * @param constrTempName the name of the template file 
	 */
	public SaveManager(String constrFileName, String constrTempName) {
		fileName = constrFileName;
		templateName = constrTempName;
		startupUpdates();		
	}
	


	/**
	 *  this sets the contents of <code>headerNames</code>, then updates everything else. 
	 *  for use on startup
	 */
	public void startupUpdates(){
		storeTemplate();//set the template
		headerNames.add("#PLAYERNAME");//set
		headerNames.add("#INVENTORY");//all
		headerNames.add("#NOTSUREYET");//the
		headerNames.add("#MAP");//header
		headerNames.add("#ENDSAVE");//names
								//manually because it makes everything easier
							//and i don't actually have to do it the other way
						//why would i need the save file to be dynamic lol
		//and then do everything else
		updateAll();
	}
	
	/**
	 * This adds every section to the <code>masterList</code>
	 */
	public void buildMasterList() {
		masterList.add(playernameConts);
		masterList.add(inventoryConts);
		masterList.add(notsureyetConts);
		masterList.add(mapConts);
		masterList.add(endsaveConts);
	}
	
	//these are all of the methods that update internal variables
	
	//this updates everything (template, fullSave, headerPos, 
	public void updateAll() {
		storeFromFile();//set the save file		
		updateHeaderPos();//store the position of each header 
		//store the contents of every section in each list
		updateAllContents();
		buildMasterList();
		System.out.println("post build \r\n" + masterList.toString());
	}

	
	/** this fills all of the contents lists at once
	 * 
	 */
	public void updateAllContents() {
		
		playernameConts = readSection(0);
		inventoryConts = readSection(1);
		notsureyetConts = readSection(2);
		mapConts = readSection(3);
		endsaveConts.add("#ENDSAVE");
		
	}
	
	
		
	
	//everything above this is gonna be about writing to the file
				
	
	//this reads the position of the headers, and stores them internally
	public void updateHeaderPos() {
		storeFromFile();				
		headerPos = SillyTools.locateChars(HEADER_KEY, storedSave);		
		
	}

	
	/**This method updates fullSave, and then reads a chosen section and returns it as an ArrayList
	 * @param int sectionNumber - the number of the desired category within headerNames
	 * @return ArrayList<String> - a list made up of the contents of said section
	 */
	public ArrayList<String> readSection(int sectionNumber) {
		storeFromFile();
		Scanner findName = new Scanner(storedSave);
		ArrayList<String> contents = new ArrayList<String>();
			
		//brings the scanner to the appropriate section
		SillyTools.moveScanner(findName, headerPos.get(sectionNumber));
		//adds the header to the list
		contents.add(headerNames.get(sectionNumber));
		String currentLine = findName.nextLine();
		//the while loop updates currentLine, checks if it's empty, and continues if it is not
		while(!(currentLine.isBlank())) {
			currentLine = findName.nextLine();
			if(currentLine.isBlank() != true) {
				contents.add(currentLine);
			}
		}		
		//close scanner
		findName.close();
		return contents;	
	}
	
	
	/**
	 * This looks at the save file, reads all of the data on it and saves it to fullSave as a String
	 */
	public void storeFromFile() {
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
			storedSave = sbSave.toString();
			if(moreInfo) {
				System.out.println("fullSave updated with current save file data from file named " + fileName);
			}
		} catch (FileNotFoundException e) {	 //for FileReader		
			e.printStackTrace();
		} catch (IOException e) { //for BufferedReader
			e.printStackTrace();
		} 
	
	}

	public void storeFromMaster() {
		storedSave = masterlistToString(masterList);
	}
	
	//this replaces a target section with nothing!!! is this lazy or good practice lol
	public void clearSection(int section) {
		
		storeFromFile();
		updateHeaderPos();
		(masterList.get(section)).removeAll(masterList.get(section));
		(masterList.get(section)).add(headerNames.get(section));
		storeFromMaster();
		updateSaveFile(storedSave);
	}
	
	//i love this stupid thing
	public String masterlistToString(ArrayList<ArrayList<String>> masterList) {
		StringBuilder sb = new StringBuilder();
		//iterates through the list, adding the contents of each sub list to a StringBuilder
		for(int i = 0; i < masterList.size() ; i++) {
			sb.append(SillyTools.listToString(masterList.get(i)));
			//add a line separator between each section
			sb.append(System.lineSeparator());
		}		

		return sb.toString();
		
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
		storeFromFile();
		//checking for an existing save
		if(storedSave.isBlank() != true) {
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
			storeFromFile();
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
		return storedSave;
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
