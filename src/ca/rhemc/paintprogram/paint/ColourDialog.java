package ca.rhemc.paintprogram.paint;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.event.ChangeListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;

/**
 * ColourDialog is a JDialog that uses the JColorChooser component
 * and passes the chosen colour to the PaintPanel to be used for the next shapes.
 */
public class ColourDialog extends JDialog {

	private JColorChooser jcc;

	/**
	 * Creates a JColorChooser with the appropriate title.
	 * @param view Paint View
	 */
	public ColourDialog(Frame view, String title) {
		super(view);
		setTitle(title);
		jcc = new JColorChooser();
		jcc.setColor(Color.black);
		jcc.setBorder(BorderFactory.createTitledBorder("Choose Color"));
		add(jcc);
		jcc.setPreviewPanel(new JPanel());
		setSize(new Dimension(455, 215));
	}

	public void addChangeListener(ChangeListener changeListener) {
		jcc.getSelectionModel().addChangeListener(changeListener);
	}

}
