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
		          VisualisationPathPresenter pres_path = new VisualisationPathPresenter(view_path, new VisualisationPathModel());
				
				
				RoutePlanningView view = new RoutePlanningView();
				VisualisationManufacturersModel v_model = new VisualisationManufacturersModel();
				VisualistaionManufacturersView v_view = new VisualistaionManufacturersView();
				VisualistaionManufacturersPresenter v_presenter = new VisualistaionManufacturersPresenter(v_view,v_model);
				RoutePlanningPresenter presenter = 
						new RoutePlanningPresenter(view, new RoutePlanningModel(),v_presenter,pres_path);
				v_presenter.set_route_presenter(presenter);
				view.setPresenter(presenter);
				view.setPresenters();
				view.returnJFrame().setVisible(true);
			}
		});

	}
}
