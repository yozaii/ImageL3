package projet;

import java.io.IOException;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import projet.PreProcessing;

public class Morphology {
	
	/**
	 * Applies dilation morphology on a matrix
	 * @param mat : The matrix to be dilated
	 * @param shape : cross by default. "Square" if square kernel is needed
	 * @return : Dilated matrix
	 */
	public static Mat customDilation(Mat mat, String shape)  {
		Mat res = mat.clone();
		int rows = mat.rows();
		int cols = mat.cols();
		double[] data;
		double[] dataTop;
		double[] dataBot;
		double[] dataRight;
		double[] dataLeft;
		double[] dataTopL;
		double[] dataTopR;
		double[] dataBotL;
		double[] dataBotR;
		
		for (int i=1 ; i<rows-1 ; i++) {
			for (int j=1 ; j<cols-1; j++) {
				int counter = 0;
				data = mat.get(i, j);
				dataTop = mat.get(i, j-1);
				dataBot = mat.get(i, j+1);
				dataRight = mat.get(i+1, j);
				dataLeft = mat.get(i-1, j);
				if (data[0]==0) counter++;
				if (dataTop[0] == 0) counter++;
				if (dataBot[0] == 0) counter++;
				if (dataRight[0] == 0) counter++;
				if (dataLeft[0] == 0) counter++;
				
				if (shape == "Square") {
					dataTopL = mat.get(i-1, j-1);
					dataTopR = mat.get(i+1, j-1);
					dataBotL = mat.get(i-1, j+1);
					dataBotR = mat.get(i+1, j+1);
					if (dataTopL[0]==0) counter++;
					if (dataTopR[0] == 0) counter++;
					if (dataBotL[0] == 0) counter++;
					if (dataBotR[0] == 0) counter++;
					
				}
				if (counter>0) {
					data[0] = 0;
					res.put(i, j, data[0]);
				}
			}
		}
		return res;
	}
	
	/**
	 * Applies erosion morphology on a matrix
	 * @param mat : The matrix to erode
	 * @param thresh : higher thresh -> more erosion
	 * @param shape : cross by default. "Square" if square kernel is needed
	 * @return : Eroded matrix
	 */
	public static Mat customErosion(Mat mat, int thresh, String shape)  {

		Mat res = mat.clone();
		int rows = mat.rows();
		int cols = mat.cols();
		double[] data;
		double[] dataTop;
		double[] dataBot;
		double[] dataRight;
		double[] dataLeft;
		double[] dataTopL;
		double[] dataTopR;
		double[] dataBotL;
		double[] dataBotR;
		
		for (int i=1 ; i<rows-1 ; i++) {
			for (int j=1 ; j<cols-1; j++) {
				int counter = 0;
				data = mat.get(i, j);
				dataTop = mat.get(i, j-1);
				dataBot = mat.get(i, j+1);
				dataRight = mat.get(i+1, j);
				dataLeft = mat.get(i-1, j);
				if (data[0]==0) counter++;
				if (dataTop[0] == 0) counter++;
				if (dataBot[0] == 0) counter++;
				if (dataRight[0] == 0) counter++;
				if (dataLeft[0] == 0) counter++;
				
				if (shape == "Square") {
					dataTopL = mat.get(i-1, j-1);
					dataTopR = mat.get(i+1, j-1);
					dataBotL = mat.get(i-1, j+1);
					dataBotR = mat.get(i+1, j+1);
					if (dataTopL[0]==0) counter++;
					if (dataTopR[0] == 0) counter++;
					if (dataBotL[0] == 0) counter++;
					if (dataBotR[0] == 0) counter++;
				}
				if (counter>0 && counter <thresh) {
					data[0] = 255;
					res.put(i, j, data[0]);
				}
			}
		}
		return res;
	}
	
	/**
	 * Applies opening morphology on a matrix
	 * @param mat : The matrix to open
	 * @param thresh : higher thresh -> more erosion
	 * @param shape : cross by default. "Square" if square kernel is needed
	 * @return : Opened matrix
	 */
	public static Mat customOpening(Mat mat, int thresh, String shape) {
		Mat er = customErosion(mat, thresh, shape);
		Mat opening = customDilation(mat, shape);
		return opening;
	}
	
	/**
	 * Applies closing morphology on a matrix
	 * @param mat : The matrix to close
	 * @param thresh : higher thresh -> more erosion
	 * @param shape : cross by default. "Square" if square kernel is needed
	 * @return : Closed matrix
	 */
	public static Mat customClosing(Mat mat, int thresh, String shape) {
		Mat dil = customDilation(mat,shape);
		Mat closing = customErosion(dil, thresh,shape);
		return closing;
	}

}
