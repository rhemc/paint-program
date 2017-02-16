package ca.rhemc.paintprogram.paint;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * Creates the paint application
 */
public class Paint {
	PaintModel model; // Model
	View view; // View+Controller
	public Paint() {
		// Create MVC components and hook them together

		// Model
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		model = new PaintModel(screenSize);

		// View+Controller
		view = new View(model);

	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Paint();
			}
		});
	}
}
