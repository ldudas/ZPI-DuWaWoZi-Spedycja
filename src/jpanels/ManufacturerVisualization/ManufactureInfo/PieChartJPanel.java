package jpanels.ManufacturerVisualization.ManufactureInfo;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

import dataModels.Manufacturer;

import java.awt.CardLayout;
import java.util.ArrayList;

public class PieChartJPanel extends JPanel
{

  private static final long serialVersionUID = 1L;
  private JFreeChart chart;
  private ArrayList<String> quarters;

  public PieChartJPanel() 
  {
	    createQuartesName();
        PieDataset dataset = createDataset();
        createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(232, 162));
	    setLocation(10, 10);
	    setSize(232, 162);
	    setBorder(new LineBorder(Color.BLACK, 5, false));
        setLayout(new CardLayout(0, 0));
        add(chartPanel, "name_6269072302659");
   }
  
  
  private void createQuartesName()
  {
	  quarters = new ArrayList<String>(4);
	  
	  quarters.add("Wio");
	  quarters.add("Lat");
	  quarters.add("Jes");
	  quarters.add("Zim"); 
  }
    
/**
     * Creates a sample dataset 
     */

    private  PieDataset createDataset() 
    {
        DefaultPieDataset monthChar = new DefaultPieDataset();
        
        for( String q : quarters)
        	monthChar.setValue(q, 30);  
        
        return monthChar;       
    }
    
    public void setColors(final Manufacturer manufacturer)
    {
    	PiePlot3D plot = (PiePlot3D) chart.getPlot();
  	  
  	  for(int i= 0 ; i<quarters.size() ;i++)
  		  plot.setSectionPaint(quarters.get(i), manufacturer.getQuarterActivityColor(i));

    }
    
/**
     * Creates a chart
     */

    private void createChart(PieDataset dataset) 
    {
        chart = ChartFactory.createPieChart3D("Aktywność kwartalna",dataset,false,true,false);
        
        chart.setBackgroundPaint(SystemColor.inactiveCaptionText); 
        chart.getTitle().setPaint(new Color(255, 204, 0));

        PiePlot3D plot = (PiePlot3D) chart.getPlot(); 
        plot.setBackgroundPaint(SystemColor.activeCaption);
        plot.setLabelFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
        plot.setLabelPaint(new Color(255, 204, 0));
        plot.setLabelLinkMargin(0.01);
        plot.setLabelBackgroundPaint(SystemColor.inactiveCaptionText);
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.9f);     
    }
    
 
}