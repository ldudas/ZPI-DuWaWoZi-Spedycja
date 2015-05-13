package visualisations;

import java.util.ArrayList;

import dataModels.City;
import dataModels.SizeCategory;
import dataModels.Transporter;
import database.DataAccessObjectFactory;
import database.DataAccessObjectPathVisualisation;
import database.DataAccessObjectTransportersVisualisation;

public class VisualisationTransportersModel 
{
	
	/**
	 * Data Access Object dla wizualizacji przewoznikow
	 */
	private DataAccessObjectTransportersVisualisation DAO_TransVis;
	
	/**
	 * Miasto początkowe
	 */
	private String city_from;
	
	/**
	 * Miasto końcowe
	 */
	private String city_to;
	
	/**
	 * Wszyscy przeoźnicy z miasta city_form do city_to
	 */
	private ArrayList<Transporter> transporters;
	
	/**
	 * Przeoźnicy z miasta city_form do city_to przefiltrowani wg prefernecji rozmiaru i typu specjalnego (np. chłodnia)
	 */
	private ArrayList<Transporter> transporters_filtered;
	
	/**
	 * wybrana kategoria rozmiaru do filtrowania
	 */
	private SizeCategory size_category;
	
	/**
	 * wybrany typ pojazdu do filtrowania
	 */
	private String type_of_vechicle;
	
	public VisualisationTransportersModel()
	{
		DataAccessObjectFactory factory = new DataAccessObjectFactory();
		DAO_TransVis = factory.getDataAccessObjectTransportersVisualisation();
	}

}
