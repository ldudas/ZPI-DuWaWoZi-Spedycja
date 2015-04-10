package views;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JList;

public class ViewManufacturersVisualisation_Manufacturer extends JPanel {
	private JTextField man_to_textField;
	private JTextField man_tripDate_textField;
	private JTextField man_arrivalDate_textField;

	/**
	 * Create the panel.
	 */
	public ViewManufacturersVisualisation_Manufacturer() {
		setLayout(null);
		
		man_to_textField = new JTextField();
		man_to_textField.setBounds(68, 54, 86, 20);
		add(man_to_textField);
		man_to_textField.setColumns(10);
		
		man_tripDate_textField = new JTextField();
		man_tripDate_textField.setBounds(24, 133, 86, 20);
		add(man_tripDate_textField);
		man_tripDate_textField.setColumns(10);
		
		man_arrivalDate_textField = new JTextField();
		man_arrivalDate_textField.setBounds(174, 133, 86, 20);
		add(man_arrivalDate_textField);
		man_arrivalDate_textField.setColumns(10);
		
		JLabel lblMiastoDo = new JLabel("Miasto do:");
		lblMiastoDo.setBounds(68, 43, 65, 14);
		add(lblMiastoDo);
		
		JLabel lblDataWyjazdu = new JLabel("Data wyjazdu:");
		lblDataWyjazdu.setBounds(24, 116, 86, 14);
		add(lblDataWyjazdu);
		
		JLabel lblDataPrzyjazdu = new JLabel("Data przyjazdu: ");
		lblDataPrzyjazdu.setBounds(174, 116, 86, 14);
		add(lblDataPrzyjazdu);
		
		JButton man_confirmButton = new JButton("Zatwierd\u017A");
		man_confirmButton.setBounds(24, 266, 89, 23);
		add(man_confirmButton);
		
		JButton man_notConfirmButton = new JButton("Nie odpowiada");
		man_notConfirmButton.setBounds(132, 266, 110, 23);
		add(man_notConfirmButton);
		
		JButton man_cancelButton = new JButton("Anuluj");
		man_cancelButton.setBounds(351, 266, 89, 23);
		add(man_cancelButton);
		
		JLabel lblDanePrzewonika = new JLabel("Dane przewo\u017Anika");
		lblDanePrzewonika.setBounds(281, 43, 110, 14);
		add(lblDanePrzewonika);
		
		JList man_list = new JList();
		man_list.setBounds(281, 78, 159, 129);
		add(man_list);

	}

}
