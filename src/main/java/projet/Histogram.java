package projet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.statistics.HistogramDataset;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;

/**
 * This class is for histogram related methods
 * @author Youssef AL OZAIBI
 *
 */
public class Histogram  {
	
	//Fills table with zeros
	public static void InitTable(double[] table) {
		
		for (int i = 0 ; i < table.length ; i++) {
			table[i] = 0;
		}
	}
		
		
	//Displays table
	public static void DisplayTable(double[] table) {
		for (int i = 0 ; i < table.length; i++) {
			System.out.println(i + ": " + table[i]);
		}
	}
	
	//Finds first non zero value in table
	public static int FindMinIndex(double[] table) {
		int i = 0;
		while (table[i]==0) i++;
		return i;
	}
	
	
	//Finds last non zero value in table
	public static int FindMaxIndex(double[] table) {
		
		int max = 0;
		for (int i = 0; i < table.length ; i++) {
			if (table[i]!=0) max = i;
		}
		return max;
	}
	
	//Creates a table of size 256 and fills it with zeros
	public static double[] CreateHistogram() {
		double [] histogram = new double[256];
		InitTable(histogram);
		return histogram;
	}
	
	//Calculates histogram of an image
	public static double[] CalculateHistogram(Mat mat, double[] histogram) {
		
		int rows = mat.rows();
		int cols = mat.cols();
		double data[];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				data = mat.get(i, j);
				histogram[(int)data[0]]+=1;
			}
		}
		return histogram;
	}
	
	
	//Equalizes the pixels of an image
	public static Mat EqualizeImage(Mat mat, double[]histogram) {
		
		int rows = mat.rows();
		int cols = mat.cols();
		int min = FindMinIndex(histogram);
		System.out.println("Min:" + min);
		int max = FindMaxIndex(histogram);
		System.out.println("Max:" + max);
		Mat res = new Mat(rows,cols,0);

		double data[];
		
		//Equalize values
		for (int i = 0; i < rows; i++) {
			for (int j = 0 ; j < cols; j++) {
				data = mat.get(i, j);
				data[0] = ((data[0] - min)/(max - min))*255;
				res.put(i, j, data);
			}
		}
		
		//Recalculate the histogram
		histogram = Histogram.CalculateHistogram(res, histogram);
		return res;
	}
	
	//Creates a table has repeating values derived from the histogram frequency
	public static double[] HistogramToTable(double[]histogram) {
		ArrayList<Double> lst = new ArrayList<Double>();//To store and add values
		
		//histogram clone
		double[] tempHistogram = new double[256];
		for(int i=0;i<histogram.length;i++) tempHistogram[i]=histogram[i];
		
		//Loops until all values are stored in the list
		for (int i=0;i<histogram.length;i++) {
			
			while (tempHistogram[i]>0) {
				tempHistogram[i]-=1;
				double d = i;
				lst.add(d);
			}
		}
		double[] returnedArray = new double[lst.size()];
		//Adds all list values to the array to be returned
		for (int i=0; i<returnedArray.length;i++) returnedArray[i]=lst.get(i);
		return returnedArray;
	}
	
	//Creates a PNG file that represents the histogram
	public static void CreateHistogramPNG(double[] histogram, String filename) throws IOException {
		
		double [] values = Histogram.HistogramToTable(histogram);
		HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries("key", values, 255);
        JFreeChart histogramChart = ChartFactory.createHistogram("JFreeChart Histogram",
                               "Data", "Frequency", dataset);
        String path = "C:\\Users\\Youssef\\Desktop\\Histograms\\" + filename + ".png";
        ChartUtils.saveChartAsPNG(new File(path), histogramChart, 600, 400);
	}
	
	
	
}
