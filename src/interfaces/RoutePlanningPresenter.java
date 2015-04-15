package interfaces;

import com.esri.map.JMap;
import maps.VMVPresenter;



public class RoutePlanningPresenter 
{
	private RoutePlanningModel model_ManufacturersVis;
	private RoutePlanningView view_ManufacturersVis;
	private VMVPresenter map_presenter;
	
	public RoutePlanningPresenter(final RoutePlanningView view,final RoutePlanningModel model,final VMVPresenter map)
	{
		model_ManufacturersVis = model;
		view_ManufacturersVis = view;
		map_presenter=map;
	}
	
	public void changeView(){
		send_city_name();
		view_ManufacturersVis.change_start_to_map();
	}
	
	public void send_city_name(){
		map_presenter.startManufacturersVisualisation(view_ManufacturersVis.city_to());
	}
	
	public void add_map(JMap map_to_set){
		view_ManufacturersVis.returnMap().startuj(map_to_set);
	}
	
	
}