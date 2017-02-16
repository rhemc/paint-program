package ca.rhemc.paintprogram.pointer;

import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Object Handle Win32 API Calls from JNI bridge
 * Bin for windows can be compiled from WindowsProc.cpp and WindowsProc.h in the root dir
 */
public class WindowsPointer extends MouseAdapter {
	public static final int POINTER_MAX = 20;
	public static final int MAX_PRESSURE = 1024;
	private static final boolean TOUCH_SUPPORTED;

	static {
		boolean dllLoaded = true;
		try {
			// try to load the dll in the working dir, if failed set the flag to tell the call its failed
			System.loadLibrary("JNI");
		} catch(Error e) {
			System.out.println("JNI failed to load... falling back to MouseListener");
			dllLoaded = false;
		}
		TOUCH_SUPPORTED = dllLoaded;
	}

	private Frame frame;
	private int[] points = new int[POINTER_MAX];
	private Component[] components = new Component[POINTER_MAX];
	private Map<Component, WindowsEventComponent> listeners = new HashMap<Component, WindowsEventComponent>();

	/**
	 * Create WindowsPointer handle for frame.
	 * @param frame the frame
	 */
	public WindowsPointer(Frame frame) {
		if(frame == null)
			throw new IllegalArgumentException("null frame");
		if(TOUCH_SUPPORTED)
			try {
				init(getHWnd(frame));
				this.frame = frame;
				for(int i = 0; i < points.length; i++) {
					points[i] = -1;
				}
			} catch(RuntimeException | UnsatisfiedLinkError e) {
				e.printStackTrace();
			}
	}

	/**
	 *  Returns the windows handle id for the Frame
	 *
	 * @param frame the Frame
	 * @return
	 */
	private static long getHWnd(Frame frame) {
		//noinspection deprecation
		Object peer = frame.getPeer();
		Class c = peer.getClass();
		try {
			for(Method m : c.getMethods()) {
				if("getHWnd".equals(m.getName())) {
					return (Long) m.invoke(peer);
				}
			}
		} catch(IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("No HWND found for " + c);
	}

	/**
	 *  Called by c++ part when mouse is updated
	 *
	 * @param eventId
	 * @param when
	 * @param modifiers
	 * @param xAbs
	 * @param yAbs
	 * @param clickCount
	 * @param button
	 * @param pointerId
	 * @param pressure
	 */
	private void update(int eventId, long when, int modifiers, int xAbs, int yAbs, int clickCount,int button, int pointerId, int pressure) {
		float fPressure = pressure == 0 ? 1f : ((float) pressure / MAX_PRESSURE);
		int index = getPointId(pointerId);
		if(eventId==MouseEvent.MOUSE_PRESSED) {
			Point p = new Point(xAbs, yAbs);
			SwingUtilities.convertPointFromScreen(p,frame);
			Component comp = frame.findComponentAt(p);
			if(listeners.containsKey(comp))
				components[index] = comp;
			else
				components[index]=null;
		}

		if(components[index]!=null){
			listeners.get(components[index]).firePointerEvent(eventId, when, modifiers, xAbs, yAbs, clickCount, button, index, fPressure);
		}


		if(eventId == MouseEvent.MOUSE_EXITED&&index!=-1)
			releasePoint(index);
	}

	/**
	 *  Called by c++ part when key is pressed
	 *
	 * @param eventId
	 * @param when
	 * @param modifiers
	 * @param keyCode
	 * @param keyChar
	 */
	private void keyUpdate(int eventId, long when, int modifiers, int keyCode, char keyChar) {
		ModifierEvent event = new ModifierEvent(frame, eventId, when, modifiers, keyCode, keyChar);
		for(WindowsEventComponent e : listeners.values()) {
			e.fireModifierEvent(event);
		}
	}

	/**
	 *  initialize the c++ part of the class
	 *
	 * @param hWnd Window Handle from winodows
	 */
	private native void init(long hWnd);

	/**
	 * Adds the specified pointer listener to receive pointer and key events for the component.
	 *
	 * @param pointerListener the mouse listener
	 * @param component the component
	 */
	public void addListener(PointerListener pointerListener, Component component) {
		if(frame != null&&frame.isAncestorOf(component)) {
			WindowsEventComponent f = listeners.get(component);
			if(f == null) {
				WindowsEventComponent windowsEventComponent = new WindowsEventComponent(component);
				windowsEventComponent.add(pointerListener);
				listeners.put(component, windowsEventComponent);
			} else
				f.add(pointerListener);
		} else {
			InputEventProxy eventProxy = new InputEventProxy(pointerListener);
			component.addKeyListener(eventProxy);
			component.addMouseListener(eventProxy);
			component.addMouseMotionListener(eventProxy);
		}
	}

	/**
	 * Returns the index in the array of a system pointer id.
	 *
	 * @param id system id for the pointer
	 * @return index of the pointer
	 */
	private int getPointId(int id) {
		for(int i = 0; i < points.length; i++) {
			if(points[i] == id)
				return i;
		}

		for(int i = 0; i < points.length; i++) {
			if(points[i] == -1) {
				points[i] = id;
				return i;
			}
		}
		return -1;
	}

	private void releasePoint(int id) {
		components[id]=null;
		points[id] = -1;
	}
}