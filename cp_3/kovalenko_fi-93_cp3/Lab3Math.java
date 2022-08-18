import java.util.*;
import java.util.stream.Collectors;
import java.lang.Math;
import java.lang.Thread;
/*
  а::1072 0; hex:: 0x430 0x0
  б::1073 1; hex:: 0x431 0x1
  в::1074 2; hex:: 0x432 0x2
  г::1075 3; hex:: 0x433 0x3
  д::1076 4; hex:: 0x434 0x4
  е::1077 5; hex:: 0x435 0x5
  ж::1078 6; hex:: 0x436 0x6
  з::1079 7; hex:: 0x437 0x7
  и::1080 8; hex:: 0x438 0x8
  й::1081 9; hex:: 0x439 0x9
  к::1082 10; hex:: 0x43a 0xa
  л::1083 11; hex:: 0x43b 0xb
  м::1084 12; hex:: 0x43c 0xc
  н::1085 13; hex:: 0x43d 0xd
  о::1086 14; hex:: 0x43e 0xe
  п::1087 15; hex:: 0x43f 0xf
  р::1088 16; hex:: 0x440 0x10
  с::1089 17; hex:: 0x441 0x11
  т::1090 18; hex:: 0x442 0x12
  у::1091 19; hex:: 0x443 0x13
  ф::1092 20; hex:: 0x444 0x14
  х::1093 21; hex:: 0x445 0x15
  ц::1094 22; hex:: 0x446 0x16
  ч::1095 23; hex:: 0x447 0x17
  ш::1096 24; hex:: 0x448 0x18
  щ::1097 25; hex:: 0x449 0x19
  ъ::1098 26; hex:: 0x44a 0x1a	REMOVE	
  ы::1099 27; hex:: 0x44b 0x1b	SWAP	26	27
  ь::1100 28; hex:: 0x44c 0x1c	SWAP	27	26
  э::1101 29; hex:: 0x44d 0x1d			28	28
  ю::1102 30; hex:: 0x44e 0x1e			29	29
  я::1103 31; hex:: 0x44f 0x1f			30	30
 * */
class Afin {

	private static int alphLen = 31;

	public static List<Integer> encode(String text){
	List<Integer> ints = text.chars().mapToObj(e -> (int)e).collect(Collectors.toList());
	ints = ints.stream().map( Afin::encodeSingle ).collect( Collectors.toList() );
	return ints;
	}

	private static int encodeSingle(int chr){
		
		//System.out.println("In: " + chr);

		// ъ -> ь
		if (chr == 1098)
			chr = 1100;

		if (chr == 1099)
			chr = 27;
	
		if (chr == 1100)
			chr = 26;

		if (0x0430 <= chr && chr <= 0x44a){
			chr = chr - 0x430;
			}
		if (0x044a < chr && chr <= 0x44f){
			chr = chr - 0x430 - 1;
			} 
		//System.out.println("Out: " + chr);
		return chr;
	}

