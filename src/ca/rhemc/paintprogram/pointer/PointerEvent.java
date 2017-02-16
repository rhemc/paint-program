package ca.rhemc.paintprogram.pointer;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

/**
 * Event Object for Win32 Pointer Events
 */
public class PointerEvent {

	private final MouseEvent event;
	private int pointerId;
	private float pressure;
	private int overridEvent;

	/**
	 * Constructs a prototypical Event.
	 *
	 * @param source   The object on which the Event initially occurred.
	 * @param pressure
	 * @throws IllegalArgumentException if source is null.
	 */
	PointerEvent(Component source, int eventId, long when, int modifiers, int x, int y, int xAbs, int yAbs, int clickCount, int button, int pointerId, float pressure) {
		event = new MouseEvent(source, eventId, when, modifiers, x, y, xAbs, yAbs, clickCount, false, button);
		this.pointerId = pointerId;
		this.pressure = pressure;
	}

	PointerEvent(MouseEvent event) {
		this.event = event;
		pointerId = 0;
		pressure = 0;
	}

	public static String getMouseModifiersText(int modifiers) {
		return MouseEvent.getMouseModifiersText(modifiers);
	}

	public static int getMaskForButton(int button) {
		return InputEvent.getMaskForButton(button);
	}

	public static String getModifiersExText(int modifiers) {
		return InputEvent.getModifiersExText(modifiers);
	}

	public int getPointerId() {
		return pointerId;
	}

	public float getPressure() {
		return pressure;
	}

	public MouseEvent getEvent() {
		return event;
	}

	public Point getLocationOnScreen() {
		return event.getLocationOnScreen();
	}

	public int getXOnScreen() {
		return event.getXOnScreen();
	}

	public int getYOnScreen() {
		return event.getYOnScreen();
	}

	public int getModifiersEx() {
		return event.getModifiersEx();
	}

	public int getX() {
		return event.getX();
	}

	public int getY() {
		return event.getY();
	}

	public Point getPoint() {
		return event.getPoint();
	}

	public void translatePoint(int x, int y) {
		event.translatePoint(x, y);
	}

	public int getClickCount() {
		return event.getClickCount();
	}

	public int getButton() {
		return event.getButton();
	}

	public boolean isPopupTrigger() {
		return event.isPopupTrigger();
	}

	public String paramString() {
		return event.paramString();
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

	public void consume() {
		event.consume();
	}

	public boolean isConsumed() {
		return event.isConsumed();
	}

	public Component getComponent() {
		return event.getComponent();
	}

	void eventOverride(int id) {
		overridEvent = id;
	}

	public int getID() {
		if(overridEvent != 0)
			return overridEvent;
		return event.getID();
	}

	public Object getSource() {
		return event.getSource();
	}

	public void setSource(Object newSource) {
		event.setSource(newSource);
	}
}
