package visualisations.Transporters;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import dataModels.SizeCategory;
import dataModels.Transporter;

import javax.swing.SwingConstants;


public class TransVisInterfaceJPanel extends JPanel
{
	
	private static final long serialVersionUID = 1L;
	private JPanel visualization;
	private JPanel control;
	private JButton btn_confirmPath;
	private JButton btn_savePathToDatabase;
	private JLabel lblChosenTransporter;
	private JLabel lblChosenTransporterDetails;
	VisualisationTransportersView transporter_view;
	
	private String city_from;
	private String city_to;
	private SizeCategory sc;

	public TransVisInterfaceJPanel(VisualisationTransportersView view, int opening_flag) 
	{
		transporter_view = view;
		
		setBackground(SystemColor.inactiveCaption);
		setForeground(SystemColor.textHighlightText);
		setLayout(null);
		setBounds(0,0,824,674);
				
			    control = new JPanel();
				control.setBackground(SystemColor.inactiveCaptionText);
				control.setBounds(10, 11, 797, 121);
				control.setLayout(null);
				btn_confirmPath = new JButton("Potwierd\u017A");
				
				btn_confirmPath.setBackground(SystemColor.inactiveCaption);
				btn_confirmPath.setForeground(new Color(0, 0, 0));
				btn_confirmPath.setFont(btn_confirmPath.getFont().deriveFont(btn_confirmPath.getFont().getStyle() | Font.BOLD | Font.ITALIC, 13f));
				btn_confirmPath.setBounds(10, 63, 255, 42);
				
				
						
				control.add(btn_confirmPath);
				
				
				if(opening_flag ==1)
				{
					btn_savePathToDatabase = new JButton("Zapisz trasę");
					btn_savePathToDatabase.setBackground(SystemColor.inactiveCaption);
					btn_savePathToDatabase.setForeground(new Color(0, 0, 0));
					btn_savePathToDatabase.setFont(btn_confirmPath.getFont().deriveFont(btn_confirmPath.getFont().getStyle() | Font.BOLD | Font.ITALIC, 13f));
					btn_savePathToDatabase.setBounds(612, 63, 150, 42);
					btn_savePathToDatabase.addActionListener(new ActionListener() 
					{
						public void actionPerformed(ActionEvent e) 
						{
							
							Transporter chosen_tr = transporter_view.getChosenTransporter();
							if( chosen_tr!=null)
							{
								int dialogResult = JOptionPane.showConfirmDialog(null, "Czy chcesz zapisać trasę z przewoźnikiem:"
										+ "\n\nNazwa: " + chosen_tr.getName() 
										+ "\nKategoria rozmiaru: " +(chosen_tr.getSizeCategory() == SizeCategory.SMALL?"Małe":
																	chosen_tr.getSizeCategory() == SizeCategory.MEDIUM?"Średnie":
																	"Duże")
										+ "\nŚredni koszt (w kat. rozm.): " + chosen_tr.getCost()
										+ "\nLiczba zleceń: " + chosen_tr.getNumber_of_orders()
										 +"\n\n", 
										"Potwierdzenie", JOptionPane.YES_NO_OPTION);
								if(dialogResult == JOptionPane.YES_OPTION) 
								{
									transporter_view.saveOrdersToDatabase();	
								} 
							}
							else
								JOptionPane.showMessageDialog(null, "Nie wybrano żadnego przewoźnika", "Błąd", 
																JOptionPane.ERROR_MESSAGE);		
							
							
							
							
							
						}
					});
					control.add(btn_savePathToDatabase);
					
					lblChosenTransporter = new JLabel("Brak wybranego przewoźnika");
					lblChosenTransporter.setHorizontalAlignment(SwingConstants.RIGHT);
					lblChosenTransporter.setForeground(new Color(255, 204, 0));
					lblChosenTransporter.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
					lblChosenTransporter.setBounds(325, 7, 450, 31);
					control.add(lblChosenTransporter);
					
					
					lblChosenTransporterDetails = new JLabel("");
					lblChosenTransporterDetails.setHorizontalAlignment(SwingConstants.RIGHT);
					lblChosenTransporterDetails.setForeground(new Color(255, 204, 0));
					lblChosenTransporterDetails.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
					lblChosenTransporterDetails.setBounds(325, 33, 480, 31);
					control.add(lblChosenTransporterDetails);
				}
				
			
				
				
				
				JComboBox<String> comboBox_cityTo = new JComboBox<String>();
				//comboBox_cityTo.setModel(new DefaultComboBoxModel<String>(cities));
				comboBox_cityTo.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 12));
				comboBox_cityTo.setBackground(SystemColor.inactiveCaption);
				comboBox_cityTo.setBounds(137, 33, 128, 31);
				control.add(comboBox_cityTo);
				
				
				JComboBox<String> comboBox_CityFrom = new JComboBox<String>();
				//comboBox_CityFrom.setModel(new DefaultComboBoxModel<String>(cities));
				comboBox_CityFrom.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 12));
				comboBox_CityFrom.setBackground(SystemColor.inactiveCaption);
				comboBox_CityFrom.setBounds(10, 33, 128, 31);
				control.add(comboBox_CityFrom);
				
				ArrayList<String> listOfCityNames = transporter_view.getAllCityNames();
				
				addAllCityToComboBox(comboBox_cityTo, listOfCityNames);
				addAllCityToComboBox(comboBox_CityFrom, listOfCityNames);
				
				
				JLabel lbl_cityFrom = new JLabel("   Z Miasta");
				lbl_cityFrom.setForeground(new Color(255, 204, 0));
				lbl_cityFrom.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
				lbl_cityFrom.setLabelFor(comboBox_cityTo);
				lbl_cityFrom.setBounds(10, 11, 128, 23);
				control.add(lbl_cityFrom);
				
				JComboBox<String> comboBox_size = new JComboBox<String>();
				comboBox_size.setModel(new DefaultComboBoxModel<String>(new String[] {"Wszystkie", "Małe", "Średnie", "Duże"}));
				comboBox_size.setSelectedIndex(0);
				comboBox_size.setBackground(SystemColor.inactiveCaption);
				comboBox_size.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
				comboBox_size.setBounds(339, 33, 96, 31);
				control.add(comboBox_size);
				
				JLabel lbl_cityTo = new JLabel("  Do Miasta");
				lbl_cityTo.setForeground(new Color(255, 204, 0));
				lbl_cityTo.setBounds(137, 9, 128, 26);
				control.add(lbl_cityTo);
				lbl_cityTo.setBackground(SystemColor.textHighlight);
				lbl_cityTo.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
				lbl_cityTo.setLabelFor(comboBox_CityFrom);
				
				JLabel label_backgraound = new JLabel("");
				label_backgraound.setBounds(0, -6, 797, 127);
				control.add(label_backgraound);
				
				/*JComboBox<String> comboBox_specialType = new JComboBox<String>();
				comboBox_specialType.setModel(new DefaultComboBoxModel(new String[] {"Wszytkie", "Chłodnia", "Cysterna"}));
				comboBox_specialType.setBackground(SystemColor.inactiveCaption);
				comboBox_specialType.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
				comboBox_specialType.setBounds(552, 33, 96, 31);
				control.add(comboBox_specialType);
				*/
				
				btn_confirmPath.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						if(comboBox_CityFrom.getSelectedIndex() != -1 && comboBox_cityTo.getSelectedIndex() != -1)
						{
							String curr_city_from = (String) comboBox_CityFrom.getSelectedItem();
							String curr_city_to = (String) comboBox_cityTo.getSelectedItem();
							
							int size_ind = comboBox_size.getSelectedIndex();
							SizeCategory curr_sc = size_ind == 0 ? SizeCategory.ALL : 
												size_ind == 1 ? SizeCategory.SMALL :
												size_ind == 2 ?	SizeCategory.MEDIUM :
																SizeCategory.BIG;
							
							if(!curr_city_from.equals(city_from) || !curr_city_to.equals(city_to) || curr_sc!=sc)
							{
								city_from = curr_city_from;
								city_to = curr_city_to;
								sc = curr_sc;
								transporter_view.drawTransporters(city_from, city_to,sc);
							}
							
						}
						else
						{
							System.out.println("Proszę wybrać miasta");
						}
						
						
					}
				});
				
				
				
				
				JLabel lbl_size = new JLabel("Rozmiar");
				lbl_size.setForeground(new Color(255, 204, 0));
				lbl_size.setLabelFor(comboBox_size);
				lbl_size.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
				lbl_size.setBounds(339, 7, 96, 31);
				control.add(lbl_size);
				
				/*JLabel lbl_specjalType = new JLabel("Typ Specjalny");
				lbl_specjalType.setForeground(new Color(255, 204, 0));
				lbl_specjalType.setLabelFor(comboBox_specialType);
				lbl_specjalType.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
				lbl_specjalType.setBounds(674, 33, 96, 31);
				control.add(lbl_specjalType);*/
				
				visualization = new JPanel();
				//visualization.addMouseListener(visualization);
				visualization.setBackground(SystemColor.inactiveCaptionText);
				visualization.setBounds(10, 138, 797, 523);
				add(visualization);
				visualization.setLayout(null);
			
				
				
				add(control);
				
			
		
	}
	
	private void addAllCityToComboBox(JComboBox<String> comboBox, ArrayList<String> listOfCityNames)
	{
		if(comboBox != null)
		{	
		
			for ( String cityName : listOfCityNames )

			{
					comboBox.addItem(cityName);
			}
		
		}
	}
	
	public void removeStartVisualisationPanel()
	{
		remove(visualization);
	}
	
	public void addVisualisation()
	{
		visualization = new TransVisJPanel(transporter_view);
 		visualization.addMouseListener((TransVisJPanel)visualization);
 		visualization.addMouseMotionListener((TransVisJPanel)visualization);
		visualization.setBackground(SystemColor.inactiveCaptionText);
		visualization.setBounds(10, 138, 797, 523);
		add(visualization);
		visualization.setLayout(null);
	}
	
	public void repaintVisualisation()
	{
		visualization.repaint();
	}
	
	public void setChosenTransporter(Transporter t)
	{
		if(lblChosenTransporter!= null)
		{
			lblChosenTransporter.setText("Wybrany przewoźnik:");
			lblChosenTransporterDetails.setText(t.getName()+", Kat. rozmiaru - "+(t.getSizeCategory() == SizeCategory.SMALL?"Małe":
																								t.getSizeCategory() == SizeCategory.MEDIUM?"Średnie":
																																			"Duże"));
		}
	}
}
