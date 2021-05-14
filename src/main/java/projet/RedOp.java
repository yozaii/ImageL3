package projet;

import java.util.ArrayList;

import org.opencv.core.Mat;

/**
 * This class provides methods relating to the red lines derived from hough transforms.
 * @author Youssef AL OZAIBI
 *
 */
public class RedOp {
	
	
	/**
	 * Takes the pixels of the red colors in the first mat, and removes them from the second mat.
	 * @param mat : first mat
	 * @param rMat : second mat
	 */
	public static void removeDetails(Mat mat, Mat rMat) {
		
		Mat res = rMat.clone();
		double[] data;
		double[] black = {0,0,0};
		for (int i = 0; i < mat.rows(); i++)
			for (int j = 0 ; j < mat.cols(); j++) {
				data = mat.get(i, j);
				if (data[0] == 0 && data[1] == 0 && data[2] == 255) {
					res.put(i, j, black);
				}
			}
	}
	
	
	/**
	 * Finds first and last occurence of red color in a matrix row
	 * @param row : The row to test
	 * @param mat : The mat to test
	 * @return : Returns a tuple with int[0] = min and int[1] = max
	 */
	public static int[] findMinMaxRed(int row, Mat mat) {
		
		
		int min = 25000; //25000 is an arbitrary value (big number)
		int current = 0;
		int max = 0;
		double data[];
		
		
		for (int i = 0 ; i< mat.cols(); i++) {
			data = mat.get(row, i);
			if (data[0] == 0 && data[1] == 0 && data[2] == 255) {
				current = i;
				if (i < min) min = i;//min is first occurence of red in the line
			}
		}
		
		max = current;//Max is the last occurence of red in the line
		
		int[] ret = new int[2];//The tuple containing min and max
		ret[0] = min;
		ret[1] = max;
		return ret;
	}
	
	/**
	 * A list that returns the vertical red intervals in an image
	 * with even number list index as the start of the interval,
	 * and the odd number list index as the end
	 * @param mat : matrix with horizontal red lines
	 * @return : list with even indices = start, odd indices = end
	 */
	public static ArrayList<Integer> redIntervals(Mat mat){
		
		ArrayList<Integer> ret = new ArrayList<Integer>();//The returned list
		int center = mat.cols()/2;
		double data[];
		for (int i = 0; i < mat.rows(); i++) {
			data = mat.get(i , center);
			int start = i;
			while (data[0] == 0 && data[1] == 0 && data[2] == 255 && i < mat.rows()) { //While pixel is red
				i++;
				if (i < mat.rows())	data = mat.get(i, center);
				
			}
			int end = i;
			if (end!= start) {
				ret.add(start);
				ret.add(end);
			}
				
		}
		return ret;
	}
	
	
	/**
	 * A list that returns the vertical red intervals size
	 * @param mat : matrix with horizontal red lines
	 * @return : list with size of red intervals
	 */
	public static ArrayList<Integer> redIntervalsSize(Mat mat){
		
		ArrayList<Integer> ret = new ArrayList<Integer>();//The returned list
		int center = mat.cols()/2;
		double data[];
		for (int i = 0; i < mat.rows(); i++) {
			data = mat.get(i , center);
			int counter = 0;
			while (data[0] == 0 && data[1] == 0 && data[2] == 255 && i < mat.rows()) { //While pixel is red
				counter ++;
				i++;
				if (i < mat.rows()) data = mat.get(i, center);
			}
			if (counter != 0)
				ret.add(counter);	
		}
		
		return ret;
	}
	
	
	/**
	 * A list that returns the vertical black intervals in an image
	 * with even number list index as the start of the interval,
	 * and the odd number list index as the end
	 * @param mat : matrix with horizontal red lines
	 * @return : list with even indices = start, odd indices = end
	 */
	public static ArrayList<Integer> nonRedIntervals(Mat mat){
		
		ArrayList<Integer> ret = new ArrayList<Integer>();//The returned list
		int center = mat.cols()/2;
		double data[];
		for (int i = 0; i < mat.rows(); i++) {
			data = mat.get(i , center);
			int start = i;
			while (!(data[0] == 0 && data[1] == 0 && data[2] == 255) && i < mat.rows()) { //While pixel is red
				i++;
				if (i < mat.rows())	data = mat.get(i, center);
			}
			int end = i;
			if (end!= start) {
				ret.add(start);
				ret.add(end);
			}
				
		}
		return ret;
	}
	
	
	/**
	 * A list that returns the vertical black intervals size
	 * @param mat : matrix with horizontal red lines
	 * @return : list with size of black intervals
	 */
	public static ArrayList<Integer> nonRedIntervalsSize(Mat mat){
		
		ArrayList<Integer> ret = new ArrayList<Integer>();//The returned list
		int center = mat.cols()/2;
		double data[];
		for (int i = 0; i < mat.rows(); i++) {
			data = mat.get(i , center);
			int counter = 0;
			while (!(data[0] == 0 && data[1] == 0 && data[2] == 255) && i < mat.rows()) { //While pixel is red
				i++;
				counter++;
				if (i < mat.rows()) data = mat.get(i, center);
				
				
			}
			if (counter > 0)
				ret.add(counter);
			
		}
		return ret;
	}
	
	/**
	 * Returns a list of intervals with only values of size of intervals < threshold
	 * @param l : List of interval sizes
	 * @param l2 : List of intervals
	 * @param thresh : threshold
	 */
	public static ArrayList<Integer> listsThreshold(ArrayList<Integer> l, ArrayList<Integer> l2, int thresh) {
		
		
			ArrayList<Integer> newL = new ArrayList<Integer>();
			ArrayList<Integer> newL2 = new ArrayList<Integer>();
			for (int i = 0; i < l.size(); i++) {
				int x = l.get(i);
				
				int x2 = l2.get(i*2);
				int x3 = l2.get((i*2)+1);
				if (x < thresh) {
					newL.add(x);
					newL2.add(x2);
					newL2.add(x3);
				}
			}
			
			return newL2;
			
	}
	
	
}
