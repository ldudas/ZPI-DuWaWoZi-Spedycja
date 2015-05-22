package jpanels.ManufacturerVisualization.ManufactureInfo;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.RingPlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;


public class RingChartJPanel extends JPanel
{

  private static final long serialVersionUID = 1L;
  
  private JFreeChart chart;
  private ArrayList<String> months;

  public RingChartJPanel() 
  {
	  	createMonthName();
        // This will create the dataset 
        PieDataset dataset = createDataset();
        // based on the dataset we create the chart
        JFreeChart chart = createChart(dataset);
        // we put the chart into a panel
        ChartPanel chartPanel = new ChartPanel(chart);
        // default size
        chartPanel.setPreferredSize(new java.awt.Dimension(600, 370));       
       // chartPanel.addChartMouseListener(listener);
        // add it to our application
        add(chartPanel);

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
  
  public void setColors(ArrayList<Color> colors)
  {
	  RingPlot plot = (RingPlot) chart.getPlot();
	  
	  for(int i= 0 ; i<colors.size() ;i++)
		  plot.setSectionPaint(months.get(i), colors.get(i));

  }
        
  	/**
     * Creates a sample dataset 
     */

    private PieDataset createDataset() 
    {
    	
        DefaultPieDataset monthChar = new DefaultPieDataset();
        
        for( String m : months)
        	monthChar.setValue(m, 30);  
        
        return monthChar;
        
    }
       
    /**
     * Creates a chart
     */

    private JFreeChart createChart(PieDataset dataset) 
    {
        
        chart = ChartFactory.createRingChart("",          // chart title
            dataset,                // data
            false,                   // include legend
            true,
            false);

        RingPlot plot = (RingPlot) chart.getPlot();
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
        RingChartJPanel pieChart = new RingChartJPanel();
        JFrame frame = new JFrame();
        frame.setBounds(100, 100, 500, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        ArrayList<Color> colors = new ArrayList<Color>();
        
        colors.add(Color.RED);
        colors.add(Color.blue);
        colors.add(Color.RED);
        colors.add(Color.blue);
        colors.add(Color.RED);
        colors.add(Color.blue);
        colors.add(Color.RED);
        colors.add(Color.blue);
        colors.add(Color.RED);
        colors.add(Color.blue);
        colors.add(Color.RED);
        colors.add(Color.blue);
        
        pieChart.setColors(colors);
        
        frame.add(pieChart);
    }
}