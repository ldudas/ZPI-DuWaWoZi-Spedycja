package main;


import interfaces.mvp.RoutePlanningModel;
import interfaces.mvp.RoutePlanningPresenter;
import interfaces.mvp.RoutePlanningView;

import javax.swing.SwingUtilities;

import path.mvp.VisualisationPathModel;
import path.mvp.VisualisationPathPresenter;
import path.mvp.VisualisationPathView;
import manufacturers.mvp.VisualisationManufacturersModel;
import manufacturers.mvp.VisualistaionManufacturersPresenter;
import manufacturers.mvp.VisualistaionManufacturersView;
import transporters.mvp.VisualisationTransportersModel;
import transporters.mvp.VisualisationTransportersPresenter;
import transporters.mvp.VisualisationTransportersView;
import unfinishedCommissions.mvp.UnfinishedCommissionsModel;
import unfinishedCommissions.mvp.UnfinishedCommissionsPresenter;
import unfinishedCommissions.mvp.UnfinishedCommissionsView;

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
				
            	UnfinishedCommissionsModel comm_model = new UnfinishedCommissionsModel();
            	UnfinishedCommissionsView comm_view = new UnfinishedCommissionsView();
            	UnfinishedCommissionsPresenter comm_presenter = new UnfinishedCommissionsPresenter(comm_model,comm_view);
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
