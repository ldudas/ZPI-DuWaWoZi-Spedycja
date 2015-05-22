package visualisations.Transporters;

import java.util.ArrayList;

import dataModels.SizeCategory;

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

}
