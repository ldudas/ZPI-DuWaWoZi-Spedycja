package visualisations.Path;
import java.awt.Color;
import java.util.ArrayList;

import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.*;
import com.esri.core.symbol.SimpleMarkerSymbol.Style;
import com.esri.map.ArcGISTiledMapServiceLayer;
import com.esri.map.GraphicsLayer;
import com.esri.map.JMap;
import com.esri.map.MapEvent;
import com.esri.map.MapEventListener;

import dataModels.City;
import dataModels.User;
import database.DataAccessObjectFactory;
import database.DataAccessObjectPathVisualisation;


public class VisualisationPathModel 
{
	
	
	/**
	 * Data Access Object dla wizualizacji trasy
	 */
	private DataAccessObjectPathVisualisation DAO_PathVis;
	
	/**
	 * Kolekcja zawierajaca wspolrzedne wszystkich miast w bazie
	 * get(i) -> koleckja danych konkretnego miasta
	 * get(i).get(0) -> nazwa miasta jako String (wystarczy rzutowac)
	 * get(i).get(1) -> dlugosc geograficzna jako String (wystarczy rzutowac)
	 * get(i).get(2) -> szerokosc geograficzna (wystarczy rzutowac)
	 */
	private ArrayList<ArrayList<Object>> cities_coordinates;
	
	/**
	 * Mapa z naniesionymi grafikami wizualizujacymi trase
	 */ 
	private JMap path_map;
	
	/**
	 * Warstwa mapy na ktora nanosi sie grafiki wizualizacyjne
	 */ 
	private GraphicsLayer graphicsLayer;
	
	/**
	 * Lista kolejnych miast z trasy
	 */ 
	private ArrayList<City> path_cities;
	
	/**
	 * Lista indeksow obiektow na mapie dla miasta
	 */ 
	private ArrayList<ArrayList<Integer>> path_cities_vis_objects; 
	
	/**
	 * Liczba miast wchodząca w skład wizualizacji
	 */ 
	private int number_of_cities;
	
	
	
	
	
	public VisualisationPathModel()
	{
		DataAccessObjectFactory factory = new DataAccessObjectFactory();
		DAO_PathVis = factory.getDataAccessObjectPathVisualisation();
		path_cities = new ArrayList<City>();
		path_cities_vis_objects = new ArrayList<ArrayList<Integer>>();
		path_cities_vis_objects.add(new ArrayList<Integer>());
		path_cities_vis_objects.add(new ArrayList<Integer>());
		number_of_cities = 0;
	}
	
