package com.allenday.image;

import java.io.File;
import java.util.List;

import cn.shenyanchao.image.ImageFeatures;
import cn.shenyanchao.image.ImageProcessor;
import org.junit.Test;

public class ImageProcessorTest {
	ImageProcessor processor = new ImageProcessor();
	
	@Test
	public void test() {
		processor.setFiles(new File("src/test/resources/image"));
		List<ImageFeatures> r = processor.processImages();
	}
}
