package cn.shenyanchao.image.comparer;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by shenyanchao on 14-1-15.
 * @author shenyanchao
 */
public class HistogramFactory {
    private  int redBinCount = 4;
    private  int greenBinCount = 4;
    private  int blueBinCount =4 ;

    public HistogramFactory() {

    }

    public void setRedBinCount(int redBinCount) {
        this.redBinCount = redBinCount;
    }

    public void setGreenBinCount(int greenBinCount) {
        this.greenBinCount = greenBinCount;
    }

    public void setBlueBinCount(int blueBinCount) {
        this.blueBinCount = blueBinCount;
    }

    public double[] getHistogram(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
//        int[] inPixels = new int[width * height];
        double[] histogramData = new double[redBinCount * greenBinCount * blueBinCount];
        int[] inPixels = getRGB(image, 0, 0, width, height);
        int index = 0;
        int redIdx = 0, greenIdx = 0, blueIdx = 0;
        int singleIndex = 0;
        double total = 0.0;
        for (int row = 0; row < height; row++) {
            int ta = 0, tr = 0, tg = 0, tb = 0;
            for (int col = 0; col < width; col++) {
                index = row * width + col;
                Color color = new Color(inPixels[index]);
//                ta=color.getAlpha();
                tr=color.getRed();
                tg=color.getGreen();
                tb=color.getBlue();
                redIdx = (int) getBinIndex(redBinCount, tr, 255);
                greenIdx = (int) getBinIndex(greenBinCount, tg, 255);
                blueIdx = (int) getBinIndex(blueBinCount, tb, 255);
                singleIndex = redIdx + greenIdx * redBinCount + blueIdx * redBinCount * greenBinCount;
                histogramData[singleIndex] += 1;
                total += 1;
            }
        }

        // start to normalize the histogram data
        for (int i = 0; i < histogramData.length; i++) {
            histogramData[i] = histogramData[i] / total;
        }

        return histogramData;
    }

    private float getBinIndex(int binCount, int color, int colorMaxValue) {
        float binIndex = (((float) color) / ((float) colorMaxValue)) * ((float) binCount);
        if (binIndex >= binCount){
            binIndex = binCount - 1;
        }
        return binIndex;
    }

    public int[] getRGB(BufferedImage image, int x, int y, int width, int height) {
        int type = image.getType();
        if (type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB)
            return (int[]) image.getRaster().getDataElements(x, y, width, height, null);
        return image.getRGB(x, y, width, height, null, 0, width);
    }



}
