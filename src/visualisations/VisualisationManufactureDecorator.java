package visualisations;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

import com.esri.client.toolkit.overlays.InfoPopupOverlay;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Feature;
import com.esri.core.map.Graphic;
import com.esri.core.map.popup.PopupMediaInfo;
import com.esri.core.map.popup.PopupMediaValue;
import com.esri.core.map.popup.PopupMediaValue.VALUE_TYPE;
import com.esri.core.portal.WebMapPopupInfo;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.map.ArcGISFeatureLayer;
import com.esri.map.ArcGISPopupInfo;
import com.esri.map.ArcGISTiledMapServiceLayer;
import com.esri.map.GraphicsLayer;
import com.esri.map.JMap;
import com.esri.map.MapEvent;
import com.esri.map.MapEventListener;
import com.esri.map.popup.PopupMediaView;
import com.esri.map.popup.PopupView;
import com.esri.toolkit.overlays.HitTestEvent;
import com.esri.toolkit.overlays.HitTestListener;
import com.esri.toolkit.overlays.HitTestOverlay;

import database.DataAccessObjectFactory;
import database.DataAccessObjectManufacturersVisualisation;

public class VisualisationManufactureDecorator extends JMapDecorator
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DataAccessObjectManufacturersVisualisation DAO_ManufacturersVis;
	private final int DISPLAY_AREA_OF_CITY_ON_MAP = 30000;
	InfoPopupOverlay infoPopupOverlay;
	
	public VisualisationManufactureDecorator(String cityName)
	{
		DataAccessObjectFactory factory = new DataAccessObjectFactory();
		DAO_ManufacturersVis = factory.getDataAccessObjectManufacturersVisualisation();
		decorateMapWithVisualisationManufacturersInCity(cityName);
	}
	
	
	/**
	 * Metoda tworzaca mape z przyblizeniem na okreslone miasto.
	 * Na mape nanoszone sa obiekty w ktorych zawarte sa dane o producentach
	 * ich aktywnosci i przydatnosci wyboru.
	 * @return JMap
	 * @author Kamil Zimny
	 */
	public void decorateMapWithVisualisationManufacturersInCity(final String cityName)
	{				
		ArcGISTiledMapServiceLayer tiledLayer = new ArcGISTiledMapServiceLayer(
				  "http://services.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer");
		this.getLayers().add(tiledLayer);
		
		final GraphicsLayer graphicsLayer = new GraphicsLayer();
		graphicsLayer.setName("Manufacturers graphics");
		this.getLayers().add(graphicsLayer);
		
		this.addMapEventListener(new MapEventListener() 
		{

		  @Override
		  public void mapReady(MapEvent event) 
		  {
			  final SpatialReference mapSR = event.getMap().getSpatialReference();
			  // Ustawianie mapy na obszar	
			  event.getMap().setExtent(setCityAreaToDisplay(mapSR, cityName));
			  // Dodaje producentow na mapie
			  addManufacturerGraphicOnMap(mapSR, graphicsLayer, cityName);
			  // Reakcja na klikniecie myszy
			  event.getMap().addMapOverlay(addResponseToMouseClick(graphicsLayer)); 
			  event.getMap().addMapOverlay(addResponseToMouseClickInfo(graphicsLayer));
		  }

		  @Override
		  public void mapDispose(MapEvent arg0) {}

		  @Override
		  public void mapExtentChanged(MapEvent arg0) {}
		 });
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
					graphicsLayer.select( (int)manufacturer.getId());
					graphicsLayer.setSelectionColor(Color.BLUE);
					
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
		  infoPopupOverlay = new InfoPopupOverlay(); 
		  infoPopupOverlay.setName("Info");
		  infoPopupOverlay.setPopupTitle("Dane producenta");
		  infoPopupOverlay.addLayer(graphicsLayer);

		  return infoPopupOverlay;
		 
	}
	
	/**
	 * Dodaje elementy wizualizacji dotyczace producentow, oraz dodaje wszystkie informacje 
	 * o producencie do Mapy atrybutow danego elementu grafiki. Zawartosc danych producenta
	 * opisana ponizej.
	 * @attributes HashMap (String, String) attr:
	 * <br>attr.get("Nazwa: ") -> nazwa
	 * <br>attr.get("Ostatnia aktywnoœæ: ") -> ostatnia aktywnosc
	 * <br>attr.get("Liczba zleceñ: ") -> liczba zlecen
	 * <br>attr.get("Suma wartoœci zleceñ: ") -> suma wartosci zlecen
	 * <br>attr.get("Suma dni wykonywanych zleceñ: ") -> suma dni wykonywanych zlecen
	 * <br>attr.get("Telefon: ") -> telefon
	 * @author Kamil Zimny
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
			attributes.put("Nazwa: ", manufacturersData.get(i).get(0));
			attributes.put("Ostatnia aktywnoœæ: ", manufacturersData.get(i).get(3));
			attributes.put("Liczba zleceñ: ", manufacturersData.get(i).get(4));
			attributes.put("Suma wartoœci zleceñ: ", manufacturersData.get(i).get(5));
			attributes.put("Suma dni wykonywanych zleceñ: ", manufacturersData.get(i).get(6));
			attributes.put("Telefon: ", manufacturersData.get(i).get(7));
			
			//Wartosc aktywnosci
			Color activityColor = new Color(255,activityOfManufacturers.get(i).intValue(),activityOfManufacturers.get(i).intValue());		
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
		int activityValueOfTheBest = 0;
		int activityValueOfTheWorst = 255;
		
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
			double evaluationOfManufacturers;
			if(activityOfManufacturers.get(i) ==  activityValueOfTheBest)
				evaluationOfManufacturers = 0;
			else	
				evaluationOfManufacturers = activityValueOfTheWorst- activityOfManufacturers.get(i)/theBestEvaluation*activityValueOfTheWorst;
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


}
