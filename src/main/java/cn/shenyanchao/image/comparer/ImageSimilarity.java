package cn.shenyanchao.image.comparer;

import java.awt.image.BufferedImage;

/**
 * Created by shenyanchao on 14-1-15.
 *
 * @author shenyanchao
 */
public class ImageSimilarity {

    /**
     * Bhattacharyya Coefficient
     * http://www.cse.yorku.ca/~kosta/CompVis_Notes/bhattacharyya.pdf
     *
     * @return
     */
    public static double getSimilarty(BufferedImage sourceImage, BufferedImage candidateImage) {
        HistogramFactory hFactory = new HistogramFactory();
        double[] sourceData = hFactory.getHistogram(sourceImage);
        double[] candidateData = hFactory.getHistogram(candidateImage);
        double[] mixedData = new double[sourceData.length];
        for (int i = 0; i < sourceData.length; i++) {
            mixedData[i] = Math.sqrt(sourceData[i] * candidateData[i]);
        }

        // The values of Bhattacharyya Coefficient ranges from 0 to 1,
        double similarity = 0.0;
        for (int i = 0; i < mixedData.length; i++) {
            similarity += mixedData[i];
        }

        // The degree of similarity
//        if (similarity > 1) {
//            similarity = 1.0;
//        } else if (similarity < 0.0) {
//            similarity = 0.0;
//        }
        return similarity;
    }
}
