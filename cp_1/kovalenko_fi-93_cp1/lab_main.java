import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;  
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors	
 
class  Lab1App
{  
    public static void main(String args[])
	{
	try {
		
		System.out.println("\n\n\tThis program requires\n\t'./resourses' diretory\n\tto work properly\n\n");

		// Get file path
		Scanner in = new Scanner(System.in);

		System.out.println("Enter the path");
        String a = in.nextLine();
        System.out.println("You entered: " + a);
		
		// read path
		String data = "";
		File myObj = new File(a);
		String temp = "";
		Scanner myReader = new Scanner(myObj);
		while (myReader.hasNextLine()) 
		{
			temp = temp + myReader.nextLine();
		}

		myReader.close();
		data = temp.toLowerCase();
		
		String text;
        
		System.out.println("Enter outFile name");
        String filename = in.nextLine();
        System.out.println("You entered: " + filename);
		
		// true whitespaces, false no whitespaces
		
		// Monograms	
        System.out.println("Монограми з пробілами");
		text =  Filter.filterText(data, true); // whitespaces	
		Ngrams mongr = new Ngrams(text, 1); // monograms
		Helpers.writeToFile("./resourses/", filename + "_mon_white.txt", mongr);

        System.out.println("Монограми без пробілів");
		text =  Filter.filterText(data, false); // no whitespaces	
		mongr = new Ngrams(text, 1); // monograms
		Helpers.writeToFile("./resourses/", filename + "_mon_non_white.txt", mongr);

		// Bigrams
        System.out.println("Біграми що не пересікаються, з пробілами");
		text =  Filter.filterText(data, true); // whitespaces	
		BigrNoCross bWNC = new BigrNoCross(text, 2); // bigrams no crossing
		Helpers.writeToFile("./resourses/", filename + "_bigr_white_no_cross.txt", bWNC);

        System.out.println("Біграми що не пересікаються, без пробілів");
		text =  Filter.filterText(data, false); // no whitespaces	
		BigrNoCross bNWNC = new BigrNoCross(text, 2); // bigr no cross
		Helpers.writeToFile("./resourses/", filename + "_bigr_no_w_no_cross.txt", bNWNC);

        System.out.println("Біграми що пересікаються, з пробілами");
		text =  Filter.filterText(data, true); // whitespaces	
		BigrCross bWC = new BigrCross(text, 2); // bigr cross
		Helpers.writeToFile("./resourses/", filename + "_bigr_w_cross.txt", bWC);

        System.out.println("Біграми що пересікаються, без пробілів");
		text =  Filter.filterText(data, false); // no whitespaces	
		BigrCross bNWC = new BigrCross(text, 2); // bigr cross
		Helpers.writeToFile("./resourses/", filename + "_bigr_no_w_cross.txt", bNWC);

		}

	catch (FileNotFoundException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
		}  
	}
}

class Ngrams
{
	Map<String, Float> ngrams = new LinkedHashMap<String, Float>();
	float entropy, redundancy;

	Ngrams(String str, int n)
	{
		List<String> ngrams = this.split(str);
		this.setFreqs(ngrams);

		List<Float> freqs = new ArrayList<Float>(this.ngrams.values());
		this.entropy = lab1Maths.entropy(freqs, n);
		this.redundancy = lab1Maths.redundancy(this.entropy);
	}


	private List<String> split(String str)
	{
		List<String> list = new ArrayList();

		for (int i = 0; i < str.length(); i++)
		{
			String currStr = Character.toString(str.charAt(i));
			list.add(currStr);
		}
	return list;
	}

	protected static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> unsortMap) {

        List<Map.Entry<K, V>> list =
                new LinkedList<Map.Entry<K, V>>(unsortMap.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
	}

	protected void setFreqs(List<String> ngrams)
	{
		Map<String, Float> unsorted = new HashMap<String, Float>();
		HashSet<String> uniques = new HashSet<>(ngrams);
		for(String ngram: uniques)
		{
			float occurences = Collections.frequency(ngrams, ngram);
			float frequency = occurences / ngrams.size();
			unsorted.put(ngram, frequency);
		}
	this.ngrams = sortByValue(unsorted);
	}

	public float getEntropy()
	{
		return this.entropy;
	}


	public float getRedundancy()
	{
		return this.redundancy;
	}

}

