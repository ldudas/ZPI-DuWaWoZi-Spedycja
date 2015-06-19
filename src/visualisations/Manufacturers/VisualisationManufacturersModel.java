package visualisations.Manufacturers;

import java.awt.AWTEvent;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.stream.Collectors;

import builders.CityBuilder;
import builders.ManufacturersCollectionBuilder;

import com.esri.map.GraphicsLayer;
import com.esri.map.JMap;
import com.esri.map.Layer;
import com.esri.map.LayerList;






import comparators.ComparatorManufactureActivite;
import dataModels.City;
import dataModels.EvaluatorOfManufacturers;
import dataModels.Manufacturer;
import dataModels.User;
import database.DataAccessObjectManufacturersVisualisation;
import decorators.VisualisationManufactureDecorator;

public class VisualisationManufacturersModel 
{	
	private JMap map;
	private ArrayList<Manufacturer> manufacturersData;
	private ArrayList<Manufacturer> currentDisplayManufacturersData;
	private ArrayList<City> path_cities;
	
	private VisualisationManufactureDecorator dec;
	private DataAccessObjectManufacturersVisualisation dao_manufacturersVis;
	
	public VisualisationManufacturersModel()
	{
		dao_manufacturersVis = new DataAccessObjectManufacturersVisualisation();
		manufacturersData = new ArrayList<Manufacturer>();
		currentDisplayManufacturersData = new ArrayList<Manufacturer>();
		path_cities = new ArrayList<City>();
	}
	
	public void clearData()
	{
		if( manufacturersData != null )
			manufacturersData.clear();
		if( currentDisplayManufacturersData != null)
			currentDisplayManufacturersData.clear();
		if( path_cities != null)
			path_cities.clear();
	}
	
	public void setExternalDatabaseConnectionProperty(User currentLoggedUser) throws Exception
	{
		if( currentLoggedUser != null )
		{
			dao_manufacturersVis.setExternalDatabaseConnectionProperty(currentLoggedUser.getServerAddress(), 
				currentLoggedUser.getServerPort(),currentLoggedUser.getDatabaseName(), 
				currentLoggedUser.getDatabaseLogin(), currentLoggedUser.getDatabasePassword());
		}
		else
			throw new Exception("Użytkownik nie został zalogowany."); //nie powinno się zdarzyć.
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
		manufacturersData = manufacturersCollectionBuilder.buildManufacturersCollection
							(
									dao_manufacturersVis.getDataAboutManufacturerToVizualization(cityName,null,null,null,null), 
									dao_manufacturersVis.getManufacturersActivityInEachMonth(cityName),
									dao_manufacturersVis.getManufacturersCostInEachMonth(cityName)
						    );
		
		manufacturersData.stream().forEach(currentDisplayManufacturersData::add);
		
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
	
	/**
	 * Metoda zwracajaca producenta o okreslonym id z kolekcji wszystkich 
	 * producentow w wybranym obszarze.
	 * @param ID
	 * @return Manufacturer jesli znalazl || null jestli nie znalazl
	 * @author Kamil Zimny
	 */
	public Manufacturer getManufacturerByID(String ID)
	{
		for( Manufacturer man : manufacturersData )
		{
			if( man.getID().equals(ID) )
				return man;
		}
		
		return null;
	}
	
	/**
	 * Metoda zwracajaca warstwe graficzna w której znajdują się grafiki porducentów.
	 * @return GraphicsLayer z obiektami producentow || null jestli nie znajdzie warstwy
	 * @author Kamil Zimny
	 */
	public GraphicsLayer getGraphicsLayerWithManufacturers()
	{
		LayerList layerlist = map.getLayers();

		for( Layer layer : layerlist)
			if( layer.getName().toString().equals( "Manufacturers graphics" ) )//Znajdujemy odpowiedzni layer
				return ((GraphicsLayer) layer);
		return null;
	}
	
	/**
	 * Sortuje kolekcje producentow na podstawie komparatora.
	 * @author Kamil Zimny
	 */
	public void sortManufacturerCollection(Comparator<Manufacturer> manufactureComparator)
	{
	   manufacturersData.sort( manufactureComparator);
	}
	
	public void show()
	{
		for( Manufacturer man : manufacturersData )
		{
			System.out.println(man.getRankOfDailyProfit() +" "+ man.getRankOfNumberOfOrders()+ " " +man.getName());
		}
	}
	
	/**
	 * Metda zwracajca kolekcje podana liczbe w parametrze porducentow z kolekcji producentow. 
	 * @param number
	 * @return ArrayList<Manufacturer> pierwszych producentow z kolekcji
	 * @author Kamil Zimny
	 */
	public ArrayList<Manufacturer> getNumberOfManufacturerFromBegin(int number)
	{
		return currentDisplayManufacturersData.stream()
				.sorted(new ComparatorManufactureActivite() )
				.limit(number)
	            .collect(Collectors.toCollection(ArrayList::new));
	}
	
	/**
	 * Metoda zwracajaca kolekcje producentow ktorych ostatnia aktywnosc jest
	 * wczesniejsza niz data podana w parametrze.
	 * @param dateBefore
	 * @return ArrayList<Manufacturer>
	 * @author Kamil Zimny
	 */
	public ArrayList<Manufacturer> getManufacturersWhoseLastActiviteIsBeforeDate(Date dateBefore)
	{
		SimpleDateFormat sDateFormat  = new SimpleDateFormat("yyyy-MM-dd");
		return currentDisplayManufacturersData =  manufacturersData.stream()
			.filter(man -> 
			{
				try 
				{		
					return sDateFormat.parse( man.getLastActivity() ).after(dateBefore);
				} catch (ParseException e) {return false;}
			} )
			.collect(Collectors.toCollection(ArrayList::new));
	}
	
	/**
	 * Metoda zwracajaca kolekcje poroducentow ktorych ostatnia aktywnosc
	 * jest pomiedzy datami podanymi w parametrach.
	 * @param dateFrom
	 * @param dateTo
	 * @return ArrayList<Manufacturer>
	 * @author Kamil Zimny
	 */
	public ArrayList<Manufacturer> getManufacturersWhoseLastActiviteIsBetween(Date dateFrom,Date dateTo)
	{
		SimpleDateFormat sDateFormat  = new SimpleDateFormat("yyyy-MM-dd");
		
		return currentDisplayManufacturersData = manufacturersData.stream()
				.filter(man -> 
				{
					try 
					{		
						return sDateFormat.parse( man.getLastActivity() ).after(dateFrom) && 
								sDateFormat.parse( man.getLastActivity() ).before(dateTo);
					} catch (ParseException e) {return false;}
				})
				.collect(Collectors.toCollection(ArrayList::new));
	}	
	
	
}