package com.is.utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


/**
 * Just logger class.
 * 
 * Usage:
 * 
 * You have to set level first:
 * LOGGER.setLevel(Level.INFO);
 * 
 * The u can use one of the following methods:
 * LOGGER.severe("Info Log");
 * LOGGER.warning("Info Log");
 * LOGGER.info("Info Log");
 * LOGGER.finest("Really not important");
 *   
 * @author Grzegorz Polek <grzegorz.polek@gmail.com>
 *
 */
public class ISLogger {

	static private FileHandler fileTxt;
	static private SimpleFormatter formatterTxt;

	/**
	 * Setup function for ISLogger
	 * 
	 * @throws java.io.IOException
	 * 
	 * @author Grzegorz Polek <grzegorz.polek@gmail.com>
	 */
	static public void setup() throws IOException 
	{
	    // Get the global logger to configure it
	    Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	    logger.setLevel(Level.INFO);
	    fileTxt = new FileHandler("is-log.txt");
	
	    // Create txt Formatter
	    formatterTxt = new SimpleFormatter();
	    fileTxt.setFormatter(formatterTxt);
	    logger.addHandler(fileTxt);
	}
  
} 