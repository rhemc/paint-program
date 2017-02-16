package ca.rhemc.paintprogram.paint;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Observable;

/**
 * Models the Shapes being
 */
public class PaintModel extends Observable implements ComponentListener {

	private static final int MAX_UNDO = 50;

	private static final Color BACKGROUND_COLOUR = Color.WHITE;
	//Store a image of that cant be undo.
	private BufferedImage image;
	//Store Shapes that can be undo.
	private LinkedList<Drawable> drawables = new LinkedList<Drawable>();
	//Store Shapes that can be redo.
	private LinkedList<Drawable> redo = new LinkedList<Drawable>();

	public PaintModel(Dimension screenSize) {
		this(screenSize.width / 2, screenSize.height / 2);
	}

	public PaintModel() {
		this(1000, 1000);
	}

	/**
	 * Create a PaintModel that store all Shapes and Image of the Panel
	 */
	public PaintModel(int width, int height) {
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = image.createGraphics();
		graphics.setColor(BACKGROUND_COLOUR);
		graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
		graphics.dispose();
	}

	public int getWidth() {
		return image.getWidth();
	}

	public int getHeight() {
		return image.getHeight();
	}

	public void addAllPrint(Iterable<Drawable> l) {
		if(l != null)
			for(Drawable d : l) {
				addPrint(d);
			}
	}

	public void addPrint(Drawable c) {
		if(c == null)
			return;
		synchronized(drawables) {
			drawables.add(c);
			while(drawables.size() > MAX_UNDO) {
				synchronized(image) {
					Graphics2D g = image.createGraphics();
					drawables.poll().print(g);
					g.dispose();
				}
			}
		}
		redo.clear();
		setChanged();
		notifyObservers();
	}

	public void clear() {
		synchronized(drawables) {
			drawables.add(new ClearMask(image.getWidth(), image.getHeight()));
		}
		setChanged();
		notifyObservers();
	}

	public void undo() {
		synchronized(drawables) {
			if(!drawables.isEmpty())
				redo.add(drawables.removeLast());
		}
		setChanged();
		notifyObservers();
	}

	public void redo() {
		synchronized(drawables) {
			if(!redo.isEmpty())
				drawables.add(redo.removeLast());
		}
		setChanged();
		notifyObservers();
	}

	public boolean canUndo() {
		return !drawables.isEmpty();
	}

	public boolean canRedo() {
		return !redo.isEmpty();
	}

	public void paint(Graphics2D g2) {
		synchronized(image) {
			g2.drawImage(image, 0, 0, null);
		}
		synchronized(drawables) {
			for(Drawable d : drawables) {
				d.print(g2);
			}
		}
	}

	private void resize(int x, int y) {
		synchronized(image) {
			BufferedImage i = new BufferedImage(x, y, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = i.createGraphics();
			g.setColor(BACKGROUND_COLOUR);
			g.fillRect(0, 0, i.getWidth(), i.getHeight());
			g.drawImage(image, 0, 0, null);
			g.dispose();
			image = i;
		}
	}

	@Override
	public void componentResized(ComponentEvent e) {
		synchronized(image) {
			Dimension d = e.getComponent().getSize();
			int x = d.width, y = d.height;
			if(x > image.getHeight() || y > image.getWidth()) {
				resize(Math.max(x, image.getWidth()), Math.max(y, image.getHeight()));
			}
		}
		((JPanel) e.getSource()).setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
	}

	@Override
	public void componentMoved(ComponentEvent e) {

	}

	@Override
	public void componentShown(ComponentEvent e) {

	}

	@Override
	public void componentHidden(ComponentEvent e) {

	}
}
