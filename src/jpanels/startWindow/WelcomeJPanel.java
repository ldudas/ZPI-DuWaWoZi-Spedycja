package jpanels.startWindow;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JTextPane;
import java.awt.SystemColor;
import javax.swing.ImageIcon;

public class WelcomeJPanel extends JPanel 
{
	public WelcomeJPanel() 
	{
		setBackground(SystemColor.inactiveCaptionText);
		setLayout(null);
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon("images/ImageToApplication.png"));
		label.setBounds(50, 55, 511, 314);
		add(label);
		
		JLabel label_1 = new JLabel("Witaj w aplikacji");
		label_1.setForeground(new Color(255, 204, 0));
		label_1.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 27));
		label_1.setBounds(177, 11, 269, 33);
		add(label_1);
		
		JTextPane txtpnZarzdzajacuchemDostaw = new JTextPane();
		txtpnZarzdzajacuchemDostaw.setText("Zarządzaj łańcuchem dostaw w znacznie szybszy i wydajniejszy sposób za pomocą naszej aplickacji.");
		txtpnZarzdzajacuchemDostaw.setForeground(new Color(255, 204, 0));
		txtpnZarzdzajacuchemDostaw.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));
		txtpnZarzdzajacuchemDostaw.setEditable(false);
		txtpnZarzdzajacuchemDostaw.setBackground(SystemColor.inactiveCaptionText);
		txtpnZarzdzajacuchemDostaw.setBounds(74, 380, 474, 74);
		add(txtpnZarzdzajacuchemDostaw);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
}
