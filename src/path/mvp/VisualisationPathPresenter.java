package path.mvp;

import shared.dataModels.City;
import shared.dataModels.User;
import interfaces.mvp.RoutePlanningPresenter;

/**
 * Prezenter wizualizacji trasy
 * @author Łukasz
 *
 */
public class VisualisationPathPresenter {
	
	/**
	 * model wizualizacji trasy
	 */
	private VisualisationPathModel model_PathVis;
	
	/**
	 * widok wizualizacji trasy
	 */		
	private VisualisationPathView view_PathVis;
	
	/**
	 * prezenter interfejsu route planning
	 */
	private RoutePlanningPresenter route_planning_presenter;
	
	
	
	public VisualisationPathPresenter(final VisualisationPathView view, VisualisationPathModel model)
	{
		view_PathVis = view;
		model_PathVis = model;
	}
	
	
	/**
	 * Utrwórz w modelu mape z pierwszymi dwoma miastami
	 * @param  cityNameFrom Nazwa miasta startu.    
	 * @param  cityNameTo Nazwa miasta konca.    
	 * @author Łukasz Dudaszek
	 */
	public void createInitialMap(final String cityNameFrom, final String cityNameTo)
	{
		model_PathVis.createInitialMap(cityNameFrom, cityNameTo);
	}
	
	
	/**
	 * Ustaw prezentera interfejsu route planning.
	 * @param  presenter Prezenter do ustawienia    
	 * @author Łukasz Dudaszek
	 */
	public void set_route_presenter(final RoutePlanningPresenter presenter)
	{
		route_planning_presenter = presenter;
	}
	
	
	/**
	 * Podaj widokowi wizualizacji zakladke w ktorej umiesci wizualizacje 
	 * oraz dodaj aktualna mape z modelu do tej zakladki    
	 * @author Łukasz Dudaszek
	 */
	public void startPathVisualisation()
	{
		view_PathVis.set_tab(route_planning_presenter.getTabWithMaps());
		view_PathVis.add_map_to_tab(model_PathVis.getPathMap());
	}
	
	/**
	 * Usuń ostatnie miasto z trasy
	 */
	public void removeLastCity()
	{
		model_PathVis.removeLastCityFormPath();
	}
	
	/**
	 * Dodaj miasto na koniec trasy
	 * @param city miasto do dodania
	 */
	public void addCityToPath(City city)
	{
		model_PathVis.addCityToPath(city.getCityName());
	}
	
	public void setExternalDatabaseConnectionProperty(User currentLoggedUser) throws Exception
	{
		model_PathVis.setExternalDatabaseConnectionProperty(currentLoggedUser);
	}
	
	/**
	 * Wyczyść dane w modelu
	 */
	public void clearDataInModel()
	{
		model_PathVis.clearData();
	}
	
	/**
	 * @return Liczba miast wchodząca w skład wizualizacji trasy
	 */
	public int getNumberOfCities()
	{
		return model_PathVis.getNumberOfCities();
	}
	

}
