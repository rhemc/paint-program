package ca.rhemc.paintprogram.paint;

import ca.rhemc.paintprogram.pointer.ModifierEvent;
import ca.rhemc.paintprogram.pointer.PointerEvent;

import java.awt.event.MouseEvent;

/**
 * The TextBoxTool builds TextBox PaintShapes depending on the parameters in the StylePanel and handles mouse events.
 */
public class TextBoxTool implements ShapeManipulatorStrategy {

	private TextBoxDialog textBoxDialog;
	private PaintShape[] shapes;
	private StylePanel stylePanel;

	public TextBoxTool(StylePanel stylePanel, TextBoxDialog textBoxDialog, PaintShape[] shapes) {
		this.textBoxDialog = textBoxDialog;
		this.stylePanel = stylePanel;
		this.shapes = shapes;
	}

	@Override
	public Drawable deselect() {
		textBoxDialog.setVisible(false);
		return null;
	}

	@Override
	public void selected() {
		textBoxDialog.setVisible(true);
	}

	@Override
	public Drawable handlePointerUpdate(PointerEvent e) {
		Drawable rtn = null;
		switch(e.getID()) {
			case MouseEvent.MOUSE_PRESSED:
				int fontStyle = (textBoxDialog.getBoldCheck().isSelected() ? 1 : 0) + (textBoxDialog.getItalicCheck().isSelected() ? 2 : 0);
				System.out.println(fontStyle);
				shapes[e.getPointerId()] = new TextBox(e.getX(), e.getY(), stylePanel.getColour(),
						Integer.valueOf(textBoxDialog.getFontSizeChooser().getSelectedItem().toString()),
						textBoxDialog.getFontChooser().getSelectedItem().toString(),
						textBoxDialog.getTextField().getText(), fontStyle);
				break;
			case MouseEvent.MOUSE_MOVED:
				if(shapes[e.getPointerId()] != null) {
					shapes[e.getPointerId()].mouseMoved(e.getX(), e.getY());
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
