package jpanels.startWindow;

import interfaces.RoutePlanningPresenter;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.SystemColor;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MenuJPanel extends JPanel 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RoutePlanningPresenter presenter_route_planning;
	private String loggedUser;
	private final String NOT_LOGGED_USER = "Brak";
	private JLabel lblNewLabel;
	private JButton btn_startPlanning;
	private JButton btn_commisionEdit;
	
	public MenuJPanel() 
	{	
		loggedUser = NOT_LOGGED_USER;
		setBackground(SystemColor.inactiveCaption);
		setLayout(null);
		
		JPanel panel = new JPanel();
		
		panel.setBackground(SystemColor.inactiveCaptionText);
		panel.setBounds(22, 38, 242, 313);
		add(panel);
		panel.setLayout(null);
		
		JLabel lblMenu = new JLabel("Menu");
		lblMenu.setForeground(new Color(255, 204, 0));
		lblMenu.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 27));
		lblMenu.setBounds(82, 10, 84, 51);
		panel.add(lblMenu);
		
		btn_startPlanning = new JButton("Rozpocznij planowanie");
		btn_startPlanning.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				presenter_route_planning.setExternalDatabaseConnectionProperty();
				presenter_route_planning.changeMenu_to_startPlanning();
			}
		});
		btn_startPlanning.setEnabled(false);
		btn_startPlanning.setBackground(SystemColor.activeCaption);
		btn_startPlanning.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		btn_startPlanning.setBounds(33, 184, 184, 51);
		panel.add(btn_startPlanning);
		
		JButton btn_registery = new JButton("Rejestracja");
		btn_registery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				presenter_route_planning.changeMenu_to_registryNewUser();
			}
		});
		btn_registery.setBackground(SystemColor.activeCaption);
		btn_registery.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		btn_registery.setBounds(33, 128, 184, 51);
		panel.add(btn_registery);
		
		btn_commisionEdit = new JButton("Edycja zlece\u0144");
		btn_commisionEdit.setEnabled(false);
		btn_commisionEdit.setBackground(SystemColor.activeCaption);
		btn_commisionEdit.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		btn_commisionEdit.setBounds(33, 240, 184, 51);
		panel.add(btn_commisionEdit);
		
		JButton btn_logIn = new JButton("Logowanie");
		btn_logIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				if( loggedUser.equals(NOT_LOGGED_USER) )
					presenter_route_planning.changeMenu_to_loginUser();
				else
				{
					int dialogResult = JOptionPane.showConfirmDialog(MenuJPanel.this, "Ponowna próba zalogowania spowoduje wylogowanie z poprzedniego konta.\n"
							+ "Czy jesteś pewien, że chcesz zalogować się ponownie?", 
							"Ponowne logowanie", JOptionPane.YES_NO_OPTION);
					if(dialogResult == JOptionPane.YES_OPTION) 
					{
						loggedUser = NOT_LOGGED_USER;
						presenter_route_planning.changeMenu_to_loginUser();				
					}
				}
			}
		});
		btn_logIn.setBackground(SystemColor.activeCaption);
		btn_logIn.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		btn_logIn.setBounds(33, 72, 184, 51);
		panel.add(btn_logIn);
		
		lblNewLabel = new JLabel("Użytkownik: "+loggedUser);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblNewLabel.setBounds(22, 11, 251, 16);
		add(lblNewLabel);
	}
	
	public void setNewLoggedUser(String login)
	{
		loggedUser = login;
		lblNewLabel.setText("Użytkownik: "+loggedUser);
	}
	
	public void setNotLoggedUser()
	{
		loggedUser = NOT_LOGGED_USER;
		lblNewLabel.setText("Użytkownik: "+loggedUser);
	}
	
	public void setPresenter(final RoutePlanningPresenter presenter)
	{
		presenter_route_planning = presenter;
	}

	public void setEnableButtonsToUserAction(boolean flag)
	{
		btn_startPlanning.setEnabled(flag);
		btn_commisionEdit.setEnabled(flag);
	}
}
