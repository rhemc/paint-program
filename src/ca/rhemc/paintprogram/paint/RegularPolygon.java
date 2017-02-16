package ca.rhemc.paintprogram.paint;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/*creates a polygon that is equiangular (verticie anglues are of equal value). This can be stretched in either the x
* or y direction and flipped around.*/
public class RegularPolygon extends PaintShape {
	protected boolean center;
	protected boolean right;
	protected Polygon polygon;
	protected double stretchFactorX;
	protected double stretchFactorY;
	protected Path2D model;
	protected Path2D shape;
	protected int nVerticies;
	protected AffineTransform t = new AffineTransform();
	protected boolean border;
	protected Color borderColour;

	/**
	 * creates a regular polygon
	 *
	 * @param x             the initial x coordinate
	 * @param y             the initial y coordinate
	 * @param colour        the color of the polygon outline
	 * @param lineThickness the value of the line thickness
	 * @param fill          the color of the inside of the polygon
	 * @param strokeStyle   the style of the outline of the polygon
	 * @param vertices      the number of verticies in the polygon
	 * @param center        if the polygon is centered or not
	 * @param right         if the polygon has equilateral.
	 */
	public RegularPolygon(int x, int y, Color colour, float lineThickness, boolean fill, int strokeStyle,
	                      int vertices, boolean border, Color borderColour, boolean center, boolean right) {
		super(x, y, colour, lineThickness, fill, strokeStyle);
		model = new Path2D.Double();
		shape = new Path2D.Double();
		nVerticies = vertices;
		this.center = false;
		this.right = right;
		calculateModel();
		this.border = border;
		this.borderColour = borderColour;
	}

	//draws the polygon out in model view (centered on axis with radius 1)
	protected void calculateModel() {
		double angles = 2 * Math.PI / nVerticies;
		model.moveTo(Math.sin(Math.PI / nVerticies), Math.cos(Math.PI / nVerticies));//every polygon vertex starts from the top middle
		for(int i = 1; i < nVerticies; i++) {
			double x = Math.sin(i * angles + Math.PI / nVerticies);
			double y = Math.cos(i * angles + Math.PI / nVerticies);
			model.lineTo(x, y);
		}
		model.closePath();
		stretchFactorX = 1.0 / (model.getBounds2D().getWidth());
		stretchFactorY = 1.0 / (model.getBounds2D().getHeight());
	}

	//updates the width and height of the shape (corresponds to when control is pressed, shape is centered)
	protected void centeredPolygonCreation() {
		t.setToTranslation(x, y);
		double mouseAngle = Math.atan2(-getWidth(), getHeight());
		t.rotate(mouseAngle - Math.PI / nVerticies);
		int dx = getWidth();
		int dy = getHeight();
		double r = Math.sqrt(dx * dx + dy * dy);
		t.scale(r, r);
		shape = (Path2D) t.createTransformedShape(model);//applies the transformations to the model
	}

	//updates the width and height of the shape (shape is drawn from corner)
	protected void stretchPolygonCreation() {
		t.setToTranslation(x + getWidth() / 2, y + getHeight() / 2);
		t.scale(getWidth() * stretchFactorX, getHeight() * stretchFactorY);
		shape = (Path2D) t.createTransformedShape(model);
	}

	//updates the width and height of the shape (corresponds to when shift is pressed,
	// shape is drawn from corner but length and height are minimum of the dx and dy)
	protected void regularPolygonCreation() {
		int dx = getWidth();
		int dy = -getHeight();
		int scaleAmount = Math.min(Math.abs(dx), Math.abs(dy));
		int yflip = 1;
		int xflip = 1;
		if(Math.abs(dx) != 0 && Math.abs(dy) != 0) {
			xflip = dx / Math.abs(dx);
			yflip = dy / Math.abs(dy);
		}
		t.setToTranslation(xflip*(scaleAmount / 2), -yflip*(scaleAmount / 2));
		t.scale(xflip * stretchFactorX * scaleAmount, -yflip * stretchFactorY * scaleAmount);
		shape = (Path2D) t.createTransformedShape(model);
		t.setToTranslation(x,y);
		shape = (Path2D) t.createTransformedShape(shape);
	}

	//returns the x difference between the start point and where the mouse is dragged to
	protected int getHeight() {
		return yEnd - y;
	}

	//returns the y difference between the start point and where the mouse is dragged to
	protected int getWidth() {
		return xEnd - x;
	}


	@Override
	/*
    updates the mouse movement and calls the apropriate shape building methods based on key input
     * @param x the x coordinate of the mouse
     * @param y the y coordinate of the mouse
     */
	public void mouseMoved(int x, int y) {
		xEnd = x;
		yEnd = y;
		updatePolygon();
	}

	private void updatePolygon() {
		if(center) {
			centeredPolygonCreation();
		} else if(right) {
			regularPolygonCreation();
		} else {
			stretchPolygonCreation();
		}
	}

	//prints the polygon to the screen
	@Override
	public void print(Graphics2D g2) {
		prepare(g2);
		g2.setStroke(stroke);
		if(fill)
			g2.fill(shape);
		else {
			g2.draw(shape);
		}
		if(border) {
			g2.setColor(borderColour);
			g2.draw(shape);
		}
	}

	/*sets the center boolean to the value specified by center
	 * @param center - whether or not the shape should be drawn from center
	 */
	public boolean setCenter(boolean center) {
		boolean orig = this.center;
		this.center = center;
		updatePolygon();
		return orig != center;
	}

	/*sets the right boolean to the value specified by right
	 * @param right - whether or not the shape should be at right angles
	 */
	public boolean setRight(boolean right) {
		boolean orig = this.right;
		this.right = right;
		updatePolygon();
		return orig != right;
	}

	@Override
	public Rectangle getBounds() {
		return shape.getBounds();
	}

	public boolean contains(Point p) {
		return shape.contains(p);
	}

	public boolean contains(int x, int y) {
		return shape.contains(x, y);
	}

	@Override
	public Rectangle2D getBounds2D() {
		return shape.getBounds2D();
	}

	@Override
	public boolean contains(double x, double y) {
		return shape.contains(x, y);
	}

	@Override
	public boolean contains(Point2D p) {
		return shape.contains(p);
	}

	@Override
	public boolean intersects(double x, double y, double w, double h) {
		return shape.intersects(x, y, w, h);
	}

	@Override
	public boolean intersects(Rectangle2D r) {
		return shape.intersects(r);
	}

	@Override
	public boolean contains(double x, double y, double w, double h) {
		return shape.contains(x, y, w, h);
	}

	@Override
	public boolean contains(Rectangle2D r) {
		return shape.contains(r);
	}

	@Override
	public PathIterator getPathIterator(AffineTransform at) {
		return shape.getPathIterator(at);
	}

	@Override
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		return shape.getPathIterator(at, flatness);
	}
}
