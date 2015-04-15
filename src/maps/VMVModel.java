package maps;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.esri.client.toolkit.overlays.InfoPopupOverlay;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Feature;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.map.ArcGISTiledMapServiceLayer;
import com.esri.map.GraphicsLayer;
import com.esri.map.JMap;
import com.esri.map.MapEvent;
import com.esri.map.MapEventListener;
import com.esri.toolkit.overlays.HitTestEvent;
import com.esri.toolkit.overlays.HitTestListener;
import com.esri.toolkit.overlays.HitTestOverlay;
import database.DataAccessObjectFactory;
import database.DataAccessObjectManufacturersVisualisation;

@SuppressWarnings("deprecation")
public class VMVModel 
{
	private DataAccessObjectManufacturersVisualisation DAO_ManufacturersVis;
	private final int DISPLAY_AREA_OF_CITY_ON_MAP = 30000;
	
	public VMVModel()
	{
		DataAccessObjectFactory factory = new DataAccessObjectFactory();
		DAO_ManufacturersVis = factory.getDataAccessObjectManufacturersVisualisation();
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
		final JMap map = new JMap();
		ArcGISTiledMapServiceLayer tiledLayer = new ArcGISTiledMapServiceLayer(
				  "http://services.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer");
		map.getLayers().add(tiledLayer);
		
		final GraphicsLayer graphicsLayer = new GraphicsLayer();
		graphicsLayer.setName("Manufacturers graphics");
		map.getLayers().add(graphicsLayer);
		
		map.addMapEventListener(new MapEventListener() 
		{

		  @Override
		  public void mapReady(MapEvent event) 
		  {
			  final SpatialReference mapSR = event.getMap().getSpatialReference();
			  // Ustawianie mapy na obszar	
			  map.setExtent(setCityAreaToDisplay(mapSR, cityName));
			  // Dodaje producentow na mapie
			  addManufacturerGraphicOnMap(mapSR, graphicsLayer, cityName);
			  // Reakcja na klikniecie myszy
			  map.addMapOverlay(addResponseToMouseClick(graphicsLayer)); 
			  map.addMapOverlay(addResponseToMouseClickInfo(graphicsLayer));		 
		  }

		  @Override
		  public void mapDispose(MapEvent arg0) {}

		  @Override
		  public void mapExtentChanged(MapEvent arg0) {}
		 });
		return map;
	}
	
	 
	
	/**
	 * Metoda dodajaca listenery na kazdy obiekt umieszczony w podanej warstwie,
	 * czyli wszystkie obiekty producentow.
	 * @return HitTestOverlay
	 * @author Kamil Zimny
	 */
	private HitTestOverlay addResponseToMouseClick(final GraphicsLayer graphicsLayer)
	{
		  final HitTestOverlay hitTestOverlay = new HitTestOverlay(graphicsLayer);
		  hitTestOverlay.addHitTestListener(new HitTestListener() 
		  {							
			@Override
			public void featureHit(HitTestEvent arg0) 
			{		
				graphicsLayer.clearSelection();
				
				List<Feature> hitFeatures = hitTestOverlay.getHitFeatures();
				for (Feature manufacturer : hitFeatures) 
		        {	
					//tu ma byc pojawianie sie nowego okna z danymi producenta
					//w parametrze przekazywana kolekcja Map atrybutow danego producenta
					Map<String, Object> attributes = manufacturer.getAttributes();
					testReactionAferClick(attributes);					
		        }
									
			}
		  });
		  return hitTestOverlay;
	}
	
	/**
	 * Metoda dodajaca listenery na kazdy obiekt umieszczony w podanej warstwie,
	 * czyli wszystkie obiekty.
	 * @return InfoPopupOverlay
	 * @author Kamil Zimny
	 */
	private InfoPopupOverlay addResponseToMouseClickInfo(final GraphicsLayer graphicsLayer)
	{
		  final InfoPopupOverlay infoPopupOverlay = new InfoPopupOverlay(); 
		  infoPopupOverlay.setPopupTitle("Dane producenta");
		  infoPopupOverlay.addLayer(graphicsLayer);

		  return infoPopupOverlay;
	}
	
