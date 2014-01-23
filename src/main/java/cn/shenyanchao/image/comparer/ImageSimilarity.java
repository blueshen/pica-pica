package cn.shenyanchao.image.comparer;

import cn.shenyanchao.image.algorithm.IDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

/**
 * Created by shenyanchao on 14-1-15.
 *
 * @author shenyanchao
 */
@Component
public class ImageSimilarity {

    @Qualifier("bhattacharyyaDistance")
    @Autowired
    private IDistance distance;

    @Autowired
    private HistogramFactory histogramFactory;

    /**
     *
     * @return
     */
    public  double getSimilarty(BufferedImage sourceImage, BufferedImage candidateImage) {

        double[] sourceData = histogramFactory.getHistogram(sourceImage);
        double[] candidateData = histogramFactory.getHistogram(candidateImage);

        double similarity = distance.distance(sourceData,candidateData);

        // The degree of similarity
        if (similarity > 1.0) {
            similarity = 1.0;
        } else if (similarity < 0.0) {
            similarity = 0.0;
        }
        return similarity;
    }
}
