package ca.rhemc.paintprogram.paint;

import java.awt.Graphics;

/**
 * Objects can draw it self on Graphics Object.
 */
public interface Drawable {
	/**
	 * Draw on Graphics Object
	 *
	 * @param g Graphics drawing to
	 */
	public void print(Graphics g);
}
