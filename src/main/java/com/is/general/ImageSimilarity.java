package com.is.general;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.is.algorithm.Compare;
import com.is.utils.Check;
import com.is.utils.ISLogger;
import com.is.utils.ImageHolder;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

/**
 * Class that checks for similarity between two images using naive algorithm
 * 
 * @author Grzegorz Polek <grzegorz.polek@gmail.com>
 */
public class ImageSimilarity {

	private final static Logger LOG = Logger.getLogger(ImageSimilarity.class .getName()); 
	
	@Parameter
	private List<String> parameters = new ArrayList<String>();
	
	@Parameter(names = {"-h", "--help"}, description = "Show help")
	private boolean help = false;
	
	@Parameter(names = {"-g", "--gui"}, description = "Enable Graphical User Interface")
	private boolean gui = false;
	
	@Parameter(names = {"-i", "--img"}, description = "Absolute Path to the image")
	private String img;

	@Parameter(names = {"-d", "--dir"}, description = "Absolute Path to directory with images to compare with")
	private String dir;
	
	@Parameter(names = {"-t", "--threads"}, description = "Number of threads")
	private int threads;

	private static JCommander jc;
	
	private static ImageSimilarity is;
	
	File image = null;
	File directory = null;
	
	/**
	 * 
	 * @return
	 */
	public static ImageSimilarity getInstance()
	{
		return is;
	}
	
    /**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws RuntimeException, Exception 
	{
		is = new ImageSimilarity();
		
		jc = new JCommander(is);
		jc.setProgramName("ImageSimilarity");
		
		try
		{
			jc.parse(args);
		} 
		catch (ParameterException pe) 
		{
			System.out.println("Wrong console parameters. See usage of ImageSimilarity below:");
			jc.usage();
		}
		
		is.setup();
	}
	
	/**
	 * Setups all necessary things for this application
	 * 
	 * @author Grzegorz Polek <grzegorz.polek@gmail.com>
	 * @author Lukasz Pycia <fryta1990@gmail.com>
	 */
	private void setup()
	{
		// Show help
		if(help)
		{
			jc.usage();
		}
		
		// Disable MediaLib. lol.
		System.setProperty("com.sun.media.jai.disableMediaLib", "true");
		
		// GUI enabled
		if(gui)
		{
			try {
				UserInterface ui = UserInterface.getInstance();
				ui.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(threads == 0)
		{
			threads = 1;
		}
		
		// Set up the logger
		try {
			ISLogger.setup();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    	throw new RuntimeException("Problems with creating the log files!");
	    }
		
		// Run if not gui and all arguments are set up
		if(img != null && dir != null)
		{
			if(!gui)
			{
				check();
				run();
			}
		}
	}
	
	/**
	 * Sets image reference
	 * 
	 * @param i
	 * 
	 * @author Grzegorz Polek <grzegorz.polek@gmail.com>
	 */
	public void setImage(File i)
	{
		this.image = i;
	}
	
	/**
	 * Sets directory with images
	 * 
	 * @param d
	 * 
	 * @author Grzegorz Polek <grzegorz.polek@gmail.com>
	 */
	public void setDirectory(File d)
	{
		this.directory = d;
	}
	
	/**
	 * Checks something
	 * 
	 * @author Grzegorz Polek <grzegorz.polek@gmail.com>
	 */
	private void check()
	{
		if(Check.url(img))
		{
			if(!img.toLowerCase().endsWith(".jpeg") && !img.toLowerCase().endsWith(".jpg"))
			{
				throw new RuntimeException("ImageSimilarity accepts only JPEG files!");
			}
			
			URL url;
			
			try {
				url = new URL(img);
				
				HttpURLConnection huc;
				
				try {
					huc = ( HttpURLConnection )  url.openConnection ();
					huc.setRequestMethod ("GET");  //OR  huc.setRequestMethod ("HEAD"); 
					
					huc.connect () ; 
					int code = huc.getResponseCode() ;

					if(code < 200 || code > 300) // Accept all 2XX codes.
					{
						throw new RuntimeException("URL " + img + " returned " + code + " http error code!");
					}
					
				} catch (IOException e1) {
					throw new RuntimeException("Error occurred with testing connection for " + img);
				} 
				
				InputStream in;
				ByteArrayOutputStream out;
				
				try {
					in = new BufferedInputStream(url.openStream());
					
					out = new ByteArrayOutputStream();
					
					byte[] buf = new byte[1024];
					int n = 0;
					while (-1!=(n=in.read(buf)))
					{
					   out.write(buf, 0, n);
					}
					
					out.close();
					in.close();
					byte[] response = out.toByteArray();
					
					// Creates temporary image file
					File tf = File.createTempFile("image-similarity-temp", ".jpg");
					
					// Deletes file when the virtual machine terminate
			        tf.deleteOnExit();
					
			        img = tf.getAbsolutePath();
			        
					FileOutputStream fos = new FileOutputStream(img);
					
				    fos.write(response);
				    fos.close();
				} catch (IOException e) {
					throw new RuntimeException("Error occurred with donwloading a file form URL " + img);
				}
			} catch (MalformedURLException e1) {
				throw new RuntimeException("Wrong URL " + img);
			}
		}
		
		image = new File(img);
		directory = new File(dir);
	}
	
	public boolean ifFiles()
	{
		if(image != null && directory != null)
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * Runs algorithm and checks some things
	 * 
	 * @author Grzegorz Polek <grzegorz.polek@gmail.com>
	 */
	public void run()
	{
		if(image != null && !image.exists())
		{
			throw new RuntimeException("Image doesn't exist!");
		}
		
		if(directory != null && !directory.exists())
		{
			throw new RuntimeException("Directory doesn't exist!");
		}
		
		DecimalFormat df = new DecimalFormat("#.##");
		
		// Compare images
		try {
			
			Compare compare = new Compare(image, directory, threads);
			
			List<ImageHolder> images = compare.getResults();
			
			if(images != null && !images.isEmpty())
			{
				for(ImageHolder i : images)
				{
					System.out.println("Image: " + i.getFile().getPath());
					System.out.println("Distance: " + df.format(i.getDistance()));
					System.out.println("Difference: " + df.format(i.getDifference()) + "%");
					System.out.println("Similarity: " + df.format(i.getSimilarity()) + "%");
					System.out.println("\n");
				}
			}
			
		} catch (Exception e) {
			LOG.severe("Comparision failed. Ooops!");
			throw new RuntimeException("Comparision failed. Ooops!");
		}
	}

}
