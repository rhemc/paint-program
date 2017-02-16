package ca.rhemc.paintprogram.pointer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * Proxy Java MouseEvent to PointerEvent
 */
public class InputEventProxy extends MouseAdapter implements KeyListener {

	private PointerListener listener;

	InputEventProxy(PointerListener listener) {
		this.listener = listener;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		listener.pointerUpdated(new PointerEvent(e));
	}

	@Override
	public void mousePressed(MouseEvent e) {
		listener.pointerUpdated(new PointerEvent(e));
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		listener.pointerUpdated(new PointerEvent(e));
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		PointerEvent p = new PointerEvent(e);
		p.eventOverride(MouseEvent.MOUSE_MOVED);
		listener.pointerUpdated(p);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		listener.pointerUpdated(new PointerEvent(e));
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		listener.modifierUpdated(new ModifierEvent(e));
	}

	@Override
	public void keyReleased(KeyEvent e) {
		listener.modifierUpdated(new ModifierEvent(e));
	}
}
