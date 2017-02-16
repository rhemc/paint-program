package ca.rhemc.paintprogram.paint;

import ca.rhemc.paintprogram.pointer.ModifierEvent;
import ca.rhemc.paintprogram.pointer.PointerEvent;

import java.awt.event.MouseEvent;

public class SelectionTool implements ShapeManipulatorStrategy {
	private PaintModel model;
	private PaintShape[] shapes;
	private int activePointer = -1;

	public SelectionTool(PaintModel model, PaintShape[] shapes) {
		this.model = model;
		this.shapes = shapes;
	}

	@Override
	public Drawable deselect() {
		Drawable rtn = null;
		if(shapes[0] != null) {
			if(((Selection) shapes[0]).setReleased())
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
		Selection selection = (Selection) shapes[0];
		switch(e.getID()) {
			case MouseEvent.MOUSE_PRESSED:
				if(activePointer != -1)
					break;
				activePointer = e.getPointerId();
				if(selection == null) {
					shapes[0] = new Selection(e.getX(), e.getY());
				} else {
					if(selection.contains(e.getX(), e.getY())&&e.getButton()!=MouseEvent.BUTTON3) {
						selection.setMove(e.getX(), e.getY());
					} else {
						rtn = deselect();
					}
				}
				break;
			case MouseEvent.MOUSE_MOVED:
				if(activePointer == -1 || activePointer != e.getPointerId() || selection == null)
					break;
				selection.mouseMoved(e.getX(), e.getY());
				break;
			case MouseEvent.MOUSE_RELEASED:
				if(activePointer == -1 || activePointer != e.getPointerId() || selection == null)
					break;
				if(!selection.release(e.getX(), e.getY(), model))
					deselect();
				activePointer = -1;
		}
		return rtn;
	}

	@Override
	public boolean handleModifierUpdated(ModifierEvent e) {

		return false;
	}
}
