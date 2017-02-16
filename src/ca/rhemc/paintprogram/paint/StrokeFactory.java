package ca.rhemc.paintprogram.paint;

import java.awt.BasicStroke;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.QuadCurve2D;

public class StrokeFactory {

	public Stroke createStroke(int strokeStyle, float lineThickness) {
		Stroke stroke = null;
		switch (strokeStyle) {
			case 1:
				stroke = new BasicStroke(lineThickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
						10.0f, new float[]{16.0f, 20.0f}, 0.0f);
				break;
			case 2:
				stroke = new ShapeStroke(new Ellipse2D.Double(0, 0, lineThickness, lineThickness),
						lineThickness + 5, false);
				break;
			case 3:
				stroke = new ShapeStroke(new QuadCurve2D.Double(-1, 1, 0, 0, 1, 1),
						1, true);
				break;
			case 4:
				stroke = new ShapeStroke(new QuadCurve2D.Double(50, 100, 100, 170, 150, 100),
						1, false);
				break;
			default:
				stroke = new BasicStroke(lineThickness);
				break;
		}
		return stroke;
	}
}
