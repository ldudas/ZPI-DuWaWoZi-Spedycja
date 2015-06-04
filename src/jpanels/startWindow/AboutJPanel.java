package jpanels.startWindow;

import interfaces.RoutePlanningPresenter;

import javax.swing.JPanel;

import java.awt.SystemColor;

import javax.swing.JTextPane;

import java.awt.Font;
import java.awt.Color;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;

public class AboutJPanel extends JPanel 
{
	private RoutePlanningPresenter presenter_route_planning;
	
	public AboutJPanel() {
		setBackground(SystemColor.inactiveCaption);
		setLayout(null);
		
		JPanel panel_about = new JPanel();
		panel_about.setBackground(SystemColor.inactiveCaptionText);
		panel_about.setBounds(10, 11, 537, 391);
		add(panel_about);
		panel_about.setLayout(null);
		
		JTextPane about = new JTextPane();
		about.setEditable(false);
		about.setText("Aplikacja ... powstała na rzecz realizacji kursu zespołowe przedsięwzięcie inżynierskie, organizowanego na Politechnice Wrocławskiej na wydziale Informatyki i Zarządzania, kierunku Informatyka w semestrze 2014/2015 Letnim. \r\n\r\nTytuł projektu: System informatyczny z interaktywnymi technikami wizualizacyjnymi, wspomagający decyzje operatora logistycznego będącego koordynatorem w łańcuchu dostaw. \r\n\r\nProwadzący zajęcia: Dr inż. Martin Tabakow.\r\n\r\nAutorzy projektu:\r\nDudaszek Łukasz\r\nWasielewski Tomasz\r\nWołoszyk Piotr\r\nZimny Kamil");
		about.setForeground(new Color(255, 204, 0));
		about.setBackground(SystemColor.inactiveCaptionText);
		about.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		about.setBounds(10, 62, 516, 262);
		panel_about.add(about);
		
		JButton btn_ok = new JButton("Ok");
		btn_ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				presenter_route_planning.closeAbout();
			}
		});
		btn_ok.setBackground(SystemColor.activeCaption);
		btn_ok.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 16));
		btn_ok.setBounds(373, 335, 153, 48);
		panel_about.add(btn_ok);
		
		JLabel lblNewLabel = new JLabel("Informacje o aplikacji");
		lblNewLabel.setForeground(new Color(255, 204, 0));
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));
		lblNewLabel.setBounds(141, 11, 252, 23);
		panel_about.add(lblNewLabel);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void setPresenter(final RoutePlanningPresenter presenter)
	{
		presenter_route_planning = presenter;
	}

	
}
