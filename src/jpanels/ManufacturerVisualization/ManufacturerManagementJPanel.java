package jpanels.ManufacturerVisualization;

import interfaces.RoutePlanningPresenter;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Color;

public class ManufacturerManagementJPanel extends JPanel 
{

	private static final long serialVersionUID = 1L;
	private RoutePlanningPresenter presenter_route_planning;

	private JLabel lblTolerancja;
	private JComboBox<String> comboBox_tolerance_unit;
	private JComboBox<Integer> comboBox_tolerance_count;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	
	public ManufacturerManagementJPanel()
	{
		setBackground(SystemColor.inactiveCaption);
		setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.inactiveCaptionText);
		panel.setBounds(0, 0, 401, 536);
		add(panel);
		panel.setLayout(null);
		
		JLabel lblPoka = new JLabel("Poka\u017C");
		lblPoka.setBounds(10, 39, 46, 14);
		panel.add(lblPoka);
		lblPoka.setForeground(new Color(255, 204, 0));
		lblPoka.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		
		JLabel lblNewLabel_3 = new JLabel("Czas");
		lblNewLabel_3.setBounds(10, 69, 46, 14);
		panel.add(lblNewLabel_3);
		lblNewLabel_3.setForeground(new Color(255, 204, 0));
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		
		JLabel lblNajbardziejAktywnych = new JLabel("najbardziej aktywnych");
		lblNajbardziejAktywnych.setBounds(194, 40, 150, 14);
		panel.add(lblNajbardziejAktywnych);
		lblNajbardziejAktywnych.setForeground(new Color(255, 204, 0));
		lblNajbardziejAktywnych.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		
		JLabel lblOkres = new JLabel("Typ");
		lblOkres.setBounds(54, 94, 46, 14);
		panel.add(lblOkres);
		lblOkres.setForeground(new Color(255, 204, 0));
		lblOkres.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		
		JLabel lblJednostka = new JLabel("Jednostka");
		lblJednostka.setBounds(54, 119, 74, 14);
		panel.add(lblJednostka);
		lblJednostka.setForeground(new Color(255, 204, 0));
		lblJednostka.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		
		JLabel lblNewLabel = new JLabel("Liczba");
		lblNewLabel.setBounds(54, 144, 46, 14);
		panel.add(lblNewLabel);
		lblNewLabel.setForeground(new Color(255, 204, 0));
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		
		lblTolerancja = new JLabel("Tolerancja");
		lblTolerancja.setBounds(10, 196, 148, 14);
		panel.add(lblTolerancja);
		lblTolerancja.setForeground(new Color(255, 204, 0));
		lblTolerancja.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		
		lblNewLabel_1 = new JLabel("Jednostka");
		lblNewLabel_1.setBounds(54, 230, 74, 14);
		panel.add(lblNewLabel_1);
		lblNewLabel_1.setForeground(new Color(255, 204, 0));
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		
		lblNewLabel_2 = new JLabel("Liczba");
		lblNewLabel_2.setBounds(54, 255, 46, 14);
		panel.add(lblNewLabel_2);
		lblNewLabel_2.setForeground(new Color(255, 204, 0));
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		
		JButton btn_chooseManufacturer = new JButton("Wybierz");
		btn_chooseManufacturer.setBounds(165, 386, 179, 61);
		panel.add(btn_chooseManufacturer);
		btn_chooseManufacturer.setForeground(SystemColor.desktop);
		btn_chooseManufacturer.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		btn_chooseManufacturer.setBackground(SystemColor.activeCaption);
		
		final JComboBox<String> comboBox_map_period = new JComboBox<String>();
		comboBox_map_period.setBounds(165, 90, 153, 22);
		panel.add(comboBox_map_period);
		comboBox_map_period.setBackground(SystemColor.activeCaption);
		comboBox_map_period.setForeground(new Color(0, 0, 0));
		comboBox_map_period.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		
		final JComboBox<String> comboBox_map_unit = new JComboBox<String>();
		comboBox_map_unit.setBounds(165, 115, 153, 22);
		panel.add(comboBox_map_unit);
		comboBox_map_unit.setBackground(SystemColor.activeCaption);
		comboBox_map_unit.setForeground(new Color(0, 0, 0));
		comboBox_map_unit.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		addUnitToComboBox(comboBox_map_unit);
		
		final JComboBox<Integer> comboBox_map_count = new JComboBox<Integer>();
		comboBox_map_count.setBounds(165, 140, 153, 22);
		panel.add(comboBox_map_count);
		comboBox_map_count.setBackground(SystemColor.activeCaption);
		comboBox_map_count.setForeground(new Color(0, 0, 0));
		comboBox_map_count.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		addCountToComboBox(comboBox_map_count);
		
		comboBox_tolerance_unit = new JComboBox<String>();
		comboBox_tolerance_unit.setBounds(165, 226, 153, 22);
		panel.add(comboBox_tolerance_unit);
		comboBox_tolerance_unit.setBackground(SystemColor.activeCaption);
		comboBox_tolerance_unit.setForeground(new Color(0, 0, 0));
		comboBox_tolerance_unit.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		addUnitToComboBox(comboBox_tolerance_unit);
		
		comboBox_tolerance_count = new JComboBox<Integer>();
		comboBox_tolerance_count.setBounds(165, 251, 153, 22);
		panel.add(comboBox_tolerance_count);
		comboBox_tolerance_count.setBackground(SystemColor.activeCaption);
		comboBox_tolerance_count.setForeground(new Color(0, 0, 0));
		comboBox_tolerance_count.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		addCountToComboBox(comboBox_tolerance_count);
		
