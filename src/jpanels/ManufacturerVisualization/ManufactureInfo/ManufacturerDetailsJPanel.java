package jpanels.ManufacturerVisualization.ManufactureInfo;

import interfaces.RoutePlanningPresenter;

import javax.swing.JPanel;

import java.awt.SystemColor;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JList;

import dataModels.Manufacturer;

import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ManufacturerDetailsJPanel extends JPanel
{
	private JList<String> man_list;
	private RoutePlanningPresenter presenter_RoutePlanning;
	private JPanel panel_data;
	
	public ManufacturerDetailsJPanel() 
	{
		setBackground(SystemColor.inactiveCaption);
		setLayout(null);
		
		panel_data = new JPanel();
		panel_data.setBackground(SystemColor.inactiveCaptionText);
		panel_data.setBounds(10, 11, 627, 302);
		add(panel_data);
		panel_data.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Dane producenta:");
		lblNewLabel.setForeground(new Color(255, 204, 0));
		lblNewLabel.setBackground(SystemColor.inactiveCaptionText);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblNewLabel.setBounds(10, 11, 129, 23);
		panel_data.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Aktywność producenta w misiącach:\r\n");
		lblNewLabel_1.setForeground(new Color(255, 204, 0));
		lblNewLabel_1.setBackground(SystemColor.inactiveCaptionText);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblNewLabel_1.setBounds(257, 11, 264, 23);
		panel_data.add(lblNewLabel_1);
		
		JPanel panel_buttons = new JPanel();
		panel_buttons.setBackground(SystemColor.inactiveCaptionText);
		panel_buttons.setBounds(10, 319, 627, 73);
		add(panel_buttons);
		panel_buttons.setLayout(null);
		
		JButton btn_cancel = new JButton("Anuluj\r\n");
		btn_cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				presenter_RoutePlanning.closeManufacturerInfo();
			}
		});
		btn_cancel.setBackground(SystemColor.activeCaption);
		btn_cancel.setForeground(SystemColor.desktop);
		btn_cancel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		btn_cancel.setBounds(10, 11, 145, 51);
		panel_buttons.add(btn_cancel);
		
		JButton btn_unpropertyChoose = new JButton("Nie Odpowiada\r\n");
		btn_unpropertyChoose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				presenter_RoutePlanning.markAsUnsuitable();
				presenter_RoutePlanning.closeManufacturerInfo();
			}
		});
		btn_unpropertyChoose.setBackground(SystemColor.activeCaption);
		btn_unpropertyChoose.setForeground(SystemColor.desktop);
		btn_unpropertyChoose.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		btn_unpropertyChoose.setBounds(165, 11, 145, 51);
		panel_buttons.add(btn_unpropertyChoose);
		
		JButton btn_propertyChoose = new JButton("Odpowiada\r\n");
		btn_propertyChoose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				
			}
		});
		btn_propertyChoose.setBackground(SystemColor.activeCaption);
		btn_propertyChoose.setForeground(SystemColor.desktop);
		btn_propertyChoose.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		btn_propertyChoose.setBounds(472, 11, 145, 51);
		panel_buttons.add(btn_propertyChoose);
		
	}
	
	public static void main(String [] args)
	{
		JFrame frame = new JFrame();
		ManufacturerDetailsJPanel panel = new ManufacturerDetailsJPanel();
		
		Manufacturer man = new Manufacturer("AAAA", 123.2, 123.2, 
				"1993-09-28", 20, 33, 12, "987-123-123", "d");
		panel.setInfoAboutManufacturerInToList( man );
		
		frame.setBounds(10, 10, 800, 500);
		frame.getContentPane().add(panel);
		frame.setVisible(true);
		
	}
	
	public void setPresenter(final RoutePlanningPresenter presenter)
	{
		presenter_RoutePlanning = presenter;
		
	}
	
	/**
	 * Metoda dodajaca do listy informacje o produceńcie
	 * @param attr
	 * @author Kamil Zimny
	 */
	public void setInfoAboutManufacturerInToList( Manufacturer manufacturer)
	{ 
			if(man_list != null)
				remove(man_list);
			String [] details = new String [8];
			details[0] = "Nazwa: " + manufacturer.getName();
			details[1] = "Telefon: " + manufacturer.getPhone();
			details[2] = "Ostatnia aktywność: " + manufacturer.getLastActivity();
			details[3] = "Liczba zleceń: " + manufacturer.getNumberOfOrders();
			details[4] = "Suma wartości zleceń: " + manufacturer.getSumOfOrdersValue();
			details[5] = "Suma dni wykonywanych zleceń: " + manufacturer.getSumOfDays();
			details[6] = "Dodatkowe informacje: " ;
			details[7] = " dosajdisajdiajsidjisajdisajd";
			man_list = new JList<String>(details);
			man_list.setBounds(10, 40, 220, 170);
			man_list.setBackground(SystemColor.activeCaption);
			panel_data.add(man_list);	
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
