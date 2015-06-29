package manufacturers.jPanels.info;

import interfaces.mvp.RoutePlanningPresenter;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import java.awt.Font;
import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JComboBox;

import shared.jPanels.calendar.JCalendar;



public class ManufacturerOrderDataJPanel extends JPanel 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static String DATE_FORMAT = "yyyy-MM-dd";
	private RoutePlanningPresenter presenter_RoutePlanning;
	
	private JCalendar calendare_dateFrom;
	private JCalendar calendare_dateTo;
	private DateFormat dateFormat;
	
	private JComboBox<String> comboBox_nextCityName;
	/**
	 * Create the panel.
	 */

	public ManufacturerOrderDataJPanel() 
	{
		dateFormat = new SimpleDateFormat(DATE_FORMAT);
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
		calendare_dateFrom.setBounds(24, 83, 280, 240);
		calendare_dateFrom.setLocale(new Locale("pl", "PL"));
		
		
		calendare_dateTo = new JCalendar();
		calendare_dateTo.getMonthChooser().getComboBox().setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 12));
		calendare_dateTo.getYearChooser().getSpinner().setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		calendare_dateTo.getYearChooser().getSpinner().setForeground(SystemColor.desktop);
		calendare_dateTo.getYearChooser().getSpinner().setBackground(SystemColor.activeCaption);
		calendare_dateTo.getDayChooser().getDayPanel().setBackground(SystemColor.inactiveCaptionText);
		calendare_dateTo.setBounds(318, 83, 280, 240);
		calendare_dateTo.setLocale(new Locale("pl", "PL"));
		calendare_dateTo.setDate( new Date(calendare_dateFrom.getDate().getTime() + (1000 * 60 * 60 * 24)) );
		
		JPanel panel_data = new JPanel();
		panel_data.setBackground(SystemColor.inactiveCaptionText);
		panel_data.setBounds(10, 11, 621, 328);
		panel_data.add(calendare_dateFrom);
		panel_data.add(calendare_dateTo);
		add(panel_data);
		panel_data.setLayout(null);
		
		comboBox_nextCityName = new JComboBox<String>();
		comboBox_nextCityName.setBounds(143, 19, 303, 32);
		panel_data.add(comboBox_nextCityName);
		comboBox_nextCityName.setBackground(SystemColor.activeCaption);
		comboBox_nextCityName.setForeground(SystemColor.desktop);
		comboBox_nextCityName.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		
		JLabel lblMiastoDo = new JLabel("Miasto do:");
		lblMiastoDo.setBounds(24, 28, 96, 14);
		panel_data.add(lblMiastoDo);
		lblMiastoDo.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblMiastoDo.setForeground(new Color(255, 204, 0));
		
		JLabel lblDataWyjazdu = new JLabel("Data wyjazdu:");
		lblDataWyjazdu.setBounds(24, 63, 127, 14);
		panel_data.add(lblDataWyjazdu);
		lblDataWyjazdu.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblDataWyjazdu.setForeground(new Color(255, 204, 0));
		
		JLabel lblDataPrzyjazdu = new JLabel("Data przyjazdu: ");
		lblDataPrzyjazdu.setBounds(316, 63, 111, 14);
		panel_data.add(lblDataPrzyjazdu);
		lblDataPrzyjazdu.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblDataPrzyjazdu.setForeground(new Color(255, 204, 0));
		
		JPanel panel_buttons = new JPanel();
		panel_buttons.setBackground(SystemColor.inactiveCaptionText);
		panel_buttons.setBounds(10, 341, 621, 86);
		add(panel_buttons);
		panel_buttons.setLayout(null);
		
		JButton man_cancelButton = new JButton("Anuluj ");
		man_cancelButton.setBounds(23, 11, 135, 62);
		panel_buttons.add(man_cancelButton);
		man_cancelButton.setBackground(SystemColor.activeCaption);
		man_cancelButton.setForeground(SystemColor.desktop);
		man_cancelButton.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		
		JButton man_back = new JButton("Wróć ");
		man_back.setBounds(168, 11, 135, 62);
		panel_buttons.add(man_back);
		man_back.setBackground(SystemColor.activeCaption);
		man_back.setForeground(SystemColor.desktop);
		man_back.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		
		JButton man_confirmButton = new JButton("Zatwierd\u017A");
		man_confirmButton.setBounds(462, 11, 135, 62);
		panel_buttons.add(man_confirmButton);
		man_confirmButton.setBackground(SystemColor.activeCaption);
		man_confirmButton.setForeground(SystemColor.desktop);
		man_confirmButton.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		man_confirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				
				String error = presenter_RoutePlanning.checkCorrectnessOfData_nextOrders();
				if( error == null )
				{
					int dialogResult = JOptionPane.showConfirmDialog(ManufacturerOrderDataJPanel.this, "Czy podane dane się zgadzają:"
							+ "\n\nData wyjazdu: " + getStartDate() 
							+ "\nData przyjazdu: " + getFinishDate()
							+ "\nMiasto docelowe: " + getNextCityName() +"\n\n", 
							"Potwierdzenie", JOptionPane.YES_NO_OPTION);
					if(dialogResult == JOptionPane.YES_OPTION) 
					{
						presenter_RoutePlanning.send_nextCityNameAfterConfirm();
						presenter_RoutePlanning.closeManufacturerInfo();
						presenter_RoutePlanning.addCityToPath();
					
						presenter_RoutePlanning.addNextOrder();
						presenter_RoutePlanning.addOrderToTab();
					}
				}
				else
					JOptionPane.showMessageDialog(ManufacturerOrderDataJPanel.this, "Bład wprowadzanych danych:\n" + error, "Błąd danych", 
							JOptionPane.ERROR_MESSAGE);	
			}
		});
		man_back.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				presenter_RoutePlanning.changeManufacturerOrderData_manufacturerInfoFrame();
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
		ManufacturerOrderDataJPanel panel = new ManufacturerOrderDataJPanel();
		
		frame.setBounds(10, 10, 658, 475);
		frame.getContentPane().add(panel);
		frame.setVisible(true);
		
	}
	
	public void setPresenter(final RoutePlanningPresenter presenter)
	{
		presenter_RoutePlanning = presenter;
		
	}
	
	public void addAllCityToList()
	{
		presenter_RoutePlanning.addAllCityToList(comboBox_nextCityName,null);
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
	
	public void setStartDateOn(Date date)
	{
		calendare_dateFrom.setDate( date );
		calendare_dateTo.setDate( new Date(date.getTime() + (1000 * 60 * 60 * 24)) );
	}
}