	public void setExternalDatabaseConnectionProperty(User currentLoggedUser) throws Exception
	{
		if( currentLoggedUser != null )
		{
			DAO_PathVis.setExternalDatabaseConnectionProperty(currentLoggedUser.getServerAddress(), 
				currentLoggedUser.getServerPort(),currentLoggedUser.getDatabaseName(), 
				currentLoggedUser.getDatabaseLogin(), currentLoggedUser.getDatabasePassword());
		}
		else
			throw new Exception("Użytkownik nie został zalogowany."); //nie powinno się zdarzyć.
	}
	
	
	/**
	 * Ustawia mape modelu jako mape z naniesionymi dwoma poczatkowymi                         
	 * miastami
	 * @param  cityNameFrom Nazwa miasta startu.    
	 * @param  cityNameFrom Nazwa miasta konca.        
	 * @author Łukasz Dudaszek
	 */
	public void createInitialMap(final String cityNameFrom, final String cityNameTo){
		path_map = getMapWithInitialCities(cityNameFrom, cityNameTo);
	}
	
	
	/**
	 * Zwraca aktualna mape modelu.
	 * @return Aktualna mapa modelu.        
	 * @author Łukasz Dudaszek
	 */
	public JMap getPathMap(){
		return path_map;
	}
	
	
	/**
	 * Tworzy mape z naniesionymi dwoma poczatkowymi                         
	 * miastami
	 * @param  cityNameFrom Nazwa miasta startu.    
	 * @param  cityNameFrom Nazwa miasta konca.    
	 * @return   Mapa z naniesionymi dwoma poczatkowymi miastami   
	 * @author Łukasz Dudaszek
	 */
	private JMap getMapWithInitialCities(final String cityNameFrom, final String cityNameTo)
	{
		//utworz mape
		final JMap map = new JMap();
		//pobierz warstwe mapy
		ArcGISTiledMapServiceLayer tiledLayer = new ArcGISTiledMapServiceLayer("http://services.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer");
		//dodaj pobrana warstwe do mapy
		map.getLayers().add(tiledLayer);
		
		//utworz warstwe graficzna
		graphicsLayer = new GraphicsLayer();
		graphicsLayer.setName("Path graphics");
		//dodaj warstwe graficzna do mapy
		map.getLayers().add(graphicsLayer);
		
		map.addMapEventListener(new MapEventListener() 
		{

		  @Override
		  public void mapReady(MapEvent event) 
		  {
			  final SpatialReference mapSR = event.getMap().getSpatialReference();
			  // Ustaw mape na obszar	
			  map.setExtent(new Envelope( GeometryEngine.project(19.46,52.0,mapSR),1000000,1000000));
			  // Dodaj wziualizacje trasy miedzy dwoma pierszymi miastami na mape
			  addInitialPathGraphicsOnMap(cityNameFrom, cityNameTo);	 
		  }

		  @Override
		  public void mapDispose(MapEvent arg0) {}

		  @Override
		  public void mapExtentChanged(MapEvent arg0) {}
		 });
		
	  return map;
	}
	
	
	/**
	 * Dodaje poczatkowe miasta na warstwe graficzna mapy
	 * @param  cityNameFrom Nazwa miasta startu.    
	 * @param  cityNameFrom Nazwa miasta konca.      
	 * @author Łukasz Dudaszek
	 */
	private void addInitialPathGraphicsOnMap(final String cityNameFrom, final String cityNameTo)
	{
		cities_coordinates = DAO_PathVis.getCitiesCoordinates();
		SpatialReference mapSR = path_map.getSpatialReference();
		
		//znajdz indeksy podanych miast cityNameTo, cityNameFrom
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
		double secondCityCoordinate1 = parseCoordinate((String)cities_coordinates.get(ind_2).get(1));
		double secondCityCoordinate2 =  parseCoordinate((String)cities_coordinates.get(ind_2).get(2));
		
		
		//utworz i zapamietaj obiekty trasy w kolekcji miast na trasie path_cities
		City city_from = new City(cityNameFrom, firstCityCoordinate1, firstCityCoordinate2);
		City city_to = new City(cityNameTo, secondCityCoordinate1, secondCityCoordinate2);
		path_cities.add(city_from);
		path_cities.add(city_to);
		
		
		//z wspolrzednych jako double utworz obiekty Ponit
		Point firstCityLocation = GeometryEngine.project(firstCityCoordinate1, firstCityCoordinate2, mapSR);
		Point secondCityLocation = GeometryEngine.project(secondCityCoordinate1,secondCityCoordinate2,  mapSR);

		
		//utworz linie miedzy punktami
		 Polyline polyline = new Polyline();
		 polyline.startPath(firstCityLocation);
		 polyline.lineTo(secondCityLocation);
		    
		 
		 //ustaw styl linii
		 SimpleLineSymbol polyLineStyle = new SimpleLineSymbol(Color.blue, 4);
		 polyLineStyle.setStyle(SimpleLineSymbol.Style.DASH);
		   
		 
		 //oblicz o ile stopni trzeba obrocic grafike grotu strzalki
		 double arrowRotationDegrees = get_arrow_rotation_degrees(firstCityCoordinate1, firstCityCoordinate2, secondCityCoordinate1, secondCityCoordinate2);
		     
		 //utworz grot strzalki
		 SimpleMarkerSymbol triangle = new SimpleMarkerSymbol(Color.BLUE, 16, Style.TRIANGLE);
		 triangle.setAngle((float)(arrowRotationDegrees));

		 //utworz kolo
		 SimpleMarkerSymbol circle = new SimpleMarkerSymbol(Color.black, 25, SimpleMarkerSymbol.Style.CIRCLE);
		 
		//dodaj dwa punkty jako kola, linie oraz grot jako trojkat do warsty graficznej	
		graphicsLayer.addGraphic(new Graphic(firstCityLocation, circle));	
		graphicsLayer.addGraphic(new Graphic(secondCityLocation, circle));
		graphicsLayer.addGraphic(new Graphic(polyline, polyLineStyle));
		graphicsLayer.addGraphic(new Graphic(secondCityLocation, triangle));
		
		//ustaw liczbe miast na 2
		number_of_cities = 2;
	}
	

	
	
