import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;  
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors	

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

	public static void writeToFile(String path, String decrypted)
	{
		try {
		// System.out.println("Filename: " + path);
		File myObj = new File(path);
		if (myObj.createNewFile()) {
        System.out.println("File created: " + myObj.getName());
		} 
		else {
        System.out.println("File already exists.");
		}
	
		FileWriter myWriter = new FileWriter(path);
		
		myWriter.write(decrypted);
		myWriter.close();
	    } catch (IOException e) {
			  System.out.println("An error while opening the file occurred.");
			 e.printStackTrace();
		}
	}

	public static <T extends Ngrams> void writeToFile(String dirPath, String name, T data)
	{
		try {
		// System.out.println("Filename: " + dirPath+name);
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
			  System.out.println("An error while writing ot the file occurred.");
			}
		});

		myWriter.close();
	    } catch (IOException e) {
			  System.out.println("An error while opening the file occurred.");
			 e.printStackTrace();
		}
	}

	/**
	 * taken from the ngrams
	 * */
	public static List<String> split(String str)
	{
		List<String> list = new ArrayList();

		for (int i = 0; i < str.length(); i++)
		{
			String currStr = Character.toString(str.charAt(i));
			list.add(currStr);
		}
	return list;
	}

	private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> unsortMap) {

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

	/**
	 * Повертає словник з унікальних символів та частотою їх появ у строці
	 * */
	public static Map<String, Float> setFreqs(List<String> ngrams)
	{
		Map<String, Float> unsorted = new HashMap<String, Float>();
		HashSet<String> uniques = new HashSet<>(ngrams);
		for(String ngram: uniques)
		{
			float occurences = Collections.frequency(ngrams, ngram);
			float frequency = occurences / ngrams.size();
			unsorted.put(ngram, frequency);
		}
	return sortByValue(unsorted);
	}
	/**
	 * Needed for the conformity indexes
	 * Повертає словник з унікальних символів та кількістю їх появ у строці
	 * */
	public static Map<String, Float> setCounts(List<String> ngrams)
	{
		Map<String, Float> unsorted = new HashMap<String, Float>();
		HashSet<String> uniques = new HashSet<>(ngrams);
		for(String ngram: uniques)
		{
			float occurences = Collections.frequency(ngrams, ngram);
			unsorted.put(ngram, occurences);
		}
	return sortByValue(unsorted);
	}

	/**
	 * Розділяє строку на Н списків
	 * */
	public static List<List<String>> splitNth(String text, int n)
	{
		List<List<String>> lists = new ArrayList<List<String>>();
		for (int i = 0; i < n; i++)
		{
			List<String> str = new ArrayList<String>();
			for (int j = i; j < text.length(); j += n){
				str.add(Character.toString(text.charAt(j)));
			}
			lists.add(str);
		}
		return lists;
	}

	/**
	 * Ррозділя є список на Н списків
	 * */
	public static List<List<String>> splitNth(List<String> text, int n)
	{
		List<List<String>> lists = new ArrayList<List<String>>();
		for (int i = 0; i < n; i++)
		{
			List<String> str = new ArrayList<String>();
			for (int j = i; j < text.size(); j += n){
				str.add(text.get(j));
			}
			lists.add(str);
		}
		return lists;
	}

	public static double calcCI(List<List<String>> lists, int n)
	{
		double index = 0.0;
		double sum = 0.0;
		int i = 0;
		for (List<String> lst : lists)
		{
			i++;	
			Map <String, Float> fr = setCounts(lst);
			index = CI.c_i(fr);
			System.out.println( "\tБлок {"+i+"}\t"+index);
			sum += index;
		}
		
		return sum/n;
	}

	public static List<String> returnFrequent(List<List<String>> lists)
	{
		List <String> res = new ArrayList<>();

		for (List<String> lst : lists)
		{

			Map <String, Float> fr = setFreqs(lst);
			Map.Entry<String, Float> entry = fr.entrySet().iterator().next();
			res.add(entry.getKey());
		}

		return res;
	}

	/***
	 * copy of itertools product
	 * */

	public static <T> List<Collection<T>> product(Collection<T> a, int r) {
        List<Collection<T>> result = Collections.nCopies(1, Collections.emptyList());
        for (Collection<T> pool : Collections.nCopies(r, new LinkedHashSet<>(a))) {
            List<Collection<T>> temp = new ArrayList<>();
            for (Collection<T> x : result) {
                for (T y : pool) {
                    Collection<T> z = new ArrayList<>(x);
                    z.add(y);
                    temp.add(z);
                }
            }
            result = temp;
        }
        return result;
    }
}
