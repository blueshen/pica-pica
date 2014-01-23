package cn.shenyanchao.image.comparer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.media.jai.Histogram;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.HistogramDescriptor;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.SampleModel;

/**
 * @date 14-1-15.
 * @author shenyanchao
 */
@Component
public class HistogramFactory {

    private static final Logger LOG = LoggerFactory.getLogger(HistogramFactory.class);
    private static final int RED_CHANNEL = 16;
    private static final int GREEN_CHANNEL = 16;
    private static final int BLUE_CHANNEL = 16;

    public double[] getHistogram(BufferedImage image) {
        PlanarImage pImage = PlanarImage.wrapRenderedImage(image);
        SampleModel sampleModel = pImage.getSampleModel();
        int bandCount = sampleModel.getNumBands(); //确定有几个band，RGB OR RGBA OR CMY OR CMYB
        int bits = DataBuffer.getDataTypeSize(sampleModel.getDataType());//图片是多少位数表示的,用于确定灰度最大值
        int width = image.getWidth();
        int height = image.getHeight();
        double[] histogramData = new double[RED_CHANNEL * GREEN_CHANNEL * BLUE_CHANNEL];
        int[] inPixels = getRGB(image, 0, 0, width, height);
        int ta = 0, tr = 0, tg = 0, tb = 0;
        for (int index = 0; index < inPixels.length; index++) {
            Color color = new Color(inPixels[index]);
            tr = color.getRed();
            tg = color.getGreen();
            tb = color.getBlue();

            int redIdx = (int) getBinIndex(RED_CHANNEL, tr);
            int greenIdx = (int) getBinIndex(GREEN_CHANNEL, tg);
            int blueIdx = (int) getBinIndex(BLUE_CHANNEL, tb);
            int singleIndex = getIndex(redIdx, greenIdx, blueIdx);
            histogramData[singleIndex] += 1;
        }
        double totalPixels = inPixels.length;// or weight * height
        for (int i = 0; i < histogramData.length; i++) {
            histogramData[i] = histogramData[i] / totalPixels;
        }

        return histogramData;
    }

    public Histogram getHistogramWithJAI(BufferedImage bImage) {
        PlanarImage image = PlanarImage.wrapRenderedImage(bImage);
        SampleModel sampleModel = image.getSampleModel();
        int bandCount = sampleModel.getNumBands(); //确定有几个band，RGB OR RGBA OR CMY OR CMYB
        int bits = DataBuffer.getDataTypeSize(sampleModel.getDataType());//图片是多少位数表示的,用于确定灰度最大值
        System.out.println("bits=" + bits);
        int[] binz = new int[bandCount];
        double[] low = new double[bandCount];
        double[] high = new double[bandCount];
        int max = 1 << bits;
        LOG.debug("max=" + max);

        for (int i = 0; i < bandCount; i++) {
            binz[i] = 256;
            low[i] = 0f;
            high[i] = max;
        }
        RenderedOp op = HistogramDescriptor.create(image, null, 1, 1, binz, low, high, null);
        Histogram histogramx = (Histogram) op.getProperty("histogram");
        return histogramx;
    }

    //RGB三维降到一维数组
    public int[] threeDimension2OneDimension(Histogram histogram) {

        int[] histogramData = new int[RED_CHANNEL * GREEN_CHANNEL * BLUE_CHANNEL];
        int bands = histogram.getNumBands();
        if (bands < 2) {
            LOG.error("not a rgb histogram");
        }
        int[] redBins = histogram.getBins(0);
        int[] greenBins = histogram.getBins(1);
        int[] blueBins = histogram.getBins(2);

        for (int i = 0; i < 256; i++) {

        }

        return histogramData;

    }

    private int getIndex(int redIndex, int greenIndex, int blueIndex) {
        return redIndex + greenIndex * RED_CHANNEL + blueIndex * RED_CHANNEL * GREEN_CHANNEL;
    }

    public void printBand(int band, int[] bins) {
        for (int i = 0; i < bins.length; i++) {
            System.out.println("(" + band + "," + i + ")=" + bins[i]);
        }
    }

    /**
     * 看颜色落在那个区间
     *
     * @param binCount
     * @param color
     * @return
     */
    private float getBinIndex(int binCount, int color) {
        int colorMaxValue = 255;
        float binIndex = (((float) color) / ((float) colorMaxValue)) * ((float) binCount);
        if (binIndex >= binCount) {
            binIndex = binCount - 1;
        }
        return binIndex;
    }

    /**
     * 获取图像的像素值
     *
     * @param image
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    public int[] getRGB(BufferedImage image, int x, int y, int width, int height) {
        int type = image.getType();
        if (type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB) {
            return (int[]) image.getRaster().getDataElements(x, y, width, height, null);
        }
        return image.getRGB(x, y, width, height, null, 0, width);
    }


}
