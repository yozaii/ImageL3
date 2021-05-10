package tp;

import java.io.IOException;

import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

public class Exo2 {
	
	//Fills table with zeros
	public static void InitTable(int[] table) {
		
		for (int i = 0 ; i < table.length ; i++) {
			table[i] = 0;
		}
	}
	
	
	//Displays table
	public static void DisplayTable(int[] table) {
		for (int i = 0 ; i < table.length; i++) {
			System.out.println(i + ": " + table[i]);
		}
	}
	
	
	//Finds first non zero value in table
	public static int FindMin(int[] table) {
		int i = 0;
		while (table[i]==0) i++;
		return i;
	}
	
	
	//Finds last non zero value in table
	public static int FindMax(int[] table) {
		
		int max = 0;
		for (int i = 0; i < table.length ; i++) {
			if (table[i]!=0) max = i;
		}
		return max;
	}
	
	
	
	
	public static void main(String[] args) throws IOException {
		//nu.pattern.OpenCV.loadShared();
		nu.pattern.OpenCV.loadLocally();
		
		String path = "D:\\UE_Image\\Base_Image\\Base\\70.jpeg";//Remplir ici ou se trouve le fichier

		Mat mat;
		mat = Imgcodecs.imread(path,0);//Reading image and loading it into a matrix
		HighGui.imshow("test",mat);
		
		int histogram[] = new int[256];
		double data[];
		
		
		//initialize to 0
		for (int i =0 ; i< histogram.length ; i++) {
			histogram[i]= 0;
		}
		
		
		int rows = mat.rows();
		int cols = mat.cols();
		Mat res = new Mat(rows, cols, 0);
		
		//Histogram filling
		InitTable(histogram);
		
		//Calculate Histogram
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				data = mat.get(i, j);
				histogram[(int)data[0]]+=1;
			}
		}
		
		//Histogram display
		DisplayTable(histogram);
		System.out.println("***************");
		
		
		int min = FindMin(histogram);
		System.out.println("Min:" + min);
		int max = FindMax(histogram);
		System.out.println("Max:" + max);
		
		//Equalize values
		for (int i = 0; i < rows; i++) {
			for (int j = 0 ; j < cols; j++) {
				data = mat.get(i, j);
				data[0] = ((data[0] - min)/(max - min))*256;
				res.put(i, j, data);
			}
		}
		
		int histogram2[] = new int[256];
		InitTable(histogram2);
		//Calculate histogram2
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				data = res.get(i, j);
				histogram2[(int)data[0]]+=1;
			}
		}
		
		DisplayTable(histogram2);
		
		HighGui.imshow("result", res);
		HighGui.waitKey();
		
	}
}
