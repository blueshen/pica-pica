package cn.shenyanchao.image.comparer;

import cn.shenyanchao.image.entity.ImageCompareResult;
import cn.shenyanchao.image.entity.ImageForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import javax.media.jai.Interpolation;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.ScaleDescriptor;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.UUID;

/**
 * @author shenyanchao
 * @date 14-1-15.
 */

@Service
public class ImageComparer {

    private static final Logger LOG = LoggerFactory.getLogger(ImageComparer.class);
    private int splitRow;
    private int splitCol;
    private double similarityThreshold; //just experience

    static {
        System.setProperty("com.sun.media.jai.disableMediaLib", "true");
    }

    @Autowired
    private ImageSimilarity imageSimilarity;
    private BufferedImage sourceImage;
    private BufferedImage candidateImage;
    private BufferedImage changedImage;
    private int eachBlockWidth = 0;
    private int eachBlockHeight = 0;

    private String path;

    public ImageComparer() {
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

    // read a jpeg file into a buffered image
    protected static Image loadJPG(String filename) {
        FileInputStream in = null;
        BufferedImage bi = null;
        try {
            in = new FileInputStream(filename);
            bi = ImageIO.read(in);
        } catch (FileNotFoundException io) {
            LOG.error("File Not Found");
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }

        return bi;
    }

    public void populateConfig(ImageForm imageForm,String path) {
        this.splitRow = imageForm.getRow();
        this.splitCol = imageForm.getCol();
        this.similarityThreshold = imageForm.getSimilarityThreshold();
        this.path = path;

    }

    public void setSplitRow(int splitRow) {
        this.splitRow = splitRow;
    }

    public void setSplitCol(int splitCol) {
        this.splitCol = splitCol;
    }

    public void setSimilarityThreshold(double similarityThreshold) {
        this.similarityThreshold = similarityThreshold;
    }

    public void setSourceImage(BufferedImage sourceImage) {
        this.sourceImage = sourceImage;
    }

    public void setCandidateImage(BufferedImage candidateImage) {
        this.candidateImage = candidateImage;
    }

    public boolean compare() {
        resizeCandidateImage();
        boolean match = true;
        double similarity = imageSimilarity.getSimilarty(sourceImage, candidateImage);
        if (similarity < similarityThreshold) {
            match = false;
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("相似度：{}", similarity);
        }
        return false;
    }

    public ImageCompareResult compareWithBlock() throws IOException {
        resizeCandidateImage();
        ImageCompareResult result = new ImageCompareResult();
        boolean match = true;
        int width = sourceImage.getWidth();
        int height = sourceImage.getHeight();
        changedImage = candidateImage;

        eachBlockWidth = width / splitCol;
        eachBlockHeight = height / splitRow;
        for (int x = 0; x + eachBlockWidth <= width; x += eachBlockWidth) {
            for (int y = 0; y + eachBlockHeight <= height; y += eachBlockHeight) {
                BufferedImage sourceBlockImage = sourceImage.getSubimage(x, y, eachBlockWidth, eachBlockHeight);
                BufferedImage candicateBlockImage = candidateImage.getSubimage(x, y, eachBlockWidth, eachBlockHeight);
                double blockSimilarity = imageSimilarity.getSimilarty(sourceBlockImage, candicateBlockImage);
                if (LOG.isDebugEnabled()) {
                    LOG.debug(String.valueOf(blockSimilarity));
                }
                if (blockSimilarity < similarityThreshold) {
                    paintDiff(changedImage, x, y, eachBlockWidth, eachBlockHeight, blockSimilarity);
                    match = false;
                }
            }
        }
        String uuid = UUID.randomUUID().toString();
        ImageIO.write(changedImage, "png", new FileOutputStream(new File(path +File.separator+ uuid + ".png")));
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

    public BufferedImage getChangedImage() {
        return changedImage;
    }

    private void resizeCandidateImage() {
        int sourceX = sourceImage.getWidth();
        int sourceY = sourceImage.getHeight();
        int candidateX = candidateImage.getWidth();
        int candidateY = candidateImage.getHeight();
        float xScale = sourceX / (float) candidateX;
        float yScale = sourceY / (float) candidateY;
        RenderedOp renderedOp = ScaleDescriptor.create(candidateImage, new Float(xScale), new Float(yScale),
                new Float(0.0f), new Float(0.0f), Interpolation.getInstance(Interpolation.INTERP_BICUBIC), null);
        candidateImage = renderedOp.getAsBufferedImage();
    }
}
