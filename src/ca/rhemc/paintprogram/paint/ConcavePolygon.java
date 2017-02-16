package ca.rhemc.paintprogram.paint;

import java.awt.Color;
import java.awt.geom.Path2D;

public class ConcavePolygon extends RegularPolygon {
	/**
	 * creates a regular concave polygon (star-shaped)
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
	public ConcavePolygon(int x, int y, Color colour, float lineThickness, boolean fill, int strokeStyle, int vertices, boolean border, Color borderColour, boolean center, boolean right) {
		super(x, y, colour, lineThickness, fill, strokeStyle, vertices * 2, border, borderColour, center, right);
	}

	@Override
	protected void calculateModel() {
		double angles = 2 * Math.PI / nVerticies;
		model.moveTo(0, 1);//every polygon vertex starts from the top middle
		for(int i = 1; i < nVerticies; i++) {
			double x = (1 - 0.5 * (i % 2)) * Math.sin(i * angles);
			double y = (1 - 0.5 * (i % 2)) * Math.cos(i * angles);
			model.lineTo(x, y);
		}
		model.closePath();
		stretchFactorX = 1.0 / (model.getBounds2D().getWidth());
		stretchFactorY = 1.0 / (model.getBounds2D().getHeight());
	}

	@Override
	protected void centeredPolygonCreation() {
		t.setToTranslation(x, y);
		double mouseAngle = Math.atan2(-getWidth(), getHeight());
		t.rotate(mouseAngle);
		int dx = getWidth();
		int dy = getHeight();
		double r = Math.sqrt(dx * dx + dy * dy);
		t.scale(r, r);
		shape = (Path2D) t.createTransformedShape(model);//applies the transformations to the model
	}
}
