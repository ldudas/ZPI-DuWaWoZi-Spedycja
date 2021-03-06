package manufacturers.jPanels.visualisation;

import interfaces.mvp.*;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.UIManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JComboBox;

import shared.jPanels.calendar.JCalendar;


/**
 * Panel zawierający dane do wypełnienia podczas
 * tworzenia nowej trasy. Dane dotyczą pierwszej trasy/pierwszego zlecenia.
 * @author Kamil Zimny
 *
 */
public class StartPlanningJPanel extends JPanel 
{
	/**
	 * 
	 */
	private final static String DATE_FORMAT = "yyyy-MM-dd";
	
	private static final long serialVersionUID = 1L;
	private JComboBox<String> comboBox_cityFrom;
	private JComboBox<String> comboBox_cityTo;
	
	private RoutePlanningPresenter presenter_route_planning;
	private DateFormat dateFormat;
	private JCalendar calendare_dateFrom;
	private JCalendar calendare_dateTo;
	private JButton btn_Clear;
	
	/**
	 * Create the panel.
	 */
	public StartPlanningJPanel() 
	{
		setBackground(SystemColor.activeCaption);
		setLayout(null);
		dateFormat = new SimpleDateFormat(DATE_FORMAT);
		
		JPanel panel_from = new JPanel();	
		panel_from.setBackground(SystemColor.inactiveCaptionText);
		panel_from.setBounds(0, 0, 297, 361);
		add(panel_from);
		panel_from.setLayout(null);
		
		calendare_dateFrom = new JCalendar();
		calendare_dateFrom.getMonthChooser().getComboBox().setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 12));
		calendare_dateFrom.getYearChooser().getSpinner().setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		calendare_dateFrom.getYearChooser().getSpinner().setForeground(SystemColor.desktop);
		calendare_dateFrom.getYearChooser().getSpinner().setBackground(SystemColor.activeCaption);
		calendare_dateFrom.getDayChooser().getDayPanel().setBackground(SystemColor.inactiveCaptionText);
		calendare_dateFrom.setBounds(10, 120, 280, 220);
		calendare_dateFrom.setLocale(new Locale("pl", "PL"));
		panel_from.add(calendare_dateFrom);
		
		JLabel lblDataWyjazdu = new JLabel("Data wyjazdu:");
		lblDataWyjazdu.setBounds(10, 87, 120, 14);
		panel_from.add(lblDataWyjazdu);
		lblDataWyjazdu.setForeground(new Color(255, 204, 0));
		lblDataWyjazdu.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		
		JLabel lblSkd = new JLabel("Sk\u0105d:");
		lblSkd.setBounds(10, 11, 46, 14);
		panel_from.add(lblSkd);
		lblSkd.setForeground(new Color(255, 204, 0));
		lblSkd.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		
	    comboBox_cityFrom = new JComboBox<String>();
		
		comboBox_cityFrom.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		comboBox_cityFrom.setBackground(SystemColor.activeCaption);
		comboBox_cityFrom.setForeground(SystemColor.desktop);
		comboBox_cityFrom.setBounds(10, 36, 280, 30);
		panel_from.add(comboBox_cityFrom);
		
		JPanel panel_to = new JPanel();
		panel_to.setBackground(SystemColor.inactiveCaptionText);
		panel_to.setBounds(306, 0, 304, 361);
		add(panel_to);
		panel_to.setLayout(null);
		
		calendare_dateTo = new JCalendar();
		calendare_dateTo.getMonthChooser().getComboBox().setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 12));
		calendare_dateTo.getYearChooser().getSpinner().setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		calendare_dateTo.getYearChooser().getSpinner().setForeground(SystemColor.desktop);
		calendare_dateTo.getYearChooser().getSpinner().setBackground(SystemColor.activeCaption);
		calendare_dateTo.getDayChooser().getDayPanel().setBackground(SystemColor.inactiveCaptionText);
		calendare_dateTo.setBounds(10, 120, 280, 220);
		calendare_dateTo.setLocale(new Locale("pl", "PL"));
		calendare_dateTo.setDate( new Date(calendare_dateFrom.getDate().getTime() + (1000 * 60 * 60 * 24)) );
	
		panel_to.add(calendare_dateTo);
		
		JLabel lblDokd = new JLabel("Dok\u0105d:");
		lblDokd.setBounds(10, 11, 46, 14);
		panel_to.add(lblDokd);
		lblDokd.setForeground(new Color(255, 204, 0));
		lblDokd.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		
		JLabel lblDataPrzyjazdu = new JLabel("Data przyjazdu:");
		lblDataPrzyjazdu.setBounds(10, 84, 125, 14);
		panel_to.add(lblDataPrzyjazdu);
		lblDataPrzyjazdu.setForeground(new Color(255, 204, 0));
		lblDataPrzyjazdu.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		
	    comboBox_cityTo = new JComboBox<String>();
		comboBox_cityTo.setForeground(SystemColor.desktop);
		comboBox_cityTo.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		comboBox_cityTo.setBackground(SystemColor.activeCaption);
		comboBox_cityTo.setBounds(10, 36, 280, 30);
		panel_to.add(comboBox_cityTo);
		
		JPanel panel = new JPanel();
		panel.setForeground(SystemColor.desktop);
		panel.setBackground(SystemColor.inactiveCaptionText);
		panel.setBounds(0, 366, 610, 98);
		add(panel);
		panel.setLayout(null);
		
		JButton start_okButton = new JButton("OK");
		start_okButton.setBounds(347, 26, 224, 50);
		panel.add(start_okButton);
		start_okButton.setForeground(SystemColor.desktop);
		start_okButton.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));
		start_okButton.setBackground(SystemColor.activeCaption);
		
		setUIOptionPane();
		
		btn_Clear = new JButton("Wyczyść");
		btn_Clear.setBorder(UIManager.getBorder("Button.border"));
		btn_Clear.setForeground(SystemColor.desktop);
		btn_Clear.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				int dialogResult = JOptionPane.showConfirmDialog(StartPlanningJPanel.this, "Czy na pewno chcesz wyczyścić uzupełnione pola?", 
																	"Czyszczenie", JOptionPane.YES_NO_OPTION);
				if(dialogResult == JOptionPane.YES_OPTION) 
				{
					clearData();
				} 
						
			}
		});
		btn_Clear.setBackground(SystemColor.activeCaption);
		btn_Clear.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));
		btn_Clear.setBounds(34, 26, 224, 50);
		panel.add(btn_Clear);
		start_okButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				String error = presenter_route_planning.checkCorrectnessOfData_firstOrder();
				if( error == null )
				{
					int dialogResult = JOptionPane.showConfirmDialog(StartPlanningJPanel.this, "Czy podane dane się zgadzają:"
							+ "\n\nData wyjazdu: " + getStartDate() 
							+ "\nMiasto wyjazdu: " + get_city_from()
							+ "\nData przyjazdu: " + getFinishDate()
							+ "\nMiasto przyjazdu: " + get_city_to() +"\n\n", 
							"Potwierdzenie", JOptionPane.YES_NO_OPTION);
					if(dialogResult == JOptionPane.YES_OPTION) 
					{
						presenter_route_planning.startManufacturersVisualisation();	
					} 
				}
				else
					JOptionPane.showMessageDialog(StartPlanningJPanel.this, "Bład wprowadzanych danych:\n" + error, "Błąd danych", 
													JOptionPane.ERROR_MESSAGE);		
			}
		});
		
	}
	
	public void clearData()
	{
		calendare_dateFrom.setDate(new Date());
		calendare_dateTo.setDate(new Date(calendare_dateFrom.getDate().getTime() + (1000 * 60 * 60 * 24)) );
		if( comboBox_cityFrom.getItemCount() > 0)
			comboBox_cityFrom.setSelectedIndex(0);
		if( comboBox_cityTo.getItemCount() > 0)
			comboBox_cityTo.setSelectedIndex(0);
	}
	 
	public void setPresenter(final RoutePlanningPresenter presenter)
	{
		presenter_route_planning = presenter;
	}
	
	public void addAllCityToList()
	{
		presenter_route_planning.addAllCityToList(comboBox_cityFrom,comboBox_cityTo);
	}
	
	public String get_city_to()
	{
		return comboBox_cityTo.getSelectedItem().toString();
	}
	
	public String get_city_from()
	{
		return comboBox_cityFrom.getSelectedItem().toString();
	}
	
	public String getStartDate()
	{
		return dateFormat.format(calendare_dateFrom.getDate());
	}
	
	public String getFinishDate()
	{
		return dateFormat.format(calendare_dateTo.getDate());
	}
	
	private void setUIOptionPane()
	{
		UIManager.put("OptionPane.background", SystemColor.inactiveCaption);
		UIManager.put("Panel.background", SystemColor.inactiveCaption);
		UIManager.put("OptionPane.noButtonText", "Nie");
	    UIManager.put("OptionPane.okButtonText", "Ok");
	    UIManager.put("OptionPane.yesButtonText", "Tak");
	    UIManager.put("OptionPane.cancelButtonText", "Anuluj");
	}
}
