package transporters.mvp;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.Collectors;

import shared.dataModels.SizeCategory;
import shared.dataModels.Transporter;
import shared.dataModels.User;
import shared.database.DataAccessObjectFactory;
import shared.database.DataAccessObjectTransportersVisualisation;
import transporters.specification.TransporterSizeCategorySpecification;

import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.Style;
import com.esri.map.ArcGISTiledMapServiceLayer;
import com.esri.map.GraphicsLayer;
import com.esri.map.JMap;
import com.esri.map.MapEvent;
import com.esri.map.MapEventListener;



/**
 * Model wizualizacji przewoźników
 * @author Łukasz Dudaszek
 *
 */
public class VisualisationTransportersModel 
{
	
	/**
	 * Data Access Object dla wizualizacji przewoznikow
	 */
	private DataAccessObjectTransportersVisualisation DAO_TransVis;
	
	/**
	 * Miasto początkowe
	 */
	private String cityFrom;
	
	/**
	 * Miasto końcowe
	 */
	private String cityTo;
	
	/**
	 * Wszyscy przewoźnicy z miasta city_form do city_to
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
	 * zapamiętane mapy dla przewoznikow
	 */
	private HashMap<Integer, JMap> maps_cache;
	
	/**
	 * Kolekcja zawierajaca wspolrzedne wszystkich miast w bazie
	 * get(i) -> koleckja danych konkretnego miasta
	 * get(i).get(0) -> nazwa miasta jako String (wystarczy rzutowac)
	 * get(i).get(1) -> dlugosc geograficzna jako String (wystarczy rzutowac)
	 * get(i).get(2) -> szerokosc geograficzna (wystarczy rzutowac)
	 */
	private ArrayList<ArrayList<Object>> cities_coordinates;
	
	/**
	 * id wybranego przewoznika
	 */
	private Transporter chosen_transporter;
	
	/**
	 * opcja kozystania z wizulaziacji
	 * tylko przegladanie przewoznikow: 	-1
	 * konczaca sie zapisem trasy do bazy: 	 1
	 */
	private int opening_flag;
	
	/**
	 *  zapamiętana maksymalna liczba zleceń (wśród wyświetlanych przewoźników)
	 */
	private double max_num_of_orders;
	
	/**
	 *  zapamiętana minimalna liczba zleceń (wśród wyświetlanych przewoźników)
	 */
	private double min_num_of_orders;
	
	/**
	 *  zapamiętany maksymalny koszt (wśród wyświetlanych przewoźników)
	 */
	private double max_cost;
	
	/**
	 *  zapamiętany minimalny koszt (wśród wyświetlanych przewoźników)
	 */
	private double min_cost;
	
	/**
	 *  zapamiętany maksymalna objętość (wśród wyświetlanych przewoźników)
	 */
	private double max_volume;
	
	/**
	 *  zapamiętana maksymalna ładowność (wśród wyświetlanych przewoźników)
	 */
	private double max_capacity;

	
	public VisualisationTransportersModel()
	{
		DataAccessObjectFactory factory = new DataAccessObjectFactory();
		DAO_TransVis = factory.getDataAccessObjectTransportersVisualisation();
		transporters = new ArrayList<Transporter>();
		transporters_filtered = new ArrayList<Transporter>();
		maps_cache = new HashMap<>();
		cityFrom="";
		cityTo="";
		size_category = null;
		chosen_transporter = null;
		opening_flag = 0;
	}
	
