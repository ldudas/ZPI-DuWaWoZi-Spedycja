package mainTest;

import java.util.ArrayList;

import javax.swing.SwingUtilities;

import visualisations.VisualisationTransportersModel;
import visualisations.VisualisationTransportersPresenter;
import visualisations.VisualisationTransportersView;

public class MainTransporters {

	public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	
            	VisualisationTransportersModel trasn_model = new VisualisationTransportersModel();
            	VisualisationTransportersView trans_view = new VisualisationTransportersView();
            	VisualisationTransportersPresenter trans_pres = new VisualisationTransportersPresenter(trans_view, trasn_model);
          
            }
        });
    }

}