	/**
	 * Dodaje elementy wizualizacji dotyczace producentow, oraz dodaje wszystkie informacje 
	 * o producencie do Mapy atrybutow danego elementu grafiki. Zawartosc danych producenta
	 * opisana ponizej.
	 * @attributes HashMap (String, String) attr:
	 * <br>attr.get("Name") -> nazwa
	 * <br>attr.get("LastActivity") -> ostatnia aktywnosc
	 * <br>attr.get("NumberOfOrders") -> liczba zlecen
	 * <br>attr.get("TotalValuOfOrder") -> suma wartosci zlecen
	 * <br>attr.get("TotalDays") -> suma dni wykonywanych zlecen
	 */
	private void addManufacturerGraphicOnMap(final SpatialReference mapSR,final GraphicsLayer graphicsLayer, final String cityName)
	{
		ArrayList<ArrayList<String>> manufacturersData = 
				DAO_ManufacturersVis.getDataAboutManufacturerToVizualization(cityName);
		if( manufacturersData == null )
			return;
		//Obliczanie aktywnosci producentow
		ArrayList<Double> activityOfManufacturers = evaluateActivityOfManufacturers(manufacturersData);
		 //dla kazdego producenta
		for(int i=0 ; i<manufacturersData.size() ; i++)
		{
			//Dane producenta
			Map<String,Object> attributes = new HashMap<String, Object>();
			attributes.put("Name", manufacturersData.get(i).get(0));
			attributes.put("LastActivity", manufacturersData.get(i).get(3));
			attributes.put("NumberOfOrders", manufacturersData.get(i).get(4));
			attributes.put("TotalValuOfOrder", manufacturersData.get(i).get(5));
			attributes.put("TotalDays", manufacturersData.get(i).get(6));
			attributes.put("Phone", manufacturersData.get(i).get(7));
			
			//Wartosc aktywnosci
			Color activityColor = new Color(activityOfManufacturers.get(i).intValue(),0,0);		
			int sizeOfSymbol = 30; //Jesli wielkosc tez bedzie parametrem wizualizacji bedzie sie zmieniac
			
			SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(activityColor, sizeOfSymbol, 
											SimpleMarkerSymbol.Style.CIRCLE);
			
			Point manufacturerLocation = 
					GeometryEngine.project(parseCoordinate(manufacturersData.get(i).get(1)),
										   parseCoordinate(manufacturersData.get(i).get(2)), 
										   mapSR);
						
			graphicsLayer.addGraphic(new Graphic(manufacturerLocation, symbol, attributes));
		}
		
	}
	
	/**
	 * Metoda obliczajaca aktywnosc kazdego producenta na podstawie danych pobranych z bazy.
	 * @return ArrayList<Double> 
	 * <br> Tablica wartosci obliczonych dla kazdego producenta.
	 * @author Kamil Zimny
	 */
	private ArrayList<Double> evaluateActivityOfManufacturers(ArrayList<ArrayList<String>> manufacturersData)
	{
		int activityValueOfTheBest = 255;
		
		ArrayList<Double> activityOfManufacturers = new ArrayList<Double>();
		double theBestEvaluation = -1;
		for(int i=0; i<manufacturersData.size() ; i++)
		{
			int numberOfOr = Integer.parseInt(manufacturersData.get(i).get(4));
			double totalValue = Double.parseDouble(manufacturersData.get(i).get(5));
			int totalD = Integer.parseInt(manufacturersData.get(i).get(6));
			double evaluationOfManufacturers = numberOfOr*totalValue/totalD;
			
			if( evaluationOfManufacturers > theBestEvaluation)
				theBestEvaluation = evaluationOfManufacturers;
			
			activityOfManufacturers.add( evaluationOfManufacturers ); 
		}

		for(int i=0; i<manufacturersData.size() ; i++)
		{
			double evaluationOfManufacturers = activityOfManufacturers.get(i)/theBestEvaluation*activityValueOfTheBest;
			activityOfManufacturers.set(i,evaluationOfManufacturers);
		}
		return activityOfManufacturers;
	}
	
	/**
	 * Ustawia powiekszenie mapy na okreslony region o okreslonej
	 * przez parametr DISPLAY_AREA_OF_CITY_ON_MAP skali przyblizenia.
	 * @return NULL OR Envelope 
	 * @author Kamil Zimny 
	 */
	private Envelope setCityAreaToDisplay(final SpatialReference mapSR, final String cityName)
	{
		 String [] cityCoordinates = DAO_ManufacturersVis.getCityCoordinates(cityName);
		 
		 if( cityCoordinates == null )
			 return null;
		 Point city = GeometryEngine.project(parseCoordinate(cityCoordinates[0]), 
				 							 parseCoordinate(cityCoordinates[1]), mapSR);
		 
		 return new Envelope(city,DISPLAY_AREA_OF_CITY_ON_MAP,DISPLAY_AREA_OF_CITY_ON_MAP );
	}
	
	/**
	 * Zamiana wspolrzednej geograficznej na liczbe dziesietna 
	 * odpowiadajaca tej wspolrzednej (tylko stopnie i minuty).
	 * @return double coordiante
	 * @author Piotr Wo³oszyk
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

	
	private void testReactionAferClick(Map<String, Object> attr)
	{  
		System.out.println("");
		System.out.println("Nazwa " + attr.get("Name"));
		System.out.println("Telefon " + attr.get("Phone"));
		System.out.println("Ostatnia aktywnosc " + attr.get("LastActivity"));
		System.out.println("Liczba zlecen " + attr.get("NumberOfOrders"));
		System.out.println("Suma wartosci zlecen " + attr.get("TotalValuOfOrder"));
		System.out.println("Suma dni wykonywanych zlecen " + attr.get("TotalDays"));
	}
	
}