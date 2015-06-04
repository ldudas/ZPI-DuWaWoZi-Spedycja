package jpanels.startWindow;

import interfaces.RoutePlanningPresenter;

import javax.swing.JPanel;

import java.awt.SystemColor;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.Color;

import javax.swing.JPasswordField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LogJPanel extends JPanel 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField textField_login;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JPasswordField passwordField_password;
	private RoutePlanningPresenter presenter_route_planning;
	
	public LogJPanel() 
	{
		setBackground(SystemColor.inactiveCaption);
		setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.inactiveCaptionText);
		panel.setBounds(10, 11, 249, 192);
		add(panel);
		panel.setLayout(null);
		
		textField_login = new JTextField();
		textField_login.setBackground(SystemColor.activeCaption);
		textField_login.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 13));
		textField_login.setBounds(10, 34, 229, 23);
		panel.add(textField_login);
		textField_login.setColumns(10);
		
		JButton btn_LogIn = new JButton("Zaloguj");
		btn_LogIn.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				String error = presenter_route_planning.validateLoginData();
				if( error == null )
				{
					presenter_route_planning.setNewLoggedUser();
					clearTexts();
					presenter_route_planning.setEnableButtonsToUserAction(true);
					presenter_route_planning.changeLoginUser_to_Menu();
				}
				else
					JOptionPane.showMessageDialog(LogJPanel.this, "Błąd: " + error,	
							"Błąd wprowadzanych danych", JOptionPane.ERROR_MESSAGE);

			}
		});
		btn_LogIn.setBackground(SystemColor.activeCaption);
		btn_LogIn.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		btn_LogIn.setBounds(142, 141, 97, 39);
		panel.add(btn_LogIn);
		
		lblNewLabel = new JLabel("Login:");
		lblNewLabel.setForeground(new Color(255, 204, 0));
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblNewLabel.setBounds(10, 9, 46, 14);
		panel.add(lblNewLabel);
		
		lblNewLabel_1 = new JLabel("Has\u0142o:");
		lblNewLabel_1.setForeground(new Color(255, 204, 0));
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblNewLabel_1.setBounds(10, 75, 46, 14);
		panel.add(lblNewLabel_1);
		
		passwordField_password = new JPasswordField();
		passwordField_password.setBackground(SystemColor.activeCaption);
		passwordField_password.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 11));
		passwordField_password.setBounds(10, 101, 229, 23);
		panel.add(passwordField_password);
		
		JButton btn_back = new JButton("Wróć");
		btn_back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				presenter_route_planning.setNotLoggedUser();
				clearTexts();
				presenter_route_planning.setEnableButtonsToUserAction(false);
				presenter_route_planning.changeLoginUser_to_Menu();
			}
		});
		btn_back.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		btn_back.setBackground(SystemColor.activeCaption);
		btn_back.setBounds(10, 142, 97, 39);
		panel.add(btn_back);
	}

	public void setPresenter(final RoutePlanningPresenter presenter)
	{
		presenter_route_planning = presenter;
	}

	public String getLogin()
	{
		return textField_login.getText();
	}
	
	public void clearTexts()
	{
		textField_login.setText("");
		passwordField_password.setText("");
	}
	
	public String getPassword()
	{
		return new String(passwordField_password.getPassword());
	}
	
}
