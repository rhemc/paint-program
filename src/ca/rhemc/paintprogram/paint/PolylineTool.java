package ca.rhemc.paintprogram.paint;

import ca.rhemc.paintprogram.pointer.ModifierEvent;
import ca.rhemc.paintprogram.pointer.PointerEvent;

import java.awt.event.MouseEvent;

public class PolylineTool implements ShapeManipulatorStrategy {
	private StylePanel style;
	private PaintShape[] shapes;
	private int activePointer = -1;

	public PolylineTool(StylePanel style, PaintShape[] shapes) {
		this.style = style;
		this.shapes = shapes;
	}

	@Override
	public Drawable deselect() {
		Drawable rtn = null;
		if(shapes[0] != null) {
			((Polyline) shapes[0]).end();
			rtn = shapes[0];
			shapes[0] = null;
		}
		activePointer = -1;
		return rtn;
	}

	@Override
	public void selected() {

	}

	@Override
	public Drawable handlePointerUpdate(PointerEvent e) {
		Drawable rtn = null;
		switch(e.getID()) {
			case MouseEvent.MOUSE_PRESSED:
				if(activePointer != -1)
					break;
				activePointer = e.getPointerId();
				if(shapes[0] == null) {
					shapes[0] = new Polyline(e.getX(), e.getY(), style.getColour(),
							style.getLineThickness(), style.isFill(), style.getStrokeStyle());
				}
				break;
			case MouseEvent.MOUSE_MOVED:
				if(shapes[0] != null) {
					((Polyline) shapes[0]).mouseTemp(e.getX(), e.getY());
				}
				break;
			case MouseEvent.MOUSE_RELEASED:
				if(activePointer == -1)
					break;
				if(e.getButton() == 3) {
					rtn = deselect();
				} else {
					shapes[0].mouseMoved(e.getX(), e.getY());
				}
				activePointer = -1;


		}
		return rtn;
	}

	@Override
	public boolean handleModifierUpdated(ModifierEvent e) {

		return false;
	}

}
