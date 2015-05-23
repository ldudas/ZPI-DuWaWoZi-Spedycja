package mainTest;

import java.awt.EventQueue;

import visualisations.Transporters.VisualisationTransportersModel;
import visualisations.Transporters.VisualisationTransportersPresenter;
import visualisations.Transporters.VisualisationTransportersView;

public class MainTransporters {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VisualisationTransportersModel trasn_model = new VisualisationTransportersModel();
	            	VisualisationTransportersView trans_view = new VisualisationTransportersView();
	            	VisualisationTransportersPresenter trans_pres = new VisualisationTransportersPresenter(trans_view, trasn_model);
	            	trans_view.setPresenter(trans_pres);
	            	System.out.println("Running");
	            	
	            	//trans_pres.drawTransporters();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
