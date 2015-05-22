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

        result.setValue("Styczeń", 30);
        result.setValue("Luty", 30);
        result.setValue("Marzec", 30);
        result.setValue("Kwiecień", 30);
        result.setValue("Maj", 30);
        result.setValue("Czerwiec", 30);
        result.setValue("Lipiec", 30);
        result.setValue("Sierpień", 30);
        result.setValue("Wrzesień", 30);
        result.setValue("Październik", 30);
        result.setValue("Listopad", 30);
        result.setValue("Grudzień", 30);
        
        
        return result;       
    }
    
    
/**
     * Creates a chart
     */

    private JFreeChart createChart(PieDataset dataset) 
    {
        
        JFreeChart chart = ChartFactory.createPieChart3D("",dataset,false,true,false);
        
        chart.setBackgroundPaint(SystemColor.inactiveCaptionText);
     

        PiePlot3D plot = (PiePlot3D) chart.getPlot(); 
        plot.setBackgroundPaint(SystemColor.activeCaption);
        plot.setLabelFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 10));
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