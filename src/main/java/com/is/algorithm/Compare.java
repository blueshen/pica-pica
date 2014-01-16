package com.is.algorithm;

import java.awt.Color;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;

import com.is.filters.JPEGFilter;
import com.is.general.ImageSimilarity;
import com.is.general.UserInterface;
import com.is.utils.ImageHolder;

/**
 * This class uses a very simple, naive similarity algorithm to compare an image
 * with all images in specified directory
 * 
 * @author Grzegorz Polek <grzegorz.polek@gmail.com>
 * @author Lukasz Pycia <fryta1990@gmail.com>
 */
public class Compare extends Thread
{
	// The reference image "signature" (25 representative pixels, each in R,G,B).
	// We use instances of Color to make things simpler.
	private Color[][] signature;
	
	// The base size of the images.
	private static final int baseSize = 300;
	
	// Where are all the files?
	private static final String basePath = "/";
	  
	private List<ImageHolder> images = null;
	private File[] others = null;
	private RenderedImage[] rothers = null;
	private double[] distances = null;
	private Thread[] compareThread;
	private boolean[] threadWorks;
	private int threads;
	public long waitMills=100;
	
	private double progress;
	private double theProgress;
	
	/**
	 * Constructor for comparing reference file with all images inside specified directory.
	 * 
	 * @param reference	the reference file
	 * @param directory	the directory with files to compare with reference
	 * @param _threads	number of threads to run on
	 * @see java.io.File
	 * 
	 * @author Grzegorz Polek <grzegorz.polek@gmail.com>
	 * @author Lukasz Pycia <fryta1990@gmail.com>
	 * @throws Exception 
	 */
	public Compare(File reference, File directory, int _threads) throws Exception
	{
		// Number of threads
		threads = _threads;
		compareThread = new Thread[threads];
		threadWorks = new boolean[threads];
	
		// Progress bar
		theProgress = 0;
		
		// Scale image
		RenderedImage ref = rescale(ImageIO.read(reference));
		
		// Calculate the signature vector for the reference.
	    signature = calcSignature(ref);
	    
	    // Now we need a component to store X images in a stack, where X is the
	    // number of images in specified directory.
	    others = getOtherImageFiles(directory);
	    
	    if(others.length <= 0)
	    {
	    	UserInterface.getInstance().showError("There are no .jpg images in specified directory!");
	    	ImageSimilarity.getInstance().setImage(null);
	    	ImageSimilarity.getInstance().setDirectory(null);
	    	
	    	throw new Exception("No .jpg images");
	    }
	    
	    progress = 100/others.length;
	    
	    // For each image, calculate its signature and its distance from the
	    // reference signature.
	    rothers = new RenderedImage[others.length];
	    
	    distances = new double[others.length];
	    
	    for(int i=0;i<threads;i++)
		{
	    	compareThread[i] = new Thread(new CompareRunnable(i));
	    	threadWorks[i]=true;
	    	compareThread[i].start();
		}
	    
		try {
			while(isThreadsWorks())
				Thread.sleep(waitMills);
		} catch (InterruptedException e) {
			// TODO: do something if thread fails
		}
		
	    // Sort those vectors *together*.
	    for (int p1 = 0; p1 < others.length - 1; p1++)
	    {
	    	for (int p2 = p1 + 1; p2 < others.length; p2++)
	        {
	    		if (distances[p1] > distances[p2])
	            {
	    			double tempDist = distances[p1];
	    			distances[p1] = distances[p2];
	    			distances[p2] = tempDist;
	    			RenderedImage tempR = rothers[p1];
	    			rothers[p1] = rothers[p2];
	    			rothers[p2] = tempR;
	    			File tempF = others[p1];
	    			others[p1] = others[p2];
	    			others[p2] = tempF;
	            }
	        }
	    }
	    
	    images = new ArrayList<ImageHolder>();
	    
	    // Do something with files
	    for (int o = 0; o < others.length; o++)
	    {
	    	images.add(new ImageHolder(others[o], distances[o]));
	    }
	}
	   
	/**
	 * This method rescales an image to 300,300 pixels using the JAI scale operator.
	 * 
	 * @param i	Image			
	 * @return RenderedImage
	 * @see java.awt.image.RenderedImage
	 * 
	 * @author Grzegorz Polek <grzegorz.polek@gmail.com>
	 */
	private RenderedImage rescale(RenderedImage i)
	{
		float scaleW = ((float) baseSize) / i.getWidth();
	    float scaleH = ((float) baseSize) / i.getHeight();
	    
	    // Scales the original image
	    ParameterBlock pb = new ParameterBlock();
	    pb.addSource(i);
	    pb.add(scaleW);
	    pb.add(scaleH);
	    pb.add(0.0F);
	    pb.add(0.0F);
	    pb.add(new InterpolationNearest());
	      
	    // Creates a new, scaled image and uses it on the DisplayJAI component
	    return JAI.create("scale", pb);
	}
	    
