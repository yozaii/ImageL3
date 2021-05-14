package projet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
		
		Mat kernel = new Mat( 3, 3, 0);
		int kRow = kernel.rows();
		int kCol = kernel.cols();
        kernel.put(kRow ,kCol,
                1, 1, 1,
                1, 1, 1,
                1, 1, 1 );
		
		
		String path = "D:\\UE_Image\\Base_Image\\Base\\0.jpg";//Remplir ici ou se trouve le fichier

		/*-----------------------------------------------------------*/
		/*-------------------Resizing the image----------------------*/
		/*-----------------------------------------------------------*/
		Mat mat;
		mat = Imgcodecs.imread(path,0);//Reading image and loading it into a matrix
		mat = PreProcessing.resizeMat(mat, 480, 640);
		HighGui.imshow("source", mat);
	
		
		/*-----------------------------------------------------------*/
		/*-----------Finding the vertical region of interest---------*/
		/*-----------------------------------------------------------*/
		Mat cl = mat.clone();//Clone of source image
		int[] minMax = Processing.verticalROI(mat);
	    for (int i = 0; i < cl.rows(); i++) {
	    	Processing.blackBeforeAndAfter(i, minMax[0],minMax[1], cl);
	    }
	    //HighGui.imshow("vertical ROI", cl);
	    
	    
	    /*-----------------------------------------------------------*/
		/*-----------Finding the reverse region of interest----------*/
		/*-----------------------------------------------------------*/
	    Mat reverse = mat.clone();
	    for (int i = 0; i < reverse.rows(); i++) {
	    	Processing.blackBetween(i, minMax[0], minMax[1], reverse);
	    }
	    //HighGui.imshow("reverse", reverse);

		
	    /*-----------------------------------------------------------*/
		/*----------Applying vertical change sobel filter------------*/
		/*-----------------------------------------------------------*/
		Mat sobelY = Filters.sobelY(cl);
		HighGui.imshow("sobelY before", sobelY);
		Size s = new Size(5,5);
		for (int i =0 ; i < 5 ; i++)
			Imgproc.blur(sobelY, sobelY, s);
		//HighGui.imshow("sobelY after blur", sobelY);
		
		
		/*-----------------------------------------------------------*/
		/*---Applying vertical change sobel filter on reverse image--*/
		/*-----------------------------------------------------------*/
		Mat sobelYR = Filters.sobelY(reverse);
		//HighGui.imshow("sobelY reverse before", sobelYR);
		s = new Size(5,5);
		for (int i =0 ; i < 5 ; i++)
			Imgproc.blur(sobelYR, sobelYR, s);
		
		/*
		double[] tab = {-1,-1,-1,-1,8,-1,-1,-1,-1};
		Mat cFil = Filters.custom3x3Mat(tab, 0);
		sobelY = Filters.customFilter(sobelY, cFil);
		HighGui.imshow("sobelY after precision", sobelY);
		*/
		
		/*-----------------------------------------------------------*/
		/*-----------------------Mean threshold----------------------*/
		/*-----------------------------------------------------------*/
		sobelY = Filters.meanThreshold(sobelY, 20);
		//HighGui.imshow("sobelY after thresh", sobelY);
		kernel.put(kRow ,kCol,
	                1, 1, 1,
	                1, 1, 1,
	                0, 0, 0 );
		
		
		/*-----------------------------------------------------------*/
		/*------------------Mean threshold on reverse----------------*/
		/*-----------------------------------------------------------*/
		sobelYR = Filters.thresholding(sobelYR, 30);
		//HighGui.imshow("sobelY reverse after thresh", sobelYR);
		
		/*
		int cannyThresh = (minMax[1] - minMax[0]);
		Mat edgesH = new Mat();
		Imgproc.Canny(sobelY, edgesH, cannyThresh, cannyThresh*3);
		HighGui.imshow("Edges horizontal", edgesH);
		*/
		
		/*-----------------------------------------------------------*/
		/*------------------Hough horizontal lines-------------------*/
		/*-----------------------------------------------------------*/
		int houghThresh = (minMax[1] - minMax[0])/3;
		System.out.println("Region of interest min: " + minMax[0] + " max: " + minMax[1] +  " thresh: " + houghThresh);
		Mat houghH = Processing.hough(sobelY, houghThresh, "Horizontal");
		HighGui.imshow("Hough horizontal", houghH);
		
		
		/*-----------------------------------------------------------*/
		/*------------Hough horizontal lines on reverse--------------*/
		/*-----------------------------------------------------------*/
		houghThresh = (sobelYR.cols() - minMax[1] - minMax[0])/3;
		Mat houghHR = Processing.hough(sobelYR, houghThresh, "Horizontal");
		//HighGui.imshow("Hough reverse horizontal", houghHR);
		
		
		/*-----------------------------------------------------------*/
		/*------------------non red intervals/distance---------------*/
		/*-----------------------------------------------------------*/
		Mat houghH2 = Processing.removeNonRedIntervals(houghH, 10);
		
		//HighGui.imshow("HOUGH TWOOO", houghH2);
		
		for (int i = 0; i < 30; i++)
			Processing.blackBetween(i, 0, houghH2.cols(), houghH2);
		
		for (int i = houghH2.rows()-30; i < houghH2.rows(); i++)
			Processing.blackBetween(i, 0, houghH2.cols(), houghH2);
		
		ArrayList<Integer> redIntervals = RedOp.redIntervals(houghH2);
		ArrayList<Integer> redDistance = RedOp.nonRedIntervalsSize(houghH2);
		
		System.out.println("red intervals after: " + redIntervals.toString());
		Mat mat2 = Imgcodecs.imread(path, Imgcodecs.CV_LOAD_IMAGE_COLOR);
		mat2 = PreProcessing.resizeMat(mat2, 480, 640);
		Mat result = Processing.drawClusters(mat2, redIntervals);
		HighGui.imshow("result", result);
		
		HighGui.waitKey();
		HighGui.destroyAllWindows();
		
	}

}
