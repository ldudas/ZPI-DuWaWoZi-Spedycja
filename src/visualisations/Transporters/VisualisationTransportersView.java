package visualisations.Transporters;



import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.SystemColor;
import java.awt.Dialog.ModalExclusionType;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dataModels.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.DefaultComboBoxModel;

import com.esri.map.JMap;

import jpanels.TransportersVisualisation.TransporterInfo.TransporterDetailsJPanel;


public class VisualisationTransportersView 
{
	private VisualisationTransportersPresenter trans_presenter;
	private String city_from;
	private String city_to;
	private SizeCategory sc;
	
	private int lastWindowPos;
	
	private JFrame carrierVisualization;
	private JPanel visualization;
	private JPanel control;
	private boolean isVisualisationStarted;
	private ArrayList<Transporter> transporters;
	private ArrayList<Shape> drawnShapes;
	private double max_num_of_orders;
	private double min_num_of_orders;
	private double max_cost;
	private double min_cost;
	private double max_volume;
	private double max_capacity;

	private JButton btn_confirmPath;

	public VisualisationTransportersView() 
	{
		drawnShapes = new ArrayList<>();
		isVisualisationStarted = false;
		city_from = "";
		city_to = "";
		sc = null;
		//initialize();
		lastWindowPos = 50;
	}
	
	public void setPresenter(VisualisationTransportersPresenter pres)
	{
		trans_presenter = pres;
	}
	
