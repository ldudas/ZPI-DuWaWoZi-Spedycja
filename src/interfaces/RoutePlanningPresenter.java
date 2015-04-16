package interfaces;

import com.esri.map.JMap;
import maps.VMVPresenter;



public class RoutePlanningPresenter 
{
	private RoutePlanningModel route_planning_model;
	private RoutePlanningView route_planning_view;
	private VMVPresenter map_presenter;
	
	public RoutePlanningPresenter(final RoutePlanningView view,final RoutePlanningModel model,final VMVPresenter map)
	{
		route_planning_model = model;
		route_planning_view = view;
		map_presenter=map;
	}
	
	public void changeView(){
		send_city_name();
		route_planning_view.change_start_to_map();
	}
	
	public void send_city_name(){
		map_presenter.startManufacturersVisualisation(route_planning_view.city_to());
	}
	
	public RoutePlanningView return_view(){
		return route_planning_view;
	}
}