	public static String decode(List<Integer> ints){

	ints = ints.stream().map( Afin::decodeSingle ).collect( Collectors.toList() );
	List<Character> text = ints.stream().map( e -> Afin.chrToUtf(e)).collect(Collectors.toList());

	return text.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(""));
	}

	private static char chrToUtf(int chr){ return (char) chr; }

	private static int decodeSingle(int chr){
		
		//System.out.println("In: " + chr):;

		if (chr == 26)
			chr = 1100;

		if (chr == 27)
			chr = 1099;

		if (0 <= chr && chr <= 26){
			chr = chr + 0x430;
		}
		if (26 < chr && chr <= 32){
			chr = chr + 0x430 + 1;
		}

		//System.out.println("Out: " + chr);
		return chr;
	}

	// encode to bigramme
	
	public static List<Integer> encodeBigr (List<Integer> ints){
	int length = ints.size();
	int bigr = 0;

	List<Integer> res = new ArrayList<Integer>();
	for (int i = 1; i < length; i+=2)
		{
			bigr = (ints.get(i-1) * alphLen + ints.get(i));
			res.add(bigr);
		}
	//System.out.println("code " + res);
	return res;
	}

	public static List<Integer> decodeBigr (List<Integer> bigrs){
		List<Integer> res = new ArrayList();

		// iterate bigrams
		for (Integer bigr : bigrs){
			
			// iterate firts character
			int a = -1;
			int b = -1;
			//System.out.println(bigr+"::");
			for (int i = 0; i < alphLen; i++){
				
				int reverse = Lab3Math.revMod(i, alphLen);
				// break якщо немає оберненого елемета за модулем
				if (reverse == -1)
					continue;

				// iterate second character
				for (int j = 0; j < alphLen; j++){
					
					int temp = reverse * (bigr - j) % (alphLen *alphLen);
					// //System.out.println("\tReverse:: a: "+ i+ " b: " +j+ " len: " + alphLen + " rev: "+ reverse);
					// //System.out.println("\tTemp:: "+temp);

					if (temp == alphLen){
						// //System.out.println("\tReverse:: a: "+ i+ " b: " +j+ " len: " + alphLen + " rev: "+ reverse);
						// //System.out.println("\tTemp:: "+temp);
						a = i;
						b = j;
						break;
					}
					if (temp == 0){
						// //System.out.println("\tReverse:: a: "+ i+ " b: " +j+ " len: " + alphLen + " rev: "+ reverse);
						// //System.out.println("\tTemp:: "+temp);
						a = 0;
						b = j;
						break;
					}
				}
				if (a != -1 && b != -1)
					break;

			}
			res.add(a);
			res.add(b);
			// //System.out.println("\tResult:: bigr: "+bigr+" a: "+a+" b: "+b+" string: "+chrToUtf(decodeSingle(a))+chrToUtf(decodeSingle(b)));
		}
		//System.out.println(res);
		return res;
	}

	public static List<Integer> encrypt(List<Integer> bigrs, int a, int b){
		
		List<Integer> res = new ArrayList();
		
		for (Integer bigr: bigrs){
			int temp = (a * bigr + b) % (alphLen*alphLen);
			//System.out.println("\t"+a+"*"+bigr+"+"+b+"="+temp);
			res.add(temp);
		}
		//System.out.println("encr " + res);
		return res;
	}

	public static List<Integer> decrypt(List<Integer> bigrs, int a, int b){
		
		int rev = Lab3Math.revMod(a, alphLen*alphLen);
		
		List<Integer> res = new ArrayList();

		for (Integer bigr: bigrs){

			if (rev == -1)
				res.add(-1);
			else{
				int temp = (rev * (bigr - b) % (alphLen*alphLen));
				//System.out.println("\t"+rev+"*("+bigr+"-"+b+")="+temp);
				if (temp < 0)
					temp+=alphLen*alphLen;
				res.add(temp);
			}
		}
		//System.out.println("decr " + res);
		return res;
	}
}

class Lab3Math {

	public static int mod(int x, int modulo){
		int res = x % modulo;
		while (res < 0)
			res += modulo;
		return res;
	}

	public static int gcd(int x, int y){
		if (y == 0)
			return x;
		return gcd (y, x % y);
	}

	public static int[] gcdExt(int x, int y){
		int[] res = new int[3];
		if (x == 0) {
			res[0] = y;
			res[1] = 0;
			res[2] = 1;
			return res;
		}
		res = gcdExt(y%x, x);
		int temp_x = res[2] - Math.floorDiv(y, x) * res[1];
		int temp_y = res[1];
		
		res[1] = temp_x;
		res[2] = temp_y;
		return res;
	}

	// обернений елемент в кільці по модулю
	public static int revMod(int elem, int modulo){
	int[] res = gcdExt(elem, modulo);
		if (res[0] == 1){
			//System.out.println("\t\telem "+elem+" ["+res[0]+" "+res[1]+" "+res[2]+ "] rev: "+(res[1] + modulo) % modulo);
			return (res[1] + modulo) % modulo;
		}
		return -1;
	}

	public static List<Integer> LinComp(int a, int b, int modulo){
		List<Integer> res = new ArrayList();

		int d = gcd(a, modulo);
		if (d == 1)
			res.add(revMod(a, modulo) * b % modulo);
		else
			if (d > 1 && Math.floorDiv(b, d) == 0){
				int a_ = a / d;
				int b_ = b / d;
				int modulo_ = modulo / d;
				a_ = revMod(a_, modulo_) * b_ % modulo_;
				for(int i =0; i< d; i++){
					res.add(a_ + modulo_ * i);
				}
			}
			else
				res.add(-1);
		return res;
	}

}
