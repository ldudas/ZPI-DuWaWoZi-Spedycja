package jpanels;

import interfaces.RoutePlanningPresenter;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;

import com.esri.map.JMap;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class MapJPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTextField map_count_textField;

	/**
	 * Create the panel.
	 */
	
	private RoutePlanningPresenter presenter_ManufacturersVis;
	private JTabbedPane map_tabbedPane;

	public MapJPanel() 
	{
		setLayout(null);
		
		map_tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		map_tabbedPane.setBounds(361, 45, 700, 480);
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
		
		JButton btn_chooseManufacturer = new JButton("Wybierz");
		btn_chooseManufacturer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				presenter_ManufacturersVis.showManufacturerInfo();
			}
		});
		btn_chooseManufacturer.setBounds(132, 386, 132, 49);
		add(btn_chooseManufacturer);

	}
	
	public void setPresenter(final RoutePlanningPresenter presenter)
	{
		presenter_ManufacturersVis = presenter;
	}
	
	public JTabbedPane return_tab()
	{
		return map_tabbedPane;
	}
	
	public void setCurrentTabOfMap()
	{
		map_tabbedPane.setSelectedIndex(map_tabbedPane.getTabCount()-1);
	}
}
