package views;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;

import com.esri.map.JMap;

import presenters.PresenterManufacturersVisualisation;

public class ViewManufacturersVisualisation_Map extends JPanel {
	private JTextField map_count_textField;

	/**
	 * Create the panel.
	 */
	
	private PresenterManufacturersVisualisation presenter_ManufacturersVis;
	private JTabbedPane map_tabbedPane;

	public ViewManufacturersVisualisation_Map() {
		setLayout(null);
		
		map_tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		map_tabbedPane.setBounds(361, 45, 448, 443);
		add(map_tabbedPane);
		
		JLabel lblPoka = new JLabel("Poka\u017C");
		lblPoka.setBounds(59, 59, 39, 14);
		add(lblPoka);
		
		map_count_textField = new JTextField();
		map_count_textField.setBounds(132, 56, 19, 20);
		add(map_count_textField);
		map_count_textField.setColumns(10);
		
		JLabel lblNajbardziejAktywnych = new JLabel("najbardziej aktywnych");
		lblNajbardziejAktywnych.setBounds(156, 59, 132, 14);
		add(lblNajbardziejAktywnych);
		
		JLabel lblOkres = new JLabel("Okres");
		lblOkres.setBounds(59, 87, 46, 14);
		add(lblOkres);
		
		JComboBox map_period_comboBox = new JComboBox();
		map_period_comboBox.setBounds(132, 84, 132, 20);
		add(map_period_comboBox);
		
		JLabel lblJednostka = new JLabel("Jednostka");
		lblJednostka.setBounds(59, 119, 62, 14);
		add(lblJednostka);
		
		JComboBox map_unit_comboBox = new JComboBox();
		map_unit_comboBox.setBounds(132, 116, 132, 20);
		add(map_unit_comboBox);
		
		JLabel lblNewLabel = new JLabel("Liczba");
		lblNewLabel.setBounds(59, 153, 46, 14);
		add(lblNewLabel);
		
		JComboBox map_count_comboBox = new JComboBox();
		map_count_comboBox.setBounds(132, 147, 132, 20);
		add(map_count_comboBox);

	}
	
	public void setPresenter(final PresenterManufacturersVisualisation presenter)
	{
		presenter_ManufacturersVis = presenter;
	}
	
	public void startuj(JMap mapka){
		map_tabbedPane.addTab("Trasa",mapka);
	}

}
