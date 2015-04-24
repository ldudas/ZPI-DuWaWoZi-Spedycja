package visualisations;

import java.util.ArrayList;

import builders.CityBuilder;
import builders.ManufacturersCollectionBuilder;

import com.esri.map.JMap;

import dataModels.City;
import dataModels.EvaluatorOfManufacturers;
import dataModels.Manufacturer;
import database.DataAccessObjectManufacturersVisualisation;
import decorators.VisualisationManufactureDecorator;

public class VisualisationManufacturersModel 
{	
	private JMap map;
	private ArrayList<Manufacturer> manufacturersData;
	private ArrayList<City> path_cities;
	
	private VisualisationManufactureDecorator dec;
	private DataAccessObjectManufacturersVisualisation dao_manufacturersVis;
	public VisualisationManufacturersModel()
	{
		dao_manufacturersVis = new DataAccessObjectManufacturersVisualisation();
		manufacturersData = new ArrayList<Manufacturer>();
		path_cities = new ArrayList<City>();
	}
	
		
	/**
	 * Metoda tworzaca mape z przyblizeniem na okreslone miasto.
	 * Na mape nanoszone sa obiekty w ktorych zawarte sa dane o producentach
	 * ich aktywnosci i przydatnosci wyboru.
	 * @return JMap
	 * @author Kamil Zimny
	 */
	public JMap getMapWithVisualisationManufacturersInCity(final String cityName)
	{
		ManufacturersCollectionBuilder manufacturersCollectionBuilder = new ManufacturersCollectionBuilder();
		manufacturersData = manufacturersCollectionBuilder.buildManufacturersCollection(
						    dao_manufacturersVis.getDataAboutManufacturerToVizualization(cityName,null,null,null,null) );
		
		CityBuilder cityBuilder = new CityBuilder();
		String [] coordinations = dao_manufacturersVis.getCityCoordinates(cityName);
		path_cities.add( cityBuilder.buildCity(cityName, coordinations[0], coordinations[1]) );
		
		
		EvaluatorOfManufacturers evaluator = new EvaluatorOfManufacturers();
		evaluator.evaluateManfacturersCollection(manufacturersData);
		
		dec = new VisualisationManufactureDecorator(path_cities.get(path_cities.size() - 1), manufacturersData);

		map =  dec;
		return map;
	}
		
	public JMap getMap()
	{
		return map;
	}
	
	public Manufacturer getManufacturerByID(String ID)
	{
		for( Manufacturer man : manufacturersData )
		{
			if( man.getID().equals(ID) )
				return man;
		}
		
		return null;
	}
	
	 

	
}