package interfaces.jPanels.startWindow;

import javax.swing.JPanel;
import java.awt.SystemColor;
import java.awt.CardLayout;

/**
 * Panel startowy w którym znajduje się menu i rożne logiki w zalężności od 
 * wybranej opcji ( logowanie, rejestracja, powitanie ).
 * @author Kamil Zimny
 *
 */
public class StartApplicationJPanel extends JPanel 
{
	private JPanel panel_control;
	private JPanel  panel_logic;
	
	public StartApplicationJPanel() {
		setBackground(SystemColor.activeCaption);
		setLayout(null);
		
		panel_control = new JPanel();
		panel_control.setBackground(SystemColor.window);
		panel_control.setBounds(10, 11, 245, 462);
		add(panel_control);
		panel_control.setLayout(new CardLayout(0, 0));
		
		panel_logic = new JPanel();
		panel_logic.setBackground(SystemColor.window);
		panel_logic.setBounds(265, 11, 610, 462);
		add(panel_logic);
		panel_logic.setLayout(new CardLayout(0, 0));
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void addControlJPanel(JPanel control )
	{
		panel_control.add(control);
	}
	
	public void addLogicJPanel(JPanel logic)
	{
		panel_logic.add(logic);
	}
	
	public void removeLogicJPanel()
	{
		panel_logic.removeAll();
	}
	
}
