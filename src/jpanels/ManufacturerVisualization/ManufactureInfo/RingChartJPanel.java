package jpanels.ManufacturerVisualization.ManufactureInfo;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.RingPlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

import dataModels.Manufacturer;


public class RingChartJPanel extends JPanel
{

  private static final long serialVersionUID = 1L;
  
  private JFreeChart chart;
  private ArrayList<String> months;

  public RingChartJPanel() 
  {
	  	createMonthName();     

   }
  
  private void createMonthName()
  {
	  months = new ArrayList<String>();
	  
	  months.add("Sty");
      months.add("Lut");
      months.add("Mar");
      months.add("Kwi");
      months.add("Maj");
      months.add("Czer");
      months.add("Lip");
      months.add("Sie");
      months.add("Wrz");
      months.add("Paź");
      months.add("Lis");
      months.add("Gru");
  }
  
  private void setColors(final Manufacturer manufacturer)
  {
	  RingPlot plot = (RingPlot) chart.getPlot();
	  
	  for(int i= 0 ; i<months.size() ;i++)
		  plot.setSectionPaint(months.get(i), manufacturer.getMonthCostColor(i));

  }
  
  public void setManufacturerDataOnChart(final Manufacturer currentMan)
  {
  	  removeAll();
      PieDataset dataset = createDataset(currentMan);
      createChart(dataset);
      ChartPanel chartPanel = new ChartPanel(chart);
      setColors(currentMan);
      chartPanel.setPreferredSize(new java.awt.Dimension(232, 162));
      add(chartPanel, "name_6269072302659");
  }
        
  	/**
     * Creates a sample dataset 
     */

    private PieDataset createDataset(final Manufacturer currentMan) 
    {
    	
        DefaultPieDataset monthChar = new DefaultPieDataset();
        
        int i= 0;
        for( String m : months)
        {
        	monthChar.setValue(m, currentMan.getMonthActivity(i) );  
        	i++;
        }
        return monthChar;
        
    }
       
    /**
     * Creates a chart
     */

    private void createChart(PieDataset dataset) 
    {
        
        chart = ChartFactory.createRingChart("",          // chart title
            dataset,                // data
            false,                   // include legend
            true,
            false);
        
        chart.setBackgroundPaint(SystemColor.inactiveCaptionText);

        RingPlot plot = (RingPlot) chart.getPlot();
        plot.setBackgroundPaint(SystemColor.activeCaption);
        plot.setLabelFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 10));
        plot.setLabelPaint(new Color(255, 204, 0));
        plot.setLabelLinkMargin(0.01);
        plot.setLabelBackgroundPaint(SystemColor.inactiveCaptionText);
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.9f);
    }
    
  /*  public static void main(String[] args) 
    {
        RingChartJPanel pieChart = new RingChartJPanel();
        JFrame frame = new JFrame();
        frame.setBounds(100, 100, 500, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
      //  pieChart.setColors(colors);
        
        frame.add(pieChart);
    }
    */
}