package decorators;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jpanels.DiscriptionOnMapJPanel;

import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Feature;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.map.ArcGISTiledMapServiceLayer;
import com.esri.map.GraphicsLayer;
import com.esri.map.MapEvent;
import com.esri.map.MapEventListener;
import com.esri.toolkit.overlays.HitTestEvent;
import com.esri.toolkit.overlays.HitTestListener;
import com.esri.toolkit.overlays.HitTestOverlay;

import dataModels.City;
import dataModels.Manufacturer;
import database.DataAccessObjectFactory;
import database.DataAccessObjectManufacturersVisualisation;

public class VisualisationManufactureDecorator extends JMapDecorator
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private DataAccessObjectManufacturersVisualisation DAO_ManufacturersVis;
	private final static int DISPLAY_AREA_OF_CITY_ON_MAP = 30000;
	private SimpleMarkerSymbol symbol;
	private DiscriptionOnMapJPanel discription;
	
	public VisualisationManufactureDecorator(final City city,final ArrayList<Manufacturer> manfacturers)
	{
		DataAccessObjectFactory factory = new DataAccessObjectFactory();
		DAO_ManufacturersVis = factory.getDataAccessObjectManufacturersVisualisation();
		decorateMapWithVisualisationManufacturersInCity(city, manfacturers);
		discription = new DiscriptionOnMapJPanel();
		add(discription);
	}
	
	
	/**
	 * Metoda tworzaca mape z przyblizeniem na okreslone miasto.
	 * Na mape nanoszone sa obiekty w ktorych zawarte sa dane o producentach
	 * ich aktywnosci i przydatnosci wyboru.
	 * @return JMap
	 * @author Kamil Zimny
	 */
	public void decorateMapWithVisualisationManufacturersInCity(final City city,final ArrayList<Manufacturer> manfacturers)
	{				
		ArcGISTiledMapServiceLayer tiledLayer = new ArcGISTiledMapServiceLayer(
				  "http://services.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer");
		this.getLayers().add(tiledLayer);
		
		final GraphicsLayer graphicsLayer = new GraphicsLayer();
		graphicsLayer.setRenderer(new SimpleRenderer(symbol));
		graphicsLayer.setName("Manufacturers graphics");
		this.getLayers().add(graphicsLayer);
		
		this.addMapEventListener(new MapEventListener() 
		{

		  @Override
		  public void mapReady(MapEvent event) 
		  {
			  final SpatialReference mapSR = event.getMap().getSpatialReference();
			  // Ustawianie mapy na obszar	
			  event.getMap().setExtent(setCityAreaToDisplay(mapSR, city));
			  // Dodaje producentow na mapie
			  addManufacturerGraphicOnMap(mapSR, graphicsLayer, manfacturers);
			  // Reakcja na klikniecie myszy
			  event.getMap().addMapOverlay(addResponseToMouseClick(graphicsLayer)); 
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
			String NEW_LINE_SEP = System.getProperty("line.separator");
			
			@Override
			public void featureHit(HitTestEvent arg0) 
			{		
				graphicsLayer.clearSelection();
				List<Feature> hitFeatures = hitTestOverlay.getHitFeatures();
				
				StringBuilder str = new StringBuilder();
		        str.append("Zaznaczono producentów: " + hitFeatures.size());
		        str.append(NEW_LINE_SEP);
		        str.append(NEW_LINE_SEP);
		        int index = 1;
		        
				for (Feature manufacturer : hitFeatures) 
		        {	  
					graphicsLayer.select( (int)manufacturer.getId());
					graphicsLayer.setSelectionColor(Color.BLUE);
			        str.append(index++ + ") ");
			        // get the damaged place name value from the graphic
			        str.append(manufacturer.getAttributeValue("Name"));
			        str.append(NEW_LINE_SEP);

		        }
				discription.getDiscriptionArea().setText("");
				discription.getDiscriptionArea().setText(str.toString());
									
			}
		  });
		  return hitTestOverlay;
	}
	
	
	/**
	 * Dodaje elementy wizualizacji dotyczace producentow, oraz dodaje wszystkie informacje 
	 * o producencie do Mapy atrybutow danego elementu grafiki. Zawartosc danych producenta
	 * opisana ponizej.
	 * @attributes HashMap (String, String) attr:
	 * <br>attr.get("Name") -> nazwa 
	 * <br>attr.get("LastActivity") -> ostatnia aktywnosc
	 * <br>attr.get("NumberOfOrders") -> liczba zlecen
	 * <br>attr.get("SumOfOrders") -> suma wartosci zlecen
	 * <br>attr.get("SumOfDays") -> suma dni wykonywanych zlecen
	 * <br>attr.get("Phone") -> telefon
	 * * <br>attr.get("ID") -> identyfikator 
	 * @author Kamil Zimny
	 */
	private void addManufacturerGraphicOnMap(final SpatialReference mapSR,final GraphicsLayer graphicsLayer, final ArrayList<Manufacturer> manufacturers)
	{

		 //dla kazdego producenta
		for(int i=0 ; i<manufacturers.size() ; i++)
		{
			//Dane producenta
			Map<String,Object> attributes = new HashMap<String, Object>();
			attributes.put("Name", manufacturers.get(i).getName());
			attributes.put("ID", manufacturers.get(i).getID());
			
			//Wartosc aktywnosci
			Color activityColor = new Color(255,manufacturers.get(i).getRankOfDailyProfit(),manufacturers.get(i).getRankOfDailyProfit());		
			int sizeOfSymbol = manufacturers.get(i).getRankOfNumberOfOrders(); 
			
			symbol = new SimpleMarkerSymbol(activityColor, sizeOfSymbol,
											SimpleMarkerSymbol.Style.CIRCLE);
			
			Point manufacturerLocation = 
					GeometryEngine.project(manufacturers.get(i).getLongtitude(), manufacturers.get(i).getLatitude(), mapSR);
						
			graphicsLayer.addGraphic(new Graphic(manufacturerLocation, symbol, attributes));
		}
		
	}
	
	/**
	 * Ustawia powiekszenie mapy na okreslony region o okreslonej
	 * przez parametr DISPLAY_AREA_OF_CITY_ON_MAP skali przyblizenia.
	 * @return NULL OR Envelope 
	 * @author Kamil Zimny 
	 */
	private Envelope setCityAreaToDisplay(final SpatialReference mapSR, final City city)
	{		 
		 Point cityPoint = GeometryEngine.project(city.getLongtitude(), city.getLatitude(), mapSR);
		 
		 return new Envelope(cityPoint,DISPLAY_AREA_OF_CITY_ON_MAP,DISPLAY_AREA_OF_CITY_ON_MAP );
	}
	



}
