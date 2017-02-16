package ca.rhemc.paintprogram.paint;

import ca.rhemc.paintprogram.pointer.ModifierEvent;
import ca.rhemc.paintprogram.pointer.PointerEvent;

import java.awt.Color;
import java.awt.event.MouseEvent;

public class SquiggleTool implements ShapeManipulatorStrategy {
	private StylePanel style;
	private PaintShape[] shapes;
	private boolean erase;

	public SquiggleTool(StylePanel style, PaintShape[] shapes) {
		this(style,false,shapes);
	}

	public SquiggleTool(StylePanel style, boolean erase, PaintShape[] shapes) {
		this.style = style;
		this.shapes = shapes;
		this.erase = erase;
	}

	@Override
	public Drawable deselect() {
		return null;
	}

	@Override
	public void selected() {

	}

	@Override
	public Drawable handlePointerUpdate(PointerEvent e) {
		Drawable rtn = null;
		switch(e.getID()) {
			case MouseEvent.MOUSE_PRESSED:
				Color colour = style.getColour();
				if (erase) {
					colour = Color.white;
				}
				shapes[e.getPointerId()] = new Polyline(e.getX(), e.getY(), colour,
						style.getLineThickness(), style.isFill(), style.getStrokeStyle());
				break;
			case MouseEvent.MOUSE_MOVED:
				if(shapes[e.getPointerId()] != null) {
					shapes[e.getPointerId()].mouseMoved(e.getX(), e.getY());
					shapes[e.getPointerId()].setLineThickness(style.getLineThickness(e.getPressure()));
				}
				break;
			case MouseEvent.MOUSE_RELEASED:
				if(shapes[e.getPointerId()] == null)
					break;
				shapes[e.getPointerId()].mouseMoved(e.getX(), e.getY());
				rtn = shapes[e.getPointerId()];
				shapes[e.getPointerId()] = null;
		}
		return rtn;
	}

	@Override
	public boolean handleModifierUpdated(ModifierEvent e) {

		return false;
	}

}
