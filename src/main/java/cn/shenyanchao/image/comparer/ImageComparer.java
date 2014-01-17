package cn.shenyanchao.image.comparer;

import cn.shenyanchao.image.entity.ImageCompareResult;
import javax.imageio.ImageIO;
import javax.media.jai.Interpolation;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.ScaleDescriptor;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.UUID;

/**
 * Created by shenyanchao on 14-1-15.
 *
 * @author shenyanchao
 */
public class ImageComparer {

    private BufferedImage sourceImage;
    private BufferedImage candidateImage;
    private BufferedImage changedImage;
    private final int COL = 20;
    private final int ROW = 20;

    int eachBlockWidth = 0;
    int eachBlockHeight = 0;

    private final double SIMILARITY_THRESHOLD = 0.8;

    public boolean compare() {
        resizeCandidateImage();
        boolean match = true;
        double similarity = ImageSimilarity.getSimilarty(sourceImage, candidateImage);
        if (similarity < SIMILARITY_THRESHOLD) {
            match = false;
        }
        System.out.println("相似度：" + similarity);
        return false;
    }

    public ImageCompareResult compareWithBlock() throws IOException {
        resizeCandidateImage();
        ImageCompareResult result = new ImageCompareResult();
        boolean match = true;
        int width = sourceImage.getWidth();
        int height = sourceImage.getHeight();
        changedImage = candidateImage;

        eachBlockWidth = width / COL;
        eachBlockHeight = height / ROW;
        for (int x = 0; x + eachBlockWidth <= width; x += eachBlockWidth) {
            for (int y = 0; y + eachBlockHeight <= height; y += eachBlockHeight) {
                BufferedImage sourceBlockImage = sourceImage.getSubimage(x, y, eachBlockWidth, eachBlockHeight);
                BufferedImage candicateBlockImage = candidateImage.getSubimage(x, y, eachBlockWidth, eachBlockHeight);
                double blockSimilarity = ImageSimilarity.getSimilarty(sourceBlockImage, candicateBlockImage);
                System.out.println(blockSimilarity);
                if (blockSimilarity < SIMILARITY_THRESHOLD) {
                    paintDiff(changedImage, x, y, eachBlockWidth, eachBlockHeight, blockSimilarity);
                    match = false;
                }
            }
        }
        String uuid = UUID.randomUUID().toString();
        ImageIO.write(changedImage, "png", new FileOutputStream(new File("/home/shenyanchao/git/pica-pica" +
                "/src/main/webapp/upload/"+uuid+".png")));
        result.setMatch(match);
        result.setDiffImageId(uuid);
        return result;
    }

    private void paintDiff(BufferedImage bImage, int x, int y, int weight, int height, double blockSimilarity) {
        BufferedImage imgc = bImage;
        Graphics2D gc = imgc.createGraphics();
        gc.setColor(Color.RED);
        gc.drawRect(x, y, weight, height);
        String similarityStr = String.format("%.2f", blockSimilarity);
        gc.setColor(Color.CYAN);
        gc.drawString(similarityStr, x + eachBlockWidth / 2, y + eachBlockHeight / 2);
    }

    // constructor 1. use filenames
    public ImageComparer(String file1, String file2) {
        this(loadJPG(file1), loadJPG(file2));
    }

    // constructor 2. use awt images.
    public ImageComparer(Image img1, Image img2) {
        this(imageToBufferedImage(img1), imageToBufferedImage(img2));
    }

    // constructor 3. use buffered images. all roads lead to the same place. this place.
    public ImageComparer(BufferedImage sourceImage, BufferedImage candidateImage) {
        this.sourceImage = sourceImage;
        this.candidateImage = candidateImage;
    }

    // buffered images are just better.
    private static BufferedImage imageToBufferedImage(Image img) {
        BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bi.createGraphics();
        g2.drawImage(img, null, null);
        return bi;
    }

    public BufferedImage getChangedImage() {
        return changedImage;
    }

    // read a jpeg file into a buffered image
    protected static Image loadJPG(String filename) {
        FileInputStream in = null;
        BufferedImage bi = null;
        try {
            in = new FileInputStream(filename);
            bi = ImageIO.read(in);
        } catch (FileNotFoundException io) {
            System.out.println("File Not Found");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bi;
    }

    private  void resizeCandidateImage(){
        int sourceX = sourceImage.getWidth();
        int sourceY = sourceImage.getHeight();
        int candidateX = candidateImage.getWidth();
        int candidateY = candidateImage.getHeight();
        float xScale = sourceX/(float)candidateX;
        float yScale = sourceY/(float)candidateY;
        RenderedOp renderedOp = ScaleDescriptor.create(candidateImage, new Float(xScale), new Float(yScale),
                new Float(0.0f), new Float(0.0f), Interpolation.getInstance(Interpolation.INTERP_BICUBIC), null);
        candidateImage = renderedOp.getAsBufferedImage();
    }
}
