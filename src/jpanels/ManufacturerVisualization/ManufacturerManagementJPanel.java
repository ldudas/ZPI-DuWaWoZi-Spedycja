package jpanels.ManufacturerVisualization;

import interfaces.RoutePlanningPresenter;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class ManufacturerManagementJPanel extends JPanel 
{

	private static final long serialVersionUID = 1L;
	private RoutePlanningPresenter presenter_route_planning;
	private JTextField map_count_textField;

	private JLabel lblTolerancja;
	private JComboBox<String> tolerance_unit_comboBox;
	private JComboBox<Integer> tolerance_count_comboBox_1;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	
	public ManufacturerManagementJPanel()
	{
		setLayout(null);
		
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
		
		JLabel lblOkres = new JLabel("Typ");
		lblOkres.setBounds(59, 114, 46, 14);
		add(lblOkres);
		
		final JComboBox<String> map_period_comboBox = new JComboBox<String>();

		map_period_comboBox.setBounds(132, 111, 132, 20);
		map_period_comboBox.addItem("");
		map_period_comboBox.addItem("Okres wstecz");
		map_period_comboBox.addItem("Przedział czasowy");
		add(map_period_comboBox);
		
		JLabel lblJednostka = new JLabel("Jednostka");
		lblJednostka.setBounds(59, 146, 62, 14);
		add(lblJednostka);
		
		JComboBox<String> map_unit_comboBox = new JComboBox<String>();
		map_unit_comboBox.setBounds(132, 143, 132, 20);
		addUnitToComboBox(map_unit_comboBox);
		add(map_unit_comboBox);
		
		JLabel lblNewLabel = new JLabel("Liczba");
		lblNewLabel.setBounds(59, 180, 46, 14);
		add(lblNewLabel);
		
		JComboBox<Integer> map_count_comboBox = new JComboBox<Integer>();
		map_count_comboBox.setBounds(132, 174, 132, 20);	
		addCountToComboBox(map_count_comboBox);
		add(map_count_comboBox);
		
		JButton btn_chooseManufacturer = new JButton("Wybierz");
		btn_chooseManufacturer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				presenter_route_planning.showManufacturerInfo();
			}
		});
		btn_chooseManufacturer.setBounds(184, 390, 132, 49);
		add(btn_chooseManufacturer);
		
		tolerance_unit_comboBox = new JComboBox<String>();
		tolerance_unit_comboBox.setBounds(132, 245, 132, 20);
		addUnitToComboBox(tolerance_unit_comboBox);
		add(tolerance_unit_comboBox);
		
		tolerance_count_comboBox_1 = new JComboBox<Integer>();
		tolerance_count_comboBox_1.setBounds(132, 276, 132, 20);
		addCountToComboBox(tolerance_count_comboBox_1);
		add(tolerance_count_comboBox_1);
		
		lblNewLabel_1 = new JLabel("Jednostka");
		lblNewLabel_1.setBounds(59, 248, 62, 14);
		add(lblNewLabel_1);
		
		lblNewLabel_2 = new JLabel("Liczba");
		lblNewLabel_2.setBounds(59, 279, 46, 14);
		add(lblNewLabel_2);
		
		lblTolerancja = new JLabel("Tolerancja");
		lblTolerancja.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		lblTolerancja.setBounds(59, 223, 148, 14);
		add(lblTolerancja);
		
		JLabel lblNewLabel_3 = new JLabel("Czas");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel_3.setBounds(59, 84, 46, 14);
		add(lblNewLabel_3);
		
		setVisibilityOfTolerance(false);
		map_period_comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				if( map_period_comboBox.getSelectedIndex() == 1 )
					setVisibilityOfTolerance(true);
				else
					setVisibilityOfTolerance(false);
			}
		});
	}
	
	public void setPresenter(final RoutePlanningPresenter presenter)
	{
		presenter_route_planning = presenter;
	}
	
	private void addUnitToComboBox(JComboBox<String> combo )
	{
		combo.addItem("");
		combo.addItem("Dni");
		combo.addItem("Tygodnie");
		combo.addItem("Miesiące");
		combo.addItem("Lata");
	}
	
	private void addCountToComboBox(JComboBox<Integer> combo)
	{
		for(int i=0;i<=31 ; i++)
			combo.addItem(i);
	}
	
	private void setVisibilityOfTolerance(boolean visibility)
	{
		lblTolerancja.setVisible(visibility);
		tolerance_unit_comboBox.setVisible(visibility);
		tolerance_count_comboBox_1.setVisible(visibility);
		lblNewLabel_1.setVisible(visibility);
		lblNewLabel_2.setVisible(visibility);
	}
}
