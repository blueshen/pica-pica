package cn.shenyanchao.image.compare;

import org.junit.Test;

import javax.imageio.ImageIO;
import javax.media.jai.*;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.IOException;

/**
 * Created by shenyanchao on 14-1-21.
 */
public class HistogramExample {
    static
    {
        System.setProperty("com.sun.media.jai.disableMediaLib", "true");
    }

    // Retrieves a histogram for the image.
    private Histogram getHistogram1(RenderedOp img,
                                   int binCount) throws IOException {
        BufferedImage image = ImageIO.read(new File("/home/shenyanchao/git/pica-pica/src/test/resources/test1.jpg"));
//        RenderedOp img = image.getSampleModel().getNumBands();
// Get the band count.
        int numBands = image.getSampleModel().getNumBands();
// Allocate histogram memory.
        int[] numBins = new int[numBands];
        double[] lowValue = new double[numBands];
        double[] highValue = new double[numBands];
        for (int i = 0; i < numBands; i++) {
            numBins[i] = binCount;
            lowValue[i] = 0.0;
            highValue[i] = 255.0;
        }
// Create the Histogram object.
        Histogram hist = new Histogram(numBins, lowValue, highValue);
// Set the ROI to the entire image.
        ROIShape roi = new ROIShape(img.getBounds());
// Create the histogram op.
        RenderedOp histImage =
                JAI.create("histogram", img,
                        hist, roi, new Integer(1), new Integer(1));
// Retrieve the histogram.
        hist = (Histogram) histImage.getProperty("histogram");
        return hist;
    }

    public Histogram getHistogram() throws IOException {

        PlanarImage image = JAI.create("fileload", "/home/shenyanchao/git/pica-pica/src/test/resources/test1.jpg");
        int numbands =   image.getSampleModel().getNumBands();
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(image);
        pb.add(null); // The ROI.
        pb.add(1); // Samplings.
        pb.add(1);
        pb.add(new int[]{256}); // Num. bins.
        pb.add(new double[]{0}); // Min value to be considered.
        pb.add(new double[]{256}); // Max value to be considered.
        // Creates the histogram.
        PlanarImage temp = JAI.create("histogram", pb);
        Histogram h = (Histogram) temp.getProperty("histogram");
        return h;
    }

    @Test
    public void test() throws IOException {
        HistogramExample histogramExample = new HistogramExample();
        Histogram h = histogramExample.getHistogram();
        int[][] bins = h.getBins();
        for ( int i = 0; i < bins.length; i++ ) {
            for ( int j = 0; j < bins[i].length; j++ ) {
                System.out.println("(" + i + "," + j + ")=" + bins[i][j]);
            }
        }
    }
}
