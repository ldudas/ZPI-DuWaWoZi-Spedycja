package interfaces;


import java.util.Map;

import visualisations.*;


public class RoutePlanningPresenter 
{
	private RoutePlanningModel route_planning_model;
	private RoutePlanningView route_planning_view;
	private VisualistaionManufacturersPresenter manu_presenter;
	private VisualisationPathPresenter path_presenter;
	
	public RoutePlanningPresenter(final RoutePlanningView view,final RoutePlanningModel model,final VisualistaionManufacturersPresenter map, VisualisationPathPresenter path_p)
	{
		route_planning_model = model;
		route_planning_view = view;
		manu_presenter = map;
		path_presenter = path_p;
	}
	
	
	/**
	 * Zmienia widok ze starowego okna na okno z wizualizacja producentow 
	 * oraz wizulaizacja trasy 
	 */
	public void changeView()
	{
		startManuVisualisation();
		route_planning_view.change_start_to_map();
	}
	
	/**
	 * W zaleznosci od stanu, wyswietla informacje o wybranym producencie w nowym oknie lub pokazuje informacje
	 * o bledzie zaznaczenia producentow( zaznaczono wiecej niz jednego)
	 * @author Kamil Zimny
	 */
	public void showManufacturerInfo()
	{
		Map<String, Object> attributes = manu_presenter.getAttributeOfSelectedManufacturers();

		if( attributes != null )
<<<<<<< HEAD
=======
		{
			manu_presenter.clearSelection();
>>>>>>> branch 'master' of https://github.com/ldudas/ZPI-DuWaWoZi-Spedycja.git
			route_planning_view.show_manfacturerInfo(attributes);

		else
			route_planning_view.show_ErrorMessage();		
	}
	
	public void removeLastCity()
	{
		path_presenter.removeLastCity();
	}
	
	
	/**
	 * Pokaz wizualizacje trasy     
	 * @author £ukasz Dudaszek
	 */
	public void showPathMap()
	{
		path_presenter.startPathVisualisation();
	}
	
	/**
	 * Zamyka okno informajci o producencie
	 * @author Kamil Zimny
	 */
	public void closeManufacturerInfo()
	{
		route_planning_view.closeManufacturerInfoFrame();
	}
	 
	/**
	 * Uruchamia metodê prezentera wizualizacji producentów 
	 * startuj¹c¹ wizualizacjê producentów
	 */
	public void startManuVisualisation()
	{
		manu_presenter.startManufacturersVisualisation(route_planning_view.city_to());
	}
	
	public void send_nextCityNameAfterConfirm()
	{
<<<<<<< HEAD
		map_presenter.startManufacturersVisualisation(route_planning_view.city_nextCityAfterComfirm());		
=======
		manu_presenter.clearSelection();
		manu_presenter.startManufacturersVisualisation(route_planning_view.city_nextCityAfterComfirm());		
>>>>>>> branch 'master' of https://github.com/ldudas/ZPI-DuWaWoZi-Spedycja.git
		route_planning_view.changeTabOfMap();
	}
	
	public RoutePlanningView return_view()
	{
		return route_planning_view;
	}
	
	
	/**
	 * Utwórz w modelu path model mape z pierwszymi dwoma miastami   
	 * @author £ukasz Dudaszek
	 */
	public void createInitialPathMap()
	{
		//pobierz miasto starowe z widoku
		String city_to = route_planning_view.city_to();
		//pobierz miasto docelowe z widoku
		String city_from = route_planning_view.city_from();
		//Utwórz w modelu path model mape z pierwszymi dwoma miastami  
		path_presenter.createInitialMap(city_from, city_to);
	}
}