	/**
	 * Czyszczenie danych modelu
	 */
	public void clearData()
	{
		if( transporters != null)
			transporters.clear();
		if( transporters_filtered != null)
			transporters_filtered.clear();
		if( maps_cache != null)
			maps_cache.clear();
		cityFrom="";
		cityTo="";
		size_category = null;
		chosen_transporter = null;
	}
	
	
	/**
	 * Pobranie przewoźników z bazy danych na trasie
	 * @param city_from Miasto początkowe
	 * @param city_to Miasto docelowe
	 */
	private void getTranspotersFormDatabase(String city_from, String city_to)
	{
		/*transporters.clear();
		
		//pobieranie listy przewoźnikow z bazy
		ArrayList<ArrayList<Object>> result_small = DAO_TransVis.getTranspoters(city_from, city_to, SizeCategory.SMALL);
		ArrayList<ArrayList<Object>> result_medium = DAO_TransVis.getTranspoters(city_from, city_to,  SizeCategory.MEDIUM);
		ArrayList<ArrayList<Object>> result_big = DAO_TransVis.getTranspoters(city_from, city_to, SizeCategory.BIG);


		
		for(ArrayList<Object> row : result_small){
			transporters.add(new Transporter((int)row.get(0), SizeCategory.SMALL, ((Long)row.get(1)).intValue(), (double)row.get(2), ((BigDecimal)row.get(3)).intValue(), ((BigDecimal)row.get(4)).intValue(), ((BigDecimal)row.get(8)).doubleValue(), ((BigDecimal)row.get(7)).doubleValue(), (String)row.get(5), (int)row.get(6)));
		}
			
		
		for(ArrayList<Object> row : result_medium){
			transporters.add(new Transporter((int)row.get(0), SizeCategory.MEDIUM, ((Long)row.get(1)).intValue(), (double)row.get(2), ((BigDecimal)row.get(3)).intValue(), ((BigDecimal)row.get(4)).intValue(), ((BigDecimal)row.get(8)).doubleValue(), ((BigDecimal)row.get(7)).doubleValue(), (String)row.get(5), (int)row.get(6)));
			}
		
		for(ArrayList<Object> row : result_big){
			transporters.add(new Transporter((int)row.get(0), SizeCategory.BIG, ((Long)row.get(1)).intValue(), (double)row.get(2), ((BigDecimal)row.get(3)).intValue(), ((BigDecimal)row.get(4)).intValue(), ((BigDecimal)row.get(8)).doubleValue(), ((BigDecimal)row.get(7)).doubleValue(), (String)row.get(5), (int)row.get(6)));
			}*/
		
		//System.out.println(result_small.size()+" "+ result_medium.size()+" "+result_big.size());
		
		//na razie tworzę sam
		///
				transporters.clear();
				transporters.add(new Transporter(1,SizeCategory.SMALL,43,560,200,300,0.0,0,"Mark-Trans Marek Kaw",123456789));
				transporters.add(new Transporter(2,SizeCategory.MEDIUM,30,800,400,600,0.1,0.1,"Max-Przeprowadzki",123456789));
				transporters.add(new Transporter(3,SizeCategory.MEDIUM,10,670,330,900,0.3,0.01,"Quality Logistics",123456789));
				transporters.add(new Transporter(4,SizeCategory.BIG,10,1000,1000,2000,0.01,0.03,"Magnatt Transport Dr",123456789));
				transporters.add(new Transporter(5,SizeCategory.BIG,23,344,808,3000,0.12,0.022,"Dariusz Kowal Firma",123456789));
				transporters.add(new Transporter(6,SizeCategory.BIG,20,607,700,200,0.3,0.001,"RJ-TRANS Usługi Tran",123456789));
				transporters.add(new Transporter(7,SizeCategory.SMALL,20,450,180,200,0.02,0.008,"Auto Mag Usługi Tran",123456789));
				transporters.add(new Transporter(8,SizeCategory.SMALL,100,300,100,200,0.0,0.01,"Admira Michał Dziadc",123456789));
				transporters.add(new Transporter(9,SizeCategory.SMALL,80,600,280,380,0.1,0,"Margo-Express Malgor",123456789));
				transporters.add(new Transporter(10,SizeCategory.SMALL,80,570,500,500,0.05,0.04,"Bortrans Gabriela Bo",123456789));
				transporters.add(new Transporter(11,SizeCategory.MEDIUM,20,900,400,700,0,0,"A. Jezierski Sp. z o",123456789));
				transporters.add(new Transporter(12,SizeCategory.MEDIUM,30,770,470,900,0.2,0.0,"A. Jadanowski Autoan",123456789));
				transporters.add(new Transporter(13,SizeCategory.MEDIUM,40,700,500,720,0.1,0,"Net Transport i Sped",123456789));
				transporters.add(new Transporter(14,SizeCategory.MEDIUM,50,740,630,770,0.07,0.015,"Putek Zdzisław Przed",123456789));
				transporters.add(new Transporter(15,SizeCategory.MEDIUM,67,780,700,840,0.06,0,"Reno-Trans Sp. z o.o",123456789));
				transporters.add(new Transporter(16,SizeCategory.MEDIUM,80,880,680,940,0.02,0,"Bracia Tyszka",123456789));
				transporters.add(new Transporter(1,SizeCategory.BIG,102,1100,1100,2000,0.01,0.0004,"Mark-Trans Marek Kaw",123456789));
				transporters.add(new Transporter(2,SizeCategory.BIG,20,1700,1500,3000,0.12,0.09,"Max-Przeprowadzki",123456789));
				transporters.add(new Transporter(3,SizeCategory.BIG,14,1300,1200,4000,0.07,0.6,"Quality Logistics",123456789));
				//System.out.println("Przed: "+transporters);
	///
	 
		 
	}
	
