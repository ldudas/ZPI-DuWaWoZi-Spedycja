package manufacturers.mvp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.stream.Collectors;

import shared.builders.CityBuilder;
import shared.builders.ManufacturersCollectionBuilder;
import shared.dataModels.City;
import shared.dataModels.EvaluatorOfManufacturers;
import shared.dataModels.Manufacturer;
import shared.dataModels.User;
import manufacturers.comparators.ComparatorManufactureActivite;
import manufacturers.decorators.VisualisationManufactureDecorator;

import com.esri.map.GraphicsLayer;
import com.esri.map.JMap;
import com.esri.map.Layer;
import com.esri.map.LayerList;

import database.DataAccessObjectManufacturersVisualisation;

/**
 * Model wizualizacji producentów.
 * @author Kamil Zimny
 *
 */
public class VisualisationManufacturersModel 
{	
	/**
	 * Mapa na który
	 */
	private JMap map;
	
	/**
	 * Kolekcja zawierająca wszystkich producentów znajdujących się w danym mieście
	 */
	private ArrayList<Manufacturer> manufacturersData;
	
	/**
	 * Kolekcja producentów obecnie wyświetlanych ( np. po filtracji )
	 */
	private ArrayList<Manufacturer> currentDisplayManufacturersData;
	
	/**
	 * Kolekcja kolejnych miast tworzących trasę
	 */
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
	
	/**
	 * Metoda usuwająca wszystkie zapisane dane
	 * w modelu.
	 * @author Kamil Zimny
	 */
	public void clearData()
	{
		if( manufacturersData != null )
			manufacturersData.clear();
		if( currentDisplayManufacturersData != null)
			currentDisplayManufacturersData.clear();
		if( path_cities != null)
			path_cities.clear();
	}
	
	/**
	 * Ustawienie danych do połączenia z zewnętrzną bazą danych aktualnego użytkownika.
	 * @param currentLoggedUser aktualnie zalogowany użytkownik
	 * @author Kamil Zimny
	 */
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
	 * Metoda tworząca mapę z przyblizeniem na określone miasto.
	 * Na mapę nanoszone są obiekty w których zawarte są dane o producentach
	 * ich aktywności i przydatności wyboru.
	 * @param cityName - nazwa miasta
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
		
	/**
	 * Zwróć mapę 
	 * @return map
	 */
	public JMap getMap()
	{
		return map;
	}
	
	/**
	 * Metoda zwracająca producenta o określonym id z kolekcji wszystkich 
	 * producentów w wybranym obszarze.
	 * @param ID - identyfikator producenta
	 * @return Manufacturer jeśli znalazł || null jesli nie znalazł
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
	 * Metoda zwracająca warstwę graficzna w której znajdują się grafiki porducentów.
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
	 * Sortuje kolekcje producentów na podstawie komparatora.
	 * @param manufactureComparator - komparator producentów
	 * @author Kamil Zimny
	 */
	public void sortManufacturerCollection(Comparator<Manufacturer> manufactureComparator)
	{
	   manufacturersData.sort( manufactureComparator);
	}
	
	
	/**
	 * Metda zwracająca kolekcję porducentów o liczbie określonej w parametrze, 
	 * producenci pochcodzą z kolekcji producetów aktualnie wyświetlanych. 
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
	 * Metoda zwracająca kolekcję wszystkich producentów dostępnych 
	 * w danym mieście.
	 * @return ArrayList<Manufacturer> kolekcja producentów
	 */
	public ArrayList<Manufacturer> getAllManufacturerInCurrentCity()
	{
		return manufacturersData;
	}
	
	/**
	 * Metoda zwracajaca kolekcję producentow których ostatnia aktywnosc jest
	 * wcześniejsza niż data podana w parametrze.
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
	 * Metoda zwracająca kolekcję poroducentów których ostatnia aktywność
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