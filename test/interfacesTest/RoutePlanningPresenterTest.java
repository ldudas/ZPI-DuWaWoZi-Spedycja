package interfacesTest;

import static org.junit.Assert.*;
import interfaces.RoutePlanningModel;
import interfaces.RoutePlanningPresenter;
import interfaces.RoutePlanningView;

import org.junit.Test;

import visualisations.Path.VisualisationPathPresenter;
import visualisations.Manufacturers.VisualistaionManufacturersPresenter;

public class RoutePlanningPresenterTest {

	private RoutePlanningView route_planning_view = new RoutePlanningView();
	
	private RoutePlanningModel route_planning_model;
	private VisualistaionManufacturersPresenter manu_presenter;
	private VisualisationPathPresenter path_presenter;
	
	RoutePlanningPresenter rpp = new RoutePlanningPresenter(route_planning_view, route_planning_model, manu_presenter, path_presenter);
	
	@Test
	public void testNullPlanningView() {
		assertNull(rpp.return_view());
	}
	
	/*@Test
	public void testRemoveLastCity()
	{
		path_presenter.createInitialMap("Gdynia", "Warszawa");
		path_presenter.addCityToPath(new City("Kielce", 2037, 5053));
		int nbr = path_presenter.getNumberOfCities();
		path_presenter.removeLastCity();
		assertEquals(nbr-1, path_presenter.getNumberOfCities());
	}
	 */
}
