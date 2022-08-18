import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;  
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors	

import javax.swing.*;

class DecryptVigenre
{
	
    public static void main(String args[])
	{
		Scanner in = new Scanner(System.in);

		/*
		 * Infile
		 * */
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new java.io.File(".")); // start at application current directory
        fc.setFileFilter(new OpenFileFilter("txt"));
        int returnVal = fc.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File myFile = fc.getSelectedFile();
		}

		String file = fc.getSelectedFile().getPath();
        System.out.println("You entered: " + file);

		// read path
		String s = "";
		File myObj = new File(file);
		String temp = "";
		try 
		{
		Scanner myReader = new Scanner(myObj);
		while (myReader.hasNextLine()) 
		{
			temp = temp + myReader.nextLine();
		}

		myReader.close();
		}
		catch (FileNotFoundException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
				System.exit(1);
		}  

		s = temp.toLowerCase();
		s = Filter.filterText(s, false);

		System.out.println("Enter the key");
        String key = in.nextLine();
        System.out.println("You entered: " + key);
		List<Integer> encKey = Vigener.encode(key);		

		/*
		 * Decrypting file
		 * */
		List<Integer> textLst = Vigener.encode(s);
		textLst = Vigener.decrypt(textLst, encKey);
		String textDecrypted = Vigener.decode(textLst);
		
		/*
		 *Writing to file decrypted 
		*/
		fc = new JFileChooser();
		fc.setCurrentDirectory(new java.io.File(".")); // start at application current directory
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		returnVal = fc.showSaveDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File yourFolder = fc.getSelectedFile();
		}

		String dir = fc.getSelectedFile().getName();
		Helpers.writeToFile(dir +"/"+ key + ".txt", textDecrypted);
		
		System.out.println("");
		
		System.exit(0);
	}
}

