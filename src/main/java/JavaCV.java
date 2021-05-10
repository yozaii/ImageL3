import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

public class JavaCV {
	
	public static void main(String[] args) throws IOException {
		//nu.pattern.OpenCV.loadShared();
		nu.pattern.OpenCV.loadLocally();
		
		
		String path = "C:\\Users\\Youssef\\Documents\\Test_Images\\fountainSaltPepper.png";//Remplir ici ou se trouve le fichier

		Mat  mat;
		mat = Imgcodecs.imread(path);//Reading image and loading it into a matrix
		HighGui.imshow("test",mat);
		HighGui.waitKey();

		
		
		int rows = mat.rows();
		int cols = mat.cols();
		
		double seuil = 70; //la valeur de seuil
		double val = 145;//la nouvelle valeur si on depasse le seuil
		double data[];

		
		Mat result = new Mat(rows,cols,1);
		for (int i = 0; i<rows ; i++) {
			for (int j = 0 ; j < cols ; j++) {
				data = mat.get(i, j);
				if (data[0] < seuil) data[0] = val;
				result.put(i, j, data);
			}
		}
		
		HighGui.imshow("Result",result);
		HighGui.waitKey();
		
		/*Writing image to path with result matrix*/
		Imgcodecs.imwrite("C:\\Users\\Youssef\\Documents\\Test_Images\\fountaintest.png",result);//Dans la partie "" remplire ou ecrire le fichier
		System.out.println("Test");
		System.out.println("mat=" +  mat.dump());
		
	}

}
