package visualisations.Transporters;

import dataModels.SizeCategory;
import dataModels.Transporter;

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
		view_transporters.drawTransporters(model_transporters.getFilteredTransporters(city_from,city_to,sc));
	}
	
	public void showTransporterDetails(int id_trans)
	{
		Transporter t = model_transporters.getTransporter(id_trans);
		view_transporters.showTransporterDetailsWindow(t);
	}
	
	public void startTransportersVisualization_inNewFrame()
	{
		view_transporters.initialize();
	}

}
