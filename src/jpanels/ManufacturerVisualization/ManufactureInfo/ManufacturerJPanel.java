package jpanels.ManufacturerVisualization.ManufactureInfo;

import interfaces.RoutePlanningPresenter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JList;

import dataModels.Manufacturer;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import java.awt.Font;
import java.awt.Color;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JComboBox;

import jpanel.calendare.JCalendar;


public class ManufacturerJPanel extends JPanel 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JList<String> man_list;
	private RoutePlanningPresenter presenter_RoutePlanning;
	
	private JCalendar calendare_dateFrom;
	private JCalendar calendare_dateTo;
	private DateFormat dateFormat;
	
	private JComboBox<String> comboBox_nextCityName;
	/**
	 * Create the panel.
	 */
	public ManufacturerJPanel() 
	{
		setForeground(new Color(255, 204, 0));
		
		setBorder(null);
		setBackground(SystemColor.inactiveCaption);
		setLayout(null);
		calendare_dateFrom = new JCalendar();
		calendare_dateFrom.getMonthChooser().getComboBox().setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 12));
		calendare_dateFrom.getYearChooser().getSpinner().setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		calendare_dateFrom.getYearChooser().getSpinner().setForeground(SystemColor.desktop);
		calendare_dateFrom.getYearChooser().getSpinner().setBackground(SystemColor.activeCaption);
		calendare_dateFrom.getDayChooser().getDayPanel().setBackground(SystemColor.inactiveCaptionText);
		calendare_dateFrom.setBounds(24, 133, 200, 180);
		calendare_dateFrom.setLocale(new Locale("pl", "PL"));
		//add(calendare_dateFrom);
		
		calendare_dateTo = new JCalendar();
		calendare_dateTo.getMonthChooser().getComboBox().setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 12));
		calendare_dateTo.getYearChooser().getSpinner().setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		calendare_dateTo.getYearChooser().getSpinner().setForeground(SystemColor.desktop);
		calendare_dateTo.getYearChooser().getSpinner().setBackground(SystemColor.activeCaption);
		calendare_dateTo.getDayChooser().getDayPanel().setBackground(SystemColor.inactiveCaptionText);
		calendare_dateTo.setBounds(227, 133, 200, 180);
		calendare_dateTo.setLocale(new Locale("pl", "PL"));
		calendare_dateTo.setDate( new Date(calendare_dateFrom.getDate().getTime() + (1000 * 60 * 60 * 24)) );
		//add(calendare_dateTo);
		
		JLabel lblDaneProducenta = new JLabel("Dane producenta:\r\n");
		lblDaneProducenta.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblDaneProducenta.setForeground(new Color(255, 204, 0));
		lblDaneProducenta.setBounds(453, 101, 148, 14);
		add(lblDaneProducenta);
		
		JPanel panel_data = new JPanel();
		panel_data.setBackground(SystemColor.inactiveCaptionText);
		panel_data.setBounds(10, 11, 634, 328);
		panel_data.add(calendare_dateFrom);
		panel_data.add(calendare_dateTo);
		add(panel_data);
		panel_data.setLayout(null);
		
		JComboBox<String> comboBox_nextCityName_1 = new JComboBox<String>();
		comboBox_nextCityName_1.setBounds(41, 36, 288, 32);
		panel_data.add(comboBox_nextCityName_1);
		comboBox_nextCityName_1.setBackground(SystemColor.activeCaption);
		comboBox_nextCityName_1.setForeground(SystemColor.desktop);
		comboBox_nextCityName_1.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		
		JLabel lblMiastoDo = new JLabel("Miasto do:");
		lblMiastoDo.setBounds(41, 11, 96, 14);
		panel_data.add(lblMiastoDo);
		lblMiastoDo.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblMiastoDo.setForeground(new Color(255, 204, 0));
		
		JLabel lblDataWyjazdu = new JLabel("Data wyjazdu:");
		lblDataWyjazdu.setBounds(20, 91, 127, 14);
		panel_data.add(lblDataWyjazdu);
		lblDataWyjazdu.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblDataWyjazdu.setForeground(new Color(255, 204, 0));
		
		JLabel lblDataPrzyjazdu = new JLabel("Data przyjazdu: ");
		lblDataPrzyjazdu.setBounds(218, 91, 111, 14);
		panel_data.add(lblDataPrzyjazdu);
		lblDataPrzyjazdu.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblDataPrzyjazdu.setForeground(new Color(255, 204, 0));
		
		JPanel panel_buttons = new JPanel();
		panel_buttons.setBackground(SystemColor.inactiveCaptionText);
		panel_buttons.setBounds(10, 341, 634, 86);
		add(panel_buttons);
		panel_buttons.setLayout(null);
		
		JButton man_cancelButton = new JButton("Anuluj");
		man_cancelButton.setBounds(10, 11, 164, 62);
		panel_buttons.add(man_cancelButton);
		man_cancelButton.setBackground(SystemColor.activeCaption);
		man_cancelButton.setForeground(SystemColor.desktop);
		man_cancelButton.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		
		JButton man_notConfirmButton = new JButton("Nie odpowiada");
		man_notConfirmButton.setBounds(180, 11, 180, 62);
		panel_buttons.add(man_notConfirmButton);
		man_notConfirmButton.setBackground(SystemColor.activeCaption);
		man_notConfirmButton.setForeground(SystemColor.desktop);
		man_notConfirmButton.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		
		JButton man_confirmButton = new JButton("Zatwierd\u017A");
		man_confirmButton.setBounds(439, 11, 171, 62);
		panel_buttons.add(man_confirmButton);
		man_confirmButton.setBackground(SystemColor.activeCaption);
		man_confirmButton.setForeground(SystemColor.desktop);
		man_confirmButton.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		man_confirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				presenter_RoutePlanning.send_nextCityNameAfterConfirm();
				presenter_RoutePlanning.closeManufacturerInfo();
				presenter_RoutePlanning.addCityToPath();
				
				presenter_RoutePlanning.addNextOrder();
				presenter_RoutePlanning.addOrderToTab();
			}
		});
		man_notConfirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				presenter_RoutePlanning.markAsUnsuitable();
				presenter_RoutePlanning.closeManufacturerInfo();
			}
		});
		man_cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				presenter_RoutePlanning.closeManufacturerInfo();
			}
		});
		


	}
	
	public static void main(String [] args)
	{
		JFrame frame = new JFrame();
		ManufacturerJPanel panel = new ManufacturerJPanel();
		
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
			String [] details = new String [6];
			details[0] = "Nazwa: " + manufacturer.getName();
			details[1] = "Telefon: " + manufacturer.getPhone();
			details[2] = "Ostatnia aktywność: " + manufacturer.getLastActivity();
			details[3] = "Liczba zleceń: " + manufacturer.getNumberOfOrders();
			details[4] = "Suma wartości zleceń: " + manufacturer.getSumOfOrdersValue();
			details[5] = "Suma dni wykonywanych zleceń: " + manufacturer.getSumOfDays();
			man_list = new JList<String>(details);
			man_list.setBounds(452, 130, 280, 140);
			man_list.setBackground(SystemColor.activeCaption);
			add(man_list);	
	}
	
	public String getNextCityName()
	{
		return comboBox_nextCityName.getSelectedItem().toString();
	}
	
	public String getStartDate()
	{

		return dateFormat.format(calendare_dateFrom.getDate());
	}
	
	public String getFinishDate()
	{
		return dateFormat.format(calendare_dateTo.getDate());
	}
}
