package jpanels.ManufacturerVisualization.ManufactureInfo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import java.awt.SystemColor;

public class DiscriptionOnMapJPanel extends JPanel 
{
	private static final long serialVersionUID = 1L;
	private JScrollPane scrollPane;
	private JTextArea txtStatus;
	  
	public DiscriptionOnMapJPanel() 
	{
		// status UI to display the selected features
	    txtStatus = new JTextArea();
	    txtStatus.setText("Nazwy zaznaczonych producentów.");
	    txtStatus.setLineWrap(true);
	    txtStatus.setWrapStyleWord(true);
	    txtStatus.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
	    txtStatus.setBackground(SystemColor.inactiveCaptionText);
	    txtStatus.setForeground(new Color(255, 204, 0));
	    txtStatus.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	    txtStatus.setEditable(false);

	    // description
	    JTextArea description = new JTextArea(
	        "Kliknij na obiekty by zaznaczyć producenta.");
	    description.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 10));
	    description.setForeground(SystemColor.desktop);
	    description.setBackground(SystemColor.activeCaption);
	    description.setEditable(false);
	    description.setLineWrap(true);
	    description.setWrapStyleWord(true);
	    description.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	    description.setSize(200, 60);
	    description.setMaximumSize(new Dimension(200, 60));

	    // group the above UI items into a panel
	    BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
	    setLayout(boxLayout);
	    setLocation(10, 10);
	    setSize(200, 190);
	    setBackground(new Color(0, 0, 0, 80));
	    setBorder(new LineBorder(Color.BLACK, 5, false));

	    // Scroll pane for reporting features
	    scrollPane = new JScrollPane(txtStatus);

	    add(description);
	    add(scrollPane);
	}
	
	public JTextArea getDiscriptionArea()
	{
		txtStatus.setText("");
		return txtStatus;
	}
}