	/**
	 * sortowanie pobranych przewoźników ze względu na objętość
	 */
	private void sortTransporters()
	{
		transporters = transporters.stream().sorted(Transporter::compareByCapacity).collect(Collectors.toCollection(ArrayList::new));
	}
	
	/**
	 * Filtrowanie przewożników z danej kategorii rozmiaru i zapis do transporters_filtered
	 * @param sc Kategoria rozmiaru
	 */
	private void filterTransporters(SizeCategory sc)
	{
		final TransporterSizeCategorySpecification trans_spec = new TransporterSizeCategorySpecification(sc);
		transporters_filtered = transporters.stream().filter( t -> trans_spec.isSatisfiedBy(t)).collect(Collectors.toCollection(ArrayList::new));
	}
	
	/**
	 * Ustawienie przefiltrowanych przewoźników obsługujących trasę
	 * @param city_from Miasto początkowe
	 * @param city_to Miasto docelowe
	 * @param sc Kategoria rozmiaru
	 */
	public void setFilteredTransporters(String city_from, String city_to, SizeCategory sc)
	{
		boolean filtered = false;
		if(!city_from.equals(cityFrom) || !city_to.equals(cityTo))
		{
			cityFrom = city_from;
			cityTo = city_to;
			getTranspotersFormDatabase(cityFrom, cityTo);
			sortTransporters();
			filterTransporters(sc);
			filtered = true;
		}
		
		if(size_category!=sc && !filtered)
		{
			size_category = sc;
			filterTransporters(sc);
		}
	
		//System.out.println(transporters_filtered.size());
	}
	
	public void setMinMaxFilteredTransportersProperties()
	{
		max_num_of_orders = Double.MIN_VALUE;
    	min_num_of_orders = Double.MAX_VALUE;
    	max_cost = Double.MIN_VALUE;
    	min_cost = Double.MAX_VALUE;
    	max_volume = Double.MIN_VALUE;
    	max_capacity = Double.MIN_VALUE;
    	
    	transporters_filtered.stream().forEach( (Transporter tr) -> {
    		if(tr.getNumber_of_orders()<min_num_of_orders) min_num_of_orders = tr.getNumber_of_orders();
    		if(tr.getNumber_of_orders()>max_num_of_orders) max_num_of_orders = tr.getNumber_of_orders();
    		if(tr.getCost()>max_cost) max_cost = tr.getCost();
    		if(tr.getCost()<min_cost) min_cost = tr.getCost();
    		if(tr.getVolume()>max_volume) max_volume = tr.getVolume();
    		if(tr.getCapacity()>max_capacity) max_capacity = tr.getCapacity();	
    	});
	}
	
	public ArrayList<Transporter> getFilteredTransporters()
	{
		return transporters_filtered;
	}
	
	public Transporter getTransporter(int id_trans,SizeCategory sc)
	{
		for(Transporter t: transporters_filtered)
		{
			if(t.getId_trans() == id_trans && t.getSizeCategory() == sc)
			{
				return t;
			}
		}
		return null;
	}
	
