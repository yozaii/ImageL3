package projet;

import java.util.ArrayList;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;


/**
 * This class is for processing methods (filters unincluded) 
 * @author Youssef AL OZAIBI
 *
 */
public class Processing {
	
	/**Colors all rows in a matrix between the indices to red
	 * @param min : min index
	 * @param max : max index
	 * @param mat : Matrix to be colored
	 */
	public static void colorRowsRed(int min, int max, Mat mat) {
		
		double[] data = {0,0,255};
		
		for (int i = min; i< max; i++) {
			for (int j = 0; j < mat.cols(); j++) {
				mat.put(i, j, data);
			}
		}
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
	 * Colors black between min and max indices in a given row
	 * @param row : row to color
	 * @param min : index to color before
	 * @param max : index to color after
	 * @param mat : the matrix to color
	 */
	public static void blackBetween(int row, int min, int max, Mat mat) {
		double[] data = {0,0,0};//Black color
		
		//Fill black between indices
		for (int i = min; i < max; i++) {
			mat.put(row, i, data);
		}
	}
	
	/**
	 * Removes non red intervals between red lines found in an image
	 * @param mat : the matrix of the image
	 * @param thresh : the threshold for distance between the intervals
	 * @return : The new matrix with removed non red intervals
	 */
	public static Mat removeNonRedIntervals(Mat mat, int thresh) {
		/*-----------------------------------------------------------*/
		/*----------Finding non red intervals/distance---------------*/
		/*-----------------------------------------------------------*/
		ArrayList<Integer> nonRedIntervals = RedOp.nonRedIntervals(mat);
		System.out.println("The indices of the nonRed intervals" + nonRedIntervals.toString());
		
		ArrayList<Integer> nonRedSize = RedOp.nonRedIntervalsSize(mat);
		System.out.println("The distance of the nonRed intervals:" + nonRedSize.toString());
		
		/*-----------------------------------------------------------*/
		/*------------------Filling in the intervals-----------------*/
		/*-----------------------------------------------------------*/
		ArrayList<Integer> newNonRedIntervals = RedOp.listsThreshold(nonRedSize, nonRedIntervals, 21);
		System.out.println("Intervals after: " + newNonRedIntervals.toString());
		Mat res = mat.clone();
		for (int i = 0 ; i< newNonRedIntervals.size()-1; i+=2) {
			Processing.colorRowsRed(newNonRedIntervals.get(i), newNonRedIntervals.get(i+1), res);
		}
		return res;
	}
	
	/**
	 * Draws a blue rectangle that corresponds to the cup and green lines that correspond to estimated water levels
	 * @param mat : The matrix to draw the rectangle over
	 * @param intervals : the red hough line intervals of an image
	 * @param min : the min of the vertical ROI
	 * @param max : the max of the vertical ROI
	 * @return : Image (matrix) with a blue rectangle for the cup, and green lines for the estimated water levels
	 */
	public static Mat detectLevels(Mat mat, ArrayList<Integer> intervals, int min, int max) {
		Mat res = mat.clone();
		
		int size = intervals.size();
		
		/*---------------------------------------------------------*/
		/*--The upper and lower clusters are colored in blue-------*/
		/*---------------------------------------------------------*/
		
		//Top left of rectangle
		int intervalAvrg = (intervals.get(0) + intervals.get(1))/2;
		Point p1 = new Point (min , intervalAvrg);
		
		//Bottom right of rectangle
		intervalAvrg = (intervals.get(size-2 ) + intervals.get(size-1))/2;
		Point p2 = new Point (max , intervalAvrg);
			
		Scalar color = new Scalar(255, 0, 0);//Blue
		
		Imgproc.rectangle(res, p1, p2, color, 3);
		
		
		int numClusters = size/2;
		switch (numClusters) {
		
		case 0:
		
			break;
			
		case 1: 

			break;
			
		case 2:
			
			break;
			
		case 3: 
			
			//Middle cluster
			intervalAvrg = (intervals.get(2) + intervals.get(3))/2;
			p1 = new Point (min , intervalAvrg);
			p2 = new Point (max , intervalAvrg);
			color = new Scalar(0, 255, 0);//Green
			Imgproc.line(res, p1, p2, color, 3);
			
			break;
			
		case 100:
			//Middle cluster 1
			int intervalAvrg1 = (intervals.get(2) + intervals.get(3))/2;
			p1 = new Point (min , intervalAvrg1);
			p2 = new Point (max , intervalAvrg1);
			color = new Scalar(0, 255, 0);//Green
			Imgproc.line(res, p1, p2, color, 3);
			
			//Middle cluster 2
			int intervalAvrg2 = (intervals.get(size-4) + intervals.get(size-3))/2;
			p1 = new Point (min , intervalAvrg2);
			p2 = new Point (max , intervalAvrg2);
			color = new Scalar(0, 255, 0);//Green
			Imgproc.line(res, p1, p2, color, 3);
			
			//Middle cluster real middle innit
			intervalAvrg = (intervalAvrg1 + intervalAvrg2)/2;
			p1 = new Point (min , intervalAvrg);
			p2 = new Point (max , intervalAvrg);
			color = new Scalar(0, 0, 0);//Green
			Imgproc.line(res, p1, p2, color, 3);
			break;
			
		default:
			
			//Middle clusters
			for (int i=2; i< size -2; i+=2 ) {
				intervalAvrg = (intervals.get(i) + intervals.get(i+1))/2;
				p1 = new Point (min , intervalAvrg);
				p2 = new Point (max , intervalAvrg);
				color = new Scalar(0, 255, 0);//Green
				Imgproc.line(res, p1, p2, color, 3);
			}
			break;
			
		}
		
		return res;
	}
	
	/**
	 * Applies hough probabilistic transform on given mat (Given mat must be grayscale)
	 * @param edges : the given mat
	 * @param pintThreshold : the point intersection threshold in the HoughLines method
	 * @return : the mat with hough lines colored on the mat
	 */
	public static Mat houghP(Mat edges, int pointThreshold, String orientation) {
		
		Mat lines = new Mat();
		Mat edgesColor = new Mat();
		
		Imgproc.cvtColor(edges, edgesColor, Imgproc.COLOR_GRAY2BGR);
		
	    Imgproc.HoughLinesP(edges, lines, 1, Math.PI/180, pointThreshold);
	    
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
				if (Math.abs(slope)>50) {
					Imgproc.line(edgesColor, pt1, pt2, new Scalar(0, 0, 255), 3);
					System.out.println(i + " : Slope :" + slope + " x1: " + pt1.x + " x2: " + pt2.x + " y1: " + pt1.y + " y2: " + pt2.y);
				}
		}
		return edgesColor;
	}
	
