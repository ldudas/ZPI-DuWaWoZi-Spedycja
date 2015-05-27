package jpanels.ManufacturerVisualization.ManufactureInfo;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;




public class PieChartJPanel extends JPanel
{

  private static final long serialVersionUID = 1L;

  public PieChartJPanel() 
  {

        PieDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(377, 250));
        add(chartPanel);
   }
  
  
  
    
    
/**
     * Creates a sample dataset 
     */

    private  PieDataset createDataset() 
    {
    	
        DefaultPieDataset result = new DefaultPieDataset();

        result.setValue("Zima", 30);
        result.setValue("Wiosna", 30);
        result.setValue("Lato", 30);
        result.setValue("Jesień", 30);
        
        return result;       
    }
    
  /*  public void setColors(final Manufacturer manufacturer)
    {
  	  PiePlot3D plot = (PiePlot3D) chart.getPlot();
  	  
  	  for(int i= 0 ; i<12 ;i++)
  		  plot.setSectionPaint(months.get(i), manufacturer.getMonthActivityColor(i));

    }*/
    
/**
     * Creates a chart
     */

    private JFreeChart createChart(PieDataset dataset) 
    {
        
        JFreeChart chart = ChartFactory.createPieChart3D("",dataset,false,true,false);
        
        chart.setBackgroundPaint(SystemColor.inactiveCaptionText);
     

        PiePlot3D plot = (PiePlot3D) chart.getPlot(); 
        plot.setBackgroundPaint(SystemColor.activeCaption);
        plot.setLabelFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
        plot.setLabelPaint(new Color(255, 204, 0));
        plot.setLabelLinkMargin(0.01);
        plot.setLabelBackgroundPaint(SystemColor.inactiveCaptionText);
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.9f);
        
        
        return chart;
        
    }
    
    public static void main(String[] args) 
    {
        PieChartJPanel pieChart = new PieChartJPanel();
        JFrame frame = new JFrame();
        frame.setBounds(100, 100, 500, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.add(pieChart);
      //  demo.pack();
       // demo.setVisible(true);
    }
 
}