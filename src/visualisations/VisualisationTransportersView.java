package visualisations;


import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridLayout;


public class VisualisationTransportersView 
{
	
		Ellipse2D oval;
	
	    public VisualisationTransportersView () 
	    {
	        /*System.out.println("Created GUI on EDT? "+
	        SwingUtilities.isEventDispatchThread());*/
	        JFrame f = new JFrame("Wizualizacja przewoźników");
	        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        f.getContentPane().add(new MyPanel());
	        
	        //f.pack();
	        f.setVisible(true);
	        f.setSize(700, 500);
	        //f.setResizable(false);
	       
	    }
	    
	    
	    class MyPanel extends JPanel  implements MouseListener
	    {
	    	private int panel_height;
	    	private int panel_width;
	    	private int x_line_x;
	    	private int x_line_y;
	    	private int x_line_gap;

		    /*public MyPanel()
		    {
		        setBorder(BorderFactory.createLineBorder(Color.red));
		    }*/
	    	
		    public Dimension getPreferredSize() 
		    {
		        return new Dimension(700,500);
		    }

		    public void paintComponent(Graphics g) 
		    {
		        super.paintComponent(g);  
		        
		        panel_height = this.getHeight();
		        panel_width = this.getWidth();
		        
		        
		        System.out.println(panel_width+" "+panel_height);
		        
		        Graphics2D g2d = (Graphics2D) g;
		        
		        
		        
		        oval = new Ellipse2D.Double(panel_width/2, panel_height/2, 100, 100);
		        
		        g2d.fill(oval);
		        
		        this.addMouseListener(this);

		      
		        
		    }

			@Override
			public void mouseClicked(MouseEvent e) {
				
				if (oval.contains(e.getX(), e.getY()) ) {
				      //repaint();
				     System.out.println("Klikni�to na oval");
				   }
				else
				   {
					   System.out.println("Klikni�to poza ovalem");
				   }
				
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
				
				if (oval.contains(e.getX(), e.getY()) ) {
				      //repaint();
				     System.out.println("Trzymany przycisk nad ovalem");
				   }
				else
				   {
					   System.out.println("Trzymany przycisk poza ovalem");
				   }
			}

			@Override
			public void mouseReleased(MouseEvent e)
			{
				
				if (oval.contains(e.getX(), e.getY()) ) 
				{
				      //repaint();
				     System.out.println("Zwolniony przycisk nad ovalem");
				   }
				else
				   {
					   System.out.println("Zwolniony przycisk poza ovalem");
				   }
			}  
			
		}
	    
}

	

