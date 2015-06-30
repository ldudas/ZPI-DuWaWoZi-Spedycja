package interfaces.jPanels.startWindow;

import interfaces.mvp.RoutePlanningPresenter;

import javax.swing.JPanel;

import java.awt.SystemColor;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.KeyStroke;

import java.awt.Font;
import java.awt.Color;

import javax.swing.JPasswordField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Panel logowania do lokalnego konta użytkownika.
 * @author Kamil Zimny.
 *
 */
public class LogJPanel extends JPanel 
{
	private static final long serialVersionUID = 1L;
	private JTextField textField_login;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JPasswordField passwordField_password;
	private RoutePlanningPresenter presenter_route_planning;
	
	public LogJPanel() 
	{
		setBackground(SystemColor.inactiveCaptionText);
		setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.inactiveCaptionText);
		panel.setBounds(138, 92, 335, 253);
		add(panel);
		panel.setLayout(null);
		
		textField_login = new JTextField();
		textField_login.setBackground(SystemColor.activeCaption);
		textField_login.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 14));
		textField_login.setBounds(33, 45, 246, 29);
		panel.add(textField_login);
		textField_login.setColumns(10);
		
		JButton btn_LogIn = new JButton("Zaloguj ");
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
		btn_LogIn.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		btn_LogIn.setBounds(161, 187, 118, 51);
		panel.add(btn_LogIn);
		
		lblNewLabel = new JLabel("Login:");
		lblNewLabel.setForeground(new Color(255, 204, 0));
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 16));
		lblNewLabel.setBounds(33, 11, 83, 23);
		panel.add(lblNewLabel);
		
		lblNewLabel_1 = new JLabel("Has\u0142o:");
		lblNewLabel_1.setForeground(new Color(255, 204, 0));
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 16));
		lblNewLabel_1.setBounds(33, 87, 83, 23);
		panel.add(lblNewLabel_1);
		
		passwordField_password = new JPasswordField();
		passwordField_password.setBackground(SystemColor.activeCaption);
		passwordField_password.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
		passwordField_password.setBounds(33, 121, 246, 29);
		panel.add(passwordField_password);
		
		JButton btn_back = new JButton("Wróć ");
		btn_back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				presenter_route_planning.setNotLoggedUser();
				clearTexts();
				presenter_route_planning.setEnableButtonsToUserAction(false);
				presenter_route_planning.changeLoginUser_to_Menu();
			}
		});
		btn_back.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		btn_back.setBackground(SystemColor.activeCaption);
		btn_back.setBounds(33, 187, 118, 51);
		panel.add(btn_back);
		
		
		Action clickEneter = new AbstractAction() 
		{
			private static final long serialVersionUID = 1L;
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
        };
        
        passwordField_password.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "ClickEnter");		
        passwordField_password.getActionMap().put("ClickEnter", clickEneter);
        
        textField_login.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "ClickEnter");		
        textField_login.getActionMap().put("ClickEnter", clickEneter);
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
