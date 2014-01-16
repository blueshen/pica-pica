import java.awt.*;
import java.awt.image.BufferedImage;

public class HistogramFilter {

    private int redBins;
    private int greenBins;
    private int blueBins;

    public HistogramFilter() {
        redBins = greenBins = blueBins = 4;
    }

    public void setRedBinCount(int redBinCount) {
        this.redBins = redBinCount;
    }

    public void setGreenBinCount(int greenBinCount) {
        this.greenBins = greenBinCount;
    }

    public void setBlueBinCount(int blueBinCount) {
        this.blueBins = blueBinCount;
    }

    public float[] filter(BufferedImage src, BufferedImage dest) {
        int width = src.getWidth();
        int height = src.getHeight();

        int[] inPixels = new int[width * height];
        float[] histogramData = new float[redBins * greenBins * blueBins];
        getRGB(src, 0, 0, width, height, inPixels);
        int index = 0;
        int redIdx = 0, greenIdx = 0, blueIdx = 0;
        int singleIndex = 0;
        float total = 0;
        for (int row = 0; row < height; row++) {
            int ta = 0, tr = 0, tg = 0, tb = 0;
            for (int col = 0; col < width; col++) {
                index = row * width + col;
                Color color = new Color(inPixels[index]);
                ta=color.getAlpha();
                tr=color.getRed();
                tg=color.getGreen();
                tb=color.getBlue();
//                ta = (inPixels[index] >> 24) & 0xff;
//                tr = (inPixels[index] >> 16) & 0xff;
//                tg = (inPixels[index] >> 8) & 0xff;
//                tb = inPixels[index] & 0xff;
                redIdx = (int) getBinIndex(redBins, tr, 255);
                greenIdx = (int) getBinIndex(greenBins, tg, 255);
                blueIdx = (int) getBinIndex(blueBins, tb, 255);
                singleIndex = redIdx + greenIdx * redBins + blueIdx * redBins * greenBins;
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

    public int[] getRGB(BufferedImage image, int x, int y, int width, int height, int[] pixels) {
        int type = image.getType();
        if (type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB)
            return (int[]) image.getRaster().getDataElements(x, y, width, height, pixels);
        return image.getRGB(x, y, width, height, pixels, 0, width);
    }

    public static double[] getHistogram2(BufferedImage img) {
        final int GRAYBIT = 4;
        int w = img.getWidth();
        int h = img.getHeight();
        int series = (int) Math.pow(2, GRAYBIT);	//GRAYBIT=4;用12位的int表示灰度值，前4位表示red,中间4们表示green,后面4位表示blue
        int greyScope = 256/series;
        double[] hist = new double[series*series*series];
        int r, g, b, index;
        int pix[] = new int[w*h];
        pix = img.getRGB(0, 0, w, h, pix, 0, w);
        for(int i=0; i<w*h; i++) {
            r = pix[i]>>16 & 0xff;
            r = r/greyScope;
            g = pix[i]>>8 & 0xff;
            g = g/greyScope;
            b = pix[i] & 0xff;
            b = b/greyScope;
            index = r<<(2*GRAYBIT) | g<<GRAYBIT | b;
            hist[index] ++;
        }
        for(int i=0; i<hist.length; i++) {
            hist[i] = hist[i]/(w*h);
            //System.out.println(hist[i] + "  ");
        }
        return hist;
    }

}
