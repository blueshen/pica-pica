package cn.shenyanchao.image;

import java.awt.image.BufferedImage;

public class ImageComparer {
	private BufferedImage sourceImage;
	private BufferedImage candidateImage;
	
	public ImageComparer(BufferedImage srcImage, BufferedImage canImage) {
		this.sourceImage = srcImage;
		this.candidateImage = canImage;
	}
	
	/**
	 * Bhattacharyya Coefficient
	 * http://www.cse.yorku.ca/~kosta/CompVis_Notes/bhattacharyya.pdf
	 * 
	 * @return
	 */
	public double modelMatch() {
		HistogramFilter hfilter = new HistogramFilter();
		float[] sourceData = hfilter.filter(sourceImage, null);
		float[] candidateData = hfilter.filter(candidateImage, null);
		double[] mixedData = new double[sourceData.length];
		for(int i=0; i<sourceData.length; i++ ) {
			mixedData[i] = Math.sqrt(sourceData[i] * candidateData[i]);
		}
		
		// The values of Bhattacharyya Coefficient ranges from 0 to 1,
		double similarity = 0;
		for(int i=0; i<mixedData.length; i++ ) {
			similarity += mixedData[i];
		}
		
		// The degree of similarity
		return similarity;
	}

}