	/**
	 * Pobranie mapy z naniesionym łańcuchem dostaw
	 * @param id_trans
	 * @return
	 */
	public JMap getTransporterRoutesMap(int id_trans)
	{
		
		if(maps_cache.containsKey(id_trans))
		{
			return maps_cache.get(id_trans);
		}
		else
		{
		
		//stworz mape
		final JMap map = new JMap();
		
		ArcGISTiledMapServiceLayer tiledLayer = new ArcGISTiledMapServiceLayer("http://services.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer");
		//dodaj pobrana warstwe do mapy
		map.getLayers().add(tiledLayer);
		
		//utworz warstwe graficzna
		GraphicsLayer graphicsLayer = new GraphicsLayer();
		graphicsLayer.setName("Routes graphics");
		//dodaj warstwe graficzna do mapy
		map.getLayers().add(graphicsLayer);
		
		ArrayList<ArrayList<Object>> routes = DAO_TransVis.getTransporterRoutes(id_trans);
		
		map.addMapEventListener(new MapEventListener() 
		{

		  @Override
		  public void mapReady(MapEvent event) 
		  {
			  final SpatialReference mapSR = event.getMap().getSpatialReference();
			 // System.out.println("getTransporterRoutesMapSR: "+mapSR);
			  
			  // Ustaw mape na obszar	
			  map.setExtent(new Envelope( GeometryEngine.project(19.46,52.0,mapSR),1050000,1050000));
			  // Narysuj na mapie polaczenie miedzy miastami o odpowiednim kolorze
			  addRoutesOnMap(event.getMap(),graphicsLayer,routes,id_trans);	 
			  
		  }

		  @Override
		  public void mapDispose(MapEvent arg0) {}

		  @Override
		  public void mapExtentChanged(MapEvent arg0) {}
		 });
			
		maps_cache.put(id_trans, map);
		return map;
		}
	}
	
	
	/**
	 * Dodaje trasy na mapę
	 * @param  routes Trasy razem z liczbą zleceń         
	 * @author Łukasz Dudaszek
	 */
	private void addRoutesOnMap(JMap map,GraphicsLayer graphicsLayer,ArrayList<ArrayList<Object>> routes, int id_trans)
	{
		if(cities_coordinates==null)
		{
			cities_coordinates = DAO_TransVis.getCitiesCoordinates();
		}
		
		Random r = new Random();
		
		SpatialReference mapSR = map.getSpatialReference();
		
		//pobierz maksymalna liczbe zlecen
		double max_num_of_orders = getMaxNumberOfOrders(routes);
		//pobierz minimalna liczbe zlecen
		double min_num_of_orders = getMinNumberOfOrders(routes);
		
		
		for(ArrayList<Object> row: routes)
		{
			
			String cityNameFrom = (String) row.get(0);
			String cityNameTo = (String) row.get(1);
			double number_of_orders = ((long)row.get(2));
			
			//znajdz indeksy miast
			int ind_1 = 0;
			int ind_2 = 0;
			
			
			for(int i=0; i<cities_coordinates.size();i++)
			{
				if(((String)cities_coordinates.get(i).get(0)).equals(cityNameFrom)) ind_1 = i;
				if(((String)cities_coordinates.get(i).get(0)).equals(cityNameTo)) ind_2 = i;
			}
			
			//pobierz wspolrzedne miast z kolekcji city_coordinates i zamien je na double
			double firstCityCoordinate1 = parseCoordinate((String)cities_coordinates.get(ind_1).get(1));
			double firstCityCoordinate2 = parseCoordinate((String)cities_coordinates.get(ind_1).get(2));
			double secondCityCoordinate1 =  (0.004*r.nextDouble()-0.002) + parseCoordinate((String)cities_coordinates.get(ind_2).get(1));
			double secondCityCoordinate2 = (0.004*r.nextDouble()-0.002) +  parseCoordinate((String)cities_coordinates.get(ind_2).get(2));
			
			
			//z wspolrzednych jako double utworz obiekty Ponit
			Point firstCityLocation = GeometryEngine.project(firstCityCoordinate1, firstCityCoordinate2, mapSR);
			Point secondCityLocation = GeometryEngine.project(secondCityCoordinate1,secondCityCoordinate2,  mapSR);
			
			//utworz linie miedzy punktami
			 Polyline polyline = new Polyline();
			 polyline.startPath(firstCityLocation);
			 polyline.lineTo(secondCityLocation);
			 
			 double orders_ratio =  (number_of_orders-min_num_of_orders)/(max_num_of_orders-min_num_of_orders);
		
			 
			 int red = 
				 	  orders_ratio<=0.25?0:
				 		 orders_ratio<=0.25?128:
				 	  orders_ratio<=0.75?(int)(508*orders_ratio - 126):
				 	  orders_ratio<=1.0? 255: 0;
			int green = (int)(-255*orders_ratio+255);
			int blue = orders_ratio<=0.25? 255:
					   orders_ratio<=0.5? (int)(-508 * orders_ratio + 382):
					   orders_ratio<=0.75? 128 :
					   orders_ratio<=1.0 ? (int) (-512 * orders_ratio + 512): 0 ;
			 
			 Color col =  new Color(red,green,blue);
			 
			 //ustaw styl linii
			 SimpleLineSymbol polyLineStyle = new SimpleLineSymbol(col, 2);
			 polyLineStyle.setStyle(SimpleLineSymbol.Style.SOLID);
			   
			 
			 //oblicz o ile stopni trzeba obrocic grafike grotu strzalki
			 double arrowRotationDegrees = get_arrow_rotation_degrees(firstCityCoordinate1, firstCityCoordinate2, secondCityCoordinate1, secondCityCoordinate2);
			
			 
			 //utworz grot strzalki
			 SimpleMarkerSymbol triangle = new SimpleMarkerSymbol(col, 10, Style.TRIANGLE);
			 triangle.setAngle((float)(arrowRotationDegrees));
			 
			 //utworz kolo
			 //SimpleMarkerSymbol circle = new SimpleMarkerSymbol(Color.black, 2, SimpleMarkerSymbol.Style.CIRCLE);
			 
			//dodaj dwa punkty jako kola, linie oraz grot jako trojkat do warsty graficznej	
			graphicsLayer.addGraphic(new Graphic(polyline, polyLineStyle));
			graphicsLayer.addGraphic(new Graphic(secondCityLocation, triangle));
		}
		
	}
	
