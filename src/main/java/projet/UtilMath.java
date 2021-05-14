package projet;

import java.util.ArrayList;

/**
 * This class is used for math calculations
 * @author Youssef AL OZAIBI
 *
 */
public class UtilMath {
	
	public static double findListAverage(ArrayList<Integer> l) {
		int sum = 0;
		for (int i=0; i<l.size(); i++) {
			sum += l.get(i);
		}
		double average = sum / l.size();
		return average;
	}
}
