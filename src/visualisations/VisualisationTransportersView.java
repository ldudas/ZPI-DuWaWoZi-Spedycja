package visualisations;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class VisualisationTransportersView 
{
	
	    public VisualisationTransportersView () 
	    {
	        /*System.out.println("Created GUI on EDT? "+
	        SwingUtilities.isEventDispatchThread());*/
	        JFrame f = new JFrame("Wizualizacja przewoźników");
	        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        MyPanel mp = new MyPanel();
	        mp.addMouseListener(mp);
	        f.getContentPane().add(mp);
	        
	        //f.pack();
	        f.setVisible(true);
	        f.setSize(700, 500);
	        //f.setResizable(false);
	       
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
				     System.out.println("Myszka nad p��tnem");
			
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

	

