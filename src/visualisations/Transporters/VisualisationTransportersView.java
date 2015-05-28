package visualisations.Transporters;


import interfaces.RoutePlanningPresenter;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
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
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import dataModels.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.DefaultComboBoxModel;

import jpanels.ManufacturerVisualization.ManufactureInfo.ManufacturerDetailsJPanel;
import jpanels.ManufacturerVisualization.ManufactureInfo.RingChartJPanel;
import jpanels.TransportersVisualisation.TransporterInfo.TransporterDetailsJPanel;


public class VisualisationTransportersView 
{
	private VisualisationTransportersPresenter trans_presenter;
	String city_from;
	String city_to;
	SizeCategory sc;
	
	private JFrame carrierVisualization;
	private JPanel visualization;
	private boolean isVisualisationStarted;
	private ArrayList<Transporter> transporters;
	private ArrayList<Shape> drawnShapes;
	private double max_num_of_orders;
	private double min_num_of_orders;
	private double max_cost;
	private double min_cost;
	private double max_volume;
	private double max_capacity;
	private double max_delay;
	private double max_executed;



	public VisualisationTransportersView() {
		drawnShapes = new ArrayList<>();
		isVisualisationStarted = false;
		city_from = "";
		city_to = "";
		sc = null;
		initialize();
	}
	
