package visualisations;

import java.util.ArrayList;

import dataModels.City;
import database.DataAccessObjectFactory;
import database.DataAccessObjectPathVisualisation;
import database.DataAccessObjectTransportersVisualisation;

public class VisualisationTransportersModel 
{
	
	/**
	 * Data Access Object dla wizualizacji przewoznikow
	 */
	private DataAccessObjectTransportersVisualisation DAO_TransVis;
	
	public VisualisationTransportersModel()
	{
		DataAccessObjectFactory factory = new DataAccessObjectFactory();
		DAO_TransVis = factory.getDataAccessObjectTransportersVisualisation();
	
	}

}
