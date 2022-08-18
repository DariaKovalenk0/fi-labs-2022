import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;  
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors	

/** 
 * Functions firstly introduced in Lab1 (Entrpopy & Redundancy)
 **/

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
