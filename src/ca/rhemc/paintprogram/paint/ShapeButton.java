package ca.rhemc.paintprogram.paint;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

/**
 * ShapeButton is a JButton that holds an image corresponding to what it draws
 */
public class ShapeButton extends JButton {
	private int shapeNum;

	/**
	 * Creates an instance of ShapeButton, using shapeNum to choose the right image to draw on itself.
	 *
	 * @param shapeNum    number corresponding to it's shape
	 * @param buttonAsset The name of the image that it will load
	 */
	public ShapeButton(int shapeNum, String buttonAsset) {
		this.shapeNum = shapeNum;
		try {
			Image image = ImageIO.read(new File(String.format("assets" + File.separator + "%s.png", buttonAsset)));
			setIcon(new ImageIcon(image));
		} catch(IOException e) {
			setText(buttonAsset);
		}

	}

	public int getShapeNum() {
		return shapeNum;
	}
}
