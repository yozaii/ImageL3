package projet;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This class is used for list calculations
 * @author Youssef AL OZAIBI
 *
 */
public class UtilList {
	
	public static double findListAverage(ArrayList<Integer> l) {
		int sum = 0;
		for (int i=0; i<l.size(); i++) {
			sum += l.get(i);
		}
		double average = sum / l.size();
		return average;
	}
	
	public static int findListMean(ArrayList<Integer> l) {
		int meanIndex = l.size()/2;
		Collections.sort(l);
		return l.get(meanIndex);
	}
	
}
