package interfaces;


import java.util.Map;

import visualisations.*;


public class RoutePlanningPresenter 
{
	private RoutePlanningModel route_planning_model;
	private RoutePlanningView route_planning_view;
	private VisualistaionManufacturersPresenter map_presenter;
	private VisualisationPathPresenter path_presenter;
	
	public RoutePlanningPresenter(final RoutePlanningView view,final RoutePlanningModel model,final VisualistaionManufacturersPresenter map, VisualisationPathPresenter path_p)
	{
		route_planning_model = model;
		route_planning_view = view;
		map_presenter = map;
		path_presenter = path_p;
	}
	
	public void changeView()
	{
		send_city_name();
		route_planning_view.change_start_to_map();
	}
	
	/**
	 * W zaleznosci od stanu, wyswietla informacje o wybranym producencie w nowym oknie lub pokazuje informacje
	 * o bledzie zaznaczenia producentow( zaznaczono wiecej niz jednego)
	 * @author Kamil Zimny
	 */
	public void showManufacturerInfo()
	{
		Map<String, Object> attributes = map_presenter.getIdOfSelectedManufacturers();

		if( attributes != null )
			route_planning_view.show_manfacturerInfo(attributes);
		else
			route_planning_view.show_ErrorMessage();			
	}
	
	public void showPathMap()
	{
		path_presenter.showPathMap();
	}
	
	/**
	 * Zamyka okno informajci o producencie
	 * @author Kamil Zimny
	 */
	public void closeManufacturerInfo()
	{
		route_planning_view.closeManufacturerInfoFrame();
	}
	
	public void send_city_name()
	{
		map_presenter.startManufacturersVisualisation(route_planning_view.city_to());
	}
	
	public RoutePlanningView return_view()
	{
		return route_planning_view;
	}
	
	public void createInitialPathMap()
	{
		String city_to = route_planning_view.city_to();
		String city_from = route_planning_view.city_from();
		path_presenter.createInitialMap(city_from, city_to);
	}
}