class BigrNoCross extends Ngrams
{
	BigrNoCross(String str, int n)
	{
		super(str, n);
		List<String> ngrams = this.split(str);
		this.setFreqs(ngrams);

		List<Float> freqs = new ArrayList<Float>(this.ngrams.values());
		this.entropy = lab1Maths.entropy(freqs, n);
		this.redundancy = lab1Maths.redundancy(this.entropy);
	}

	private List<String> split(String str)
	{
		List<String> list = new ArrayList();
		int length = str.length();
		if (Helpers.getParity(length))
			length--;
		for (int i = 1; i < length; i+= 2)
		{	
			String prev = Character.toString(str.charAt(i-1));
			String currStr = prev + Character.toString(str.charAt(i));
			list.add(currStr);
		}
	return list;
	}
}

class BigrCross extends Ngrams
{
	BigrCross(String str, int n)
	{
		super(str, n);
		List<String> ngrams = this.split(str);
		this.setFreqs(ngrams);

		List<Float> freqs = new ArrayList<Float>(this.ngrams.values());
		this.entropy = lab1Maths.entropy(freqs, n);
		this.redundancy = lab1Maths.redundancy(this.entropy);
	}

	private List<String> split(String str)
	{
		List<String> list = new ArrayList();
		int length = str.length();
		if (Helpers.getParity(length))
			length--;
		for (int i = 1; i < length; i+= 1)
		{	
			String prev = Character.toString(str.charAt(i-1));
			String currStr = prev + Character.toString(str.charAt(i));
			list.add(currStr);
		}
	return list;
	}
}

class Helpers
{	
	// prints array
	public static <K, V> void printMap(Map<K, V> map) 
	{
        for (Map.Entry<K, V> entry : map.entrySet()) 
		{
            System.out.println("Key : " + entry.getKey()
                    + " Value : " + entry.getValue());
        }
	}
	/* Function to get parity of number n.
    It returns 1 if n has odd parity, and
    returns 0 if n has even parity */
    static boolean getParity(int n)
    {
        boolean parity = false;
        while(n != 0)
        {
            parity = !parity;
            n = n & (n-1);
        }
        return parity;
         
    }

	public static void writeToFile(String dirPath, String name, Ngrams data)
	{
		try {
		String path = dirPath+name;
		File myObj = new File(path);
		if (myObj.createNewFile()) {
        System.out.println("File created: " + myObj.getName());
		} 
		else {
        System.out.println("File already exists.");
		}
	
		FileWriter myWriter = new FileWriter(path);
		
		float entr = data.getEntropy();
        System.out.println("Data entropy: {" + entr + "}"+ System.lineSeparator());
		
		float red = data.getRedundancy();
        System.out.println("Data redundancy: {" + red + "}"+ System.lineSeparator());

		myWriter.write("ngram,value"+ System.lineSeparator());
		data.ngrams.forEach((key, value) -> {
			try {
				myWriter.write(key + "," + value + System.lineSeparator());
			} catch (IOException e) {
			  System.out.println("An error occurred.");
			}
		});

		myWriter.close();
	    } catch (IOException e) {
			  System.out.println("An error occurred.");
			 e.printStackTrace();
		}
	}

