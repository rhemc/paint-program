package ca.rhemc.paintprogram.paint;

import ca.rhemc.paintprogram.pointer.ModifierEvent;
import ca.rhemc.paintprogram.pointer.PointerEvent;

/**
 * Strategy each tool follow
 */
public interface ShapeManipulatorStrategy {

	/**
	 * Called when the tool is deselected
	 * @return the current active drawable
	 */
	public Drawable deselect();

	/**
	 *  Called when the tool is selected
	 */
	public void selected();

	/**
	 * Called when Pointer update is received
	 * @param e the PointerEvent
	 * @return
	 */
	public Drawable handlePointerUpdate(PointerEvent e);

	/**
	 * Called when Key update is received
	 * @param e the ModifierEvent
	 * @return
	 */
	public boolean handleModifierUpdated(ModifierEvent e);

}
