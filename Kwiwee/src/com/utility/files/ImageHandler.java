package com.utility.files;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageHandler implements FileLoader
{
	public static Image loadImage(String path) {
		path = HEADER_PATH + path;
		BufferedImage image = null;

		try	{
			File file = new File(path);
			InputStream fis = new FileInputStream(file);
			return ImageIO.read(fis);
		}
		catch (Exception e) { System.err.println("Could not find the image directory " + path); }
		
		return image;
	}
	
	public static ImageIcon loadIcon(String path) {
		return new ImageIcon(loadImage(path));
	}
	
	public static boolean test(String path) {
		path = HEADER_PATH + path;
		
		try	{
			File file = new File(path);
			InputStream fis = new FileInputStream(file);
			ImageIO.read(fis);
			return true;
		}
		catch (Exception e) { return false;	}
	}
	
	public static BufferedImage copy(BufferedImage source) {
	    BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
	    Graphics g = b.getGraphics();
	    g.drawImage(source, 0, 0, null);
	    g.dispose();
	    return b;
	}
	
	public static boolean compare(BufferedImage imgA, BufferedImage imgB) {
		// The images must be the same size.
		if (imgA.getWidth() != imgB.getWidth() || imgA.getHeight() != imgB.getHeight())
			return false;

		int width  = imgA.getWidth();
		int height = imgA.getHeight();

		//loop over every pixel.
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// Compare the pixels for equality.
				if (imgA.getRGB(x, y) != imgB.getRGB(x, y)) {
					return false;
				}
			}
		}
		
		return true;
	}
}