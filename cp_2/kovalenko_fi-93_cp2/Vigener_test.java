import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;  
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors	
import java.util.stream.Collectors;

class  Vigener_Test
{  
    public static void main(String args[])
	{
	try {
		
		//Scanner in = new Scanner(System.in);

		System.out.println("Введіть строку для шифурування");
        String raw = "добредобре".toLowerCase(); //in.nextLine();
        System.out.println("You entered: " + raw);
		
        String rawKey = "ыьэюя";
        System.out.println("You entered: " + rawKey);

		List<Integer> res = Vigener.encode(raw);
		List<Integer> key = Vigener.encode(rawKey);
		
		//int res = Vigener.encodeSingle(raw);

		System.out.println("Закодована строка: " + res);
		System.out.println("Закодований  ключ: " + key);

		List<Integer> enc = Vigener.encrypt(res, key);

		System.out.println("Закодована строка: " + enc);
		System.out.println("Reverse: " + Vigener.decode(enc));
		
		List<Integer> dec = Vigener.decrypt(enc, key);
		System.out.println("Закодована строка: " + dec);
		System.out.println("Reverse: " + Vigener.decode(dec));
	}
	catch (Exception e) {
				System.out.println("An error occurred.\n" + e);
		}  
	
	}
}
