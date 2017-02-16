package ca.rhemc.paintprogram.paint;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ShapeChooserPanel holds the buttons that select the mode of the paint app.
 * Contains all the tools' description that create shapes
 */
public class ShapeChooserPanel extends JPanel implements ActionListener {
	// All polygon shapes (triangle, rectangle, circle, concave polygon and polygon) are handled by the same class
	// File names for assets
	private static final String[] BUTTON_ASSETS = {"Selection", "Eraser", "TextBox", "SprayPaint", "Polyline", "Squiggle", "ConcavePolygon", "Triangle", "Rectangle", "Circle", "Polygon"};
	// Displayed text labels
	private static final String[] BUTTON_LABELS = {"Selection", "Eraser", "TextBox", "Spray Paint", "Polyline", "Squiggle", "Edges: 5", "Triangle", "Rectangle", "Circle", "Edges: 5"};
	// Each polygon has their own number of edges, the non-polygon shapes have 0 as their default
	private static final int[] BUTTON_EDGES = {0, 0, 0, 0, 0, 0, 5, 3, 4, 100, 5};
	private ModeTextField modeTextField;
	private JButton[] shapeButtons;
	private PaintPanel paintPanel;
	private int activeButton = 7;

	public ShapeChooserPanel(PaintPanel paintPanel) {
		this.paintPanel = paintPanel;
		shapeButtons = new JButton[BUTTON_LABELS.length];
		setLayout(new GridLayout(BUTTON_LABELS.length + 1, 1));
		setPreferredSize(new Dimension(105, 300));

		for(int index = 0; index < BUTTON_LABELS.length; index++) {
			shapeButtons[index] = new ShapeButton(index, BUTTON_ASSETS[index]);
			shapeButtons[index].setFocusable(false);
			add(shapeButtons[index]);
			shapeButtons[index].addActionListener(this);
		}


		modeTextField = new ModeTextField(paintPanel);
		setActiveButton(activeButton);
		add(modeTextField);
	}

	private void setActiveButton(int activeButton) {
		shapeButtons[this.activeButton].setEnabled(true);
		this.activeButton = activeButton;
		shapeButtons[this.activeButton].setEnabled(false);
		paintPanel.setMode(activeButton);
		paintPanel.setEdges(BUTTON_EDGES[activeButton]);
		modeTextField.setEnabled(false);
		modeTextField.setText(BUTTON_LABELS[activeButton]);
		modeTextField.setEnabled(BUTTON_EDGES[activeButton] == 5);
	}

	/**
	 * Controller aspect of this
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		setActiveButton(((ShapeButton) e.getSource()).getShapeNum());
	}
}

