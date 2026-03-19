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

	String fileName = "emmasave.txt";
	String templateName = "emmasave_template.txt";
	String fullSave;
	String template;
	boolean moreInfo = true;
	List<String> headerNames = new ArrayList<String>();
	
	//{"#PLAYERNAME", "#MATERIALS", "#PLACEHOLDER", "#IDK"};
	
	public SaveManager() {
		//if i add more sections, add more here.
		headerNames.add("#PLAYERNAME");
		headerNames.add("#INVENTORY");
		headerNames.add("#PLACEHOLDER");
		headerNames.add("#IDK");
	}
	
	
	// don't like this, need to make add to section and remove from section methods instead
	public void changeName(String newName) { 
		StringBuilder textBuffer = new StringBuilder();
		textBuffer.append(fullSave);
		String oldName = readSection(0).get(0);
		System.out.println("old name is " + oldName);
		fullSave.replace(oldName, newName);
		System.out.println("new name is " + newName);
		System.out.println(fullSave);
		updateSaveFile(fullSave);
		
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
	
	//everything above this is gonna be about writing to the file
	
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
	
	//getters and setters yay
	public int getHeaderAmount() {
		return headerNames.size();
	}
	public String getSaveName() {
		
		return readSection(0).toString();
		
	}
	
	//-------------------- everything above this is gonna be about reading the file, below is basic file management ig?
	
	//if there is an existing save, gives the user a yes/no prompt. If yes/there is no save, creates a new one from the template
	public boolean resetSave() {
		File targetSave = new File(fileName);
		Scanner yn = new Scanner(System.in);
		//checking for an existing save
		if(targetSave.exists()) {
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
		fillFromTemplate();
		
		return true;
	}
	
	//replaces the contents of target file with the contents of the template
	public void fillFromTemplate() {
		updateTemplate();
		try(BufferedWriter tempWriter = new BufferedWriter(new FileWriter(fileName)) ){			
			tempWriter.write(template);
			storeSave();
			if(moreInfo) {				
				System.out.println("save file filled with content of " + templateName);
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
	public void updateTemplate() {
		try(BufferedReader bufferedTemplate = new BufferedReader(new FileReader(templateName))){
			StringBuilder tempSave = new StringBuilder();
			String line = bufferedTemplate.readLine();
			
			while (line != null) {
				tempSave.append(line);
				tempSave.append(System.lineSeparator());
				line = bufferedTemplate.readLine();
			}
			
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
