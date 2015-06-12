package visualisations.Transporters;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.Collectors;

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

import dataModels.SizeCategory;
import dataModels.Transporter;
import dataModels.User;
import database.DataAccessObjectFactory;
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
	private String cityFrom;
	
	/**
	 * Miasto końcowe
	 */
	private String cityTo;
	
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
	 * zapamietane mapy dla przewoznikow
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
	}
	
	private void getTranspotersFormDatabase(String city_from, String city_to)
	{
		//pobieranie listy przewoźnikow z bazy 
		//transporters = DAO_TransVis.getTranspoters(String city_from, String city_to);
		//na razie tworzę sam
		///
				transporters.clear();
				transporters.add(new Transporter(1,SizeCategory.SMALL,430,560,200,300,0.0,0,"Mark-Trans Marek Kaw",123456789));
				transporters.add(new Transporter(2,SizeCategory.MEDIUM,30,800,400,600,0.1,0.1,"Max-Przeprowadzki",123456789));
				transporters.add(new Transporter(3,SizeCategory.MEDIUM,100,670,330,900,0.3,0.01,"Quality Logistics",123456789));
				transporters.add(new Transporter(4,SizeCategory.BIG,1000,1000,1000,2000,0.01,0.03,"Magnatt Transport Dr",123456789));
				transporters.add(new Transporter(5,SizeCategory.BIG,213,344,808,3000,0.12,0.022,"Dariusz Kowal Firma",123456789));
				transporters.add(new Transporter(6,SizeCategory.BIG,20,607,700,200,0.3,0.001,"RJ-TRANS Usługi Tran",123456789));
				transporters.add(new Transporter(7,SizeCategory.SMALL,200,450,180,200,0.02,0.008,"Auto Mag Usługi Tran",123456789));
				transporters.add(new Transporter(8,SizeCategory.SMALL,100,300,100,200,0.0,0.01,"Admira Michał Dziadc",123456789));
				transporters.add(new Transporter(9,SizeCategory.SMALL,80,600,280,380,0.1,0,"Margo-Express Malgor",123456789));
				transporters.add(new Transporter(10,SizeCategory.SMALL,830,570,500,500,0.05,0.04,"Bortrans Gabriela Bo",123456789));
				transporters.add(new Transporter(11,SizeCategory.MEDIUM,200,900,400,700,0,0,"A. Jezierski Sp. z o",123456789));
				transporters.add(new Transporter(12,SizeCategory.MEDIUM,300,770,470,900,0.2,0.0,"A. Jadanowski Autoan",123456789));
				transporters.add(new Transporter(13,SizeCategory.MEDIUM,400,700,500,720,0.1,0,"Net Transport i Sped",123456789));
				transporters.add(new Transporter(14,SizeCategory.MEDIUM,500,740,630,770,0.07,0.015,"Putek Zdzisław Przed",123456789));
				transporters.add(new Transporter(15,SizeCategory.MEDIUM,670,780,700,840,0.06,0,"Reno-Trans Sp. z o.o",123456789));
				transporters.add(new Transporter(16,SizeCategory.MEDIUM,800,880,680,940,0.02,0,"Bracia Tyszka",123456789));
				transporters.add(new Transporter(1,SizeCategory.BIG,1200,1100,1100,2000,0.01,0.0004,"Mark-Trans Marek Kaw",123456789));
				transporters.add(new Transporter(2,SizeCategory.BIG,2013,1700,1500,3000,0.12,0.09,"Max-Przeprowadzki",123456789));
				transporters.add(new Transporter(3,SizeCategory.BIG,1400,1300,1200,4000,0.07,0.6,"Quality Logistics",123456789));
				//System.out.println("Przed: "+transporters);
		///
	}
	
	private void sortTransporters()
	{
		transporters = transporters.stream().sorted(Transporter::compareByCapacity).collect(Collectors.toCollection(ArrayList::new));
		//System.out.println("Po: "+transporters);
	}
	
	private void filterTransporters(SizeCategory sc)
	{
		transporters_filtered = transporters.stream().filter( t -> (t.getSizeCategory() == sc || sc == SizeCategory.ALL)).collect(Collectors.toCollection(ArrayList::new));
	}
	
	public ArrayList<Transporter> getFilteredTransporters(String city_from, String city_to, SizeCategory sc)
	{
		if(!city_from.equals(cityFrom) || !city_to.equals(cityTo))
		{
			cityFrom = city_from;
			cityTo = city_to;
			getTranspotersFormDatabase(cityFrom, cityTo);
			sortTransporters();
		}
		
		if(size_category!=sc)
		{
			filterTransporters(sc);
		}
		return transporters_filtered;
	}
	
	public Transporter getTransporter(int id_trans)
	{
		for(Transporter t: transporters_filtered)
		{
			if(t.getId_trans() == id_trans)
			{
				return t;
			}
		}
		return null;
	}
	
	
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
		
		
		for(ArrayList<Object> row: routes)
		{
			
			String cityNameFrom = (String) row.get(0);
			String cityNameTo = (String) row.get(1);
			double number_of_orders = ((long)row.get(2));
			
			//znajdz indeksy miast
			int ind_1 = 0;
			int ind_2 = 0;
			
			//System.out.println(cityNameFrom+" "+cityNameTo+" "+number_of_orders+" ");
			
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
			 
			 double orders_ratio =  number_of_orders/max_num_of_orders;

			
			int red = orders_ratio<=0.25? 128 :
				 	  orders_ratio<=0.5?(int)(-512 * orders_ratio + 256):
				 	  orders_ratio<=0.75?(int)(1020*orders_ratio - 510):
				 	  orders_ratio<=1.0? 255: 0;
			//int green = orders_ratio<=0.75? 255 :
			//			orders_ratio<=1.0? (int)(-1020 * orders_ratio + 1020): 0;
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
		//	graphicsLayer.addGraphic(new Graphic(firstCityLocation, circle));	
		//	graphicsLayer.addGraphic(new Graphic(secondCityLocation, circle));
			graphicsLayer.addGraphic(new Graphic(polyline, polyLineStyle));
			graphicsLayer.addGraphic(new Graphic(secondCityLocation, triangle));
		}
		
		
		
		/*//utworz i zapamietaj obiekty trasy w kolekcji miast na trasie path_cities
		City city_from = new City(cityNameFrom, firstCityCoordinate1, firstCityCoordinate2);
		City city_to = new City(cityNameTo, secondCityCoordinate1, secondCityCoordinate2);
		path_cities.add(city_from);
		path_cities.add(city_to);
		*/
	}
	
	
	private int getMaxNumberOfOrders( ArrayList<ArrayList<Object>>routes)
	{
			long max = 0;
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
	 * @author Lukasz Dudaszek
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


}
