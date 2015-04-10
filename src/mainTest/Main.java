package mainTest;

import javax.swing.SwingUtilities;

import models.ModelManufacturersVisualisation;
import presenters.PresenterManufacturersVisualisation;
import views.ViewManufacturersVisualisation;

public class Main 
{
	
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater( new Runnable() 
		{		
			@Override
			public void run() 
			{
				ViewManufacturersVisualisation view = new ViewManufacturersVisualisation();
				PresenterManufacturersVisualisation presenter = 
						new PresenterManufacturersVisualisation(view, new ModelManufacturersVisualisation());
				
				view.setPresenter(presenter);
				presenter.startManufacturersVisualisation("Wroc³aw");			
			}
		});

	}

}