	//TODO: change return of matrix to return of lines
	//TODO: remove line coloring from this method to houghColoring method
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
					//System.out.println(i + " : Slope :" + slope + " x1: " + pt1.x + " x2: " + pt2.x + " y1: " + pt1.y + " y2: " + pt2.y);
				}
			
			//We keep only the vertical hough lines
			if (orientation == "Vertical")
				if (Math.abs(slope)>50) {
					Imgproc.line(edgesColor, pt1, pt2, new Scalar(0, 0, 255), 3);
					//System.out.println(i + " : Slope :" + slope + " x1: " + pt1.x + " x2: " + pt2.x + " y1: " + pt1.y + " y2: " + pt2.y);
				}
		}
		return edgesColor;
	}
	
	
	//TODO: use lines from hough method to color lines here
	public static Mat HoughColoring(Mat mat, Mat lines) {
		Mat res = mat.clone();
		return res;
	}
	
	/**
	 * Segments an image to keep the cup region of interest
	 * @param mat : The mat to be processed
	 * @return : int[] minMax where minMax[0] is the left vertical extremity, minMax[1] the right vertical extremity
	 */
	public static int[] verticalROI(Mat mat) {
		
		
		Imgproc.medianBlur(mat, mat, 5);//Blurring to remove noise
		Mat edgesV = new Mat();//Will contain canny edges
		Mat kernel = Mat.ones(5,5, CvType.CV_32F);//5x5 kernel filled with ones
		
		Mat sobelX = Filters.sobelX(mat);//Sobel of horizontal change
		
		//HighGui.imshow("sobelX", sobelX);
		
		//Finding the vertical edges from sobel x
		Imgproc.Canny(sobelX, edgesV, 100, 100*3);
		
		//Closing using kernel
		for (int i =0 ; i < 5 ; i++)
			Imgproc.morphologyEx(edgesV, edgesV, Imgproc.MORPH_CLOSE, kernel);
		
		//HighGui.imshow("edges Vertical", edgesV);
		
		int houghThresh = 30, maxLoop = 0;
		int[] minMax = new int[2];
		boolean bool = false;//Becomes true if we find a good houghThreshold calibration
		Mat edgesColorV;
		
		//Loop to find a good houghThreshold calibration
		do {
			
			edgesColorV = Processing.hough(edgesV, houghThresh, "Vertical");
			minMax = RedOp.findMinMaxRed(0, edgesColorV);
			
			int minCenter = 105;//the minimum value of the center of the hough interval
			int maxCenter = 375;//the maximum value of the center of the hough interval
			int minLeft = 40;//the minimum value of the first hough line
			int maxRight = 440;//the maximum value of the last hough line
			int average = (minMax[0] + minMax[1])/2;//
			int houghIncrement = 10;
			int minWidth = 30;
			boolean tests = true;
			
			//minMax[1]-minMax[0]>=mat.cols()-20
			if (minMax[1] > maxRight || minMax[0]< minLeft) {
				tests = false;
				houghThresh += houghIncrement;
				if (minMax[1]-minMax[0]< minWidth) 
					houghThresh -=houghIncrement;
				if (average > maxCenter || average < minCenter )
					houghThresh -=houghIncrement;
			}
			
			if (minMax[1] - minMax[0] < minWidth) {
				tests = false;
				houghThresh -=houghIncrement;
			}
				
			
			if (average > maxCenter || average < minCenter ) {
				tests = false;
				houghThresh -=houghIncrement;
			}

			if (tests)
				bool = true;
			maxLoop++;
		}
		while (maxLoop <50 && !bool);
		
		System.out.println("Vertical hough threshold: " + houghThresh);
	    //HighGui.imshow("Hough Vertical", edgesColorV);
	    
		return minMax;
	}
}
