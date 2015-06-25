package visualisations.Transporters;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import dataModels.*;

import com.esri.map.JMap;

import jpanels.TransportersVisualisation.TransporterInfo.TransporterDetailsJPanel;


public class VisualisationTransportersView 
{
	private VisualisationTransportersPresenter trans_presenter;
	
	private int lastWindowPos;
	
	private JFrame carrierVisualizationFrame;
	private TransVisInterfaceJPanel transVisInterfaceJPanel;
	
	private boolean isVisualisationStarted;
	private ArrayList<Transporter> transporters;
	private double max_num_of_orders;
	private double min_num_of_orders;
	private double max_cost;
	private double min_cost;
	private double max_volume;
	private double max_capacity;



	public VisualisationTransportersView() 
	{
		isVisualisationStarted = false;
		//initialize();
		lastWindowPos = 50;
	}
	

	public void setPresenter(VisualisationTransportersPresenter pres)
	{
		trans_presenter = pres;
	}
	
	public void clearCarrierVisualizationFrame()
	{
		if(transVisInterfaceJPanel != null)
			carrierVisualizationFrame.remove(transVisInterfaceJPanel);
		isVisualisationStarted = false;
		//initialize();
		lastWindowPos = 50;
	}

	

	public void initialize( JFrame carrierV) 
	{		
	//	carrierVisualization = new JFrame();
		this.carrierVisualizationFrame = carrierV;
	//	carrierVisualization.setIconImage(Toolkit.getDefaultToolkit().getImage(View.class.getResource("/images/025581022.jpg")));
	//	carrierVisualizationFrame.setResizable(false);
		carrierVisualizationFrame.setTitle("Przewo\u017Anicy");
	//	carrierVisualizationFrame.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
	//	carrierVisualizationFrame.getContentPane().setBackground(SystemColor.inactiveCaption);
	//	carrierVisualizationFrame.getContentPane().setForeground(SystemColor.textHighlightText);
	//	carrierVisualizationFrame.getContentPane().setLayout(null);
		
	    transVisInterfaceJPanel = new TransVisInterfaceJPanel(this,trans_presenter.getOpeningFlag());
	    carrierVisualizationFrame.add(transVisInterfaceJPanel);
		
	//	carrierVisualizationFrame.setForeground(SystemColor.menu);
	//	carrierVisualizationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//	carrierVisualizationFrame.setVisible(true);
	    
		carrierVisualizationFrame.setBounds(250, 30, 825, 725);
		carrierVisualizationFrame.invalidate();
		carrierVisualizationFrame.validate();
	}
	

	 public void drawTransporters(ArrayList<Transporter> transporters)
	    {
		 	if(!isVisualisationStarted)
		 	{
		 		transVisInterfaceJPanel.removeStartVisualisationPanel();
		 		transVisInterfaceJPanel.addVisualisation();
				isVisualisationStarted = true;
		 	}
		 	
	    	this.transporters = transporters;
	    	
	    	max_num_of_orders = Double.MIN_VALUE;
	    	min_num_of_orders = Double.MAX_VALUE;
	    	max_cost = Double.MIN_VALUE;
	    	min_cost = Double.MAX_VALUE;
	    	max_volume = Double.MIN_VALUE;
	    	max_capacity = Double.MIN_VALUE;
	    	
	    	transporters.stream().forEach( (Transporter tr) -> {
	    		if(tr.getNumber_of_orders()<min_num_of_orders) min_num_of_orders = tr.getNumber_of_orders();
	    		if(tr.getNumber_of_orders()>max_num_of_orders) max_num_of_orders = tr.getNumber_of_orders();
	    		if(tr.getCost()>max_cost) max_cost = tr.getCost();
	    		if(tr.getCost()<min_cost) min_cost = tr.getCost();
	    		if(tr.getVolume()>max_volume) max_volume = tr.getVolume();
	    		if(tr.getCapacity()>max_capacity) max_capacity = tr.getCapacity();	
	    	});
	    	
	    	transVisInterfaceJPanel.repaintVisualisation();
	    }
	 
	 public void drawTransporters(String city_from, String city_to, SizeCategory sc)
	 {
		 trans_presenter.drawTransporters(city_from, city_to, sc);
	 }
	 

	 public void showTransporterDetailsWindow(Transporter t,JMap routes)
	 {
				
				JFrame detailsWindow = new JFrame(t.getName() + " - " + (t.getSizeCategory() == SizeCategory.SMALL?"Małe":
																t.getSizeCategory() == SizeCategory.MEDIUM?"Średnie":
																											"Duże"));
				detailsWindow.setResizable(false);
				detailsWindow.setBounds(lastWindowPos, lastWindowPos, 910, 350);
				detailsWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				
				TransporterDetailsJPanel transDetailsPanel = new TransporterDetailsJPanel();
				transDetailsPanel.setInfoAboutTransporterInToList(t);
				transDetailsPanel.setRoutesMap(routes);
				detailsWindow.getContentPane().add(transDetailsPanel);
				detailsWindow.setVisible(true);		
				lastWindowPos += 25;
	 }
	 

		
		public void setChosenTransporter(Transporter t)
		{
			transVisInterfaceJPanel.setChosenTransporter(t);
			trans_presenter.setChosenTransporter(t);
		}
		
		public Transporter getChosenTransporter()
		{
			return trans_presenter.getChosenTransporter();
		}
		
		public ArrayList<String> getAllCityNames()
		{
			return trans_presenter.getAllCityNames();
		}
		
		public void saveOrdersToDatabase()
		{
			trans_presenter.saveOrdersToDatabase();
		}
	 
	 public void showTransporterDetails(int id_trans)
	 {
		 trans_presenter.showTransporterDetails(id_trans);
	 }
	 
	 public boolean showRepeatInsertionDialog()
	 {
		 return (JOptionPane.showConfirmDialog (null, "Nie wprowadzono nazwy trasy.\nCzy chcesz ponowić wprowadzenie?","Brak nazwy trasy",JOptionPane.YES_NO_OPTION)) == JOptionPane.YES_OPTION
				 ? true : false;
	 }

	public double getMax_num_of_orders()
	{
		return max_num_of_orders;
	}

	public double getMin_num_of_orders()
	{
		return min_num_of_orders;
	}

	public double getMax_cost()
	{
		return max_cost;
	}

	public double getMin_cost()
	{
		return min_cost;
	}

	public double getMax_volume()
	{
		return max_volume;
	}

	public double getMax_capacity()
	{
		return max_capacity;
	}

	public ArrayList<Transporter> getTransporters()
	{
		return transporters;
	}
	
	public void showDatabaseSaveError()
	{
		JOptionPane.showMessageDialog(null, "Nie można zapisać trasy do bazy danych", "Błąd zapisu", 
				JOptionPane.ERROR_MESSAGE);
	}
	
	public void showSaveSuccessDialog()
	{
		JOptionPane.showMessageDialog(null, "Pomyślnie zapisano trasę do bazy", "Zapisano", 
				JOptionPane.INFORMATION_MESSAGE);
	}
	
	public String showRouteNameDialog()
	{
		return JOptionPane.showInputDialog(null, "Podaj nazwę trasy: ","Nazwa trasy",JOptionPane.PLAIN_MESSAGE);
	}
	
	public void showNonUniqueRouteNameDialog()
	{
		JOptionPane.showMessageDialog(null, "Istnieje już w systemie trasa o podanej nazwie\nProszę wprowadzić inną.", "Nieunikalna nazwa trasy", 
				JOptionPane.ERROR_MESSAGE);
	}
	 
	    
}




	