	public void clearCarrierVisualizationFrame()
	{
		if(control != null)
			carrierVisualization.remove(control);
		if(visualization != null)
			carrierVisualization.remove(visualization);
		if( drawnShapes != null)
			drawnShapes.clear();
		isVisualisationStarted = false;
		city_from = "";
		city_to = "";
		sc = null;
		//initialize();
		lastWindowPos = 50;
	}

	
	public void initialize( JFrame carrierV) 
	{		
		//carrierVisualization = new JFrame();
		this.carrierVisualization = carrierV;
		//carrierVisualization.setIconImage(Toolkit.getDefaultToolkit().getImage(View.class.getResource("/images/025581022.jpg")));
		carrierVisualization.setResizable(false);
		carrierVisualization.setTitle("Przewo\u017Anicy");
		carrierVisualization.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		carrierVisualization.getContentPane().setBackground(SystemColor.inactiveCaption);
		carrierVisualization.getContentPane().setForeground(SystemColor.textHighlightText);
		carrierVisualization.getContentPane().setLayout(null);
		
	    control = new JPanel();
		control.setBackground(SystemColor.activeCaption);
		control.setBounds(10, 11, 797, 121);
		carrierVisualization.getContentPane().add(control);
		control.setLayout(null);
		btn_confirmPath = new JButton("Potwierd\u017A");
		
		btn_confirmPath.setBackground(SystemColor.inactiveCaption);
		btn_confirmPath.setForeground(new Color(0, 0, 0));
		btn_confirmPath.setFont(btn_confirmPath.getFont().deriveFont(btn_confirmPath.getFont().getStyle() | Font.BOLD | Font.ITALIC, 13f));
		btn_confirmPath.setBounds(10, 63, 255, 42);
				
		control.add(btn_confirmPath);
		
		JComboBox<String> comboBox_cityTo = new JComboBox<String>();
		comboBox_cityTo.setModel(new DefaultComboBoxModel<String>(new String[] {"Warszawa"}));
		comboBox_cityTo.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 12));
		comboBox_cityTo.setBackground(SystemColor.inactiveCaption);
		comboBox_cityTo.setBounds(137, 33, 128, 31);
		control.add(comboBox_cityTo);
			
		
		JComboBox<String> comboBox_CityFrom = new JComboBox<String>();
		comboBox_CityFrom.setModel(new DefaultComboBoxModel<String>(new String[] {"Wrocław"}));
		comboBox_CityFrom.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 12));
		comboBox_CityFrom.setBackground(SystemColor.inactiveCaption);
		comboBox_CityFrom.setBounds(10, 33, 128, 31);
		control.add(comboBox_CityFrom);
		
		
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
		comboBox_size.setBounds(670, 45, 96, 31);
		control.add(comboBox_size);
		
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
						trans_presenter.drawTransporters(city_from, city_to,sc);
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
		lbl_size.setBounds(564, 45, 96, 31);
		control.add(lbl_size);
		
		/*JLabel lbl_specjalType = new JLabel("Typ Specjalny");
		lbl_specjalType.setForeground(new Color(255, 204, 0));
		lbl_specjalType.setLabelFor(comboBox_specialType);
		lbl_specjalType.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lbl_specjalType.setBounds(674, 33, 96, 31);
		control.add(lbl_specjalType);*/
		
		visualization = new JPanel();
		//visualization.addMouseListener(visualization);
		visualization.setBackground(SystemColor.activeCaption);
		visualization.setBounds(10, 138, 797, 523);
		carrierVisualization.getContentPane().add(visualization);
		visualization.setLayout(null);
		
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
		//label_backgraound.setIcon(new ImageIcon(View.class.getResource("/images/gl-4.jpg")));
		carrierVisualization.setForeground(SystemColor.menu);
		carrierVisualization.setBounds(250, 30, 825, 725);
		carrierVisualization.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		carrierVisualization.setVisible(true);
		carrierVisualization.invalidate();
		carrierVisualization.validate();
	}
	
	 public void drawTransporters(ArrayList<Transporter> transporters)
	    {
		 	if(!isVisualisationStarted)
		 	{
		 		carrierVisualization.getContentPane().remove(visualization);
		 		visualization = new TransVisJPanel(this);
		 		visualization.addMouseListener((TransVisJPanel)visualization);
		 		visualization.addMouseMotionListener((TransVisJPanel)visualization);
				visualization.setBackground(SystemColor.activeCaption);
				visualization.setBounds(10, 138, 797, 523);
				carrierVisualization.getContentPane().add(visualization);
				visualization.setLayout(null);
				isVisualisationStarted = true;
		 	}
		 	
	    	this.transporters = transporters;
	    	
	    	max_num_of_orders = Double.MIN_VALUE;
	    	min_num_of_orders = Double.MAX_VALUE;
	    	max_cost = Double.MIN_VALUE;
	    	min_cost = Double.MAX_VALUE;
	    	max_volume = Double.MIN_VALUE;
	    	max_capacity = Double.MIN_VALUE;
	    	
	    	transporters.stream().forEach( (Transporter tr) -> {
	    		if(tr.getNumber_of_orders()<min_num_of_orders) min_num_of_orders = tr.getNumber_of_orders();
	    		if(tr.getNumber_of_orders()>max_num_of_orders) max_num_of_orders = tr.getNumber_of_orders();
	    		if(tr.getCost()>max_cost) max_cost = tr.getCost();
	    		if(tr.getCost()<min_cost) min_cost = tr.getCost();
	    		if(tr.getVolume()>max_volume) max_volume = tr.getVolume();
	    		if(tr.getCapacity()>max_capacity) max_capacity = tr.getCapacity();	
	    	});
	    	visualization.repaint();
	    }
	 
	 public void showTransporterDetailsWindow(Transporter t,JMap routes)
	 {
				
				JFrame detailsWindow = new JFrame(t.getName() + " - " + (t.getSizeCategory() == SizeCategory.SMALL?"Małe":
																t.getSizeCategory() == SizeCategory.MEDIUM?"Średnie":
																											"Duże"));
				detailsWindow.setResizable(false);
				detailsWindow.setBounds(lastWindowPos, lastWindowPos, 910, 350);
				detailsWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				
				TransporterDetailsJPanel transDetailsPanel = new TransporterDetailsJPanel();
				transDetailsPanel.setInfoAboutTransporterInToList(t);
				transDetailsPanel.setRoutesMap(routes);
				detailsWindow.getContentPane().add(transDetailsPanel);
				detailsWindow.setVisible(true);		
				lastWindowPos += 25;
	 }
	 
	 public void showTransporterDetails(int id_trans)
	 {
		 trans_presenter.showTransporterDetails(id_trans);
	 }

	public double getMax_num_of_orders()
	{
		return max_num_of_orders;
	}

	public double getMin_num_of_orders()
	{
		return min_num_of_orders;
	}

	public double getMax_cost()
	{
		return max_cost;
	}

	public double getMin_cost()
	{
		return min_cost;
	}

	public double getMax_volume()
	{
		return max_volume;
	}

	public double getMax_capacity()
	{
		return max_capacity;
	}

	public ArrayList<Transporter> getTransporters()
	{
		return transporters;
	}
	 
	    
}




	

