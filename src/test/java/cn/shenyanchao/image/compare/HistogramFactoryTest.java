package cn.shenyanchao.image.compare;

import cn.shenyanchao.image.comparer.HistogramFactory;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by shenyanchao on 14-1-22.
 */
public class HistogramFactoryTest {

    @Test
    public  void getHistogramTest() throws IOException {
        File f = new File("/home/shenyanchao/git/pica-pica/src/test/resources/test1.jpg");
        BufferedImage image = ImageIO.read(f);
        HistogramFactory histogramFactory = new HistogramFactory();
        histogramFactory.getHistogram(image);
    }
}
