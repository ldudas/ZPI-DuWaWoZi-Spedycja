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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import dataModels.Transporter;

/**
 * Wizualizacja przewoźników
 * @author Łukasz
 *
 */
public class TransVisJPanel extends JPanel  implements MouseListener, MouseMotionListener
{  

	private static final long serialVersionUID = 1L;
	
	 /**
	  * lewy margines osi x
	  */
	 private final static int x_gap = 20;
	 
	 /**
	  * dolny margines osi y
	  */
     private final static int y_gap = 20;
     
     /**
      * grubość osi
      */
     private final static int line_thickness = 2;
     
     /**
      * Czcionka opisów osi
      */
     private final static  String axis_font = "Gulim";
     
     /**
      * Styl czcionki opsiów osi
      */
     private final static int axis_font_style = Font.ITALIC;
     
     /**
      * Czcionka opisu obiektów na wizualziacji
      */
     private final static String rect_font = "Juice ITC";
     
     /**
      * Styl czcionki opisu obiektów na wizualziacji
      */
     private final static int rect_font_style = Font.PLAIN;
     
     /**
      * maksymalny rozmiar czcionki opisu osi x
      */
     private final static int x_axis_max_font_size = 15;
     
     /**
      * minimalny rozmiar czcionki opisu osi x
      */
     private final static int x_axis_min_font_size = 10;
     
     /**
      * maksymalny rozmiar czcionki opisu osi y
      */
     private final static int y_axis_max_font_size = 17;
     
     /**
      * minimalny rozmiar czcionki opisu osi y
      */
     private final static int y_axis_min_font_size = 3;
     
     /**
      * maksymalny rozmiar czcionki opsiu obiektu przewoźnika
      */
     private final static int trans_max_font_size = 50;
     /**
      * minimalny rozmiar czcionki opsiu obiektu przewoźnika
      */
     private final static int trans_min_font_size = 10;
     
     /**
      * rozmiar grotu osi
      */
     private final static int triangle_size = 4;
     
     /**
      * liczba "podziałek" na osiach
      */
     private final static int number_of_desc = 10;

     /**
      * maksymalna wartość kanału alpha obiektów przewoźników
      */
     private final static float max_alpha_minus = 0.9f;
     
     /**
      * minimalna wartość kanału alpha obiektów przewoźników
      */
     private final static float min_alpha_minus = 0.1f;
     
     /**
      * maksymalna (prog od ktorego jest najgorsza) wartość stosunku nzreal od zreal
      */
     private final static double max_exec = 0.05;
     
     
     /////////////////////////////////////////////////////////////////////////////////////////    
     /**
      * maksymalna szerokość obiektu przewoźnika
      */
     private double max_obj_width;
     /**
      * maksymalna wysokość obiektu przewoźnika
      */
     private double max_obj_height;
     
     /**
      * minimalna szerokość obiektu przewoźnika
      */
     private double min_obj_width;
     
     /**
      * minimalna wysokość obiektu przewoźnika
      */
     private double min_obj_height;
     
     /**
      * wysokość panelu wizualizacji
      */
     private int panel_height;
     
     /**
      * szerokość panelu wizualizacji
      */
     private int panel_width;
     
     /**
      * początek x osi X
      */
     private int x_line_x_beg;
     
     /**
      * początek y osi X
      */
     private int x_line_y_beg;
     
     /**
      * koniec x osi X
      */
     private int x_line_x_end;
     
     /**
      * koniec y osi X
      */
     private int x_line_y_end;
     
     /**
      * początek x osi Y
      */
     private int y_line_x_beg;
     
     /**
      * początek y osi Y
      */
     private int y_line_y_beg;
     
     /**
      * koniec x osi Y
      */
     private int y_line_x_end;
     
     /**
      * koniec y osi Y
      */
     private int y_line_y_end;
     
     /**
      * widok
      */
     private VisualisationTransportersView view;
     
     /**
      * kolekcja narysowanych na mapie obiektów przewoźników
      */
     private ArrayList<Shape> drawnShapes;
     
     /**
      * kolekcja otwartych aktualnie okien ze szczegółami przewoźników
      */
     private HashMap<Integer, JFrame> openTransportersJFrames;
    
    

    public TransVisJPanel(VisualisationTransportersView view)
    {
        this.view = view;
        drawnShapes = new ArrayList<>();
        openTransportersJFrames = new HashMap<>();
    	setOpaque(true);
        setBackground(Color.WHITE);
    }
	
    public Dimension getPreferredSize() 
    {
        return new Dimension(700,500);
    }
    
