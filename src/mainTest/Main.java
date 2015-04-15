package mainTest;

import interfaces.RoutePlanningModel;
import interfaces.RoutePlanningPresenter;
import interfaces.RoutePlanningView;
import maps.VMVPresenter;
import maps.VMVModel;
import maps.VMVView;
import javax.swing.SwingUtilities;

public class Main 
{
	
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater( new Runnable() 
		{		
			@Override
			public void run() 
			{
				RoutePlanningView view = new RoutePlanningView();
				VMVModel v_model = new VMVModel();
				VMVView v_view = new VMVView();
				VMVPresenter v_presenter = new VMVPresenter(v_view,v_model);
				RoutePlanningPresenter presenter = 
						new RoutePlanningPresenter(view, new RoutePlanningModel(),v_presenter);
				v_presenter.set_route_presenter(presenter);
				view.setPresenter(presenter);
				view.setPresenters();
				view.returnJFrame().setVisible(true);
			}
		});

	}

}
