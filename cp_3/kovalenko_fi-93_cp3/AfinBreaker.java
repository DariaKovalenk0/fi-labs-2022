import java.util.*;
import java.util.stream.Collectors;
import java.lang.Math;
import java.lang.Thread;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AfinBreaker extends Thread{


	private List<Integer> common = Afin.encodeBigr(Afin.encode("стнотонаен"));
	private int alphLen = 31;
	
	private String dir, name;
	private String words;

	// all bigrams, not sorted not counted
	private List<Integer> bigrs;
	private List<List<Integer>> keys;

	//counted & sorted
	private List<Integer> frequent;
		
	public AfinBreaker(String dir, String name, String rawText, String words, List<Integer> bigrs, List<Collection<Integer>> combs){
		this.name = name;
		if(this.name.contains("."))
			this.name = this.name.substring(0, name.lastIndexOf('.'));
		this.dir = dir;
		this.bigrs = bigrs;

		BigrNoCross bigrsNoCross = new BigrNoCross(rawText, 2);
		List<String> freqStrLst = bigrsNoCross.getFrequentNgrams();
		String freqStr = String.join("", freqStrLst);
		this.frequent = Afin.encodeBigr(Afin.encode(freqStr));
		
		this.keys=createKeys(combs);
		this.words = words;

		

	}
	
	public void run(){
		for (List <Integer> key: keys){
			int a = key.get(0);
			int b = key.get(1);
			String decryptedStr = "";
			decryptedStr = Afin.decode(Afin.decodeBigr(Afin.decrypt(bigrs, a, b)));

			
			//System.out.println(dir  +"/decrypted_"+name+ "_" + Integer.toString(a)+"_"+ Integer.toString(b) + ".txt");
			if (checkText(decryptedStr, a, b))
				Helpers.writeToFile(dir +"/decrypted_"+name+ "_" + Integer.toString(a)+"_"+ Integer.toString(b) + ".txt", decryptedStr);
		}
	}

	private List<List<Integer>> createKeys(List<Collection<Integer>> combs){
		List<List<Integer>> res = new ArrayList();
		for(Collection comb: combs)
			{
				List<Integer> combL = new ArrayList(comb); 
				int x1 = common.get(combL.get(0));
				int x2 = common.get(combL.get(1));
				int y1 = frequent.get(combL.get(2));
				int y2 = frequent.get(combL.get(3));
				
				//System.out.println(x1 + " - " + x2);
				//System.out.println(y1 + " - " + y2);
				
				int open = Lab3Math.mod(x1 - x2, alphLen*alphLen);
				int cryp = Lab3Math.mod(y1 - y2, alphLen*alphLen);

				if (open == 0 && cryp == 0)
					continue;

				List<Integer> solvs = Lab3Math.LinComp(open, cryp, alphLen*alphLen);
				//System.out.println(open + " " + cryp + " " + solvs);
				if (solvs != null && !solvs.isEmpty()) {
					for (int a: solvs){
						int b = Lab3Math.mod(y1 - a * x1, alphLen*alphLen);
						List<Integer> temp = new ArrayList<Integer>();
						temp.add(a);
						temp.add(b);

						if (!res.contains(temp))
							res.add(temp);
					}
				}
			}
		return res;
	}

	private boolean checkText(String text, int a, int b){

		String regex = this.words;

        String string = text;
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(string);
        
		// all matches
		double matches = 0;
		while (matcher.find()) {
			matcher.start();
            matcher.end();
            matches++;
        }

		// all words in wordlist
		List<String> lst = new ArrayList<String>(Arrays.asList(this.words.split("\\|")));
		int length = lst.size();

		if (matches > 20){
			System.out.println("a: " + a + " b: " + b + " :: " +matches);
		//	System.out.println("a: " + a + " b: " + b + " :: " +String.format("%.2g", matches/length*100)+"%;");
			return true;
		}
		return false;

	}
}
