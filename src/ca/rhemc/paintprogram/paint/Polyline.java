package ca.rhemc.paintprogram.paint;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * PaintShape with multiple dots connecting to each other.
 */
public class Polyline extends PaintShape {
	protected Path2D path;
	protected Point last,temp;

	public Polyline(int x, int y, Color colour, float lineThickness, boolean fill, int strokeStyle) {
		super(x, y, colour, lineThickness, fill, strokeStyle);
		path = new Path2D.Float();
		path.moveTo(x,y);
		last = new Point(x, y);
	}


	/*adds a point to the polyline
	 * @param point - the point to be added to the polyline arrayList
	 */
	@Override
	public void mouseMoved(int x, int y) {
		last = new Point(x, y);
		path.lineTo(x,y);
		end();
	}

	@Override
	//prints hte polyline to the screen
	public void print(Graphics2D g2) {
		prepare(g2);
		g2.draw(this);
		if(temp != null) {
			g2.drawLine(last.x, last.y, temp.x, temp.y);
		}
	}

	//clears the temporary point
	public void end() {
		temp = null;
	}

	@Override
	public Rectangle getBounds() {
		return path.getBounds();
	}

	@Override
	public Rectangle2D getBounds2D() {
		return path.getBounds2D();
	}

	@Override
	public boolean contains(double x, double y) {
		return path.contains(x, y);
	}

	@Override
	public boolean contains(Point2D p) {
		return path.contains(p);
	}

	@Override
	public boolean intersects(double x, double y, double w, double h) {
		return path.intersects(x, y, w, h);
	}

	@Override
	public boolean intersects(Rectangle2D r) {
		return path.intersects(r);
	}

	@Override
	public boolean contains(double x, double y, double w, double h) {
		return path.contains(x, y, w, h);
	}

	@Override
	public boolean contains(Rectangle2D r) {
		return path.contains(r);
	}

	@Override
	public PathIterator getPathIterator(AffineTransform at) {
		return path.getPathIterator(at);
	}

	@Override
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		return path.getPathIterator(at, flatness);
	}

	/*stores the temporary mouse point
	 * @param x the x coordinate of the mouse
	 * @param y the y coordinate of the mouse
	 */
	public void mouseTemp(int x, int y) {
		temp = new Point(x, y);
	}
}
