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
	 * 
	 * @param mat
	 * @param shape cross by default. Square if square kernel is needed
	 * @return
	 */
	public static Mat customDilation(Mat mat)  {
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
				dataTopL = mat.get(i-1, j-1);
				dataTopR = mat.get(i+1, j-1);
				if (data[0] > 40) counter++;
				if (dataTop[0] > 40) counter++;
				if (dataRight[0] > 40) counter++;
				if (dataLeft[0] > 40) counter++;
				if (dataTopL[0] > 40) counter++;
				if (dataTopR[0] > 40) counter++;
				if (counter>0) {
					data[0] = 255;
					res.put(i, j, data[0]);
				}
			}
		}
		return res;
	}
	
	/**
	 * 
	 * @param mat
	 * @param thresh : higher thresh -> more erosion
	 * @param shape cross by default. Square if square kernel is needed
	 * @return
	 */
	public static Mat customErosion(Mat mat, int thresh)  {

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
				dataTopL = mat.get(i-1, j-1);
				dataTopR = mat.get(i+1, j-1);
				dataBotL = mat.get(i-1, j+1);
				dataBotR = mat.get(i+1, j+1);
				if (data[0] > 40) counter++;
				if (dataTop[0] > 40) counter++;
				if (dataRight[0] > 40) counter++;
				if (dataLeft[0] > 40) counter++;
				if (dataTopL[0] > 40) counter++;
				if (dataTopR[0] > 40) counter++;
				if (counter>0 && counter <thresh) {
					data[0] = 255;
					res.put(i, j, data[0]);
				}
			}
		}
		return res;
	}
	
	public static Mat customOpening(Mat mat, int thresh) {
		Mat er = customErosion(mat, thresh);
		Mat opening = customDilation(mat);
		return opening;
	}
	
	public static Mat customClosing(Mat mat, int thresh) {
		Mat dil = customDilation(mat);
		Mat closing = customErosion(dil, thresh);
		return closing;
	}

}
