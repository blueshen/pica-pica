package com.is.utils;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
 
import javax.imageio.ImageIO;
import javax.swing.JPanel;

import net.coobird.thumbnailator.Thumbnails;

public class ImagePanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4855002270685382206L;
	File imageFile;
	int width, height;
	
	/**
	 * @param args
	 */
	private BufferedImage image;
	
	public ImagePanel() {
        super();
    }
	
	public void create(File _imageFile, int _width, int _height)
	{
		imageFile = _imageFile;
		width = _width;
		height = _height;
		try
		{
		   image = ImageIO.read(imageFile);
		   image = Thumbnails.of(image).size(width, height).asBufferedImage();
		   
		   Dimension dimension = new Dimension(image.getWidth(), image.getHeight());
		   setPreferredSize(dimension);
		}
	     catch (IOException e)
	     {
	        System.err.println("Error with loading an image.");
	        e.printStackTrace();
	    }
	}
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(image, 0, 0, this);
    }
}