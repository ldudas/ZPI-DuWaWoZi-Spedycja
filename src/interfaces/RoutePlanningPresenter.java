package interfaces;

import builders.CityBuilder;
import builders.OrderBuilder;
import dataModels.City;
import dataModels.Manufacturer;
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
		Manufacturer manufacturer = manu_presenter.getAttributeOfSelectedManufacturers();

		if( manufacturer != null )
		{
			route_planning_view.show_manfacturerInfo(manufacturer);
			
		}
		else
			route_planning_view.show_ErrorMessage();			
	}
	
	public void removeLastCity()
	{
		path_presenter.removeLastCity();
	}
	
	public void addCityToPath()
	{
		String cityName = route_planning_view.getNameOfNextCity();
		CityBuilder cityBuilder = new CityBuilder();
		String [] coordinations = route_planning_model.getCityCoordinates(cityName);
		
		City city = cityBuilder.buildCity(cityName, coordinations[0], coordinations[1]);
		path_presenter.addCityToPath(city);
	}
	
	
	/**
	 * Pokaz wizualizacje trasy     
	 * @author Łukasz Dudaszek
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
	 * Uruchamia metodę prezentera wizualizacji producentów 
	 * startującą wizualizację producentów
	 */
	public void startManuVisualisation()
	{
		manu_presenter.startManufacturersVisualisation(route_planning_view.city_to());
	}
	
	public void send_nextCityNameAfterConfirm()
	{
		manu_presenter.startManufacturersVisualisation(route_planning_view.city_nextCityAfterComfirm());
		
	}
	
	public RoutePlanningView return_view()
	{
		return route_planning_view;
	}
	
	
	/**
	 * Utwórz w modelu path model mape z pierwszymi dwoma miastami   
	 * @author Łukasz Dudaszek
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
	
	public void markAsUnsuitable()
	{
		manu_presenter.markAsUnsuitable();
	}
	
	/**
	 * Metoda pokazujaca odpowiedni JPanel z zarzadzaniem okna
	 * 
	 */
	public void tabChanged()
	{
		int index = route_planning_view.getMapJPanel().getTabWithMaps().getSelectedIndex();
		if ( index == 0 )
			setVisibleOfManagementJPanels(true);
		else
			setVisibleOfManagementJPanels(false);			
	}
		

	private void setVisibleOfManagementJPanels(boolean visibilityOfZeroTab)
	{
		route_planning_view.getMapJPanel().getManufacturerManagementJPanel().setVisible(visibilityOfZeroTab);
		route_planning_view.getMapJPanel().getPathManagementJPanel().setVisible(!visibilityOfZeroTab);
	}
	
	public void addFirstOrder()
	{
		//pobierz miasto starowe z widoku
		String city_to = route_planning_view.city_to();
		//pobierz miasto docelowe z widoku
		String city_from = route_planning_view.city_from();
		
		String startDate = route_planning_view.getStartDate();
		String finishDate = route_planning_view.getFinishDate();
		
		CityBuilder cityBuilder = new CityBuilder();
		String [] coordinations = route_planning_model.getCityCoordinates(city_to);		
		City cityTo = cityBuilder.buildCity(city_to, coordinations[0], coordinations[1]);
		
		coordinations = route_planning_model.getCityCoordinates(city_from);
		City cityFrom = cityBuilder.buildCity(city_from, coordinations[0], coordinations[1]);
		
		OrderBuilder orderBuilder = new OrderBuilder();
		route_planning_model.getOrdersCollection().add(orderBuilder.buildOrder(cityTo, cityFrom, startDate, finishDate));
	}
	
	public void addNextOrder()
	{
		//pobierz miasto starowe z widoku
		String city_to = route_planning_view.getNextCityTo();
		
		String startDate = route_planning_view.getNextStartDate();
		String finishDate = route_planning_view.getNextFinishDate();
		
		CityBuilder cityBuilder = new CityBuilder();
		String [] coordinations = route_planning_model.getCityCoordinates(city_to);		
		City cityTo = cityBuilder.buildCity(city_to, coordinations[0], coordinations[1]);
		
		
		OrderBuilder orderBuilder = new OrderBuilder();
		route_planning_model.getOrdersCollection().add(orderBuilder.buildOrder(cityTo, route_planning_model.getLastOrder().getCityTo()
														, startDate, finishDate));
	}
	
	public void addOrderToTab()
	{
		route_planning_view.addOrderToTab( route_planning_model.getLastOrder() );
	}
	
	
}
