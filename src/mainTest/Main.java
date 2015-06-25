package mainTest;


import interfaces.RoutePlanningModel;
import interfaces.RoutePlanningPresenter;
import interfaces.RoutePlanningView;
import javax.swing.SwingUtilities;
import unfinishedCommissions.Unfinished_commissions_model;
import unfinishedCommissions.Unfinished_commissions_presenter;
import unfinishedCommissions.Unfinished_commissions_view;
import visualisations.Manufacturers.VisualisationManufacturersModel;
import visualisations.Manufacturers.VisualistaionManufacturersPresenter;
import visualisations.Manufacturers.VisualistaionManufacturersView;
import visualisations.Path.VisualisationPathModel;
import visualisations.Path.VisualisationPathPresenter;
import visualisations.Path.VisualisationPathView;
import visualisations.Transporters.VisualisationTransportersModel;
import visualisations.Transporters.VisualisationTransportersPresenter;
import visualisations.Transporters.VisualisationTransportersView;

public class Main 
{
	
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater( new Runnable() 
		{		
			@Override	
			public void run() 
			{
				VisualisationPathModel path_model = new VisualisationPathModel();
				VisualisationPathView path_view  = new VisualisationPathView();
		        VisualisationPathPresenter path_presenter = new VisualisationPathPresenter(path_view, path_model);
								
				VisualisationManufacturersModel manu_model = new VisualisationManufacturersModel();
				VisualistaionManufacturersView manu_view = new VisualistaionManufacturersView();
				VisualistaionManufacturersPresenter manu_presenter = new VisualistaionManufacturersPresenter(manu_view,manu_model);
				
				VisualisationTransportersModel trasn_model = new VisualisationTransportersModel();
            	VisualisationTransportersView trans_view = new VisualisationTransportersView();
            	VisualisationTransportersPresenter trans_presenter = new VisualisationTransportersPresenter(trans_view, trasn_model);
				
            	Unfinished_commissions_model comm_model = new Unfinished_commissions_model();
            	Unfinished_commissions_view comm_view = new Unfinished_commissions_view();
            	Unfinished_commissions_presenter comm_presenter = new Unfinished_commissions_presenter(comm_model,comm_view);
            	comm_view.setPresenter(comm_presenter);
            	
            	RoutePlanningModel route_model =  new RoutePlanningModel();
            	RoutePlanningView view_route = new RoutePlanningView();
				RoutePlanningPresenter presenter_route = 
						new RoutePlanningPresenter(view_route,route_model,manu_presenter,path_presenter,trans_presenter,comm_presenter);
				
				manu_presenter.set_route_presenter(presenter_route);
				path_presenter.set_route_presenter(presenter_route);
				trans_presenter.set_route_presenter(presenter_route);
				//comm_presenter.set_route_presenter(presenter_route);
				view_route.setPresenter(presenter_route);
				trans_view.setPresenter(trans_presenter);
				view_route.setPresenters();
				view_route.setJFrameVisibility(true);
				System.out.println("Running");
			}
		});

	}
}
