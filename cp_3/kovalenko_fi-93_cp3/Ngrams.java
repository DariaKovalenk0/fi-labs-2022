import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;  
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors	

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

	public Map<String, Float> getNgrams(){
		return this.ngrams;
	}

	public List<String> getFrequentNgrams(){
		
		Set<String> keySet = this.ngrams.keySet();
		List<String> res = new ArrayList(keySet).subList(0,5);
		return res;
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
		for (int i = 1; i < length; i+= 2)
		{	
			String prev = Character.toString(str.charAt(i-1));
			String currStr = prev + Character.toString(str.charAt(i));
			list.add(currStr);
		}
	return list;
	}
}
