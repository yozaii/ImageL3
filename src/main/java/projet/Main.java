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

public class Main {
	
	public static void main(String args[]) throws IOException {
		
		//nu.pattern.OpenCV.loadShared();
		nu.pattern.OpenCV.loadLocally();
		
		String path = "D:\\UE_Image\\Base_Image\\Base\\9.jpg";//Fill here the image path

		Mat source = Imgcodecs.imread(path, 0);
		source = Imgcodecs.imread(path,0);//Reading image and loading it into a matrix
		source  = PreProcessing.resizeMat(source, 480, 640);
		HighGui.imshow("Source image", source);
		
		Mat det = Detection.detectCupWater(path);
		HighGui.imshow("Detection result", det);
		HighGui.waitKey();
		HighGui.destroyAllWindows();
		System.exit(0);
		
	}

}
