package visualisations;

import java.util.ArrayList;
import java.util.stream.Collectors;

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
		transporters = new ArrayList<Transporter>();
		transporters_filtered = new ArrayList<Transporter>();
		
	}
	
	private void getTranspotersFormDatabase(String city_from, String city_to)
	{
		//pobieranie listy przewoźnikow z bazy 
		//transporters = DAO_TransVis.getTranspoters(String city_from, String city_to);
		//na razie tworzę sam
		///
				transporters.clear();
				transporters.add(new Transporter(1,SizeCategory.SMALL,430,560,200,300,10,0));
				transporters.add(new Transporter(2,SizeCategory.MEDIUM,30,800,400,600,15,0));
				transporters.add(new Transporter(3,SizeCategory.MEDIUM,100,670,330,900,2,0.1));
				transporters.add(new Transporter(4,SizeCategory.BIG,1000,1000,1000,2000,50,0.3));
				System.out.println("Przed: "+transporters);
		///
	}
	
	private void sortTransporters()
	{
		transporters = transporters.stream().sorted(Transporter::compareByCapacity).collect(Collectors.toCollection(ArrayList::new));
		//transporters = transporters.stream().sorted((a, b) -> Transporter.compareByCapacity(a, b)).collect(Collectors.toCollection(ArrayList::new));
		System.out.println("Po: "+transporters);
	}
	
	private void filterTransporters()
	{
		transporters_filtered = transporters;
	}
	
	public ArrayList<Transporter> getFilteredTransporters()
	{
		getTranspotersFormDatabase("A", "B");
		sortTransporters();
		filterTransporters();
		return transporters_filtered;
	}

}
