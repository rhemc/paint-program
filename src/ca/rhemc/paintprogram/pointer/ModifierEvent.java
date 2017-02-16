package ca.rhemc.paintprogram.pointer;

import java.awt.Component;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class ModifierEvent {


	public KeyEvent event;

	ModifierEvent(Component source, int id, long when, int modifiers, int keyCode, char keyChar) {
		event = new KeyEvent(source, id, when, modifiers, keyCode, keyChar);
	}

	public ModifierEvent(KeyEvent event) {
		this.event = event;
	}

	public static int getMaskForButton(int button) {
		return InputEvent.getMaskForButton(button);
	}

	public static String getModifiersExText(int modifiers) {
		return InputEvent.getModifiersExText(modifiers);
	}

	public boolean isShiftDown() {
		return event.isShiftDown();
	}

	public boolean isControlDown() {
		return event.isControlDown();
	}

	public boolean isMetaDown() {
		return event.isMetaDown();
	}

	public boolean isAltDown() {
		return event.isAltDown();
	}

	public boolean isAltGraphDown() {
		return event.isAltGraphDown();
	}

	public long getWhen() {
		return event.getWhen();
	}

	public int getModifiers() {
		return event.getModifiers();
	}

	public int getModifiersEx() {
		return event.getModifiersEx();
	}

	public void consume() {
		event.consume();
	}

	public boolean isConsumed() {
		return event.isConsumed();
	}

	public Component getComponent() {
		return event.getComponent();
	}

	public String paramString() {
		return event.paramString();
	}

	public int getID() {
		return event.getID();
	}

	public Object getSource() {
		return event.getSource();
	}

	public void setSource(Object newSource) {
		event.setSource(newSource);
	}

	public int getKeyCode() {
		return event.getKeyCode();
	}

	public char getKeyChar() {
		return event.getKeyChar();
	}
}
