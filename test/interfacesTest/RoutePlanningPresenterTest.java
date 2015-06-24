package interfacesTest;

import static org.junit.Assert.*;
import interfaces.RoutePlanningModel;
import interfaces.RoutePlanningPresenter;
import interfaces.RoutePlanningView;

import org.junit.Test;

import visualisations.Path.VisualisationPathPresenter;
import visualisations.Manufacturers.VisualistaionManufacturersPresenter;

import visualisations.Transporters.VisualisationTransportersPresenter;

public class RoutePlanningPresenterTest {

	private RoutePlanningView route_planning_view = new RoutePlanningView();
	
	private RoutePlanningModel route_planning_model = new RoutePlanningModel();
	private VisualistaionManufacturersPresenter manu_presenter = null;
	private VisualisationPathPresenter path_presenter = null;
	private VisualisationTransportersPresenter trans_presenter = null;
	
	RoutePlanningPresenter rpp = new RoutePlanningPresenter(route_planning_view, route_planning_model, 
			manu_presenter, path_presenter,trans_presenter);
	
	@Test
	public void testNullPlanningView() {
		assertNull(rpp.return_view());
	}
	
}
