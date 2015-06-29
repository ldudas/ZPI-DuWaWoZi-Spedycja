package manufacturers.decorators;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import shared.dataModels.City;
import shared.dataModels.Manufacturer;
import manufacturers.jPanels.info.DiscriptionOnMapJPanel;
import manufacturers.jPanels.info.PieChartJPanel;

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

import database.DataAccessObjectFactory;
import database.DataAccessObjectManufacturersVisualisation;

/**
 * Dekorator Producenta, dodaje na mapie 
 * obiekty producentów.
 * @author Kamil
 *
 */
public class VisualisationManufactureDecorator extends JMapDecorator
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private DataAccessObjectManufacturersVisualisation DAO_ManufacturersVis;

	/**
	 * Poziom przybliżenia na mapie dla każdego miasta. 
	 */
	private final static int DISPLAY_AREA_OF_CITY_ON_MAP = 30000;
	
	/**
	 * Symbol który będzie reprezentował producenta na mapie
	 */
	private SimpleMarkerSymbol symbol;
	
	private DiscriptionOnMapJPanel discription;
	private PieChartJPanel pieChart;
	
	
	public VisualisationManufactureDecorator(final City city,final ArrayList<Manufacturer> manfacturers)
	{
		DataAccessObjectFactory factory = new DataAccessObjectFactory();
		DAO_ManufacturersVis = factory.getDataAccessObjectManufacturersVisualisation();
		decorateMapWithVisualisationManufacturersInCity(city, manfacturers);
		discription = new DiscriptionOnMapJPanel();
		add(discription);
		
		pieChart = new PieChartJPanel();
		pieChart.setVisible(false);
		add(pieChart);
		
	}
	
	public VisualisationManufactureDecorator()
	{
	}
	
	
	/**
	 * Metoda tworząca mapę z przybliżeniem na określone miasto podane jako pierwszy parametr.
	 * Na mape nanoszone są obiekty, w których zawarte są dane o producentach, obiekty
	 * przedstawiają ich aktywność oraz wyliczony zarobek.
	 * @param city - miasto/obszar miasta na którym są umieszczone obiekty producentów
	 * @param manfacturers - kolekcja producentów którzy mają być umieszczeni na mapie
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
			  event.getMap().addMapOverlay(addResponseToMouseClick(graphicsLayer,manfacturers)); 
		  }

		  @Override
		  public void mapDispose(MapEvent arg0) {}

		  @Override
		  public void mapExtentChanged(MapEvent arg0) {}
		 });
	}
	

	/**
	 * Metoda dodająca listenery na każdy obiekt umieszczony w podanej warstwie,
	 * czyli wszystkie obiekty producentów. Zwracana jest warstwa kolizji stworzona
	 * na podstawie warstwy podanej.
	 * @param graphicsLayer - warstwa mapy w której umieszone są obiekty producentów
	 * @param manfacturers - kolekcja producentów
	 * @return HitTestOverlay - warstwa odpowiedzialna za reakcje na przysiski myszy
	 */
	private HitTestOverlay addResponseToMouseClick(final GraphicsLayer graphicsLayer,final ArrayList<Manufacturer> manfacturers)
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
					graphicsLayer.setSelectionColor(new Color(255, 204, 0));
			        str.append(index++ + ") ");
			        // get the damaged place name value from the graphic
			        str.append(manufacturer.getAttributeValue("Name"));
			        str.append(NEW_LINE_SEP);
		        }
				
				discription.getDiscriptionArea().setText("");
				discription.getDiscriptionArea().setText(str.toString());
									
			}
		  });
		  
		  hitTestOverlay.addMouseListener(new MouseAdapter() 
		  {
			    private Timer timer;
			    
		        @Override
		        public void mousePressed(MouseEvent e) 
		        {
		        	if(timer == null)
                        timer = new Timer();

                    timer.schedule(new TimerTask()
                    {
                        public void run()
                        {
                        	List<Feature> hitFeatures = hitTestOverlay.getHitFeatures();
                		    
            				if( hitFeatures!= null && hitFeatures.size() == 1)
            				{
            					discription.setVisible(false);
            					
            					int [] id_ofSelectedManufacturers = null;
            					Manufacturer currentMan = null;

            					id_ofSelectedManufacturers = graphicsLayer.getSelectionIDs(); //pobieramy indeksy zaznaczonych obiektow
            					
            					if( id_ofSelectedManufacturers.length == 1 )//jesli zaznaczony tylko jeden obiekt to pobierz jego atrybuty
            					{
            						Graphic graphic = graphicsLayer.getGraphic(id_ofSelectedManufacturers[0]);
            						String ID = (String) graphic.getAttributes().get("ID");
            						
            						for( Manufacturer man : manfacturers)
            						{
            							if( man.getID().equals(ID))
            							{
            								currentMan = man;
            								break;
            							}  							
            						}
            						
            						pieChart.setManufacturerDataOnChart(currentMan);
            					}			
            					
            					pieChart.setVisible(true);
            				}
                        }
                    },500,500);
		        }
		        @Override
		        public void mouseReleased(MouseEvent e) 
		        {
		        	discription.setVisible(true);
					pieChart.setVisible(false);
		        	if(timer != null)
	                {
	                    timer.cancel();
	                    timer = null;
	                }
		        }
		  });
		  
		  return hitTestOverlay;
	}
	
	/**
	 * Dodaje elementy wizualizacji dotyczące producentów oraz dodaje wszystkie informacje 
	 * o producencie do Mapy atrybutów danego elementu grafiki.
	 * @param mapSR
	 * @param graphicsLayer
	 * @param manufacturers
	 * @author Kamil Zimny
	 */
	public void addManufacturerGraphicOnMap(final SpatialReference mapSR,final GraphicsLayer graphicsLayer, final ArrayList<Manufacturer> manufacturers)
	{
		 //dla kazdego producenta
		for(int i=0 ; i<manufacturers.size() ; i++)
		{
			//Dane producenta
			Map<String,Object> attributes = new HashMap<String, Object>();
			attributes.put("Name", manufacturers.get(i).getName());
			attributes.put("ID", manufacturers.get(i).getID());
			
			//Wartosc aktywnosci
			Color activityColor = manufacturers.get(i).getRankOfDailyProfit();		
			int sizeOfSymbol = manufacturers.get(i).getRankOfNumberOfOrders(); 
			
			symbol = new SimpleMarkerSymbol(activityColor, sizeOfSymbol,
											SimpleMarkerSymbol.Style.CIRCLE);
			
			Point manufacturerLocation = 
					GeometryEngine.project(manufacturers.get(i).getLongtitude(), manufacturers.get(i).getLatitude(), mapSR);
						
			graphicsLayer.addGraphic(new Graphic(manufacturerLocation, symbol, attributes));
		}
		
	}
	
	/**
	 * Ustawia powiększenie mapy na określony region o określonej
	 * przez parametr DISPLAY_AREA_OF_CITY_ON_MAP skali przybliżenia.
	 * @return NULL OR Envelope 
	 * @author Kamil Zimny 
	 */
	private Envelope setCityAreaToDisplay(final SpatialReference mapSR, final City city)
	{		 
		 Point cityPoint = GeometryEngine.project(city.getLongtitude(), city.getLatitude(), mapSR);
		 
		 return new Envelope(cityPoint,DISPLAY_AREA_OF_CITY_ON_MAP,DISPLAY_AREA_OF_CITY_ON_MAP );
	}
	



}
