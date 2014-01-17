package cn.shenyanchao.image;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageComparerUI extends JComponent implements ActionListener {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JButton browseBtn;
    private JButton histogramBtn;
    private JButton compareBtn;
    private Dimension mySize;

    // image operator
    private MediaTracker tracker;
    private BufferedImage sourceImage;
    private BufferedImage candidateImage;
    private double simility;

    // command constants
    public final static String BROWSE_CMD = "Browse...";
    public final static String HISTOGRAM_CMD = "Histogram Bins";
    public final static String COMPARE_CMD = "Compare Result";

    public ImageComparerUI() {
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        browseBtn = new JButton("Browse...");
        histogramBtn = new JButton("Histogram Bins");
        compareBtn = new JButton("Compare Result");

        // buttons
        btnPanel.add(browseBtn);
        btnPanel.add(histogramBtn);
        btnPanel.add(compareBtn);

        // setup listener...
        browseBtn.addActionListener(this);
        histogramBtn.addActionListener(this);
        compareBtn.addActionListener(this);

        mySize = new Dimension(620, 500);
        JFrame demoUI = new JFrame("Similiar Image Finder");
        demoUI.getContentPane().setLayout(new BorderLayout());
        demoUI.getContentPane().add(this, BorderLayout.CENTER);
        demoUI.getContentPane().add(btnPanel, BorderLayout.SOUTH);
        demoUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        demoUI.pack();
        demoUI.setVisible(true);
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (sourceImage != null) {
            Image scaledImage = sourceImage.getScaledInstance(300, 300, Image.SCALE_FAST);
            g2.drawImage(scaledImage, 0, 0, 300, 300, null);
        }

        if (candidateImage != null) {
            Image scaledImage = candidateImage.getScaledInstance(300, 330, Image.SCALE_FAST);
            g2.drawImage(scaledImage, 310, 0, 300, 300, null);
        }

        // display compare result info here
        Font myFont = new Font("Serif", Font.BOLD, 16);
        g2.setFont(myFont);
        g2.setPaint(Color.RED);
        g2.drawString("The degree of similarity : " + simility, 50, 350);
    }

    public void actionPerformed(ActionEvent e) {
        if (BROWSE_CMD.equals(e.getActionCommand())) {
            JFileChooser chooser = new JFileChooser();
            chooser.showOpenDialog(null);
            File f = chooser.getSelectedFile();
            BufferedImage bImage = null;
            if (f == null) return;
            try {
                bImage = ImageIO.read(f);

            } catch (IOException e1) {
                e1.printStackTrace();
            }

            tracker = new MediaTracker(this);
            tracker.addImage(bImage, 1);

            // blocked 10 seconds to load the image data
            try {
                if (!tracker.waitForID(1, 10000)) {
                    System.out.println("Load error.");
                    System.exit(1);
                }// end if
            } catch (InterruptedException ine) {
                ine.printStackTrace();
                System.exit(1);
            } // end catch

            if (sourceImage == null) {
                sourceImage = bImage;
            } else if (candidateImage == null) {
                candidateImage = bImage;
            } else {
                sourceImage = null;
                candidateImage = null;
            }
            repaint();
        } else if (HISTOGRAM_CMD.equals(e.getActionCommand())) {
            // do something now...
        } else if (COMPARE_CMD.equals(e.getActionCommand())) {
            ImageComparer imageCom = new ImageComparer(sourceImage, candidateImage);
            simility = imageCom.modelMatch();
            repaint();
        }

    }

    public Dimension getPreferredSize() {
        return mySize;
    }

    public Dimension getMinimumSize() {
        return mySize;
    }

    public Dimension getMaximumSize() {
        return mySize;
    }

    public static void main(String[] args) {
        new ImageComparerUI();
    }

}
