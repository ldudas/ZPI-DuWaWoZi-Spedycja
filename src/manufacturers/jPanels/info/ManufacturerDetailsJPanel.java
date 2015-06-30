package manufacturers.jPanels.info;

import interfaces.mvp.RoutePlanningPresenter;

import javax.swing.JPanel;

import java.awt.SystemColor;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTextArea;

import shared.dataModels.Manufacturer;

import java.awt.CardLayout;

/**
 * Panel zawierający informacje o 
 * aktualnie wybranym producencie.
 * @author Kamil Zimny
 *
 */
public class ManufacturerDetailsJPanel extends JPanel
{
	private RoutePlanningPresenter presenter_RoutePlanning;
	private JPanel panel_data;
	private Manufacturer currentManufacturer;
	
	private JTextArea textArea_manufacturerAdditionalInfo;
	private JScrollPane scrollManufacturerAddition;
	
	/**
	 * Wykres z aktywności i opłacalnościa 
	 * danego producenta każdego miesiąca w roku. 
	 */
	private RingChartJPanel chartJPanel;
	private JList<String> man_list;
	
	public ManufacturerDetailsJPanel() 
	{
		setBackground(SystemColor.inactiveCaption);
		setLayout(null);
		
		panel_data = new JPanel();
		panel_data.setBackground(SystemColor.inactiveCaptionText);
		panel_data.setBounds(10, 11, 627, 306);
		add(panel_data);
		panel_data.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Dane producenta:");
		lblNewLabel.setForeground(new Color(255, 204, 0));
		lblNewLabel.setBackground(SystemColor.inactiveCaptionText);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblNewLabel.setBounds(10, 11, 129, 23);
		panel_data.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Aktywność producenta w miesiącach:\r\n");
		lblNewLabel_1.setForeground(new Color(255, 204, 0));
		lblNewLabel_1.setBackground(SystemColor.inactiveCaptionText);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblNewLabel_1.setBounds(240, 11, 264, 23);
		panel_data.add(lblNewLabel_1);
		
		textArea_manufacturerAdditionalInfo = new JTextArea();
		scrollManufacturerAddition = new JScrollPane(textArea_manufacturerAdditionalInfo);
		
		textArea_manufacturerAdditionalInfo.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		textArea_manufacturerAdditionalInfo.setBackground(SystemColor.activeCaption);
		textArea_manufacturerAdditionalInfo.setForeground(new Color(139, 0, 0));
		textArea_manufacturerAdditionalInfo.setEditable(false);
		textArea_manufacturerAdditionalInfo.setLineWrap(true);
		textArea_manufacturerAdditionalInfo.setWrapStyleWord(true);
		scrollManufacturerAddition.setBackground(SystemColor.activeCaption);
		scrollManufacturerAddition.setBounds(10, 166, 220, 66);
		scrollManufacturerAddition.setVisible(false);
		panel_data.add(scrollManufacturerAddition);
		
		JPanel panel_chart = new JPanel();
		panel_chart.setBackground(SystemColor.inactiveCaption);
		panel_chart.setBounds(240, 38, 377, 248);
		panel_data.add(panel_chart);
		panel_chart.setLayout(new CardLayout(0, 0));
		
		chartJPanel = new RingChartJPanel();
		panel_chart.add(chartJPanel, "name_19959547016006");
		chartJPanel.setBackground(SystemColor.activeCaption);
		chartJPanel.setLayout(new CardLayout(0, 0));
		
		
		JPanel panel_buttons = new JPanel();
		panel_buttons.setBackground(SystemColor.inactiveCaptionText);
		panel_buttons.setBounds(10, 319, 627, 85);
		add(panel_buttons);
		panel_buttons.setLayout(null);
		
		JButton btn_cancel = new JButton("Anuluj\r\n");
		btn_cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				presenter_RoutePlanning.closeManufacturerInfo();
			}
		});
		btn_cancel.setBackground(SystemColor.activeCaption);
		btn_cancel.setForeground(SystemColor.desktop);
		btn_cancel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		btn_cancel.setBounds(10, 11, 135, 62);
		panel_buttons.add(btn_cancel);
		
		JButton btn_unpropertyChoose = new JButton("Nie Odpowiada\r\n");
		btn_unpropertyChoose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				String reason = (String) JOptionPane.showInputDialog(ManufacturerDetailsJPanel.this,"Proszę podać powód odrzucenia producenta: ",
						"Odrzucenie producenta",JOptionPane.QUESTION_MESSAGE ,null,null,"Powód odrzucenia" );

				if( reason != null )
				{
					presenter_RoutePlanning.setManufacturerAttribut_additionInfo(currentManufacturer, reason);
					presenter_RoutePlanning.markAsUnsuitable();
					presenter_RoutePlanning.closeManufacturerInfo();
				}
			}
		});
		btn_unpropertyChoose.setBackground(SystemColor.activeCaption);
		btn_unpropertyChoose.setForeground(SystemColor.desktop);
		btn_unpropertyChoose.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		btn_unpropertyChoose.setBounds(155, 11, 135, 62);
		panel_buttons.add(btn_unpropertyChoose);
		
		JButton btn_propertyChoose = new JButton("Odpowiada\r\n");
		btn_propertyChoose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				presenter_RoutePlanning.changeManufacturerInfoFrame_manufacturerOrderData();
			}
		});
		btn_propertyChoose.setBackground(SystemColor.activeCaption);
		btn_propertyChoose.setForeground(SystemColor.desktop);
		btn_propertyChoose.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		btn_propertyChoose.setBounds(482, 11, 135, 62);
		panel_buttons.add(btn_propertyChoose);
		
	}
	
	public void setPresenter(final RoutePlanningPresenter presenter)
	{
		presenter_RoutePlanning = presenter;		
	}
	
	/***
	 * Metoda ustawiajaca kolory aktywnosci producentow.
	 * @param activitiesColor
	 * @author Kamil Zimny
	 */
	public void setChartActivityColor(final Manufacturer manufacturer )
	{
		chartJPanel.setManufacturerDataOnChart(manufacturer);
	}
	
	/**
	 * Metoda dodajaca do listy informacje o produceńcie
	 * @param attr
	 * @author Kamil Zimny
	 */
	public void setInfoAboutManufacturerInToList( Manufacturer manufacturer)
	{ 
			currentManufacturer = manufacturer;

			if(man_list != null)
			{
				panel_data.remove(man_list);
			}

			String [] details = new String [8];
			details[0] = "Nazwa: " + manufacturer.getName();
			details[1] = "Telefon: " + manufacturer.getPhone();
			details[2] = "Ostatnia aktywność: " + manufacturer.getLastActivity();
			details[3] = "Liczba zleceń: " + manufacturer.getNumberOfOrders();
			details[4] = "Suma wartości zleceń: " + manufacturer.getSumOfOrdersValue();
			details[5] = "Suma dni wykonywanych zleceń: " + manufacturer.getSumOfDays();
			
			if( !manufacturer.getAdditionInfo().equals("Brak") )
			{		
				scrollManufacturerAddition.setVisible(true);
				textArea_manufacturerAdditionalInfo.setText("Powód odrzucenia: \n" + manufacturer.getAdditionInfo());
			}
			else
				scrollManufacturerAddition.setVisible(false);


			man_list = new JList<String>(details);
			man_list.setBounds(10, 40, 220, 130);
			man_list.setBackground(SystemColor.activeCaption);
			panel_data.add(man_list);	
			
	}
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
