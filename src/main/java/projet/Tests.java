package projet;

import java.io.File;
import java.io.IOException;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.statistics.HistogramDataset;

public class Tests {
	
	public static void main(String args[]) throws IOException {
		
		//nu.pattern.OpenCV.loadShared();
		nu.pattern.OpenCV.loadLocally();
		
		String path = "D:\\UE_Image\\Base_Image\\Base\\56.jpeg";//Remplir ici ou se trouve le fichier

		Mat mat;
		mat = Imgcodecs.imread(path,0);//Reading image and loading it into a matrix
		mat = PreProcessing.resizeMat(mat, 480, 640);
		HighGui.imshow("source", mat);
		
		
		Mat dst = new Mat();
		Mat edgesV = new Mat();
		Mat edgesH = new Mat();
		
		/*Imgproc.adaptiveThreshold(mat, dst, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C,
		         Imgproc.THRESH_BINARY, 3, 5);
		HighGui.imshow("dst", dst);*/

		
		Mat sobelX = Processing.sobelX(mat);
		Mat sobelY = Processing.sobelY(mat);
		Mat kernel = Mat.ones(5,5, CvType.CV_32F);
		
		sobelY = Processing.thresholding(sobelY, 30);
		
		HighGui.imshow("sobelX", sobelX);
		HighGui.imshow("sobelY" , sobelY);
		
		
		
		
		
		Imgproc.Canny(sobelX, edgesV, 100, 100*3);
		Imgproc.Canny(sobelY, edgesH, 100, 100*3);
		
		//Imgproc.morphologyEx(edges, edges, Imgproc.MORPH_DILATE, kernel);
		for (int i =0 ; i < 5 ; i++)
			Imgproc.morphologyEx(edgesV, edgesV, Imgproc.MORPH_CLOSE, kernel);
		HighGui.imshow("edges Vertical", edgesV);
		

		HighGui.imshow("edges Horizontal", edgesH);
		
		
		

		
		Mat edgesColorV = Processing.hough(edgesV,20, "Vertical");
		Mat edgesColorH = Processing.hough(edgesH,20, "Horizontal");

	    HighGui.imshow("Hough Vertical", edgesColorV);
	    HighGui.imshow("Hough Horizontal", edgesColorH);
		
		
		HighGui.waitKey();
		HighGui.destroyAllWindows();
		
	}

}
