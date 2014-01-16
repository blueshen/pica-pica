package com.is.utils;

import java.awt.image.RenderedImage;
import java.io.File;

/**
 * Simple class for holding all results of algorithm in one class
 * 
 * @author Grzegorz Polek <grzegorz.polek@gmail.com>
 */
public class ImageHolder {

	private File file = null;
	private RenderedImage render = null;
	private double distance = 11041; // 0 - 11041
	
	/**
	 * Constructor
	 * 
	 * @param file
	 * @param distance
	 * 
	 * @author Grzegorz Polek <grzegorz.polek@gmail.com>
	 */
	public ImageHolder(File file, double distance)
	{
		this.file = file;
		this.distance = distance;
	}
	
	/**
	 * Getter for file
	 * 
	 * @return
	 */
	public File getFile()
	{
		return this.file;
	}
	
	/**
	 * Getter for distance between reference's vectors and this image's vectors
	 * 
	 * @return
	 */
	public double getDistance()
	{
		return this.distance;
	}
	
	/**
	 * Getter for difference between reference and this file
	 * 
	 * @return
	 */
	public double getDifference()
	{
		return (double)((this.distance * 100) / 11041);
	}
	
	/**
	 * Getter for similarity between reference and this file
	 * 
	 * @return
	 */
	public double getSimilarity()
	{
		return (double)(100 - this.getDifference());
	}
	
}
