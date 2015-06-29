package interfaces.jPanels.startWindow;

import interfaces.mvp.RoutePlanningPresenter;

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
	private JButton btn_showTransporters;
	private JButton btn_logIn;
	private JButton btn_registery;
	
	public MenuJPanel() 
	{	
		loggedUser = NOT_LOGGED_USER;
		setBackground(SystemColor.inactiveCaptionText);
		setLayout(null);
		
		JPanel panel = new JPanel();
		
		panel.setBackground(SystemColor.inactiveCaptionText);
		panel.setBounds(0, 0, 247, 444);
		add(panel);
		panel.setLayout(null);
		
		JLabel lblMenu = new JLabel("Menu");
		lblMenu.setForeground(new Color(255, 204, 0));
		lblMenu.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 28));
		lblMenu.setBounds(83, 44, 84, 51);
		panel.add(lblMenu);
		
		btn_startPlanning = new JButton("Rozpocznij planowanie ");
		btn_startPlanning.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				presenter_route_planning.setExternalDatabaseConnectionProperty();
				presenter_route_planning.changeMenu_to_startPlanning();
			}
		});
		btn_startPlanning.setEnabled(false);
		btn_startPlanning.setBackground(SystemColor.activeCaption);
		btn_startPlanning.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		btn_startPlanning.setBounds(10, 230, 224, 51);
		panel.add(btn_startPlanning);
		
	    btn_registery = new JButton("Rejestracja ");
		btn_registery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				presenter_route_planning.changeMenu_to_registryNewUser();
			}
		});
		btn_registery.setBackground(SystemColor.activeCaption);
		btn_registery.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		btn_registery.setBounds(10, 168, 224, 51);
		panel.add(btn_registery);
		
		btn_commisionEdit = new JButton("Edycja zleceń ");
		btn_commisionEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				presenter_route_planning.setExternalDatabaseConnectionProperty();
				presenter_route_planning.changeMenu_to_UnfinishedCommissions();
			}
		});

		btn_commisionEdit.setEnabled(false);
		btn_commisionEdit.setBackground(SystemColor.activeCaption);
		btn_commisionEdit.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		btn_commisionEdit.setBounds(10, 354, 224, 51);
		panel.add(btn_commisionEdit);
		
		btn_logIn = new JButton("Logowanie ");
		btn_logIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				if( !isUserLogged() )
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
						presenter_route_planning.setNotLoggedUser();
						presenter_route_planning.setEnableButtonsToUserAction(false);
					}
				}
			}
		});
		btn_logIn.setBackground(SystemColor.activeCaption);
		btn_logIn.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		btn_logIn.setBounds(10, 106, 224, 51);
		panel.add(btn_logIn);
		
		lblNewLabel = new JLabel("Użytkownik: "+loggedUser);
		lblNewLabel.setForeground(new Color(255, 204, 0));
		lblNewLabel.setBounds(10, 11, 242, 16);
		panel.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		
		btn_showTransporters = new JButton("Pokaż przewoźników ");
		btn_showTransporters.setEnabled(false);
		btn_showTransporters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				presenter_route_planning.setExternalDatabaseConnectionProperty();
				presenter_route_planning.changeManufacurerVisualization_to_transportVisualization(-1);
			}
		});
		btn_showTransporters.setBackground(SystemColor.activeCaption);
		btn_showTransporters.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		btn_showTransporters.setBounds(10, 292, 224, 51);
		panel.add(btn_showTransporters);
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
	
	public boolean isUserLogged()
	{
		return !loggedUser.equals(NOT_LOGGED_USER);
	}
	
	public void setPresenter(final RoutePlanningPresenter presenter)
	{
		presenter_route_planning = presenter;
	}

	public void setEnableButtonsToUserAction(boolean flag)
	{
		btn_startPlanning.setEnabled(flag);
		btn_commisionEdit.setEnabled(flag);
		btn_showTransporters.setEnabled(flag);
	}
	
	public void setEnableButtonsFirstAction(boolean flag)
	{
		btn_logIn.setEnabled(flag);
		btn_registery.setEnabled(flag);
	}
	
	public void setEnableAllButtons(boolean flag)
	{
		setEnableButtonsToUserAction(flag);
		setEnableButtonsFirstAction(flag);
	}
}