	/**
	 * Dodaje kolejne miasto do wizualizacji
	 * @param  cityName Nazwa miasta do dodania.       
	 * @author Łukasz Dudaszek
	 */
	public void addCityToPath(String cityName)
	{
		cities_coordinates = DAO_PathVis.getCitiesCoordinates();
		SpatialReference mapSR = path_map.getSpatialReference();
		
		//znajdz indeks podanego miasta cityName
		int ind_2 = 0;
		for(int i=0; i<cities_coordinates.size();i++)
		{
			if(((String)cities_coordinates.get(i).get(0)).equals(cityName)) ind_2 = i;
		}
		
				
		//pobierz wspolrzedne miast z kolekcji city_coordinates i zamien je na double
		double secondCityCoordinate1 = parseCoordinate((String)cities_coordinates.get(ind_2).get(1));
		double secondCityCoordinate2 =  parseCoordinate((String)cities_coordinates.get(ind_2).get(2));
		
		
		//pobierz ostatnie miasto z dotychczasowej trasy
		City city_from = path_cities.get(path_cities.size()-1);
		
		//utworz i dodaj kolejne miasto do kolekcji miast na trasie
		City city_to = new City(cityName, secondCityCoordinate1, secondCityCoordinate2);
		path_cities.add(city_to);
		
		
		
		
		//pobierz jego wspolrzedne
		double firstCityCoordinate1 = city_from.getLatitude();
		double firstCityCoordinate2 = city_from.getLongtitude();
	
		//z wspolrzednych jako double utworz obiekty Ponit
		Point firstCityLocation = GeometryEngine.project(firstCityCoordinate1, firstCityCoordinate2, mapSR);
		Point secondCityLocation = GeometryEngine.project(secondCityCoordinate1,secondCityCoordinate2,  mapSR);

				
		//utworz linie miedzy punktami
		 Polyline polyline = new Polyline();
		 polyline.startPath(firstCityLocation);
		 polyline.lineTo(secondCityLocation);
		 
		 //ustaw styl linii
		 SimpleLineSymbol polyLineStyle = new SimpleLineSymbol(Color.blue, 4);
		 polyLineStyle.setStyle(SimpleLineSymbol.Style.DASH);
	    
	    
		 //oblicz o ile stopni trzeba obrocic grafike grotu strzalki
		 double arrowRotationDegrees = get_arrow_rotation_degrees(firstCityCoordinate1, firstCityCoordinate2, secondCityCoordinate1, secondCityCoordinate2);
	    
	    
		 //utworz grot strzalki
		 SimpleMarkerSymbol triangle = new SimpleMarkerSymbol(Color.BLUE, 16, Style.TRIANGLE);
		 triangle.setAngle((float)(arrowRotationDegrees));
		 
		 //utworz kolo
		 SimpleMarkerSymbol circle = new SimpleMarkerSymbol(Color.black, 25, SimpleMarkerSymbol.Style.CIRCLE);

		    
		//utworz jeden punkt jako kolo, linie oraz grot jako trojkat	
		Graphic g1 = new Graphic(secondCityLocation, circle);
		Graphic g2 = new Graphic(polyline, polyLineStyle);
		Graphic g3 = new Graphic(secondCityLocation, triangle);
		
		ArrayList<Integer> indexes = new ArrayList<Integer>();	
		
		//dodaj grafiki do warstwy (addGraphic zwraca index dodawanego obiektu) a index dodaj do indexes
		indexes.add(graphicsLayer.addGraphic(g1));
		indexes.add(graphicsLayer.addGraphic(g2));
		indexes.add(graphicsLayer.addGraphic(g3));
		
		//dodaj indexy obiektow do kolekcji path_cities_vis_obj dotyczace miasta o indexie odpowiadajacym miastu w kolekcji path_cities
		path_cities_vis_objects.add(indexes);
		number_of_cities++;
	}
	
	/**
	 * Usuwa ostatnie miasto z wizualizacji
	 * @param  cityName Nazwa miasta do dodania.       
	 * @author Łukasz Dudaszek
	 */
	public void removeLastCityFormPath()
	{
		if(number_of_cities>2)
		{
			number_of_cities--;
			
			ArrayList<Integer> idsLastCity = path_cities_vis_objects.get(number_of_cities);
			
			for(int i:idsLastCity)
			{
				graphicsLayer.removeGraphic(i);
			}
			
			path_cities.remove(number_of_cities);
			path_cities_vis_objects.remove(number_of_cities);
		}
		
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

}