	public static void writeToFile(String dirPath, String name, BigrNoCross data)
	{
		try {
		String path = dirPath+name;
		File myObj = new File(path);
		if (myObj.createNewFile()) {
        System.out.println("File created: " + myObj.getName());
		} 
		else {
        System.out.println("File already exists.");
		}
	
		FileWriter myWriter = new FileWriter(path);
		float entr = data.getEntropy();
        System.out.println("Data entropy: {" + entr + "}"+ System.lineSeparator());
		
		float red = data.getRedundancy();
        System.out.println("Data redundancy: {" + red + "}"+ System.lineSeparator());

		myWriter.write("ngram,value"+ System.lineSeparator());
		data.ngrams.forEach((key, value) -> {
			try {
				myWriter.write(key + "," + value + System.lineSeparator());
			} catch (IOException e) {
			  System.out.println("An error occurred.");
			}
		});
		
		myWriter.close();
	    } catch (IOException e) {
			  System.out.println("An error occurred.");
			 e.printStackTrace();
		}
	}

	public static void writeToFile(String dirPath, String name, BigrCross data)
	{
		try {
		String path = dirPath+name;
		File myObj = new File(path);
		if (myObj.createNewFile()) {
        System.out.println("File created: " + myObj.getName());
		} 
		else {
        System.out.println("File already exists.");
		}
	
		FileWriter myWriter = new FileWriter(path);
		
		float entr = data.getEntropy();
        System.out.println("Data entropy: {" + entr + "}"+ System.lineSeparator());
		
		float red = data.getRedundancy();
        System.out.println("Data redundancy: {" + red + "}"+ System.lineSeparator());

		myWriter.write("ngram,value"+ System.lineSeparator());
		
		data.ngrams.forEach((key, value) -> {
			try {
				myWriter.write(key + "," + value + System.lineSeparator());
			} catch (IOException e) {
			  System.out.println("An error occurred.");
			}
		});
		
		myWriter.close();
	    } catch (IOException e) {
			  System.out.println("An error occurred.");
			 e.printStackTrace();
		}
	}
}

