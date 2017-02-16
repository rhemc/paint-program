package ca.rhemc.paintprogram.paint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * The TextBoxDialog pops up when the TextBoxTool is selected.
 * In the dialog, a user can customize the drawable string that TextBoxTool will create.
 */
public class TextBoxDialog extends JDialog {

	private static final int DEFAULT_SIZE = 12;
	private JTextField textField;
	private JComboBox fontChooser;
	private JComboBox fontSizeChooser;
	private JCheckBox boldCheck;
	private JCheckBox italicCheck;
	private Point pointRelativeToParent;
	boolean needMove = true;

	/**
	 * Constructs the JDialog that holds the textBox customization options
	 *
	 * @param view View of the paint project
	 */
	public TextBoxDialog(Frame view) {
		super(view);
		setTitle("Text Editor");
		setPreferredSize(new Dimension(200, 150));
		Container c = getContentPane();
		c.setLayout(new GridLayout(3, 1));
		pointRelativeToParent = new Point(0,0);

		addComponentListener(new ComponentAdapter(){
			@Override
			public void componentMoved(ComponentEvent e) {
				if(needMove) {
					pointRelativeToParent = getLocation();
					SwingUtilities.convertPointFromScreen(pointRelativeToParent,getParent());
				}
				needMove=true;
			}
		});

		getParent().addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				needMove=false;
				setLocation(getParent().getLocationOnScreen().x+ pointRelativeToParent.x,
						getParent().getLocationOnScreen().y+ pointRelativeToParent.y);
			}
		});

		textField = new JTextField("Text");
		c.add(textField);


		String fonts[] =
				GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		fontChooser = new JComboBox(fonts);
		JEditorPane editorPane = new JEditorPane();
		String fontFamily = editorPane.getFont().getFamily();
		fontChooser.setSelectedItem(fontFamily);
		c.add(fontChooser);

		JPanel bottomPanel = new JPanel(new GridLayout(2, 3));

		JLabel fontSizeLabel = new JLabel("Font Size");
		String[] sizes = new String[30];
		for(int i = 1; i <= sizes.length; i++) {
			sizes[i - 1] = String.valueOf(i);
		}
		fontSizeChooser = new JComboBox(sizes);
		fontSizeChooser.setSelectedIndex(DEFAULT_SIZE - 1);

		JLabel boldLabel = new JLabel("Bold");
		boldCheck = new JCheckBox();

		JLabel italicLabel = new JLabel("Italic");
		italicCheck = new JCheckBox();

		fontSizeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		boldLabel.setHorizontalAlignment(SwingConstants.CENTER);
		italicLabel.setHorizontalAlignment(SwingConstants.CENTER);
		boldCheck.setHorizontalAlignment(SwingConstants.CENTER);
		italicCheck.setHorizontalAlignment(SwingConstants.CENTER);
		bottomPanel.add(fontSizeLabel);
		bottomPanel.add(boldLabel);
		bottomPanel.add(italicLabel);
		bottomPanel.add(fontSizeChooser);
		bottomPanel.add(boldCheck);
		bottomPanel.add(italicCheck);
		c.add(bottomPanel);

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		pack();
	}

	public JTextField getTextField() {
		return textField;
	}

	public JComboBox getFontChooser() {
		return fontChooser;
	}

	public JComboBox getFontSizeChooser() {
		return fontSizeChooser;
	}

	public JCheckBox getBoldCheck() {
		return boldCheck;
	}

	public JCheckBox getItalicCheck() {
		return italicCheck;
	}

}
