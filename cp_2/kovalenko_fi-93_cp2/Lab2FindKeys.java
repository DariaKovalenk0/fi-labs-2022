import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;  
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors	

import javax.swing.*;

class FindKeys
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
		}  

		s = temp.toLowerCase();
		s = Filter.filterText(s, false);


		List<String> sp  = Helpers.split(s);
		Map <String, Float> fr = Helpers.setCounts(sp);

		List<List<String>> test = new ArrayList<List<String>>();
		List<Integer> valid = new ArrayList<Integer>();

		for (int n = 2; n < 32; n++)
		{
			test = Helpers.splitNth(sp, n);
			// System.out.println(test);
			double averIndex = Helpers.calcCI(test, n);
			
			System.out.println("Довжина : " + n);
			System.out.println("Індекс Відповідності : " + averIndex + "");
			System.out.println("");
			
			if (averIndex >= 0.05)
			{
				valid.add(n);
			}
		}
		

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

		for (int n : valid)
		{
			System.out.println("Довжина : " + n);
			test = Helpers.splitNth(sp, n);
			List <String> res = Helpers.returnFrequent(test);

			List <String> common = Arrays.asList("о","а","е","и","н");
			List <Integer> commonEnc = Vigener.encode(String.join("",common));
			List <Integer> encoded = Vigener.encode(String.join("",res));

			for (int c : commonEnc)
			{
				List <Integer> key = Arrays.asList(c);
				List<Integer> decKey = Vigener.decrypt(encoded, key);
				String decodedKey = Vigener.decode(decKey);
				System.out.println("Можливий ключ : " + decodedKey);
				String filename = dir + "/"+Integer.toString(n)+"_"+decodedKey+".txt";
				
				/*
				 * Decrypting file
				 * */
				List<Integer> textLst = Vigener.encode(s);
				textLst = Vigener.decrypt(textLst, decKey);
				String textDecrypted = Vigener.decode(textLst);
				
				/*
				 *Writing to file decrypted 
				*/
				Helpers.writeToFile(filename, textDecrypted);
				
			}
			System.out.println("");
		}
	}
}

