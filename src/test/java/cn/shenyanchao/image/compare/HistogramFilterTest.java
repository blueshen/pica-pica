package cn.shenyanchao.image.compare;

import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.media.jai.Histogram;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.HistogramDescriptor;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.SampleModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author shenyanchao
 */
public class HistogramFilterTest {

    private HistogramFilter histogramFilter = new HistogramFilter();

    @Before
    public void before() {

        System.setProperty("com.sun.media.jai.disableMediaLib", "true");

    }

    @Test
    public void filterTest() throws IOException {
        File f = new File("/home/shenyanchao/git/image-similarity/src/test/resources/image/artists_03.jpeg");
        BufferedImage bImage = ImageIO.read(f);
        float[] histogram = histogramFilter.filter(bImage, null);
        for (float h : histogram) {
            System.out.println(h);
        }


        PlanarImage image = PlanarImage.wrapRenderedImage(bImage);
        SampleModel sampleModel = image.getSampleModel();
        int bandCount = sampleModel.getNumBands();
        int bits = DataBuffer.getDataTypeSize(sampleModel.getDataType());
        int[] binz = new int[bandCount];
        double[] min = new double[bandCount];
        double[] max = new double[bandCount];
        int maxxx = 1 << bits;

        for (int i = 0; i < bandCount; i++) {
            binz[i] = 8;
            min[i] = 0;
            max[i] = maxxx;
        }
        RenderedOp op = HistogramDescriptor.create(image, null, 1, 1, binz, min, max, null);
        Histogram histogramx = (Histogram) op.getProperty("histogram");
        for (int i = 0; i < histogramx.getBins().length; i++) {
            for (int j = 0; j < histogramx.getBins()[i].length; j++)
            System.out.println("(" + i + "," + j + ")=" +histogramx.getBins()[i][j]);
        }

        BufferedImage imgc = bImage;
        Graphics2D gc = imgc.createGraphics();
        gc.setColor(Color.RED);
        gc.drawRect(50, 50, 50, 50);
        ImageIO.write(imgc,"jpeg",new FileOutputStream(new File("/home/shenyanchao/git/image-similarity" +
                "/src/test/resources/addredchange.jpeg")));
//        ImageCompare.saveJPG(imgc,"/home/shenyanchao/git/image-similarity/src/test/resources/addredchange.jpeg");
    }
}
