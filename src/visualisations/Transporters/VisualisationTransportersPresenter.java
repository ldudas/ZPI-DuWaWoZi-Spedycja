package visualisations.Transporters;

import java.util.ArrayList;

import javax.swing.JFrame;

import com.esri.map.JMap;

import dataModels.SizeCategory;
import dataModels.Transporter;
import dataModels.User;

public class VisualisationTransportersPresenter 
{
	
	/**
	 * model wizualizacji przewoznikow
	 */
	private VisualisationTransportersModel model_transporters;
	
	/**
	 * widok wizualizacji przewoznikow
	 */		
	private VisualisationTransportersView view_transporters;
	
	
	public VisualisationTransportersPresenter(final VisualisationTransportersView view, final VisualisationTransportersModel model)
	{
		model_transporters = model;
		view_transporters = view;
	}
	
	public void drawTransporters(String city_from, String city_to, SizeCategory sc)
	{
		ArrayList<Transporter> transporters = model_transporters.getFilteredTransporters(city_from,city_to,sc);
		
		model_transporters.setMaxAndMinsOfTransportersProperties();
		
		view_transporters.drawTransporters(transporters);
	}
	
	public void showTransporterDetails(int id_trans)
	{
		Transporter t = model_transporters.getTransporter(id_trans);
		JMap routes_of_transporter = model_transporters.getTransporterRoutesMap(id_trans) ;
		view_transporters.showTransporterDetailsWindow(t,routes_of_transporter);
	}
	
	public void startTransportersVisualization_inNewFrame(JFrame mainFrame)
	{
		view_transporters.initialize(mainFrame);
	}
	

	public void setExternalDatabaseConnectionProperty(User currentLoggedUser) throws Exception
	{
		model_transporters.setExternalDatabaseConnectionProperty(currentLoggedUser);
	}
	
	public void clearTransportersFrame()
	{
		view_transporters.clearCarrierVisualizationFrame();
	}
	
	public void clearDataInModel()
	{
		model_transporters.clearData();
	}
	
	public double getMax_num_of_orders()
	{
		return model_transporters.getMax_num_of_orders();
	}


	public double getMin_num_of_orders()
	{
		return model_transporters.getMin_num_of_orders();
	}


	public double getMax_cost()
	{
		return model_transporters.getMax_cost();
	}


	public double getMin_cost()
	{
		return model_transporters.getMax_cost();
	}


	public double getMax_volume()
	{
		return model_transporters.getMax_volume();
	}

	public double getMax_capacity()
	{
		return model_transporters.getMax_capacity();
	}


}
