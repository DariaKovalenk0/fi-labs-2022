import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;  
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors	
import java.util.stream.Collectors;

import javax.swing.*;

class  ThreadTest
{  
    public static void main(String args[])
	{
	try {
		
		Scanner in = new Scanner(System.in);
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Оберіть зашифрований файл");	
		fc.setCurrentDirectory(new java.io.File(".")); // start at application current directory
        fc.setFileFilter(new OpenFileFilter("txt"));
        int returnVal = fc.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File myFile = fc.getSelectedFile();
		}
		String name = fc.getSelectedFile().getName();
		String file = fc.getSelectedFile().getPath();
		
		// read infile
		String s = "";
		File myObj = new File(file);
		String temp = "";
		try		{
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()){
				temp = temp + myReader.nextLine();
			}

			myReader.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}  

		s = temp.toLowerCase();
		s = Filter.filterText(s, false);
		List<Integer> intS = Afin.encode(s);
		List<Integer> bigrsS = Afin.encodeBigr(intS);

		/*
		 *Writing to file decrypted 
		*/
		fc = new JFileChooser();
		fc.setCurrentDirectory(new java.io.File(".")); // start at application current directory
		fc.setDialogTitle("Оберіть папку для результатів");	
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		returnVal = fc.showSaveDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File yourFolder = fc.getSelectedFile();
		}

		String dir = fc.getSelectedFile().getName();
	
		fc = new JFileChooser();
		fc.setDialogTitle("Оберіть файл зі словами російської мови (wordlist)");	
		fc.setCurrentDirectory(new java.io.File(".")); // start at application current directory
        fc.setFileFilter(new OpenFileFilter("txt"));
        returnVal = fc.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File myFile = fc.getSelectedFile();
		}
		// string to comtain the name of the wordlist
		String wlFile = fc.getSelectedFile().getPath();
		
		// read infile
		String wordlist = "";
		myObj = new File(wlFile);
		temp = "";
		try		{
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()){
				temp = temp + myReader.nextLine()+"|";
			}

			myReader.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}  

		wordlist = temp.toLowerCase();
		wordlist = "("+wordlist.substring(0, wordlist.lastIndexOf('|'))+")";			

		List<Collection<Integer>> test = Helpers.product(List.of(0, 1, 2, 3, 4), 4);
		//
		//
		
		// array of thread ot speed up the calculation
		int argSize = 25;
		AfinBreaker breaker[] = new AfinBreaker[argSize];
		for (int j = 0; j < argSize; j++) {
			List<Collection<Integer>> subTest = test.subList(25*j, 25*(j+1));
			breaker[j] = new AfinBreaker(dir, name, s, wordlist, bigrsS, subTest);
			breaker[j].start();
		}
		for (int j = 0; j < argSize; j++) {
		    breaker[j].join(); //todo add catch exception
		}
		System.exit(0);

	}
	catch (Exception e) {
				System.out.println("An error occurred.\n" + e);
		  
	}	
	}
}
