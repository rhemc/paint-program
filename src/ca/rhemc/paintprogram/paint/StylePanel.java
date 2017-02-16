package ca.rhemc.paintprogram.paint;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.colorchooser.ColorSelectionModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

/**
 * StylePanel is a JPanel using GridBagLayout to format the components,
 * that holds all the style options, including undo,redo,clear,
 * fill,lineStyle,lineThickness and the colour panel button.
 */
public class StylePanel extends JPanel implements Observer, ComponentListener {

	private final JCheckBox borderCheckBox;
	private JButton borderColourButton;
	private JButton colourButton;
	private JButton undo;
	private JButton redo;
	private Color borderColour = Color.black;
	private Color colour = Color.black;
	private float lineThickness = 5f;
	private int strokeStyle;
	private boolean fill;
	private boolean border;
	private ImageIcon[] imageIconArray;


	/**
	 *
	 * Creates a JPanel, using GridBagLayout to format the components
	 *
	 * @param paintPanel PaintPanel object
	 * @param colourDialog JDialog holding JColorChooser object corresponding to the overall colour
	 * @param borderColourDialog JDialog holding JColorChooser object corresponding to the shape border colour
	 *                           when fill and border are activated.
	 */
	public StylePanel(PaintPanel paintPanel, ColourDialog colourDialog, ColourDialog borderColourDialog) {

		setLayout(new GridBagLayout());
		setPreferredSize(new Dimension(0, 100));
		JPanel lineThicknessPanel = new JPanel();
		JLabel lineThicknessLabel = new JLabel("Line Thickness");
		lineThicknessLabel.setHorizontalAlignment(SwingConstants.CENTER);
		JTextField lineThicknessText = new JTextField("5", 3);
		JSlider lineThicknessSlider = new JSlider(1, 1024, 256);
		// JSlider max is 1024 to match what the built-in windows pen-pressure max is for convenience.
		lineThicknessText.setHorizontalAlignment(SwingConstants.CENTER);
		lineThicknessText.addActionListener(e -> {
			try {
				float value = Float.valueOf(((JTextField) e.getSource()).getText());
				if(value > 20) {
					value = 20;
				}
				if(value < 1) {
					value = 1;
				}
				lineThickness = value;
				value = Math.round(value * 51.2);
				lineThicknessSlider.setValue((int) value);
			} catch(NumberFormatException e1) {
				e1.printStackTrace();
			} catch(Exception exception) {
				((JTextField) e.getSource()).setText("1");
				lineThickness = 1;
				lineThicknessSlider.setValue(1);
			}
		});
		lineThicknessText.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				if(!Character.isDigit((e.getKeyChar()))) {
					if(e.getKeyChar() != ('.'))
						e.consume(); // Only accept digits in the textbox
				}
				String s = lineThicknessText.getText();
				if(s.length() >= 3 && (!s.substring(s.length() - 1).equals(".") ||
						s.substring(s.length() - 2).equals(".."))) {
					e.consume(); // Don't allow more than 3 digits in the textbox unless the last one was a "."
				}
			}
		});
		lineThicknessPanel.add(lineThicknessLabel);
		lineThicknessPanel.add(lineThicknessText);

		lineThicknessSlider.addChangeListener(e -> {
			float value = (float) (((JSlider) e.getSource()).getValue() / 51.2);
			lineThickness = value;
			DecimalFormat df = new DecimalFormat("#.#"); // Rounds up to nearest float at 1 decimal point
			df.setRoundingMode(RoundingMode.CEILING);
			if (value < 1) value = 1;
			lineThicknessText.setText(df.format(value));
		});

		JLabel borderLabel = new JLabel("Border");
		borderLabel.setHorizontalAlignment(SwingConstants.CENTER);
		borderCheckBox = new JCheckBox();
		borderCheckBox.setEnabled(false);
		borderCheckBox.addActionListener(e -> {
			border = borderCheckBox.isSelected();
		});
		borderCheckBox.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel fillLabel = new JLabel("Fill");
		fillLabel.setHorizontalAlignment(SwingConstants.CENTER);
		JCheckBox fillCheckBox = new JCheckBox();
		fillCheckBox.addActionListener(e -> {
			fill = fillCheckBox.isSelected();
			if(fill) {
				borderCheckBox.setEnabled(true);
			} else {
				border = false;
				borderCheckBox.setSelected(false);
				borderCheckBox.setEnabled(false);
			}
		});
		fillCheckBox.setHorizontalAlignment(SwingConstants.CENTER);


		JPanel fillAndBorderLabels = new JPanel(new GridLayout(1, 2));
		fillAndBorderLabels.add(fillLabel);
		fillAndBorderLabels.add(borderLabel);
		JPanel fillAndBorderChecks = new JPanel(new GridLayout(1, 2));
		fillAndBorderChecks.add(fillCheckBox);
		fillAndBorderChecks.add(borderCheckBox);

		JLabel styleLabel = new JLabel("Line Style");
		styleLabel.setHorizontalAlignment(SwingConstants.CENTER);

		String[] s = {"Basic Stroke", "Dashed Stroke", "Circle Stroke", "3D Stroke", "Wave Stroke"};
		imageIconArray = new ImageIcon[s.length];
		for(int i = 0; i < s.length; i++) {
			BufferedImage bufferedImage = new BufferedImage(80, 20, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = bufferedImage.createGraphics();
			g2.setColor(colour);
			StrokeFactory strokeFactory = new StrokeFactory();
			g2.setStroke(strokeFactory.createStroke(i, 5));
			g2.drawLine(0, (int) (bufferedImage.getHeight() / 2), bufferedImage.getWidth(), bufferedImage.getHeight() / 2);
			imageIconArray[i] = new ImageIcon(bufferedImage);
		}
		JComboBox styleComboBox = new JComboBox(imageIconArray);
		styleComboBox.addComponentListener(this);

		styleComboBox.setSelectedIndex(0);
		styleComboBox.addActionListener(e -> {
			strokeStyle = styleComboBox.getSelectedIndex();
		});

		JLabel colourLabel = new JLabel("Choose Colour");
		colourLabel.setHorizontalAlignment(SwingConstants.CENTER);

		JPanel colourPanel = new JPanel(new GridLayout(2, 1));

		borderColourButton = new JButton("Border Colour");
		borderColourButton.addActionListener(e -> {
			if(borderColourDialog.isVisible()) {
				borderColourDialog.setVisible(false);
			} else {
				borderColourDialog.setLocation(getLocationOnScreen().x + getWidth() - 450, getLocationOnScreen().y + getHeight() - 285);
				borderColourDialog.setVisible(true);
			}
		});

		borderColourDialog.addChangeListener(e -> {
			ColorSelectionModel jccSelectionModel = (ColorSelectionModel) e.getSource();
			Color newColor = jccSelectionModel.getSelectedColor();
			borderColourButton.setForeground(newColor);
			borderColour = newColor;
		});

		colourButton = new JButton("Colour");
		colourButton.addActionListener(e -> {
			if(colourDialog.isVisible()) {
				colourDialog.setVisible(false);
			} else {
				colourDialog.setLocation(getLocationOnScreen().x + getWidth() - 450, getLocationOnScreen().y + getHeight() - 285);
				colourDialog.setVisible(true);
			}
		});

		colourDialog.addChangeListener(e -> {
			ColorSelectionModel jccSelectionModel = (ColorSelectionModel) e.getSource();
			colour = jccSelectionModel.getSelectedColor();
			colourButton.setForeground(colour);
		});

		colourPanel.add(colourButton);
		colourPanel.add(borderColourButton);

		undo = new JButton("Undo");
		undo.setEnabled(false);
		undo.addActionListener(e -> {
			paintPanel.undo();
		});
		redo = new JButton("Redo");
		redo.setEnabled(false);
		redo.addActionListener(e -> {
			paintPanel.redo();
		});
		JButton clear = new JButton("Clear");
		clear.addActionListener(e -> {
			paintPanel.clear();
		});

		JPanel buttonPanel = new JPanel(new GridLayout(3, 1));
		buttonPanel.add(undo);
		buttonPanel.add(redo);
		buttonPanel.add(clear);

		// GridBagLayout with two rows and 11 columns.
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.1;
		c.gridx = 0;
		c.gridy = 0;
		add(new JLabel(), c);
		c.weightx = 0.5;
		c.gridx = 1;
		c.gridheight = 2;
		add(buttonPanel, c);
		c.gridheight = 1;
		c.weightx = 1;
		c.gridy = 0;
		c.gridx = 3;
		add(lineThicknessPanel, c);
		c.gridy = 1;
		add(lineThicknessSlider, c);
		c.gridy = 0;
		c.gridx = 5;
		c.weightx = 0.2;
		add(fillAndBorderLabels, c);
		c.gridy = 1;
		add(fillAndBorderChecks, c);
		c.weightx = 1;
		c.gridy = 0;
		c.gridx = 7;
		add(styleLabel, c);
		c.gridy = 1;
		add(styleComboBox, c);
		c.gridy = 0;
		c.gridx = 9;
		add(colourLabel, c);
		c.gridy = 1;
		add(colourPanel, c);
		c.weightx = 0.1;
		c.gridy = 0;
		c.gridx = 10;
		add(new JLabel(), c);
		c.weightx = 0.2;

		// Each column containing important JComponents are separated with JSeparators.
		for(int i = 2; i < 10; i += 2) {
			c.gridx = i;
			for(int j = 0; j < 3; j++) {
				c.gridy = j;
				c.fill = GridBagConstraints.VERTICAL;
				add(new JSeparator(SwingConstants.VERTICAL), c);
			}
		}

	}

	/**
	 * checks if there are objects in the undo and redo arrays
	 * and showing if undo and redo buttons can be clicked accordingly
	 *
	 * @param o   PaintModel
	 * @param arg
	 */
	@Override
	public void update(Observable o, Object arg) {
		PaintModel paintModel = (PaintModel) o;
		undo.setEnabled(paintModel.canUndo());
		redo.setEnabled(paintModel.canRedo());
	}


	public Color getColour() {
		return colour;
	}

	public float getLineThickness() {
		return lineThickness;
	}

	public int getStrokeStyle() {
		return strokeStyle;
	}

	public boolean isFill() {
		return fill;
	}

	public float getLineThickness(float ratio){
		return lineThickness*(.5f+((ratio==0)?.5f:ratio));
	}

	public boolean isBorder() {
		return border;
	}

	public Color getBorderColour() {
		return borderColour;
	}

	@Override
	public void componentResized(ComponentEvent e) {
		JComboBox comboBox = (JComboBox) e.getSource();
		int height = comboBox.getHeight();
		int width = comboBox.getWidth();
		for(int i = 0; i < imageIconArray.length; i++) {
			BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = bufferedImage.createGraphics();
			g2.setColor(colour);
			StrokeFactory strokeFactory = new StrokeFactory();
			g2.setStroke(strokeFactory.createStroke(i, 5));
			g2.drawLine(0, height / 2, width, height / 2);
			imageIconArray[i].setImage(bufferedImage);
		}
		comboBox.repaint();

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
