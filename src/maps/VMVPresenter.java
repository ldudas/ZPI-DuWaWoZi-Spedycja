package maps;

import interfaces.RoutePlanningPresenter;

public class VMVPresenter {
	
	private VMVModel model_ManufacturersVis;
	private VMVView view_ManufacturersVis;
	private RoutePlanningPresenter route_planning_presenter;
	
	public VMVPresenter(final VMVView view,final VMVModel model)
	{
		model_ManufacturersVis = model;
		view_ManufacturersVis = view;
		
	}
	
	public void set_route_presenter(final RoutePlanningPresenter presenter){
		route_planning_presenter = presenter;
	}
	
	/**
	 * Metoda ustawiajaca widokowi mape pobrana z modelu.
	 * @author Kamil Zimny
	 */
	public void startManufacturersVisualisation(final String cityName)
	{
		view_ManufacturersVis.set_tab(route_planning_presenter.return_view().returnMap().return_tab());
		view_ManufacturersVis.add_map_to_tab(model_ManufacturersVis.getMapWithVisualisationManufacturersInCity(cityName));
		
		route_presenter.add_map(model_ManufacturersVis.getMapWithVisualisationManufacturersInCity(cityName)); //powinien sie odwolac do vmvview
		
	}

}