	/**
	 * This method calculates and returns signature vectors for the input image.
	 * 
	 * @param i RenderedImage
	 * @return <code>array</code> of pixels signature
	 * 
	 * @author Grzegorz Polek <grzegorz.polek@gmail.com>
	 */
	private Color[][] calcSignature(RenderedImage i)
	{
		// Get memory for the signature.
	    Color[][] sig = new Color[5][5];
	    
	    // For each of the 25 signature values average the pixels around it.
	    // Note that the coordinate of the central pixel is in proportions.
	    float[] prop = new float[]
	    		{1f / 10f, 3f / 10f, 5f / 10f, 7f / 10f, 9f / 10f};
	    
	    for (int x = 0; x < 5; x++)
	    	for (int y = 0; y < 5; y++)
	    		sig[x][y] = averageAround(i, prop[x], prop[y]);
	      
	    return sig;
	}
	   
	/**
	 * This method averages the pixel values around a central point and return the
	 * average as an instance of Color. The point coordinates are proportional to
	 * the image.
	 * 
	 * @param i
	 * @param px
	 * @param py
	 * @return
	 * 
	 * @author Grzegorz Polek <grzegorz.polek@gmail.com>
	 */
	private Color averageAround(RenderedImage i, double px, double py)
	{
		// Get an iterator for the image.
	    RandomIter iterator = RandomIterFactory.create(i, null);
	      
	    // Get memory for a pixel and for the accumulator.
	    double[] pixel = new double[3];
	    double[] accum = new double[3];
	    
	    // The size of the sampling area.
	    int sampleSize = 15;
	    int numPixels = 0;
	      
	    // Sample the pixels.
	    for (double x = px * baseSize - sampleSize; x < px * baseSize + sampleSize; x++)
    	{
	    	for (double y = py * baseSize - sampleSize; y < py * baseSize + sampleSize; y++)
	        {
	    		iterator.getPixel((int) x, (int) y, pixel);
	    		accum[0] += pixel[0];
	    		accum[1] += pixel[1];
	    		accum[2] += pixel[2];
	    		numPixels++;
	        }
        }
	      
	    // Average the accumulated values.
	    accum[0] /= numPixels;
	    accum[1] /= numPixels;
	    accum[2] /= numPixels;
	    
	    return new Color((int) accum[0], (int) accum[1], (int) accum[2]);
	      
	}
	   
	/**
	 * This method calculates the distance between the signatures of an image and
	 * the reference one. The signatures for the image passed as the parameter are
	 * calculated inside the method.
	 * 
	 * @param other
	 * @return <code>double</code> with distance between distances
	 * 
	 * @author Grzegorz Polek <grzegorz.polek@gmail.com>
	 */
	private double calcDistance(RenderedImage other)
	{
		// Calculate the signature for that image.
		Color[][] sigOther = calcSignature(other);
  
		// There are several ways to calculate distances between two vectors,
		// we will calculate the sum of the distances between the RGB values of
		// pixels in the same positions.
		double dist = 0;
		for (int x = 0; x < 5; x++)
			for (int y = 0; y < 5; y++)
			{
				int r1 = signature[x][y].getRed();
				int g1 = signature[x][y].getGreen();
				int b1 = signature[x][y].getBlue();
				int r2 = sigOther[x][y].getRed();
				int g2 = sigOther[x][y].getGreen();
				int b2 = sigOther[x][y].getBlue();
				double tempDist = Math.sqrt((r1 - r2) * (r1 - r2) + (g1 - g2) * (g1 - g2) + (b1 - b2) * (b1 - b2));
				dist += tempDist;
			}
		return dist;
	}
	   
	/**
	 * This method get all image files in the same directory as the reference.
	 * Just for kicks include also the reference image.
	 * 
	 * @param directory
	 * @return <code>array</code> of files from speified directory
	 * 
	 * @author Grzegorz Polek <grzegorz.polek@gmail.com>
	 */
	private File[] getOtherImageFiles(File directory)
	{
		// List all the image files in that directory.
		File[] others = directory.listFiles(new JPEGFilter());
		return others;
	}
	
	/**
	 * Nothing special. Just List<> with ImageHolder's of algorithm's results
	 * 
	 * @see com.is.utils.ImageHolder
	 * @return
	 * 
	 * @author Grzegorz Polek <grzegorz.polek@gmail.com>
	 */
	public List<ImageHolder> getResults()
	{
		return images;
	}
	
	/**
	 * Class which implements Runnable interface to run on multiple threads
	 * 
	 * @author Lukasz Pycia <fryta1990@gmail.com>
	 */
	class CompareRunnable implements Runnable
	{
		private int i;
		
		CompareRunnable(int _i)
		{
			i=_i;
		}
		
		@Override
		public void run() 
		{
    		try 
    		{
    			for (int o = i; o < others.length; o+=threads)
    		    {
    		    	rothers[o] = rescale(ImageIO.read(others[o]));
    		        distances[o] = calcDistance(rothers[o]);
    		        UserInterface.getInstance().addThumbnail(others[o], distances[o]);
    		        
    		        theProgress += progress;
    		        UserInterface.getInstance().setProgress(theProgress);
    		    }
    			threadWorks[i]=false;
			} 
    		catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Checks if thread is working
	 * 
	 * @return <code>true</code> if thread is working or <code>false</code> it not
	 * @author Lukasz Pycia <fryta1990@gmail.com>
	 */
	public boolean isThreadsWorks()
	{
		 int works=0;
		 for(int t=0;t<threads;t++)
			 if(threadWorks[t])
				 works++;
		 if(works!=0)
			 return true;
		 return false;
	 }
}
	


