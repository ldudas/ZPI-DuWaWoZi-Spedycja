package visualisations;


import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import dataModels.*;


public class VisualisationTransportersView 
{
	private MyPanel mp;
	private ArrayList<Transporter> transporters;
	private ArrayList<Shape> drawnShapes;
	private int max_capacity;
	
	    public VisualisationTransportersView () 
	    {
	        /*System.out.println("Created GUI on EDT? "+
	        SwingUtilities.isEventDispatchThread());*/
	        JFrame f = new JFrame("Wizualizacja przewoźników");
	        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        mp = new MyPanel();
	        mp.addMouseListener(mp);
	        f.getContentPane().add(mp);
	        
	        //f.pack();
	        f.setVisible(true);
	        f.setSize(700, 500);
	        //f.setResizable(false);
	        
	        drawnShapes = new ArrayList<Shape>();
	       
	    }
	    
	    public void drawTransporters(ArrayList<Transporter> transporters)
	    {
	    	this.transporters = transporters;
	    	max_capacity = transporters.get(0).getCapacity();
	    	mp.repaint();
	    }
	    
	    
	    class MyPanel extends JPanel  implements MouseListener
	    {
	    	
	    	 private   int x_gap = 20;
		     private   int y_gap = 20;
		     private   int line_thickness = 5;
	    	

		    public MyPanel()
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
		        int panel_height = this.getHeight();
		        int panel_width = this.getWidth();
		        
		        //poczatek osi x - x
		        int x_line_x_beg = panel_width / x_gap;
		        //poczatek osi x - y
		        int x_line_y_beg = panel_height - (panel_height / y_gap);
		        //koniec osi x - x
		        int x_line_x_end = panel_width - (panel_width/x_gap);
		        //koniec osi x - y
		        int x_line_y_end = x_line_y_beg;
		        
		        
		        //poczatek osi y - x
		        int y_line_x_beg = x_line_x_beg;
		        //poczatek osi y - y
		        int y_line_y_beg = panel_height / y_gap;
		        //koniec osi y - x
		        int y_line_x_end = x_line_x_beg;
		        //koniec osi y - y
		        int y_line_y_end = x_line_y_beg;
		        
		       
		        
		        Graphics2D g2d = (Graphics2D) g;
		   
		        //oś x
		        BasicStroke bs1 = new BasicStroke(line_thickness, BasicStroke.CAP_ROUND,
		                BasicStroke.JOIN_BEVEL);
		        g2d.setStroke(bs1);
		        g2d.drawLine(x_line_x_beg, x_line_y_beg, x_line_x_end, x_line_y_end);
		        
		        //oś y
		        BasicStroke bs2 = new BasicStroke(line_thickness, BasicStroke.CAP_ROUND,
		                BasicStroke.JOIN_BEVEL);
		        g2d.setStroke(bs2);
		        g2d.drawLine(y_line_x_beg, y_line_y_beg, y_line_x_end, y_line_y_end);
		        
		        //punkt odniesienia jako poczatek ukladu wspolrzednych
		       g2d.translate(x_line_x_beg, x_line_y_beg);
		        
		        //
		        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
		                RenderingHints.VALUE_ANTIALIAS_ON);

		        rh.put(RenderingHints.KEY_RENDERING,
		                RenderingHints.VALUE_RENDER_QUALITY);

		        g2d.setRenderingHints(rh);

		        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
		                1f));
		        //
		        
		        int x_obj;
		        drawnShapes.clear();
		        for(Transporter t:transporters)
		        {
		        	x_obj = (t.getCapacity()/max_capacity) * (x_line_x_end - x_line_x_beg);
		        	System.out.println(t.getCapacity()+" - "+x_obj+ " "+ x_line_x_end+ " " + x_line_x_beg+ " "+ max_capacity);
		        
		        	
		        	g2d.setColor(new Color(255,0,0));
		        	Rectangle2D rect = new Rectangle2D.Double(x_obj, -100, 10, 10);
		        	drawnShapes.add(rect);
		        	g2d.fill(rect);
		        };
		        
		    }

			@Override
			public void mouseClicked(MouseEvent e) {
				
			/*	if (oval.contains(e.getX(), e.getY()) ) {
				      //repaint();
				     System.out.println("Klikni�to na oval");
				   }
				else
				   {
					   System.out.println("Klikni�to poza ovalem");
				   }*/
				
			}

			@Override
			public void mouseEntered(MouseEvent e) 
			{
				//mp.repaint();
			
			}

			@Override
			public void mouseExited(MouseEvent e) 
			{
			
				      //repaint();
				     System.out.println("Myszka poza p��tnem");
				   
			}

			@Override
			public void mousePressed(MouseEvent e) 
			{
				
				/*if (oval.contains(e.getX(), e.getY()) ) {
				      //repaint();
				     System.out.println("Trzymany przycisk nad ovalem");
				   }
				else
				   {
					   System.out.println("Trzymany przycisk poza ovalem");
				   }*/
			}

			@Override
			public void mouseReleased(MouseEvent e)
			{
				
/*				if (oval.contains(e.getX(), e.getY()) ) 
				{
				      //repaint();
				     System.out.println("Zwolniony przycisk nad ovalem");
				   }
				else
				   {
					   System.out.println("Zwolniony przycisk poza ovalem");
				   }*/
			}  
			
		}
	    
}

	

