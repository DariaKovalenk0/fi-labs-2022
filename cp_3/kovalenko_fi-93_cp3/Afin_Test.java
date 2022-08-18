import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;  
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors	
import java.util.stream.Collectors;

import javax.swing.*;

class  Afin_Test
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

		// System.out.println(s);
		// System.out.println(intS);
		// System.out.println(bigrsS);

		
		System.out.println("Enter the a");
        int a = in.nextInt();
        System.out.println("You entered: " + a);
		
		System.out.println("Enter the b");
        int b = in.nextInt();
        System.out.println("You entered: " + b);

		List<Integer> res = Afin.encode(s);
		
		// System.out.println("Закодована строка:\t\t" + res);

		List<Integer> bigrs = Afin.encodeBigr(res);

		// System.out.println("Біграми:\t\t\t" + bigrs);
		
		// System.out.println("Перевірка закодованої строки:\t" + Afin.decodeBigr(bigrs));
		
		List<Integer> enc = Afin.encrypt(bigrs, a, b);

		// System.out.println("Зашифрована строка:\t\t" + enc);
		// System.out.println("Decoded:\t\t\t" + Afin.decodeBigr(enc));
		// System.out.println("String:\t\t\t" + Afin.decode(Afin.decodeBigr(enc)));
		
		// List<Integer> dec = Afin.decrypt(enc, a, b);
		// System.out.println("Розшифрована строка:\t\t" + dec);
		// System.out.println("Decoded:\t\t\t" + Afin.decodeBigr(dec));
		// System.out.println("String:\t\t\t" + Afin.decode(Afin.decodeBigr(dec)));
		
		String encryptedStr = Afin.decode(Afin.decodeBigr(enc));
		String decryptedStr = Afin.decode(Afin.decodeBigr(Afin.decrypt(enc, a, b)));
		//System.out.println(encryptedStr);


		/*
		 *Writing to file decrypted 
		*/
		fc = new JFileChooser();
		fc.setDialogTitle("Оберіть папку для результатів");	
		fc.setCurrentDirectory(new java.io.File(".")); // start at application current directory
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		returnVal = fc.showSaveDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File yourFolder = fc.getSelectedFile();
		}

		String dir = fc.getSelectedFile().getName();
		
		System.out.println(dir  +"/encrypted_"+name+ "_" + Integer.toString(a)+"_"+ Integer.toString(b) + ".txt");
		Helpers.writeToFile(dir +"/encrypted_"+name+ "_" + Integer.toString(a)+"_"+ Integer.toString(b) + ".txt", encryptedStr);
		
		System.out.println(dir  +"/decrypted_"+name+ "_" + Integer.toString(a)+"_"+ Integer.toString(b) + ".txt");
		Helpers.writeToFile(dir +"/decrypted_"+name+ "_" + Integer.toString(a)+"_"+ Integer.toString(b) + ".txt", decryptedStr);

		System.exit(0);

	}
	catch (Exception e) {
				System.out.println("An error occurred.\n" + e);
		  
	}	
	}
}
