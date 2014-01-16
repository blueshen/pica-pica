package com.is.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class which contains static methods to check some values with specified patterns
 * 
 * @author Grzegorz Polek <grzegorz.polek@gmail.com>
 */
public class Check {

	/**
	 * Checks if string matches URL pattern
	 * 
	 * @param url
	 * @return <code>true</code> if matches <code>false</code> otherwise
	 * 
	 * @author Grzegorz Polek <grzegorz.polek@gmail.com>
	 */
	public static boolean url(String url)
	{
		Pattern url_pattern = Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
		
		Matcher matcher = null;
		
		matcher = url_pattern.matcher(url);
		
		return matcher.find();
	}
	
	/**
	 * Checks if string matches PATH pattern
	 * 
	 * @param path
	 * @return <code>true</code> if matches <code>false</code> otherwise
	 * 
	 * @author Grzegorz Polek <grzegorz.polek@gmail.com>
	 */
	public static boolean path(String path)
	{
		Pattern path_pattern = Pattern.compile("[^\0]+");
		
		Matcher matcher = null;
		
		matcher = path_pattern.matcher(path);
		
		return matcher.find();
	}
}