class Filter
{	
	/* CYRILLIC alpahbet in unicode 
	 *	0410	А	CYRILLIC CAPITAL LETTER A					0430	а	CYRILLIC SMALL LETTER A	 
	 *	0411	Б	CYRILLIC CAPITAL LETTER BE					0431	б	CYRILLIC SMALL LETTER BE	 
	 *	0412	В	CYRILLIC CAPITAL LETTER VE					0432	в	CYRILLIC SMALL LETTER VE	 
	 *	0413	Г	CYRILLIC CAPITAL LETTER GHE					0433	г	CYRILLIC SMALL LETTER GHE	 
	 *	0414	Д	CYRILLIC CAPITAL LETTER DE					0434	д	CYRILLIC SMALL LETTER DE	 
	 *	0415	Е	CYRILLIC CAPITAL LETTER IE					0435	е	CYRILLIC SMALL LETTER IE	 
	 *	0416	Ж	CYRILLIC CAPITAL LETTER ZHE					0436	ж	CYRILLIC SMALL LETTER ZHE	 
	 *	0417	З	CYRILLIC CAPITAL LETTER ZE					0437	з	CYRILLIC SMALL LETTER ZE	 
	 *	0418	И	CYRILLIC CAPITAL LETTER I					0438	и	CYRILLIC SMALL LETTER I	 
	 *	0419	Й	CYRILLIC CAPITAL LETTER SHORT I	0418 0306	0439	й	CYRILLIC SMALL LETTER SHORT I	0438 0306	 
	 *	041A	К	CYRILLIC CAPITAL LETTER KA					043A	к	CYRILLIC SMALL LETTER KA	 
	 *	041B	Л	CYRlab1ILLIC CAPITAL LETTER EL				043B	л	CYRILLIC SMALL LETTER EL	 
	 *	041C	М	CYRILLIC CAPITAL LETTER EM					043C	м	CYRILLIC SMALL LETTER EM	 
	 *	041D	Н	CYRILLIC CAPITAL LETTER EN					043D	н	CYRILLIC SMALL LETTER EN	 
	 *	041E	О	CYRILLIC CAPITAL LETTER O					043E	о	CYRILLIC SMALL LETTER O	 
	 *	041F	П	CYRILLIC CAPITAL LETTER PE					043F	п	CYRILLIC SMALL LETTER PE	 
	 *	0420	Р	CYRILLIC CAPITAL LETTER ER					0440	р	CYRILLIC SMALL LETTER ER	 
	 *	0421	С	CYRILLIC CAPITAL LETTER ES					0441	с	CYRILLIC SMALL LETTER ES	 
	 *	0422	Т	CYRILLIC CAPITAL LETTER TE					0442	т	CYRILLIC SMALL LETTER TE	 
	 *	0423	У	CYRILLIC CAPITAL LETTER U					0443	у	CYRILLIC SMALL LETTER U	 
	 *	0424	Ф	CYRILLIC CAPITAL LETTER EF					0444	ф	CYRILLIC SMALL LETTER EF	 
	 *	0425	Х	CYRILLIC CAPITAL LETTER HA					0445	х	CYRILLIC SMALL LETTER HA	 
/home/walltime/Coding/java/lab_1/resourses/utf_ test_text.txt
	 *	0426	Ц	CYRILLIC CAPITAL LETTER TSE					0446	ц	CYRILLIC SMALL LETTER TSE	 
	 *	0427	Ч	CYRILLIC CAPITAL LETTER CHE					0447	ч	CYRILLIC SMALL LETTER CHE	 
	 *	0428	Ш	CYRILLIC CAPITAL LETTER SHA					0448	ш	CYRILLIC SMALL LETTER SHA	 
	 *	0429	Щ	CYRILLIC CAPITAL LETTER SHCHA				0449	щ	CYRILLIC SMALL LETTER SHCHA	 
	 *	042A	Ъ	CYRILLIC CAPITAL LETTER HARD SIGN			044A	ъ	CYRILLIC SMALL LETTER HARD SIGN	 
	 *	042B	Ы	CYRILLIC CAPITAL LETTER YERU				044B	ы	CYRILLIC SMALL LETTER YERU	 
	 *	042C	Ь	CYRILLIC CAPITAL LETTER SOFT SIGN			044C	ь	CYRILLIC SMALL LETTER SOFT SIGN	 
	 *	042D	Э	CYRILLIC CAPITAL LETTER E					044D	э	CYRILLIC SMALL LETTER E	 
	 *	042E	Ю	CYRILLIC CAPITAL LETTER YU					044E	ю	CYRILLIC SMALL LETTER YU	 
	 *	042F	Я	CYRILLIC CAPITAL LETTER YA					044F	я	CYRILLIC SMALL LETTER YA
	 *	0401	Ё	CYRILLIC CAPITAL LETTER IO	0415 0308		0451	ё	CYRILLIC SMALL LETTER IO	0435 0308
	 *	0020	' '	WHITESPACE 
	*/

	public static String filterText(String str, boolean whitespaces)
	{	
		try	{
			String temp = str.replaceAll("\u044A", "\u044C");	//  ъ на ь
			temp = temp.replaceAll("\u0451", "\u0435");			//	ё на е
			if (whitespaces)
				return temp.replaceAll("[^\u0430-\u044f\u0020]", "");
			else if (whitespaces == false)
				return temp.replaceAll("[^\u0430-\u044f]", "");
		} 
		catch (InputMismatchException e) {
			System.out.println("Invalid input!");
		}
		System.exit(-1);
		return "";
	}
}

class lab1Maths
{
	public static float entropy(List<Float> freqs, int n)
	{
		List<Double> temp = new ArrayList<Double>();

		for (float e: freqs)
		{
			temp.add(elem_entr(e, n));
		}

		double entropy =  -1.0 * temp.stream().mapToDouble(i -> i).sum();
		
		return (float) entropy;
	}

	public static float redundancy(float e)
	{
		double temp = 1 - e / (Math.log(32) / Math.log(2));
		return (float)temp;
	}

	private static double elem_entr(float e, int n)
	{
		double temp = e * ((Math.log(e) / Math.log(2)) / (Math.log(n*2) / Math.log(2)) );
		return Math.round(temp * 10000) / 10000d; // round to 5
	}

}
