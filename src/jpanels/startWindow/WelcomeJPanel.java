package jpanels.startWindow;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;

import javax.swing.JTextPane;

import java.awt.SystemColor;

import javax.swing.ImageIcon;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class WelcomeJPanel extends JPanel 
{
	public WelcomeJPanel() 
	{
		setBackground(SystemColor.inactiveCaptionText);
		setLayout(null);
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(getClass().getResource("/additions/ImageToApplication.png")));
		label.setBounds(74, 55, 474, 314);
		add(label);
		
		JLabel lblWitajWForwarder = new JLabel("Witaj w FORWARDer");
		lblWitajWForwarder.setForeground(new Color(255, 204, 0));
		lblWitajWForwarder.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 27));
		lblWitajWForwarder.setBounds(156, 11, 314, 33);
		add(lblWitajWForwarder);
		
		JTextPane txtpnZarzdzajacuchemDostaw = new JTextPane();
		txtpnZarzdzajacuchemDostaw.setText("Zarządzaj łańcuchem dostaw w znacznie szybszy i bardziej wydajny sposób.");
		txtpnZarzdzajacuchemDostaw.setForeground(new Color(255, 204, 0));
		txtpnZarzdzajacuchemDostaw.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));
		txtpnZarzdzajacuchemDostaw.setEditable(false);
		txtpnZarzdzajacuchemDostaw.setBackground(SystemColor.inactiveCaptionText);
		txtpnZarzdzajacuchemDostaw.setBounds(74, 380, 474, 74);
		
		StyledDocument doc = txtpnZarzdzajacuchemDostaw.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		
		add(txtpnZarzdzajacuchemDostaw);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
}
