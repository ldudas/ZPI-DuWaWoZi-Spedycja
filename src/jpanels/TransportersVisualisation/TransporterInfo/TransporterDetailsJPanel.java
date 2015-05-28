package jpanels.TransportersVisualisation.TransporterInfo;


import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import dataModels.SizeCategory;
import dataModels.Transporter;

public class TransporterDetailsJPanel extends JPanel
{
	private JPanel panel_data;
	private Transporter currentTransporter;
	
	private JTextArea textArea_manufacturerAdditionalInfo;
	private JScrollPane scrollManufacturerAddition;
	private JList<String> trans_list;
	
	public TransporterDetailsJPanel() 
	{
		setBackground(SystemColor.inactiveCaption);
		setLayout(null);
		
		panel_data = new JPanel();
		panel_data.setBackground(SystemColor.inactiveCaptionText);
		panel_data.setBounds(10, 11, 627, 298);
		add(panel_data);
		panel_data.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Dane przewoźnika:");
		lblNewLabel.setForeground(new Color(255, 204, 0));
		lblNewLabel.setBackground(SystemColor.inactiveCaptionText);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblNewLabel.setBounds(10, 11, 129, 23);
		panel_data.add(lblNewLabel);
		scrollManufacturerAddition = new JScrollPane();
		scrollManufacturerAddition.setBackground(SystemColor.activeCaption);
		scrollManufacturerAddition.setBounds(10, 45, 488, 66);
		scrollManufacturerAddition.setVisible(false);
		panel_data.add(scrollManufacturerAddition);
		
		textArea_manufacturerAdditionalInfo = new JTextArea();
		scrollManufacturerAddition.setViewportView(textArea_manufacturerAdditionalInfo);
		
		textArea_manufacturerAdditionalInfo.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 20));
		textArea_manufacturerAdditionalInfo.setBackground(SystemColor.activeCaption);
		textArea_manufacturerAdditionalInfo.setForeground(new Color(139, 0, 0));
		textArea_manufacturerAdditionalInfo.setEditable(false);
		textArea_manufacturerAdditionalInfo.setLineWrap(true);
		textArea_manufacturerAdditionalInfo.setWrapStyleWord(true);
		
		
		
		JPanel panel_buttons = new JPanel();
		panel_buttons.setBackground(SystemColor.inactiveCaptionText);
		panel_buttons.setBounds(10, 319, 627, 85);
		add(panel_buttons);
		panel_buttons.setLayout(null);
		
		JButton btn_cancel = new JButton("Anuluj\r\n");
		btn_cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				// zamknij okno
			}
		});
		btn_cancel.setBackground(SystemColor.activeCaption);
		btn_cancel.setForeground(SystemColor.desktop);
		btn_cancel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		btn_cancel.setBounds(10, 11, 135, 62);
		panel_buttons.add(btn_cancel);
		
	}
	

	public void setInfoAboutTransporterInToList(Transporter tansporter)
	{ 
			currentTransporter = tansporter;

			if(trans_list != null)
			{
				panel_data.remove(trans_list);
			}

			String [] details = new String [9];
			details[0] = "Nazwa: " + currentTransporter.getName();
			details[1] = "Telefon: " + currentTransporter.getPhone_num();
			details[2] = "Liczba zleceń: " + currentTransporter.getNumber_of_orders();
			details[3] = "Średni koszt: " + currentTransporter.getCost() + " zł";
			details[4] = "Kategoria rozmiaru pojazdów: " + (currentTransporter.getSizeCategory() == SizeCategory.SMALL?"Małe":
															currentTransporter.getSizeCategory() == SizeCategory.MEDIUM?"Średnie":
															"Duże");
			details[5] = "Średnia pojemność pojazdów w kategorii: " + currentTransporter.getVolume();
			details[6] = "Średnia ładowność pojazdów w kategorii: " + currentTransporter.getCapacity();
			details[7] = "Stosunek dni opóźnienia do łącznej liczby dni zleceń: " + currentTransporter.getDelay();
			details[8] = "Stosunek zleceń zrealizowanych do łącznej liczby zleceń: " + currentTransporter.getExecuted();
			/*
			if( !transporter.getAdditionInfo().equals("Brak") )
			{		
				scrollManufacturerAddition.setVisible(true);
				textArea_manufacturerAdditionalInfo.setText("Powód odrzucenia: \n" + transporter.getAdditionInfo());
			}
			else
				scrollManufacturerAddition.setVisible(false);*/


			trans_list = new JList<String>(details);
			trans_list.setBounds(10, 40, 220, 130);
			trans_list.setBackground(SystemColor.activeCaption);
			panel_data.add(trans_list);	
			
	}
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}