		JComboBox<String> comboBox_numberOfMostActive = new JComboBox<String>();
		comboBox_numberOfMostActive.setBackground(SystemColor.activeCaption);
		comboBox_numberOfMostActive.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		comboBox_numberOfMostActive.setBounds(70, 37, 114, 20);
		panel.add(comboBox_numberOfMostActive);
		comboBox_numberOfMostActive.addItem("wszystkich");
		comboBox_numberOfMostActive.addItem("2");
		comboBox_numberOfMostActive.addItem("3");
		comboBox_numberOfMostActive.addItem("5");
		comboBox_numberOfMostActive.addItem("10");
		comboBox_numberOfMostActive.addItem("15");
		comboBox_numberOfMostActive.addItem("20");
		comboBox_numberOfMostActive.addItem("25");
		comboBox_numberOfMostActive.addItem("30");
		
		JButton btnNewButton = new JButton("Filtruj");
		btnNewButton.setForeground(SystemColor.desktop);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				if( comboBox_map_period.getSelectedItem().toString().equals("Okres wstecz"))
				{
					if(!comboBox_map_unit.getSelectedItem().equals("") && !comboBox_map_count.getSelectedItem().equals(0) )
					{
						presenter_route_planning.filterManfacturersBySinceDate( countDaysFromComboBoxs(comboBox_map_unit,comboBox_map_count) );
					}
				}
				else
					if(comboBox_map_period.getSelectedItem().toString().equals("Przedział czasowy"))
					{	
						if(!comboBox_map_unit.getSelectedItem().equals("") && !comboBox_map_count.getSelectedItem().equals(0) 
						   && !comboBox_tolerance_unit.getSelectedItem().equals("") )
						{
							int daysAgo = countDaysFromComboBoxs(comboBox_map_unit,comboBox_map_count);
							int dayTolerance = countDaysFromComboBoxs(comboBox_tolerance_unit,comboBox_tolerance_count);
							
							presenter_route_planning.filterManufacturersBetweenDate(daysAgo, dayTolerance);
						}
					}
				if( comboBox_numberOfMostActive.getSelectedItem() != null && !comboBox_numberOfMostActive.getSelectedItem().equals("") )
				{
					String numberOfManufacturersToShow = comboBox_numberOfMostActive.getSelectedItem().toString();
					int i_numberOfManufacturersToShow = 0;
					
					if(numberOfManufacturersToShow.equals("wszystkich"))
						i_numberOfManufacturersToShow = Integer.MAX_VALUE;
					else
						i_numberOfManufacturersToShow = Integer.parseInt(numberOfManufacturersToShow);
								
					presenter_route_planning.filterCountOfMostActiveManufacturers(i_numberOfManufacturersToShow);	
				}
			}
		});
		btnNewButton.setBackground(SystemColor.activeCaption);
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		btnNewButton.setBounds(165, 317, 179, 61);
		panel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Wyczyść");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				comboBox_numberOfMostActive.setSelectedIndex(0);
				comboBox_map_period.setSelectedIndex(0);
				comboBox_map_unit.setSelectedIndex(0);
				comboBox_map_count.setSelectedIndex(0);
				comboBox_tolerance_unit.setSelectedIndex(0);
				comboBox_tolerance_count.setSelectedIndex(0);
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		btnNewButton_1.setForeground(SystemColor.desktop);
		btnNewButton_1.setBackground(SystemColor.activeCaption);
		btnNewButton_1.setBounds(10, 317, 141, 61);
		panel.add(btnNewButton_1);
		
		comboBox_map_period.addItem("Okres wstecz");
		comboBox_map_period.addItem("Przedział czasowy");
		comboBox_map_period.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				if( comboBox_map_period.getSelectedIndex() == 1 )
					setVisibilityOfTolerance(true);
				else
					setVisibilityOfTolerance(false);
			}
		});
		btn_chooseManufacturer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				presenter_route_planning.showManufacturerInfo();
			}
		});
		
		setVisibilityOfTolerance(false);
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
	
	private int countDaysFromComboBoxs(JComboBox<String> unit,JComboBox<Integer> number )
	{
		int numberOfDays = 0;
		
		switch(unit.getSelectedItem().toString())
		{
		   case "Dni":
			  numberOfDays = Integer.parseInt(number.getSelectedItem().toString());
			  break;
		   case "Tygodnie":
			   numberOfDays = 7 * Integer.parseInt(number.getSelectedItem().toString());
			   break;
		   case "Miesiące":
			   numberOfDays = 31 * Integer.parseInt(number.getSelectedItem().toString());
			   break;
		   case "Lata":
			   numberOfDays = 366 * Integer.parseInt(number.getSelectedItem().toString());
			   break;
		}
		
		
		return numberOfDays;
	}
	
	private void addCountToComboBox(JComboBox<Integer> combo)
	{
		for(int i=0;i<=31 ; i++)
			combo.addItem(i);
	}
	
	private void setVisibilityOfTolerance(boolean visibility)
	{
		lblTolerancja.setVisible(visibility);
		comboBox_tolerance_unit.setVisible(visibility);
		comboBox_tolerance_count.setVisible(visibility);
		lblNewLabel_1.setVisible(visibility);
		lblNewLabel_2.setVisible(visibility);
	}
}
