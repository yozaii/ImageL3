package projet;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class PreProcessing {
	
	
	public static Mat resizeMat(Mat mat, int x, int y) {
		Mat resizedMat = new Mat();
		Size dsize = new Size(x, y);
		Imgproc.resize(mat, resizedMat, dsize);
		return resizedMat;
	}
}
