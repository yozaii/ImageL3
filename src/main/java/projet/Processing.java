package projet;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

public class Processing {
	
	public static Mat custom3x3Mat(double [] table, int type) {
			
			
			Mat mat = new Mat(3,3,type);
			double [] data = {table[0]};
			mat.put(0, 0, data);
			
			data[0] = table[1];
			mat.put(0, 1, data);
			
			data[0] = table[2];
			mat.put(0, 2, data);
			
			data[0] = table[3];
			mat.put(1, 0, data);
			
			data[0] = table[4];
			mat.put(1, 1, data);
			
			data[0] = table[5];
			mat.put(1, 2, data);
			
			data[0] = table[6];
			mat.put(2, 0, data);
			
			data[0] = table[7];
			mat.put(2, 1, data);
			
			data[0] = table[8];
			mat.put(2, 2, data);
			
			return mat;
	}
	
	
	/**
	 * 
	 * @param mat
	 * @param thresh : higher thresh -> more erosion
	 * @param shape cross by default. Square if square kernel is needed
	 * @return
	 */
	public static Mat customFilter(Mat mat, Mat kernel3x3)  {

		Mat res = mat.clone();
		int rows = mat.rows();
		int cols = mat.cols();
		
		double kCenter = kernel3x3.get(1, 1)[0];
		double kTop = kernel3x3.get(0, 1)[0];
		double kBot = kernel3x3.get(2, 1)[0];
		double kRight = kernel3x3.get(1, 2)[0];
		double kLeft = kernel3x3.get(1, 0)[0];
		double kTopL = kernel3x3.get(0, 0)[0];
		double kTopR = kernel3x3.get(0, 2)[0];
		double kBotL = kernel3x3.get(2, 0)[0];
		double kBotR = kernel3x3.get(2, 2)[0];
		
		double[] matCenter;
		double[] matTop;
		double[] matBot;
		double[] matRight;
		double[] matLeft;
		double[] matTopR;
		double[] matTopL;
		double[] matBotL;
		double[] matBotR;
		
		double center;
		double top;
		double bot;
		double right;
		double left;
		double topL;
		double topR;
		double botL;
		double botR;
		
		for (int i=1 ; i<rows-1 ; i++) {
			for (int j=1 ; j<cols-1; j++) {
				double[] result = new double[1];
				
				matCenter = mat.get(i, j);
				center = kCenter * matCenter[0];
				
				matTop = mat.get(i, j-1);
				top = kTop * matTop[0];
				
				matBot = mat.get(i, j+1);
				bot = kBot * matBot[0];
				
				matRight = mat.get(i+1, j);
				right = kRight * matRight[0];
				
				matLeft = mat.get(i-1, j);
				left = kLeft * matLeft[0];
				
				matTopL = mat.get(i-1, j-1);
				topL = kTopL * matTopL[0];
				
				matTopR = mat.get(i+1, j-1);
				topR = kTopR * matTopR[0];
				
				matBotL = mat.get(i-1, j+1);
				botL = kBotL * matBotL[0];
				
				matBotR = mat.get(i+1, j+1);
				botR = kBotR * matBotR[0];
				
				result[0] = center + top + bot + right + left + topL + topR + botR + botL;
				
				res.put(i, j, result);

			}
		}
		return res;
	}
	
	public static Mat thresholding(Mat mat, double thresh) {
		Mat res = mat.clone();
		int rows = res.rows();
		int cols = res.cols();

		
		for (int i=0 ; i<rows ; i++) {
			for (int j=0 ; j<cols; j++) {

				double[] pixel = mat.get(i,j);
				if (pixel[0] < thresh) {
					double[] data = {0};
					res.put(i, j, data);
				}
				else {
					double[] data = {255};
					res.put(i, j, data);
				}


			}
		}
		
		return res;
	}
	
	public static Mat meanThreshold(Mat mat, double thresh) {
		Mat res = mat.clone();
		int rows = res.rows();
		int cols = res.cols();
		
		double[] matCenter;
		double[] matTop;
		double[] matBot;
		double[] matRight;
		double[] matLeft;
		double[] matTopR;
		double[] matTopL;
		double[] matBotL;
		double[] matBotR;
		
		for (int i=1 ; i<rows-1 ; i++) {
			for (int j=1 ; j<cols-1; j++) {

				matCenter = mat.get(i, j);
				matTop = mat.get(i, j-1);
				matBot = mat.get(i, j+1);
				matRight = mat.get(i+1, j);
				matLeft = mat.get(i-1, j);
				matTopL = mat.get(i-1, j-1);
				matTopR = mat.get(i+1, j-1);
				matBotL = mat.get(i-1, j+1);			
				matBotR = mat.get(i+1, j+1);
				
				double total = matCenter[0] + matTop[0] + matBot[0] + matRight[0] + matLeft[0] +
						matTopL[0] + matTopR[0] + matBotL[0] + matBotR[0];
				double average = total/9;
				if (average < thresh) {
					double[] data = {0};
					res.put(i, j, data);
				}
				else {
					double[] data = {255};
					res.put(i, j, data);
				}


			}
		}
		
		return res;
	}

	/**Sobel filter x derivative
	 * @param mat : the matrix to be processed
	 * @return : the new matrix
	 * */
	public static Mat sobelX(Mat mat) {
		
		Mat dstx = new Mat();
		Imgproc.Sobel(mat, dstx, CvType.CV_16S, 1, 0, 3, 1, 0, Core.BORDER_DEFAULT);
		Core.convertScaleAbs(dstx, dstx);
		return dstx;
		
	}
	
	/**Sobel filter y derivative
	 * @param mat : the matrix to be processed
	 * @return : the new matrix
	 * */
	public static Mat sobelY(Mat mat) {
		
		Mat dsty = new Mat();
		Imgproc.Sobel(mat, dsty, CvType.CV_16S, 0, 1, 3, 1, 0, Core.BORDER_DEFAULT);
		Core.convertScaleAbs(dsty, dsty);
		return dsty;
		
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
			System.out.println(i + " : Slope :" + slope + " x1: " + pt1.x + " x2: " + pt2.x + " y1: " + pt1.y + " y2: " + pt2.y);
			
			
			//We keep only the horizontal hough lines
			if (orientation == "Horizontal")
				if (Math.abs(slope)<0.05) {
					Imgproc.line(edgesColor, pt1, pt2, new Scalar(0, 0, 255), 3);
				}
			
			//We keep only the vertical hough lines
			if (orientation == "Vertical")
				if (Math.abs(slope)>90) {
					Imgproc.line(edgesColor, pt1, pt2, new Scalar(0, 0, 255), 3);
				}
		}
		return edgesColor;
	}

}