	/**
	 * Wyszukanie największej liczby zleceń
	 * @param routes kolekcja danych tras pobranych z bazy danych
	 * @return maksymalna liczba zleceń na trasie
	 */
	private int getMaxNumberOfOrders( ArrayList<ArrayList<Object>>routes)
	{
			long max = Long.MIN_VALUE;
			for(ArrayList<Object> o: routes)
			{
				if(((long)o.get(2)) > max)
				{
					max = (long)o.get(2);
				}
			}
			
			return (int)max;
		
	}
	
	
	/**
	 * Wyszukanie najmniejszej liczby zleceń
	 * @param routes kolekcja danych tras pobranych z bazy danych
	 * @return minimalna liczba zleceń na trasie
	 */
	private int getMinNumberOfOrders( ArrayList<ArrayList<Object>>routes)
	{
			long min = Long.MAX_VALUE;
			for(ArrayList<Object> o: routes)
			{
				if(((long)o.get(2)) < min)
				{
					min = (long)o.get(2);
				}
			}
			
			return (int)min;
		
	}
	
	/**
	 * Zamienia wspolrzedna geograficzna ze String na double
	 * @param  coordiante Wspolrzedna geograficzna  
	 * @return Wspolrzedna geograficzna jako double     
	 * @author Kamil Zimny
	 */
	private double parseCoordinate(String coordinate)
	{        
		int index1 = coordinate.indexOf("°");
		int index2 = coordinate.indexOf("'");
		String part1 = coordinate.substring(0,index1);
		String part2 = coordinate.substring(index1+1,index2);	  
		double a = Double.parseDouble(part1);
	    double b = Double.parseDouble(part2);
	    double result = a + (b/60);
	      
	    if(coordinate.contains("?"))
	    {
	    	int index3 = coordinate.indexOf("?"); 
	        String part3 = coordinate.substring(index2+1,index3);
	        double c = Double.parseDouble(part3);
	        result += c/3600;
	     }
	    
	    return result;
	}
	
