package projet;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

public class Processing {
	
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
	 * Colors black before and after given indices in a given row
	 * @param row : row to color
	 * @param min : index to color before
	 * @param max : index to color after
	 * @param mat : the matrix to color
	 */
	public static void blackBeforeAndAfter(int row, int min, int max, Mat mat) {
		
		double[] data = {0,0,0};//Black color
		
		//Black before
		for (int i = 0; i < min; i++) {
			mat.put(row, i, data);
		}
		
		//Black after
		for (int i = max; i < mat.cols(); i++) {
			mat.put(row, i, data);
		}
	}

	/**
	 * Applies hough transform on given mat (Given mat must be grayscale)
	 * @param edges : the given mat
	 * @param pintThreshold : the point intersection threshold in the HoughLines method
	 * @return : the mat with hough lines colored on the mat
	 */
	public static Mat hough(Mat edges, int pointThreshold, String orientation) {
		
		Mat lines = new Mat();
		Mat edgesColor = new Mat();
		
		Imgproc.cvtColor(edges, edgesColor, Imgproc.COLOR_GRAY2BGR);
		
	    Imgproc.HoughLines(edges, lines, 1, Math.PI/180, pointThreshold);
	    
		for (int i = 0; i < lines.rows(); i++) {
			double[] data = lines.get(i, 0);
			double rho = data[0];
			double theta = data[1];
			double a = Math.cos(theta);
			double b = Math.sin(theta);
			double x0 = a*rho;
			double y0 = b*rho;
			//Drawing lines on the image
			Point pt1 = new Point();
			Point pt2 = new Point();
			pt1.x = Math.round(x0 + 1000*(-b));
			pt1.y = Math.round(y0 + 1000*(a));
			pt2.x = Math.round(x0 - 1000*(-b));
			pt2.y = Math.round(y0 - 1000 *(a));
			double slope = ((pt2.y-pt1.y)/(pt2.x-pt1.x));
			
			
			
			//We keep only the horizontal hough lines
			if (orientation == "Horizontal")
				if (Math.abs(slope)<0.05) {
					Imgproc.line(edgesColor, pt1, pt2, new Scalar(0, 0, 255), 3);
					System.out.println(i + " : Slope :" + slope + " x1: " + pt1.x + " x2: " + pt2.x + " y1: " + pt1.y + " y2: " + pt2.y);
				}
			
			//We keep only the vertical hough lines
			if (orientation == "Vertical")
				if (Math.abs(slope)>90) {
					Imgproc.line(edgesColor, pt1, pt2, new Scalar(0, 0, 255), 3);
					System.out.println(i + " : Slope :" + slope + " x1: " + pt1.x + " x2: " + pt2.x + " y1: " + pt1.y + " y2: " + pt2.y);
				}
		}
		return edgesColor;
	}
	
	public static Mat regionOfInterest(Mat mat) {

		Mat res = mat.clone();
		
		Mat edgesV = new Mat();
		Mat kernel = Mat.ones(5,5, CvType.CV_32F);
		
		Mat sobelX = Filters.sobelX(mat);
		
		HighGui.imshow("sobelX", sobelX);
		
		Imgproc.Canny(sobelX, edgesV, 100, 100*3);
		
		for (int i =0 ; i < 5 ; i++)
			Imgproc.morphologyEx(edgesV, edgesV, Imgproc.MORPH_CLOSE, kernel);
		
		HighGui.imshow("edges Vertical", edgesV);
		
		Mat edgesColorV = Processing.hough(edgesV, 20, "Vertical");

	    HighGui.imshow("Hough Vertical", edgesColorV);
		
		
		HighGui.waitKey();
		return res;
	}
}