	public void setPresenter(VisualisationTransportersPresenter pres)
	{
		trans_presenter = pres;
	}

	
	private void initialize() {
		
		
		carrierVisualization = new JFrame();
		//carrierVisualization.setIconImage(Toolkit.getDefaultToolkit().getImage(View.class.getResource("/images/025581022.jpg")));
		carrierVisualization.setResizable(false);
		carrierVisualization.setTitle("Przewo\u017Anicy");
		carrierVisualization.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		carrierVisualization.getContentPane().setBackground(SystemColor.inactiveCaption);
		carrierVisualization.getContentPane().setForeground(SystemColor.textHighlightText);
		carrierVisualization.getContentPane().setLayout(null);
		
		JPanel control = new JPanel();
		control.setBackground(SystemColor.activeCaption);
		control.setBounds(10, 11, 797, 121);
		carrierVisualization.getContentPane().add(control);
		control.setLayout(null);
		JButton btn_confirmPath = new JButton("Potwierd\u017A");
		
		btn_confirmPath.setBackground(SystemColor.inactiveCaption);
		btn_confirmPath.setForeground(new Color(0, 0, 0));
		btn_confirmPath.setFont(btn_confirmPath.getFont().deriveFont(btn_confirmPath.getFont().getStyle() | Font.BOLD | Font.ITALIC, 13f));
		btn_confirmPath.setBounds(10, 63, 255, 42);
				
		control.add(btn_confirmPath);
		
		JComboBox<String> comboBox_cityTo = new JComboBox<String>();
		comboBox_cityTo.setModel(new DefaultComboBoxModel(new String[] {"Warszawa"}));
		comboBox_cityTo.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 12));
		comboBox_cityTo.setBackground(SystemColor.inactiveCaption);
		comboBox_cityTo.setBounds(137, 33, 128, 31);
		control.add(comboBox_cityTo);
			
		
		JComboBox<String> comboBox_CityFrom = new JComboBox<String>();
		comboBox_CityFrom.setModel(new DefaultComboBoxModel(new String[] {"Wrocław"}));
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
		comboBox_size.setModel(new DefaultComboBoxModel(new String[] {"Wszystkie", "Małe", "Średnie", "Duże"}));
		comboBox_size.setSelectedIndex(0);
		comboBox_size.setBackground(SystemColor.inactiveCaption);
		comboBox_size.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		comboBox_size.setBounds(552, 69, 96, 31);
		control.add(comboBox_size);
		
		JComboBox<String> comboBox_specialType = new JComboBox<String>();
		comboBox_specialType.setModel(new DefaultComboBoxModel(new String[] {"Wszytkie", "Chłodnia", "Cysterna"}));
		comboBox_specialType.setBackground(SystemColor.inactiveCaption);
		comboBox_specialType.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		comboBox_specialType.setBounds(552, 33, 96, 31);
		control.add(comboBox_specialType);
		
		
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
		lbl_size.setBounds(674, 69, 96, 31);
		control.add(lbl_size);
		
		JLabel lbl_specjalType = new JLabel("Typ Specjalny");
		lbl_specjalType.setForeground(new Color(255, 204, 0));
		lbl_specjalType.setLabelFor(comboBox_specialType);
		lbl_specjalType.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lbl_specjalType.setBounds(674, 33, 96, 31);
		control.add(lbl_specjalType);
		
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
		carrierVisualization.setBounds(250, 30, 825, 700);
		carrierVisualization.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		carrierVisualization.setVisible(true);
	}
	
	 public void drawTransporters(ArrayList<Transporter> transporters)
	    {
		 	if(!isVisualisationStarted)
		 	{
		 		carrierVisualization.getContentPane().remove(visualization);
		 		visualization = new TransVisJPanel();
		 		visualization.addMouseListener((TransVisJPanel)visualization);
		 		visualization.addMouseMotionListener((TransVisJPanel)visualization);
				visualization.setBackground(SystemColor.activeCaption);
				visualization.setBounds(10, 138, 797, 523);
				carrierVisualization.getContentPane().add(visualization);
				visualization.setLayout(null);
				isVisualisationStarted = true;
		 	}
		 	
	    	this.transporters = transporters;
	    	max_num_of_orders = transporters.stream().max(Transporter::compareByNumbrOfOrders).get().getNumber_of_orders();
	    	min_num_of_orders = transporters.stream().min(Transporter::compareByNumbrOfOrders).get().getNumber_of_orders();
	    	max_cost = transporters.stream().max(Transporter::compareByCost).get().getCost();
	    	min_cost = transporters.stream().min(Transporter::compareByCost).get().getCost();
	    	max_volume = transporters.stream().max(Transporter::compareByVolume).get().getVolume();
	    	max_capacity = transporters.stream().max(Transporter::compareByCapacity).get().getCapacity();
	    	max_delay = transporters.stream().max(Transporter::compareByDelay).get().getDelay();
	    	max_executed = transporters.stream().max(Transporter::compareByExecuted).get().getExecuted();
	    	visualization.repaint();
	    }
	 
	 public void showTransporterDetailsWindow(Transporter t)
	 {
				
				JFrame detailsWindow = new JFrame();
				detailsWindow.setResizable(false);
				detailsWindow.setBounds(100, 100, 665, 452);
				detailsWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				
				TransporterDetailsJPanel transDetailsPanel = new TransporterDetailsJPanel();
				transDetailsPanel.setInfoAboutTransporterInToList(t);
				detailsWindow.add(transDetailsPanel);
				detailsWindow.setVisible(true);		
	 }
	 
	 class TransVisJPanel extends JPanel  implements MouseListener, MouseMotionListener
	    {
	    	
	    	 private final int x_gap = 20;
		     private final int y_gap = 20;
		     private final int line_thickness = 2;
		     
		     private int panel_height;
		     private int panel_width;
		     private int x_line_x_beg;
		     private int x_line_y_beg;
		     private int x_line_x_end;
		     private int x_line_y_end;
		     private int y_line_x_beg;
		     private int y_line_y_beg;
		     private int y_line_x_end;
		     private int y_line_y_end;
		     
		     private final String axis_font = "Gulim";
		     private final int axis_font_style = Font.ITALIC;
		     private final String rect_font = "Juice ITC";
		     private final int rect_font_style = Font.PLAIN;
		     
		     private final int x_axis_max_font_size = 15;
		     private final int x_axis_min_font_size = 7;
		     
		     private final int y_axis_max_font_size = 17;
		     private final int y_axis_min_font_size = 3;
		     
		     private final int trans_max_font_size = 50;
		     private final int trans_min_font_size = 10;
		     
		     private double max_obj_width;
		     private double max_obj_height;
		     
		     private double min_obj_width;
		     private double min_obj_height;
		     
		     private final int triangle_size = 4;
		     private final int number_of_desc = 10;

		     private final float max_alpha_minus = 0.9f;
		     private final float min_alpha_minus = 0.1f;
		     
		     private final double max_exec = 0.05;
		     

		    public TransVisJPanel()
		    {
		        setOpaque(true);
		        setBackground(Color.WHITE);
		    }
	    	
		    public Dimension getPreferredSize() 
		    {
		        return new Dimension(700,500);
		    }
		    

		    public void paintComponent(Graphics g) 
		    {
		        super.paintComponent(g);  
		        
		        
		        //wymiary płótna
		        panel_height = this.getHeight();
		        panel_width = this.getWidth();
		        
		        
		        //poczatek osi x - x
		        x_line_x_beg = panel_width / x_gap;
		        //poczatek osi x - y
		        x_line_y_beg = panel_height - (panel_height / y_gap);
		        //koniec osi x - x
		        x_line_x_end = panel_width - (3*panel_width/(x_gap));
		        //koniec osi x - y
		        x_line_y_end = x_line_y_beg;
		        
		        
		        //poczatek osi y - x
		        y_line_x_beg = x_line_x_beg;
		        //poczatek osi y - y
		        y_line_y_beg = 3 * panel_height / y_gap;
		        //koniec osi y - x
		        y_line_x_end = x_line_x_beg;
		        //koniec osi y - y
		        y_line_y_end = x_line_y_beg;
		        
		       
		        
		        Graphics2D g2d = (Graphics2D) g;
		        
		        //zapamiętanie oryginalengo przekształcenia i compozycji
		        AffineTransform atr = g2d.getTransform();
		        AlphaComposite ac = (AlphaComposite) g2d.getComposite();
		   
		        //oś x
		        	//grubość, zakończenie, łączenia
			        BasicStroke bs1 = new BasicStroke(line_thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
			        g2d.setStroke(bs1);
			        g2d.drawLine(x_line_x_beg, x_line_y_beg, x_line_x_end+panel_width/10, x_line_y_end);
			        //watrtosci na osi x
			        	//linie
				        int x_desc_gap = (x_line_x_end - x_line_x_beg) / number_of_desc;
				        
				        for(int i=1; i<=number_of_desc; i++)
				        {
				        	g2d.drawLine(x_line_x_beg + x_desc_gap * i, x_line_y_beg, x_line_x_beg + x_desc_gap * i, x_line_y_beg+panel_height/50);
				        }
				        //liczby
				        int x_axis_num_font_size = (int)(panel_height/40.0);
				        int x_axis_num_gap = (int) ((max_num_of_orders - min_num_of_orders)/ number_of_desc);
				        
				        g2d.setFont(new Font(axis_font, axis_font_style, x_axis_num_font_size));
					        //rysowanie stringa
					        for(int i=1; i<=number_of_desc; i++)
					        {
					        g2d.drawString(((int)(min_num_of_orders + x_axis_num_gap * i))+"",  x_line_x_beg + x_desc_gap * i + panel_width/300, x_line_y_beg+panel_height/40);
					        }
			        
		        //oś y
			        //grubość, zakończenie, łączenia
			        BasicStroke bs2 = new BasicStroke(line_thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
			        g2d.setStroke(bs2);
			        g2d.drawLine(y_line_x_beg, y_line_y_beg-panel_height/10, y_line_x_end, y_line_y_end);
			        //watrtosci na osi y
			        int y_desc_gap = (y_line_y_end - y_line_y_beg) / number_of_desc;
			        
			        for(int i=1; i<=number_of_desc; i++)
			        {
			        	g2d.drawLine(y_line_x_end, y_line_y_end-y_desc_gap*i, y_line_x_end - panel_width/50, y_line_y_end-y_desc_gap*i);
			        }
			        //liczby
			        	int y_axis_num_font_size = (int)(panel_width/60.0);
			        	int y_axis_num_gap = (int) ((max_cost - min_cost)/ number_of_desc);
			        
			        	g2d.setFont(new Font(axis_font, axis_font_style, y_axis_num_font_size));
			        	String number_y;
			        	FontMetrics metrics = g2d.getFontMetrics();
			        		//rysowanie stringa
			        		for(int i=1; i<=number_of_desc; i++)
			        		{
			        			//wartosc
			        			number_y = ((int)(min_cost + y_axis_num_gap * i))+"";
					        	 //pobieranie wymiarow na wizualizacji
					        	 Rectangle2D rectan = metrics.getStringBounds(number_y, g2d);
					        	 double font_width =   rectan.getWidth();
					        	 g2d.drawString(number_y,  y_line_x_end - (int)font_width - panel_width/100, y_line_y_end-y_desc_gap*i - panel_height/100);
			        		}
			      
			     //groty
				        //rysowanie trojkata
					        GeneralPath p0 = new GeneralPath();
					        p0.moveTo(0.0f, -triangle_size);
					        p0.lineTo(triangle_size, triangle_size);
					        p0.lineTo(-triangle_size, triangle_size);
					        p0.closePath();
					        
					        
					      //grot x   
					        //ustawienie poczatku ukladu wspolrzednych
					        g2d.translate(x_line_x_end+panel_width/10, x_line_y_end);
					        //obrot
					        g2d.rotate(Math.toRadians(90));
					        //rysowanie stringa (wg przeksztalcen)
					        g2d.fill(p0);
					        
					        //przywracanie oryginalnych wspolrzednych
					        g2d.setTransform(atr);
				        
				        //grot y 
					        
					        //ustawienie poczatku ukladu wspolrzednych
					        g2d.translate(y_line_x_beg, y_line_y_beg-panel_height/10);
					        //rysowanie stringa (wg przeksztalcen)
					        g2d.fill(p0);
					        
					        //przywracanie oryginalnych wspolrzednych
					        g2d.setTransform(atr);
			        
		        //punkt odniesienia jako poczatek ukladu wspolrzednych
		        //g2d.translate(x_line_x_beg, x_line_y_beg);
		       
		        //rysowanie opisu osi x
			        //wyliczanie rozmiaru czcionki
			        int x_axis_font_size = (int)((panel_height/1000.0)*(x_axis_max_font_size-x_axis_min_font_size)+x_axis_min_font_size);
			        
			        g2d.setFont(new Font(axis_font, axis_font_style, x_axis_font_size));
			        //rysowanie stringa
			        g2d.drawString("Liczba zleceń", x_line_x_end+panel_width/20, x_line_y_end + panel_height/30);
			        
		        
		        //rysowanie opisu osi y
			        //wyliczanie rozmiaru czcionki
			        int y_axis_font_size = (int)((panel_width/1000.0)*(y_axis_max_font_size-y_axis_min_font_size)+y_axis_min_font_size);
			       
			        g2d.setFont(new Font(axis_font, axis_font_style, y_axis_font_size));
			       
			        
			        //ustawienie poczatku ukladu wspolrzednych
			        g2d.translate( y_line_x_beg, y_line_y_beg);
			        //obrot
			        g2d.rotate(Math.toRadians(-90));
			        //rysowanie stringa (wg przeksztalcen)
			        g2d.drawString("Koszt", panel_height/30 , -panel_height/30);
			        
			        //przywracanie oryginalnych wspolrzednych
			        g2d.setTransform(atr);
		        
		       
		        double x_obj;
		        double y_obj;
		        double cap;
		        double vol;
		        double alpha;
		        double exec;
		        AlphaComposite alcom;
		       
		        
		        if (sc == SizeCategory.SMALL)
		        {
			        max_obj_height = panel_height/5.0;
			        min_obj_height = panel_height/20.0;
			        
			        max_obj_width = panel_width/20.0;
			        min_obj_width = panel_width/25.0;
		        }
		        else if (sc == SizeCategory.MEDIUM)
		        {
		        	   max_obj_height = panel_height/7.0;
				        min_obj_height = panel_height/20.0;
				        
				        max_obj_width = panel_width/15.0;
				        min_obj_width = panel_width/30.0;
		        }
		        else if (sc == SizeCategory.BIG)
		        {
		        	max_obj_height = panel_height/5.0;
			        min_obj_height = panel_height/20.0;
			        
			        max_obj_width = panel_width/10.0;
			        min_obj_width = panel_width/30.0;	
		        }
		        else
		        {
		        	max_obj_height = panel_height/5.0;
			        min_obj_height = panel_height/20.0;
			        
			        max_obj_width = panel_width/25.0;
			        min_obj_width = panel_width/30.0;
		        }	
		        
		        //czyszczenie kolekcji narysowanych obiektów przewoznikow
		        drawnShapes.clear();
		        
		        //rysowanie obiektow przewoznikow
		        for(Transporter t:transporters)
		        {
		        	//wyliczanie wysokosci prostokata (pojemnosc)
		        	vol = (t.getVolume()/max_volume) * (max_obj_height-min_obj_height) + min_obj_height;
		        	//wyliczanie szerokosci prostokata (ladownosc)
		        	cap = (t.getCapacity()/max_capacity) * (max_obj_width-min_obj_width) + min_obj_width;
		        	//wyliczanie pozycji x obiektu (liczba zlecen)
		        	x_obj = x_line_x_beg + ((t.getNumber_of_orders()-min_num_of_orders)/(max_num_of_orders-min_num_of_orders)) * (x_line_x_end - x_line_x_beg) - cap/2.0;
		        	//wyliczanie pozycji y obiektu (koszt)
		        	y_obj = x_line_y_beg - ((t.getCost()-min_cost)/(max_cost-min_cost)) * (y_line_y_end - y_line_y_beg) - vol/2.0;
		        	//wyliczanie przezroczystosci (wiarygodnosc - stosunek nzreal/zreal)
		        	exec = t.getExecuted()>max_exec?max_exec:
		        									t.getExecuted();
		        	alpha = 2- ((((1.0/max_exec) * exec) * (max_alpha_minus - min_alpha_minus) + min_alpha_minus)+1);
		        	System.out.println(exec+" "+alpha);
			        	 	
		        	//ustawianie alpha rysowania
		        	alcom = AlphaComposite.getInstance(
		                     AlphaComposite.SRC_ATOP, (float)alpha);
		             g2d.setComposite(alcom);
		           	
		             
		             //tworzenie prostokąta
		        	RoundRectangle2D rect = new RoundRectangle2D.Double(x_obj, y_obj, cap, vol,4,4);
		        	
		        	//rysowanie obwodki
		        	g2d.setColor(new Color(0,0,0));
		            g2d.setStroke(new BasicStroke(3));
		        	g2d.draw(rect);
		        	
		        	//dodanie prostokąta do kolekcji narysowanych obiektów przewoznikow
		        	drawnShapes.add(rect);
		        	
		        	//ustawianie koloru rysowania
		        	double delay_ratio = 4*t.getDelay()>=1.0?1:4*t.getDelay();
		        	int green = delay_ratio>=0.5?(int)(-127.5*delay_ratio + 127.5):255;
		        	int red = delay_ratio<=0.5?(int)(510*delay_ratio):255;
		        	g2d.setColor(new Color(red,green,0));
		        	//rysowanie obiektu przewoznika
		        	g2d.fill(rect);
		        	
		        	
		        	
		        	//opis producenta na grafice
		        		//ustwianie nazwy,stylu,rozmiaru czcionki
			        	 g2d.setFont(new Font(rect_font, rect_font_style, (int)((vol/100.0)*(trans_max_font_size-trans_min_font_size))+trans_min_font_size));
			        	 
			        	 //badanie rozmiaru prostokata jaki zajmuje napis
				        	 FontMetrics metrics_2 = g2d.getFontMetrics();
				        	 
				        	 //pobieranie wymiarow
				        	 String subs = t.getName().substring(0,3);
				        	 Rectangle2D rectan = metrics_2.getStringBounds(subs, g2d);
				        	 double font_width =   rectan.getWidth();
				        	 double font_height =  rectan.getHeight();
				        	
				        	g2d.setColor(Color.BLACK);
				        	
				        	//sprawdzenie czy napis zmiesci sie w obiekcie
				        	if(font_width<= cap && font_height<=vol)
				        	{
						     g2d.drawString(subs,(float)(x_obj+cap/2.0-font_width/2.0),(float) (y_obj+vol/2.0+font_height/3.0));
				        	}
		        	
				       //przywrocenie oryginalnego kompzytu
				     g2d.setComposite(ac);
		        };
		        
		       
		        
		    }

			@Override
			public void mouseClicked(MouseEvent e) 
			{
		
				for(Shape s: drawnShapes)
				{
					if(s.contains(e.getX(), e.getY()))
					{
						Transporter t = transporters.get(drawnShapes.indexOf(s));
						System.out.println("Wybrano: "+t.getName()+"\n"+t.getPhone_num());
						trans_presenter.showTransporterDetails(t.getId_trans());
					}
				}
				
			}

			
			@Override
			public void mouseMoved(MouseEvent e) 
			{
				for(Shape s: drawnShapes)
				{
					if(s.contains(e.getX(), e.getY()))
					{
						Transporter t = transporters.get(drawnShapes.indexOf(s));
						System.out.println("Jestem nad: "+t.getName()+"\n"+t.getPhone_num());
					}
				}
			} 
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e){
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
			}

			
		}
	 
	    
}




	

