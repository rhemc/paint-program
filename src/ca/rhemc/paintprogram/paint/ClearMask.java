package ca.rhemc.paintprogram.paint;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Mask the whole Panel for undoing clear
 */
public class ClearMask implements Drawable {

	private int width, height;

	public ClearMask(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public void print(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
	}
}
