package cn.shenyanchao.image.filters;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class JPEGFilter extends FileFilter implements java.io.FileFilter {

    public boolean accept(File f) {
        if (f.getName().toLowerCase().endsWith(".jpeg")) return true;
        if (f.getName().toLowerCase().endsWith(".jpg")) return true;
        return false;
    }

    public String getDescription() {
        return "JPEG files";
    }

}