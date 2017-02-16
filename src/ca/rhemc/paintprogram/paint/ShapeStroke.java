package ca.rhemc.paintprogram.paint;

import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;

//sets the stroke(the outline of the shape) to be what is specified by the shape parameter of the
// constructor
public class ShapeStroke implements Stroke {
	private Shape[] shapes;
	private double spacing;
	private AffineTransform t = new AffineTransform();
	private boolean serendipity;

	/*Default constructor for when you only have one shape
	 * @param s - the shape that you want to make repeat
	 * @param spacing - how far apart each space is
	 * @param serendipity - 3d-esque strokes
	*/
	public ShapeStroke(Shape s, double spacing, boolean serendipity) {
		this(new Shape[]{s}, spacing, serendipity);
	}

	/*Constructor for when you have multiple shapes
	 * @param s - the shape that you want to make repeat
	 * @param spacing - how far apart each space is
	 * @param serendipity - 3d-esque strokes
	*/
	public ShapeStroke(Shape[] s, double spacing, boolean serendipity) {
		shapes = new Shape[s.length];
		this.spacing = spacing;
		this.serendipity = serendipity;

		for(int i = 0; i < shapes.length; i++) {
			Rectangle2D bounds = s[i].getBounds2D();//get the shape boundries so we can find middle
			//System.out.println(bounds);
			double midX = bounds.getCenterX();
			double midY = bounds.getCenterY();
			t.setToTranslation(-midX, -midY);//translates the shape into model view (symmetric along the axis)
			shapes[i] = t.createTransformedShape(s[i]);//applies the transformation to each shape
		}
	}

	/*required method by the interface Stroke. The meat and potatoes of this class,
	 * it returns the shape that will become the line stroke.
	 * @param s - the initial shape of the stroke
	 * @return the shape of the stroke
	*/
	public Shape createStrokedShape(Shape s) {
		GeneralPath path = new GeneralPath();
		PathIterator pi = new FlatteningPathIterator(s.getPathIterator(null), 1);
		int shapeIndex = 0;
		int state = 0;
		double[] points = new double[6];
		double moveX = 0;
		double moveY = 0;
		double thisX = 0;
		double thisY = 0;
		double lastX = 0;
		double lastY = 0;
		double firstX = 0, firstY = 0;
		double offset = 0;
		boolean please = false;

		while(!pi.isDone()) {
			state = pi.currentSegment(points);//updates point array with the points of the lines
			if(please) {
				firstX = points[0];
				firstY = points[0];
				please = true;
			}
			if(state == PathIterator.SEG_MOVETO) {
				offset = 0;
				moveX = lastX = points[0];
				moveY = lastY = points[1];
				path.moveTo(moveX, moveY);

			} else if(state == PathIterator.SEG_LINETO || state == PathIterator.SEG_CLOSE) {
				if(state == PathIterator.SEG_CLOSE) {
					points[0] = moveX;
					points[1] = moveY;

				}
				thisX = points[0];
				thisY = points[1];
				double distance = Math.sqrt(Math.pow(thisX - lastX, 2) + Math.pow(thisY - lastY, 2));
				double angle = Math.atan2(thisY - lastY, thisX - lastX);
				while(distance >= offset && shapeIndex < shapes.length) {
					double x = lastX + offset * Math.cos(angle);
					double y = lastY + offset * Math.sin(angle);
					t.setToTranslation(x, y);
					t.rotate(angle);
					if(serendipity) {
						path.curveTo(x - 1, y - 1, firstX, firstY, x + 1, y + 1);
					}
					path.append(t.createTransformedShape(shapes[shapeIndex]), false);//set the shapes position to the correct spot
					offset += spacing;
					shapeIndex++;
					shapeIndex %= shapes.length;// increments and loops if it is out of bounds
				}
				offset -= distance;
				lastX = thisX;
				lastY = thisY;
			}
			pi.next();
		}
		return path;
	}
}
