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
import javax.swing.SwingConstants;

public class TransporterDetailsJPanel extends JPanel
{
	private JPanel panel_data;
	private Transporter currentTransporter;
	private JLabel lblNazwaValue;
	private JLabel lblTelefonValue;
	private JLabel lblZleceniaValue;
	private JLabel lblKosztValue;
	private JLabel lblPojemnoscValue;
	private JLabel lblLadownoscValue;
	private JLabel lblDniOpoznieniaValue;
	private JLabel lblNiezrealValue;
	private JLabel lblKategoriaValue;
	
	public TransporterDetailsJPanel() 
	{
		setBackground(SystemColor.inactiveCaption);
		setLayout(null);
		
		panel_data = new JPanel();
		panel_data.setBackground(SystemColor.inactiveCaptionText);
		panel_data.setBounds(10, 11, 436, 298);
		add(panel_data);
		panel_data.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Dane przewoźnika:");
		lblNewLabel.setForeground(new Color(255, 204, 0));
		lblNewLabel.setBackground(SystemColor.inactiveCaptionText);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		lblNewLabel.setBounds(10, 11, 209, 23);
		panel_data.add(lblNewLabel);
		
		JLabel lblNazwa = new JLabel("Nazwa:");
		lblNazwa.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNazwa.setForeground(Color.YELLOW);
		lblNazwa.setBounds(20, 40, 146, 23);
		panel_data.add(lblNazwa);
		
		JLabel lblKategoriaRozmiaruPojazdw = new JLabel("Kategoria rozmiaru pojazdów:");
		lblKategoriaRozmiaruPojazdw.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblKategoriaRozmiaruPojazdw.setForeground(Color.YELLOW);
		lblKategoriaRozmiaruPojazdw.setBounds(20, 80, 199, 23);
		panel_data.add(lblKategoriaRozmiaruPojazdw);
		
		JLabel lblTelefon = new JLabel("Telefon:");
		lblTelefon.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblTelefon.setForeground(Color.YELLOW);
		lblTelefon.setBounds(20, 120, 157, 23);
		panel_data.add(lblTelefon);
		
		JLabel lblLiczbaZlece = new JLabel("Liczba zleceń:");
		lblLiczbaZlece.setForeground(Color.YELLOW);
		lblLiczbaZlece.setBounds(20, 160, 89, 14);
		panel_data.add(lblLiczbaZlece);
		
		JLabel lblredniKoszt = new JLabel("Średni koszt (zł):");
		lblredniKoszt.setForeground(Color.YELLOW);
		lblredniKoszt.setBounds(20, 180, 119, 14);
		panel_data.add(lblredniKoszt);
		
		
		
		JLabel lblSredniaPojemnoPojazdow = new JLabel("Średnia pojemność pojazdów w kategorii:");
		lblSredniaPojemnoPojazdow.setForeground(Color.YELLOW);
		lblSredniaPojemnoPojazdow.setBounds(20, 200, 277, 14);
		panel_data.add(lblSredniaPojemnoPojazdow);
		
		JLabel lblLadownosc = new JLabel("Średnia ładowność pojazdów w kategorii:");
		lblLadownosc.setForeground(Color.YELLOW);
		lblLadownosc.setBounds(20, 220, 277, 14);
		panel_data.add(lblLadownosc);
		
		JLabel lblStosunekDniOpnienia = new JLabel("Stosunek dni opóźnienia do łącznej liczby dni zleceń:");
		lblStosunekDniOpnienia.setForeground(Color.YELLOW);
		lblStosunekDniOpnienia.setBounds(20, 240, 329, 14);
		panel_data.add(lblStosunekDniOpnienia);
		
		JLabel lblStosunekZleceZrealizowanych = new JLabel("Stosunek zleceń zrealizowanych do łącznej liczby zleceń:");
		lblStosunekZleceZrealizowanych.setForeground(Color.YELLOW);
		lblStosunekZleceZrealizowanych.setBounds(20, 260, 329, 14);
		panel_data.add(lblStosunekZleceZrealizowanych);
		
		lblNazwaValue = new JLabel("New label");
		lblNazwaValue.setForeground(Color.ORANGE);
		lblNazwaValue.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNazwaValue.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNazwaValue.setBounds(325, 40, 80, 29);
		panel_data.add(lblNazwaValue);
		
		lblKategoriaValue = new JLabel("New label");
		lblKategoriaValue.setForeground(Color.ORANGE);
		lblKategoriaValue.setHorizontalAlignment(SwingConstants.RIGHT);
		lblKategoriaValue.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblKategoriaValue.setBounds(302, 80, 103, 29);
		panel_data.add(lblKategoriaValue);
		
		
		lblTelefonValue = new JLabel("New label");
		lblTelefonValue.setForeground(Color.ORANGE);
		lblTelefonValue.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTelefonValue.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblTelefonValue.setBounds(291, 120, 114, 29);
		panel_data.add(lblTelefonValue);
		
		lblZleceniaValue = new JLabel("New label");
		lblZleceniaValue.setForeground(Color.ORANGE);
		lblZleceniaValue.setHorizontalAlignment(SwingConstants.RIGHT);
		lblZleceniaValue.setBounds(325, 160, 80, 14);
		panel_data.add(lblZleceniaValue);
		
		lblKosztValue = new JLabel("New label");
		lblKosztValue.setForeground(Color.ORANGE);
		lblKosztValue.setHorizontalAlignment(SwingConstants.RIGHT);
		lblKosztValue.setBounds(335, 180, 70, 14);
		panel_data.add(lblKosztValue);
		
		
		lblPojemnoscValue = new JLabel("New label");
		lblPojemnoscValue.setForeground(Color.ORANGE);
		lblPojemnoscValue.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPojemnoscValue.setBounds(335, 200, 70, 14);
		panel_data.add(lblPojemnoscValue);
		
		lblLadownoscValue = new JLabel("New label");
		lblLadownoscValue.setForeground(Color.ORANGE);
		lblLadownoscValue.setHorizontalAlignment(SwingConstants.RIGHT);
		lblLadownoscValue.setBounds(335, 220, 70, 14);
		panel_data.add(lblLadownoscValue);
		
		lblDniOpoznieniaValue = new JLabel("New label");
		lblDniOpoznieniaValue.setForeground(Color.ORANGE);
		lblDniOpoznieniaValue.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDniOpoznieniaValue.setBounds(335, 240, 70, 14);
		panel_data.add(lblDniOpoznieniaValue);
		
		lblNiezrealValue = new JLabel("New label");
		lblNiezrealValue.setForeground(Color.ORANGE);
		lblNiezrealValue.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNiezrealValue.setBounds(302, 260, 103, 14);
		panel_data.add(lblNiezrealValue);
		
	}
	

	public void setInfoAboutTransporterInToList(Transporter tansporter)
	{ 
			currentTransporter = tansporter;

			lblNazwaValue.setText(currentTransporter.getName());
			lblTelefonValue.setText(currentTransporter.getPhone_num()+"");
			lblZleceniaValue.setText(currentTransporter.getNumber_of_orders()+"");
			lblKosztValue.setText(currentTransporter.getCost()+"");
			lblKategoriaValue.setText(currentTransporter.getSizeCategory() == SizeCategory.SMALL?"Małe":
															currentTransporter.getSizeCategory() == SizeCategory.MEDIUM?"Średnie":
															"Duże");
		 lblPojemnoscValue.setText(currentTransporter.getVolume()+"");
		 lblLadownoscValue.setText(currentTransporter.getCapacity()+"");
		 lblDniOpoznieniaValue.setText(currentTransporter.getDelay()<0.001?"<0.001":currentTransporter.getDelay()+"");
		 lblNiezrealValue.setText(currentTransporter.getExecuted()<0.001?"<0.001":currentTransporter.getExecuted()+"");
	}
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}