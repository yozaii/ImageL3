package projet;

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
	
	
}
