package jpanels;

import interfaces.RoutePlanningPresenter;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JList;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Map;

public class ManufacturerJPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField man_to_textField;
	private JTextField man_tripDate_textField;
	private JTextField man_arrivalDate_textField;
	private JList<String> man_list;
	private RoutePlanningPresenter presenter_ManufacturersVis;

	/**
	 * Create the panel.
	 */
	public ManufacturerJPanel() 
	{
		setLayout(null);
		
		man_to_textField = new JTextField();
		man_to_textField.setBounds(68, 54, 109, 20);
		add(man_to_textField);
		man_to_textField.setColumns(10);
		
		man_tripDate_textField = new JTextField();
		man_tripDate_textField.setBounds(24, 133, 109, 20);
		add(man_tripDate_textField);
		man_tripDate_textField.setColumns(10);
		
		man_arrivalDate_textField = new JTextField();
		man_arrivalDate_textField.setBounds(174, 133, 111, 20);
		add(man_arrivalDate_textField);
		man_arrivalDate_textField.setColumns(10);
		
		JLabel lblMiastoDo = new JLabel("Miasto do:");
		lblMiastoDo.setBounds(68, 29, 65, 14);
		add(lblMiastoDo);
		
		JLabel lblDataWyjazdu = new JLabel("Data wyjazdu:");
		lblDataWyjazdu.setBounds(24, 116, 109, 14);
		add(lblDataWyjazdu);
		
		JLabel lblDataPrzyjazdu = new JLabel("Data przyjazdu: ");
		lblDataPrzyjazdu.setBounds(174, 116, 111, 14);
		add(lblDataPrzyjazdu);
		
		JButton man_confirmButton = new JButton("Zatwierd\u017A");
		man_confirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				//Tu wywolanie metody zmieniajacej mape na inny obszar oraz dodanie 
			}
		});
		man_confirmButton.setBounds(24, 266, 109, 32);
		add(man_confirmButton);
		
		JButton man_notConfirmButton = new JButton("Nie odpowiada");
		man_notConfirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				
			}
		});
		man_notConfirmButton.setBounds(146, 266, 139, 32);
		add(man_notConfirmButton);
		
		JButton man_cancelButton = new JButton("Anuluj");
		man_cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				presenter_ManufacturersVis.closeManufacturerInfo();
			}
		});
		man_cancelButton.setBounds(351, 266, 118, 32);
		add(man_cancelButton);
		
		JLabel lblDaneProducenta = new JLabel("Dane producenta");
		lblDaneProducenta.setBounds(296, 57, 110, 14);
		add(lblDaneProducenta);
		


	}
	
	public void setPresenter(final RoutePlanningPresenter presenter)
	{
		presenter_ManufacturersVis = presenter;
	}
	
	/**
	 * Metoda dodajaca do listy informacje o produce�cie
	 * @param attr
	 * @author Kamil Zimny
	 */
	public void setInfoAboutManufacturerInToList( Map<String, Object> attr)
	{ 
			System.out.println("");
			String [] details = new String [6];
			details[0] = "Nazwa : " + attr.get("Nazwa: ");
			details[1] = "Telefon : " + attr.get("Telefon: ");
			details[2] = "Ostatnia aktywnosc : " + attr.get("Ostatnia aktywno��: ");
			details[3] = "Liczba zlecen : " + attr.get("Liczba zlece�: ");
			details[4] = "Suma wartosci zlecen : " + attr.get("Suma warto�ci zlece�: ");
			details[5] = "Suma dni wykonywanych zlecen : " + attr.get("Suma dni wykonywanych zlece�: ");
			
			man_list = new JList<String>(details);
			man_list.setBounds(300, 78, 280, 140);
			add(man_list);	
	}

}
