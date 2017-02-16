package ca.rhemc.paintprogram.pointer;

public interface PointerListener {

	/**
	 *  Called when windows pointer update is received
	 * @param e the PointerEvent
	 */
	public void pointerUpdated(PointerEvent e);

	/**
	 *  Called when windows key update is received
	 * @param e the ModifierEvent
	 */
	public void modifierUpdated(ModifierEvent e);
}
