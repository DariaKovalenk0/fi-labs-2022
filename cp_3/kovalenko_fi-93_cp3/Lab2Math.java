import java.util.*;
import java.util.stream.Collectors;

class Vigener {

	public static List<Integer> encode(String text){
	List<Integer> ints = text.chars().mapToObj(e -> (int)e).collect(Collectors.toList());
	ints = ints.stream().map( Vigener::encodeSingle ).collect( Collectors.toList() );
	return ints;
	}

	private static int encodeSingle(int chr){
	if (0x0430 <= chr && chr <= 0x44f){
		return chr - 0x430;
		}
	return chr;
	}

	public static String decode(List<Integer> ints){

	ints = ints.stream().map( Vigener::decodeSingle ).collect( Collectors.toList() );
	List<Character> text = ints.stream().map( e -> Vigener.chrToUtf(e)).collect(Collectors.toList());

	return text.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(""));
	}

	private static char chrToUtf(int chr){ return (char) chr; }

	private static int decodeSingle(int chr){
	if (0 <= chr && chr <= 32){
		return chr + 0x430;
	}
	return chr;
	}

	public static List<Integer> encrypt(List<Integer> ints, List<Integer> keys){
		
		List<Integer> result = new ArrayList();
		for (int i = 0; i < ints.size(); i++)
		{
			int chr = ints.get(i);
			int key = keys.get(i % keys.size()); 
			result.add(encryptSinge(chr, key));
		}

		return result;
	}

	private static int encryptSinge(int chr, int key){
	return (chr + key) % 32;
	}

	public static List<Integer> decrypt(List<Integer> ints, List<Integer> keys){
		
		List<Integer> result = new ArrayList();
		for (int i = 0; i < ints.size(); i++)
		{
			int chr = ints.get(i);
			int key = keys.get(i % keys.size()); 
			result.add(decryptSinge(chr, key));
		}

		return result;
	}
	
	private static int decryptSinge(int chr, int key){
	int temp = chr - key;
	if (temp <= 0){
		temp = temp + 32;
	}
	return (temp) % 32;
	}
}


class CI
{
	public static float c_i(Map<String, Float> ngrams)
	{
		
		List<Float> vals = new ArrayList<Float>(ngrams.values());
		int len = 0;
        for (Float i : vals)
            len += i;

		float index = 0;
		double curr  = 0.0;

        for (Map.Entry<String, Float> entry : ngrams.entrySet()) 
		{
            // System.out.println("Key : " + entry.getKey()
            //         + " Value : " + entry.getValue());
			curr = entry.getValue();
			index += curr * (curr - 1);
        }

		index /= len* (len -1 );
		return (float) index;
	}
}
