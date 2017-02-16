package ca.rhemc.paintprogram.paint;

import ca.rhemc.paintprogram.pointer.ModifierEvent;
import ca.rhemc.paintprogram.pointer.PointerEvent;
import ca.rhemc.paintprogram.pointer.PointerListener;
import ca.rhemc.paintprogram.pointer.WindowsPointer;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

/**
 * Handles Drawing and Displaying of Shapes
 */
public class PaintPanel extends JPanel implements Observer, PointerListener {
	private PaintModel model; // slight departure from MVC, because of the way painting works
	private int mode; // modifies how we interpret input (could be better?)


	private PaintShape[] shapes = new PaintShape[WindowsPointer.POINTER_MAX];
	private ShapeManipulatorStrategy[] toolList;
	private int edges;
	private boolean keyDown;

	public PaintPanel(PaintModel model) {
		setBackground(Color.white);

		this.model = model;

		mode = 0;
		this.model.addObserver(this);
		setFocusable(true);
		addComponentListener(model);
	}

	/**
	 * Initializes all the tools (strategy) using given style panel and dialog boxes
	 * Factory is not useful because all tools are pre-created
	 * and associated with the index (mode) of an array (toolList)
	 * @param stylePanel Contains all of the styles
	 * @param textBoxDialog Contains the styles for TextBox shapes
	 */
	public void initializeTools(StylePanel stylePanel, TextBoxDialog textBoxDialog) {
		toolList = new ShapeManipulatorStrategy[]{new SelectionTool(model, shapes),
				new SquiggleTool(stylePanel, true,shapes),
				new TextBoxTool(stylePanel, textBoxDialog, shapes),
				new SprayPaintTool(stylePanel, this, shapes),
				new PolylineTool(stylePanel, shapes),
				new SquiggleTool(stylePanel, shapes),
				new PolygonTool(stylePanel, this, shapes, ConcavePolygon.class),
				new PolygonTool(stylePanel, this, shapes, RegularPolygon.class)};
	}

	/**
	 * View aspect of this
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g); //paint background
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, getWidth(), getHeight());
		model.paint(g2);
		for(PaintShape s : shapes) {
			if(s != null) {
				s.print(g2);
			}
		}
		g2.dispose();
	}

	@Override
	public void update(Observable o, Object arg) {
		repaint(); // Schedule a call to paintComponent
	}

	/**
	 * Controller aspect of this
	 * The mode decides which tool to use for when the mouse is clicked
	 */
	public void setMode(int mode) {
		model.addPrint(toolList[this.mode].deselect());
		this.mode = Math.min(mode, toolList.length - 1);
		toolList[this.mode].selected();
	}

	public int getEdges() {
		return edges;
	}

	public void setEdges(int edges) {
		this.edges = edges;
	}

	public void clear() {
		setMode(mode);
		model.clear();
		repaint();
	}

	public void undo() {
		setMode(mode);
		model.undo();
		repaint();
	}

	public void redo() {
		setMode(mode);
		model.redo();
		repaint();
	}

	@Override
	public void pointerUpdated(PointerEvent e) {
		if(e.getID()== MouseEvent.MOUSE_PRESSED)
			requestFocus();
		model.addPrint(toolList[mode].handlePointerUpdate(e));
		repaint();
	}

	@Override
	public void modifierUpdated(ModifierEvent e) {
		if(e.isControlDown() && e.getID() == KeyEvent.KEY_PRESSED) {
			if(e.getKeyChar() == 'Z' || ((char) e.getKeyCode() == 'Z')) {
				undo();
				return;
			} else if(e.getKeyChar() == 'Y' || ((char) e.getKeyCode() == 'Y')) {
				redo();
				return;
			}
		}
		if(toolList[mode].handleModifierUpdated(e)) {
			repaint();
		}
	}
}