    /**
     * Narysuj osie
     * @param g2d Graphics2D do rysowania
     */
    private void drawAxles(Graphics2D g2d)
    {
    	
        setAxlesCoordinates(); //ustawianie wspolrzednych osi
        
        //zapamiętanie oryginalengo przekształcenia i kompozycji
        AffineTransform atr = g2d.getTransform();
   
        g2d.setColor(SystemColor.inactiveCaption);
        
        //oś x
        drawXAxle(g2d);
        
        g2d.setColor(SystemColor.inactiveCaption);
	        
        //oś y
	    drawYAxle(g2d); 
	    
	    g2d.setColor(SystemColor.inactiveCaption);
	    
	    //przywracanie oryginalnych wspolrzednych
        g2d.setTransform(atr);
        
	     //groty
		        //tworzenie trojkata
			        GeneralPath p0 = new GeneralPath();
			        p0.moveTo(0.0f, -triangle_size);
			        p0.lineTo(triangle_size, triangle_size);
			        p0.lineTo(-triangle_size, triangle_size);
			        p0.closePath();
			    
			    //grot x
			    drawXArrowHead(g2d, p0);        
			    //przywracanie oryginalnych wspolrzednych
			    g2d.setTransform(atr);
		        
		        //grot y    
			    drawYArrowHead(g2d, p0); 
			    //przywracanie oryginalnych wspolrzednych
			     g2d.setTransform(atr); 
	       
    }
    
    
    /**
     * Narysuj obiekty przewoźników
     * @param g2d Graphics2D do rysowania
     */
    void drawTransportersObjects(Graphics2D g2d)
    {
	    	setTransporterObjectsSizeBounds();    
    	
	        //czyszczenie kolekcji narysowanych obiektów przewoznikow
	        drawnShapes.clear();
	        
	        //rysowanie obiektow przewoznikow
	        for(Transporter t:view.getTransporters())
	        {
	        	drawTransporterObject(g2d, t);
	        };
	        
    }
    
    
    /**
     * Ustaw max/min wymiary obiektów przewoźników wg wymiarów panelu
     */
    private void setTransporterObjectsSizeBounds()
    {
    	max_obj_height = panel_height/5.0;
    	min_obj_height = panel_height/30.0;
    
    	max_obj_width = panel_width/10.0;
    	min_obj_width = panel_width/23.0;
    }
    
    
    /**
     * Narysuj obiekt przewoźnika
     * @param g2d Graphics2D do rysowania
     * @param t Przewoźnik do narysowania
     */
    private void drawTransporterObject(Graphics2D g2d, Transporter t)
    {
    	double x_obj;
        double y_obj;
        double cap;
        double vol;
        double alpha;
        double exec;
        AlphaComposite alcom;
    	
    	//wyliczanie wysokosci prostokata (pojemnosc)
    	vol = (t.getVolume()/view.getMax_volume()) * (max_obj_height-min_obj_height) + min_obj_height;
    	//wyliczanie szerokosci prostokata (ladownosc)
    	cap = (t.getCapacity()/view.getMax_capacity()) * (max_obj_width-min_obj_width) + min_obj_width;
    	//wyliczanie pozycji x obiektu (liczba zlecen)
    	x_obj = x_line_x_beg + ((t.getNumber_of_orders()-view.getMin_num_of_orders())/(view.getMax_num_of_orders()-view.getMin_num_of_orders())) * (x_line_x_end - x_line_x_beg) - cap/2.0;
    	//wyliczanie pozycji y obiektu (koszt)
    	y_obj = x_line_y_beg - ((t.getCost()-view.getMin_cost())/(view.getMax_cost()-view.getMin_cost())) * (y_line_y_end - y_line_y_beg) - vol/2.0;
    	//wyliczanie przezroczystosci (wiarygodnosc - stosunek nzreal/zreal)
    	exec = t.getExecuted()>max_exec?max_exec:
    									t.getExecuted();
    	alpha = 2- ((((1.0/max_exec) * exec) * (max_alpha_minus - min_alpha_minus) + min_alpha_minus)+1);
        	 	
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
	    // g2d.setComposite(ac);
    }
    	    
    
    /**
     * Ustawianie współrzędnych osi wg rozmiarów panelu do rysowania
     */
    private void setAxlesCoordinates()
    {
    	 //poczatek osi x - x
        x_line_x_beg = (int)(panel_width * 1.5 / x_gap);
        //poczatek osi x - y
        x_line_y_beg = panel_height - (int) (panel_height *1.5 / y_gap);
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
    }
    
    
    /**
     * Rysuje oś X
     * @param g2d Graphics2D do rysowania
     */
    private void drawXAxle(Graphics2D g2d)
    {
    	//grubość, zakończenie, łączenia
        BasicStroke bs1 = new BasicStroke(line_thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
        g2d.setStroke(bs1);
        g2d.drawLine(x_line_x_beg, x_line_y_beg, x_line_x_end+panel_width/10, x_line_y_end);
        
        int x_line_x_desc_beg = x_line_x_beg;
        double x_line_desc_unit = ((x_line_x_end - x_line_x_beg) / (view.getMax_num_of_orders() - view.getMin_num_of_orders()));
        int begin_number = (int)(view.getMin_num_of_orders());
        int x_axis_gap = (int) ((view.getMax_num_of_orders() - view.getMin_num_of_orders())/ number_of_desc);
        int mod = x_axis_gap % 10;
        x_axis_gap = x_axis_gap - mod;
        
        while(begin_number % 10 != 0)
        {
        	begin_number ++;
        	x_line_x_desc_beg += x_line_desc_unit;
        }
        
        //watrtosci na osi x
        	//linie i liczby
	        int x_axis_num_font_size = (int)(panel_height/40.0);
	        g2d.setFont(new Font(axis_font, axis_font_style, x_axis_num_font_size));
	        
	        int number = begin_number;
	        int x = x_line_x_desc_beg;
	        while(number<view.getMax_num_of_orders())
	        {
	        	
	        	g2d.drawLine(x, x_line_y_beg, x, x_line_y_beg+panel_height/50);
	        	g2d.drawString(number+"", x + panel_width/300, x_line_y_beg+panel_height/40);
	        	
	        	number += x_axis_gap;
	        	x+= x_line_desc_unit * x_axis_gap;
	        	
	        }
	        
	        g2d.drawLine(x, x_line_y_beg, x, x_line_y_beg+panel_height/50);
        	g2d.drawString(number+"", x + panel_width/300, x_line_y_beg+panel_height/40);
		        
		        
		        
		  //rysowanie opisu osi x
		        g2d.setColor(Color.orange);
		        
		        //wyliczanie rozmiaru czcionki
		        int x_axis_font_size = (int)((panel_height/1000.0)*(x_axis_max_font_size-x_axis_min_font_size)+x_axis_min_font_size);
		        
		        g2d.setFont(new Font(axis_font, axis_font_style, x_axis_font_size));
		        //rysowanie stringa
		        g2d.drawString("Liczba zleceń", x_line_x_end+panel_width/25, x_line_y_end + panel_height/17);
    }
    
    
    /**
     * Rysuje oś Y
     * @param g2d Graphics2D do rysowania
     */
    private void drawYAxle(Graphics2D g2d)
    {
    	//grubość, zakończenie, łączenia
        BasicStroke bs2 = new BasicStroke(line_thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
        g2d.setStroke(bs2);
        g2d.drawLine(y_line_x_beg, y_line_y_beg-panel_height/10, y_line_x_end, y_line_y_end);
        
        int y_line_y_desc_beg = y_line_y_end;
        double y_line_desc_unit = ((y_line_y_end - y_line_y_beg) / (view.getMax_cost() - view.getMin_cost()));
        int begin_number = (int)(view.getMin_cost());
        int y_axis_num_gap = (int) ((view.getMax_cost() - view.getMin_cost())/ number_of_desc);
        int mod = y_axis_num_gap % 10;
        y_axis_num_gap = y_axis_num_gap - mod;
        
        while(begin_number % 10 != 0)
        {
        	begin_number ++;
        	y_line_y_desc_beg -= y_line_desc_unit;
        }
        
        //watrtosci na osi y
        	//linie i liczby
	        int y_axis_num_font_size = (int)(panel_height/40.0);
	        g2d.setFont(new Font(axis_font, axis_font_style, y_axis_num_font_size));
	        
	        int number = begin_number;
	        int y = y_line_y_desc_beg;

        	g2d.setFont(new Font(axis_font, axis_font_style, y_axis_num_font_size));
        	String number_y;
        	FontMetrics metrics = g2d.getFontMetrics();
        	
	        while(number<view.getMax_cost())
	        {
	        	
	        	g2d.drawLine(y_line_x_end,y, y_line_x_end - panel_width/50, y);

    			//wartosc
    			number_y = ((int)number)+"";
	        	 //pobieranie wymiarow na wizualizacji
	        	 Rectangle2D rectan = metrics.getStringBounds(number_y, g2d);
	        	 double font_width =   rectan.getWidth();
	        	 g2d.drawString(number_y,  y_line_x_end - (int)font_width - panel_width/100, y - panel_height/100);
	        	
	        	 number += y_axis_num_gap;
		         y-= y_line_desc_unit * y_axis_num_gap;
	        }
	        
	        
	        g2d.drawLine(y_line_x_end,y, y_line_x_end - panel_width/50, y);

			//wartosc
			number_y = ((int)number)+"";
        	 //pobieranie wymiarow na wizualizacji
        	 Rectangle2D rectan = metrics.getStringBounds(number_y, g2d);
        	 double font_width =   rectan.getWidth();
        	 g2d.drawString(number_y,  y_line_x_end - (int)font_width - panel_width/100, y - panel_height/100);
	        
        //rysowanie opisu osi y
        		g2d.setColor(Color.orange);
		        //wyliczanie rozmiaru czcionki
		        int y_axis_font_size = (int)((panel_width/1000.0)*(y_axis_max_font_size-y_axis_min_font_size)+y_axis_min_font_size);
		       
		        g2d.setFont(new Font(axis_font, axis_font_style, y_axis_font_size));
		       
		        
		        //ustawienie poczatku ukladu wspolrzednych
		        g2d.translate( y_line_x_beg, y_line_y_beg);
		        //obrot
		        g2d.rotate(Math.toRadians(-90));
		        //rysowanie stringa (wg przeksztalcen)
		        g2d.drawString("Koszt", panel_height/30 , -panel_height/13);
		             
      
    }
    
    
    /**
     * Rysowanie gortu osi X
     * @param g2d Graphics2D do rysowania
     * @param p0 obiekt graficzny grotu
     */
    private void drawXArrowHead(Graphics2D g2d, GeneralPath p0)
    {  
        //ustawienie poczatku ukladu wspolrzednych
        g2d.translate(x_line_x_end+panel_width/10, x_line_y_end);
        //obrot
        g2d.rotate(Math.toRadians(90));
        //rysowanie stringa (wg przeksztalcen)
        g2d.fill(p0);
        
    }
    
    
    /**
     * Rysowanie grotu osi Y
     * @param g2d Graphics2D do rysowania
     * @param p0 obiekt graficzny grotu
     */
    private void drawYArrowHead(Graphics2D g2d, GeneralPath p0)
    {
        //ustawienie poczatku ukladu wspolrzednych
        g2d.translate(y_line_x_beg, y_line_y_beg-panel_height/10);
        //rysowanie stringa (wg przeksztalcen)
        g2d.fill(p0);
    }
    
    /**
     * zczytanie i ustawienie wymiarów panelu rysowania
     */
    private void setPanelBounds()
    {
    	//wymiary płótna
        panel_height = this.getHeight();
        panel_width = this.getWidth();
    }
    

    
    
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);  
        		        
        setPanelBounds(); //zczytanie wymiarow panelu
        
        Graphics2D g2d = (Graphics2D) g;
        
        drawAxles(g2d); //rysowanie osi
        
        drawTransportersObjects(g2d); //rysowanie obiektow przewoznikow              
        
    }

