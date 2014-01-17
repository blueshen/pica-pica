package cn.shenyanchao.image.filters;

import java.io.File;
 
import javax.swing.filechooser.FileFilter;

/**
 * This class implements a generic file name filter that allows the listing/selection
 * of JPEG files.
 * 
 * @author Grzegorz Polek <grzegorz.polek@gmail.com>
 */
public class JPEGFilter extends FileFilter implements java.io.FileFilter
{
	/**
	 * @param 
	 * @return <code>true</code> if it is .jpeg, .jpg file <code>false</code> otherwise
	 * @see java.io.File
	 * 
	 * @author Grzegorz Polek <grzegorz.polek@gmail.com>
	 */
	public boolean accept(File f)
	{
		if (f.getName().toLowerCase().endsWith(".jpeg")) return true;
		if (f.getName().toLowerCase().endsWith(".jpg")) return true;
		return false;
	}
	
	/**
	 * Just filer description
	 * 
	 * @return String with description
	 * 
	 * @author Grzegorz Polek <grzegorz.polek@gmail.com>
	 */
	public String getDescription()
	{
		return "JPEG files";
	}
 
}