	/**
	 * Oblicza kat obrotu grotu strzalki z podanych wspolrzednych
	 * @param  firstCityCoordinate1 dlugosc geograficzna poczatku linii 
	 * @param  firstCityCoordinate2 szerokosc geograficzna poczatku linii
	 * @param  secondCityCoordinate1 dlugosc geograficzna konca linii 
	 * @param  secondCityCoordinate2 szerokosc geograficzna konca linii
	 * @return Kat obrotu grota     
	 * @author Łukasz Dudaszek
	 */
	private double get_arrow_rotation_degrees(double firstCityCoordinate1, double firstCityCoordinate2, double secondCityCoordinate1, double secondCityCoordinate2)
	{
		
		double tang;
	    double degrees=0;;
	    
	    if(secondCityCoordinate1 > firstCityCoordinate1 && secondCityCoordinate2 > firstCityCoordinate2)
	    {
	    tang =   Math.abs((firstCityCoordinate1 - secondCityCoordinate1)) / Math.abs((firstCityCoordinate2 - secondCityCoordinate2));
	    degrees = Math.toDegrees(Math.atan(tang));
	    
	    //degrees -= 0.5*degrees;
	    }
	    else if (secondCityCoordinate1 > firstCityCoordinate1 && secondCityCoordinate2 < firstCityCoordinate2)
	    {
	    	tang =   Math.abs((firstCityCoordinate2 - secondCityCoordinate2) / Math.abs((firstCityCoordinate1 - secondCityCoordinate1)));
		    degrees = Math.toDegrees(Math.atan(tang)) + 90;
		    
		    degrees += 0.05*degrees;
	    }
	    else if (secondCityCoordinate1 < firstCityCoordinate1 && secondCityCoordinate2 < firstCityCoordinate2)
	    {
	    	tang =   Math.abs((firstCityCoordinate1 - secondCityCoordinate1)) / Math.abs((firstCityCoordinate2 - secondCityCoordinate2));
		    degrees = Math.toDegrees(Math.atan(tang)) + 180;
		    
		    degrees -= 0.05*degrees;
	    }
	    else if (secondCityCoordinate1 < firstCityCoordinate1 && secondCityCoordinate2 > firstCityCoordinate2)
	    {
	    	tang =    Math.abs((firstCityCoordinate2 - secondCityCoordinate2) / Math.abs((firstCityCoordinate1 - secondCityCoordinate1)));
		    degrees = Math.toDegrees(Math.atan(tang)) + 270;
		    
		    degrees += 0.05*degrees;
	    }
	     
	    return degrees;
		
	}
	
	/**
	 * Ustawianie danych użytkownika z bazy loklanej
	 * @param currentLoggedUser zalogowany użytkownik
	 * @throws Exception
	 */
	public void setExternalDatabaseConnectionProperty(User currentLoggedUser) throws Exception
	{
		if( currentLoggedUser != null )
		{
			DAO_TransVis.setExternalDatabaseConnectionProperty(currentLoggedUser.getServerAddress(), 
				currentLoggedUser.getServerPort(),currentLoggedUser.getDatabaseName(), 
				currentLoggedUser.getDatabaseLogin(), currentLoggedUser.getDatabasePassword());
		}
		else
			throw new Exception("Użytkownik nie został zalogowany."); //nie powinno się zdarzyć.
	}
	
	
	/**
	 * Ustaw wybranego przewoźnika z wizualizacji
	 * @param t wybrany przewoźnik
	 */
	public void setChosenTransporter(Transporter t)
	{
		chosen_transporter = t;
	}
	
	/**
	 * Pobierz wybranego przewoźnika
	 * @return wybrany przewoźnik
	 */
	public Transporter getChosenTransporter()
	{
		return chosen_transporter;
	}
	
	/**
	 * @return Kolekcja nazw miast z bazy danych
	 */
	public ArrayList<String> getAllCityNames()
	{
		return DAO_TransVis.getAllCityNames();
	}

	/**
	 * Ustaw tryb otwarcia wizualiazcji przewoźników
	 * @param flag flaga otwarcia: -1 - tylko przegladanie przewoznikow, 1 -konczaca sie zapisem trasy do bazy
	 */
	public void setOpeningFlag(int flag)
	{
		if(flag == 1 || flag == -1)
		{
			opening_flag = flag;
		}
		else
		{
			opening_flag = 0;
		}
	}
	
	/**
	 * @return flaga otwarcia wizualizacji
	 */
	public int getOpeningFlag()
	{
		return opening_flag;
	}
	
	public double getMax_num_of_orders()
	{
		return max_num_of_orders;
	}

	public double getMin_num_of_orders()
	{
		return min_num_of_orders;
	}

	public double getMax_cost()
	{
		return max_cost;
	}

	public double getMin_cost()
	{
		return min_cost;
	}

	public double getMax_volume()
	{
		return max_volume;
	}

	public double getMax_capacity()
	{
		return max_capacity;
	}



}
