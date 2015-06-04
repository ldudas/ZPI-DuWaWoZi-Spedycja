package jpanels.startWindow;

import interfaces.RoutePlanningPresenter;

import javax.swing.JPanel;

import java.awt.SystemColor;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.Color;

import javax.swing.JPasswordField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class RegistryJPanel extends JPanel 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField textField_login;
	private JTextField textField_serverAddress;
	private JTextField textField_databaseName;
	private JTextField textField_serverPort;
	private JTextField textField_databaseLogin;
	private JPasswordField passwordField_password;
	private JPasswordField passwordField_passwordRepeat;
	private JLabel lblLogin;
	private JLabel lblPassword;
	private JLabel lblPowtrzHaso;
	private JLabel lblNewLabel;
	private JLabel lblPort;
	private JLabel lblNazwaBazyDanych;
	private JLabel lblLoginBazy;
	private JPasswordField passwordField_databasePassword;
	private JLabel lblHasoBazy;
	private JButton btn_testConnection;
	private JButton btn_registry;
	
	private boolean connectionSuccesedTested;
	private RoutePlanningPresenter presenter_route_planning;
	
	public RegistryJPanel() 
	{
		connectionSuccesedTested = false;
		
		setBackground(SystemColor.inactiveCaption);
		setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.inactiveCaptionText);
		panel.setBounds(10, 11, 390, 420);
		add(panel);
		panel.setLayout(null);
		
		textField_login = new JTextField();
		textField_login.setBackground(SystemColor.activeCaption);
		textField_login.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		textField_login.setBounds(133, 81, 128, 20);
		panel.add(textField_login);
		textField_login.setColumns(10);
		
		textField_serverAddress = new JTextField();
		textField_serverAddress.setBackground(SystemColor.activeCaption);
		textField_serverAddress.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		textField_serverAddress.setBounds(133, 193, 128, 20);
		panel.add(textField_serverAddress);
		textField_serverAddress.setColumns(10);
		
		textField_databaseName = new JTextField();
		textField_databaseName.setBackground(SystemColor.activeCaption);
		textField_databaseName.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		textField_databaseName.setBounds(133, 255, 128, 20);
		panel.add(textField_databaseName);
		textField_databaseName.setColumns(10);
		
		textField_serverPort = new JTextField();
		textField_serverPort.setBackground(SystemColor.activeCaption);
		textField_serverPort.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		textField_serverPort.setBounds(133, 224, 40, 20);
		panel.add(textField_serverPort);
		textField_serverPort.setColumns(10);
		
		textField_databaseLogin = new JTextField();
		textField_databaseLogin.setBackground(SystemColor.activeCaption);
		textField_databaseLogin.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		textField_databaseLogin.setBounds(133, 286, 128, 20);
		panel.add(textField_databaseLogin);
		textField_databaseLogin.setColumns(10);
		
		JLabel lblRejestracjaNowegoUytkownika = new JLabel("Rejestracja nowego u\u017Cytkownika systemu");
		lblRejestracjaNowegoUytkownika.setForeground(new Color(255, 204, 0));
		lblRejestracjaNowegoUytkownika.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 16));
		lblRejestracjaNowegoUytkownika.setBounds(21, 11, 377, 29);
		panel.add(lblRejestracjaNowegoUytkownika);
		
		passwordField_password = new JPasswordField();
		passwordField_password.setBackground(SystemColor.activeCaption);
		passwordField_password.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		passwordField_password.setBounds(133, 112, 128, 20);
		panel.add(passwordField_password);
		
		passwordField_passwordRepeat = new JPasswordField();
		passwordField_passwordRepeat.setBackground(SystemColor.activeCaption);
		passwordField_passwordRepeat.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		passwordField_passwordRepeat.setBounds(133, 143, 128, 20);
		panel.add(passwordField_passwordRepeat);
		
		lblLogin = new JLabel("Login:");
		lblLogin.setForeground(new Color(255, 204, 0));
		lblLogin.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblLogin.setBounds(21, 84, 46, 14);
		panel.add(lblLogin);
		
		lblPassword = new JLabel("Has\u0142o:");
		lblPassword.setForeground(new Color(255, 204, 0));
		lblPassword.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblPassword.setBounds(21, 117, 60, 14);
		panel.add(lblPassword);
		
		lblPowtrzHaso = new JLabel("Powt\u00F3rz has\u0142o:");
		lblPowtrzHaso.setForeground(new Color(255, 204, 0));
		lblPowtrzHaso.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblPowtrzHaso.setBounds(21, 146, 116, 17);
		panel.add(lblPowtrzHaso);
		
		lblNewLabel = new JLabel("Adres serwera:");
		lblNewLabel.setForeground(new Color(255, 204, 0));
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblNewLabel.setBounds(21, 196, 116, 14);
		panel.add(lblNewLabel);
		
		lblPort = new JLabel("Port:");
		lblPort.setForeground(new Color(255, 204, 0));
		lblPort.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblPort.setBounds(21, 227, 46, 14);
		panel.add(lblPort);
		
		lblNazwaBazyDanych = new JLabel("Nazwa bazy:");
		lblNazwaBazyDanych.setForeground(new Color(255, 204, 0));
		lblNazwaBazyDanych.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblNazwaBazyDanych.setBounds(21, 255, 110, 14);
		panel.add(lblNazwaBazyDanych);
		
		lblLoginBazy = new JLabel("Login bazy:");
		lblLoginBazy.setForeground(new Color(255, 204, 0));
		lblLoginBazy.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblLoginBazy.setBounds(21, 289, 84, 14);
		panel.add(lblLoginBazy);
		
		passwordField_databasePassword = new JPasswordField();
		passwordField_databasePassword.setBackground(SystemColor.activeCaption);
		passwordField_databasePassword.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		passwordField_databasePassword.setBounds(133, 317, 128, 20);
		panel.add(passwordField_databasePassword);
		
		lblHasoBazy = new JLabel("Has\u0142o bazy:");
		lblHasoBazy.setForeground(new Color(255, 204, 0));
		lblHasoBazy.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblHasoBazy.setBounds(21, 320, 84, 14);
		panel.add(lblHasoBazy);
		
		btn_testConnection = new JButton("Test po\u0142\u0105czenia");
		btn_testConnection.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				String error = presenter_route_planning.validateServerData();
				if( error == null )
				{
					if( presenter_route_planning.testConnectionToExternalDatabase() )
					{
						JOptionPane.showMessageDialog(RegistryJPanel.this, "Test połączenia z zewnętrznym serwerem zakończony pomyślnie.",	
								"Sukces", JOptionPane.INFORMATION_MESSAGE);	
						connectionSuccesedTested = true;
					}
					else
					{
						JOptionPane.showMessageDialog(RegistryJPanel.this, "Nie udało się połączyć z zewnętrzną bazą.\n"
								+ "Sprawdź dane dotyczące logowania do bazy na zewnętrzym serwerze. ",	
								"Błąd połączenia z bazą", JOptionPane.ERROR_MESSAGE);
						connectionSuccesedTested = false;
					}
				}
				else
					JOptionPane.showMessageDialog(RegistryJPanel.this, "Błąd: " + error,	
							"Błąd wprowadzanych danych", JOptionPane.ERROR_MESSAGE);
			}
		});
		btn_testConnection.setBackground(SystemColor.activeCaption);
		btn_testConnection.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		btn_testConnection.setBounds(123, 355, 138, 49);
		panel.add(btn_testConnection);
		
		btn_registry = new JButton("Zarejestruj");
		btn_registry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				
				if( connectionSuccesedTested )
				{
					String error = presenter_route_planning.validateRegistryData() ;
					if( error == null )
					{
						presenter_route_planning.saveNewAccountToLocalDatabase();
						JOptionPane.showMessageDialog(RegistryJPanel.this, "Twoje konto zostało zarejestrowane.",	
								"Potwierdzenie", JOptionPane.INFORMATION_MESSAGE);
						clearTexts();
						presenter_route_planning.changeRegistryUser_to_Menu();
					}
					else
						JOptionPane.showMessageDialog(RegistryJPanel.this, "Błąd: " + error,	
								"Brak wprowadzanych danych", JOptionPane.ERROR_MESSAGE);
				}
				else
					JOptionPane.showMessageDialog(RegistryJPanel.this, "Połącznie z bazą danych nie zostało przetestowane lub nie jest poprawne.\n"
							+ "Przed zakończenie rejestracji należy dokonać testu połączenia z bazą, który się powiedzie.", 
							"Brak testu poprawności połączenia", JOptionPane.ERROR_MESSAGE);

				
				connectionSuccesedTested = false;
			}
		});
		btn_registry.setBackground(SystemColor.activeCaption);
		btn_registry.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		btn_registry.setBounds(264, 355, 116, 49);
		panel.add(btn_registry);
		
		JButton btn_back = new JButton("Wróć");
		btn_back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				presenter_route_planning.changeRegistryUser_to_Menu();
			}
		});
		btn_back.setBackground(SystemColor.activeCaption);
		btn_back.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		btn_back.setBounds(10, 355, 110, 49);
		panel.add(btn_back);
	}
	
	private void clearTexts()
	{
		textField_databaseLogin.setText("");
		textField_databaseName.setText("");
		textField_login.setText("");
		textField_serverAddress.setText("");
		textField_serverPort.setText("");
		
		passwordField_databasePassword.setText("");
		passwordField_password.setText("");
		passwordField_passwordRepeat.setText("");
	}
	
	public void setPresenter(final RoutePlanningPresenter presenter)
	{
		presenter_route_planning = presenter;
	}

	public String getLogin()
	{
		return textField_login.getText();
	}
	
	/**
	 * Metoda zwracajaca podane przez u�ytkownika has�a.
	 * @return [] String
	 * <br> [0] -> password
	 * <br> [1] -> repeat password
	 * @author Kamil Zimny
	 */
	public String [] getPasswords()
	{
		String [] passwords = new String[2];
		passwords[0] = new String(passwordField_password.getPassword());
		passwords[1] = new String(passwordField_passwordRepeat.getPassword());
		return passwords;
	}
	
	public void clearText()
	{
		textField_databaseLogin.setText("");
		textField_databaseName.setText("");
		textField_login.setText("");
		textField_serverAddress.setText("");
		textField_serverPort.setText("");
		passwordField_databasePassword.setText("");
		passwordField_password.setText("");
		passwordField_passwordRepeat.setText("");
	}
	
	public String getServerAddress()
	{
		return textField_serverAddress.getText();
	}
	
	public String getServerPort()
	{
		return textField_serverPort.getText();
	}
	
	public String getDatabaseName()
	{
		return textField_databaseName.getText();
	}
	
	public String getDatabaseLogin()
	{
		return textField_databaseLogin.getText();
	}
	
	public String getDatabasePassword()
	{
		return new String(passwordField_databasePassword.getPassword());
	}
}