    ///////////////////listenery//////////////////////////////////////////////////////////////////////
    
	@Override
	public void mouseClicked(MouseEvent e) 
	{
		ArrayList<Transporter> clicked = new ArrayList<>();
		
		for(Shape s: drawnShapes)
		{
			if(s.contains(e.getX(), e.getY()))
			{
				clicked.add(view.getTransporters().get(drawnShapes.indexOf(s)));
			}
		}
		
		Transporter t = null;
		
		
		if(clicked.size() ==1 )
		{
			t = clicked.get(0);
			
		}
		else if(clicked.size() > 1 )
		{
			t = (Transporter) JOptionPane.showInputDialog(this, "Wybrałeś więcej niż jedngo przewożnika.\nWybierz jednego z poniższych:", "Wybór przewoźnika", JOptionPane.PLAIN_MESSAGE,
			        null, clicked.toArray(),clicked.get(0));
			
		}
		
		if(t!=null)
		{
			final Transporter tr = t;
			
			if(openTransportersJFrames.containsKey(t.getId_trans()))
			{
				JFrame trans_details_jframe = openTransportersJFrames.get(t.getId_trans());
				java.awt.EventQueue.invokeLater(new Runnable() 
				{
				    @Override
				    public void run() 
				    {
				    	trans_details_jframe.toFront();
				    	trans_details_jframe.repaint();
				    }
				});
			}
			else
			{
				JFrame trans_details_jframe = view.showTransporterDetails(t.getId_trans());
				
				trans_details_jframe.addWindowListener(new WindowListener() {
		            public void windowClosed(WindowEvent arg0) {
		            	openTransportersJFrames.remove(tr.getId_trans(),trans_details_jframe);
		            }
		            public void windowActivated(WindowEvent arg0) {
		            }
		            public void windowClosing(WindowEvent arg0) {
		            }
		            public void windowDeactivated(WindowEvent arg0) {
		            }
		            public void windowDeiconified(WindowEvent arg0) {
		            }
		            public void windowIconified(WindowEvent arg0) {
		            }
		            public void windowOpened(WindowEvent arg0) {
		            }
		        });
				
				openTransportersJFrames.put(t.getId_trans(), trans_details_jframe);
				view.setChosenTransporter(t);
			}
		}
	}

	
	@Override
	public void mouseMoved(MouseEvent e) {
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