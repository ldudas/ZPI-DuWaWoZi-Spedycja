package mainTest;


import interfaces.RoutePlanningModel;
import interfaces.RoutePlanningPresenter;
import interfaces.RoutePlanningView;

import javax.swing.SwingUtilities;

import visualisations.*;

public class Main 
{
	
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater( new Runnable() 
		{		
			@Override
			public void run() 
			{
				 VisualisationPathView view_path  = new VisualisationPathView();
		          VisualisationPathPresenter presenter_path = new VisualisationPathPresenter(view_path, new VisualisationPathModel());
				
				RoutePlanningView view_route = new RoutePlanningView();
				VisualisationManufacturersModel model_man = new VisualisationManufacturersModel();
				VisualistaionManufacturersView view_man = new VisualistaionManufacturersView();
				VisualistaionManufacturersPresenter presenter_man = new VisualistaionManufacturersPresenter(view_man,model_man);
				RoutePlanningPresenter presenter_route = 
						new RoutePlanningPresenter(view_route, new RoutePlanningModel(),presenter_man,presenter_path);
				presenter_man.set_route_presenter(presenter_route);
				presenter_path.set_route_presenter(presenter_route);
				view_route.setPresenter(presenter_route);
				view_route.setPresenters();
				view_route.returnJFrame().setVisible(true);
			}
		});

	}
}
