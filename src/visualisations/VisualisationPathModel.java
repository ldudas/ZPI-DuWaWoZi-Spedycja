package visualisations;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Map;

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

import dataModel.City;
import database.DataAccessObjectFactory;
import database.DataAccessObjectPathVisualisation;


public class VisualisationPathModel {
	
	private DataAccessObjectPathVisualisation DAO_PathVis;
	private ArrayList<ArrayList<Object>> cities_coordinates;
	private JMap path_map;
	private GraphicsLayer graphicsLayer;
	private ArrayList<City> path_cities;
	
	public VisualisationPathModel()
	{
		DataAccessObjectFactory factory = new DataAccessObjectFactory();
		DAO_PathVis = factory.getDataAccessObjectPathVisualisation();
		cities_coordinates = DAO_PathVis.getCitiesCoordinates();
		path_cities = new ArrayList<City>();
	}
	
	public void setInitialMap(final String cityNameFrom, final String cityNameTo){
		path_map = getMapWithInitialCities(cityNameFrom, cityNameTo);
	}
	
	public JMap getPathMap(){
		return path_map;
	}
	
	private JMap getMapWithInitialCities(final String cityNameFrom, final String cityNameTo)
	{
		final JMap map = new JMap();
		ArcGISTiledMapServiceLayer tiledLayer = new ArcGISTiledMapServiceLayer(
				  "http://services.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer");
		map.getLayers().add(tiledLayer);
		
		graphicsLayer = new GraphicsLayer();
		graphicsLayer.setName("Path graphics");
		map.getLayers().add(graphicsLayer);
		
		map.addMapEventListener(new MapEventListener() 
		{

		  @Override
		  public void mapReady(MapEvent event) 
		  {
			  final SpatialReference mapSR = event.getMap().getSpatialReference();
			  // Ustawianie mapy na obszar	
			  map.setExtent(new Envelope(
					  GeometryEngine.project(19.46,52.0,mapSR),1000000,1000000));
			  // Dodaje producentow na mapi
			  addInitialPathGraphicsOnMap(mapSR, graphicsLayer, cityNameFrom, cityNameTo);
			  // Reakcja na klikniecie myszy
			  //map.addMapOverlay(addResponseToMouseClick(graphicsLayer)); 
			  //map.addMapOverlay(addResponseToMouseClickInfo(graphicsLayer));		 
		  }

		  @Override
		  public void mapDispose(MapEvent arg0) {}

		  @Override
		  public void mapExtentChanged(MapEvent arg0) {}
		 });
		
	  return map;
	}
	
	private void addInitialPathGraphicsOnMap(final SpatialReference mapSR,final GraphicsLayer graphicsLayer, final String cityNameFrom, final String cityNameTo)
	{
		
		int ind_1 = 0;
		int ind_2 = 0;
		
		for(int i=0; i<cities_coordinates.size();i++)
		{
			if(((String)cities_coordinates.get(i).get(0)).equals(cityNameFrom)) ind_1 = i;
			if(((String)cities_coordinates.get(i).get(0)).equals(cityNameTo)) ind_2 = i;
		}
		
		if( cities_coordinates == null) return;
		
		SimpleMarkerSymbol symbol1 = new SimpleMarkerSymbol(Color.black, 25, 
				SimpleMarkerSymbol.Style.CIRCLE);
		
		SimpleMarkerSymbol symbol2 = new SimpleMarkerSymbol(Color.black, 25, 
				SimpleMarkerSymbol.Style.CIRCLE);
		
		double firstCityCoordinate1 = parseCoordinate((String)cities_coordinates.get(ind_1).get(1));
		double firstCityCoordinate2 = parseCoordinate((String)cities_coordinates.get(ind_1).get(2));
		
		double secondCityCoordinate1 = parseCoordinate((String)cities_coordinates.get(ind_2).get(1));
		double secondCityCoordinate2 =  parseCoordinate((String)cities_coordinates.get(ind_2).get(2));
		
		City city_from = new City(cityNameFrom, firstCityCoordinate1, firstCityCoordinate2);
		City city_to = new City(cityNameTo, secondCityCoordinate1, secondCityCoordinate2);
		
		path_cities.add(city_from);
		path_cities.add(city_to);
		
		
		Point firstCityLocation = 
				GeometryEngine.project(firstCityCoordinate1,
									 firstCityCoordinate2, 
									   mapSR);
		
		Point secondCityLocation = 
				GeometryEngine.project(secondCityCoordinate1,
						   secondCityCoordinate2, 
						   mapSR);

		 	Polyline polyline = new Polyline();
		    polyline.startPath(firstCityLocation);
		    polyline.lineTo(secondCityLocation);
		    

		    SimpleLineSymbol symbol = new SimpleLineSymbol(Color.blue, 4);
		    symbol.setStyle(SimpleLineSymbol.Style.DASH);
		    
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
		     
		    
		    SimpleMarkerSymbol blackTriangle = new SimpleMarkerSymbol(Color.BLUE,
		            16, Style.TRIANGLE);
		    blackTriangle.setAngle((float)(degrees));

		    
					
		graphicsLayer.addGraphic(new Graphic(firstCityLocation, symbol1));
		graphicsLayer.addGraphic(new Graphic(secondCityLocation, symbol2));
		graphicsLayer.addGraphic(new Graphic(polyline, symbol));
		 graphicsLayer.addGraphic(new Graphic(secondCityLocation, blackTriangle));

		addCityToPath("Gdañsk");
	}
	
	public void addCityToPath(String cityName)
	{
		SpatialReference mapSR = path_map.getSpatialReference();
		
		int ind_2 = 0;
		
		for(int i=0; i<cities_coordinates.size();i++)
		{
			if(((String)cities_coordinates.get(i).get(0)).equals(cityName)) ind_2 = i;
		}
		
		if( cities_coordinates == null) return;
		
		SimpleMarkerSymbol symbol1 = new SimpleMarkerSymbol(Color.black, 25, 
				SimpleMarkerSymbol.Style.CIRCLE);
		
		double secondCityCoordinate1 = parseCoordinate((String)cities_coordinates.get(ind_2).get(1));
		double secondCityCoordinate2 =  parseCoordinate((String)cities_coordinates.get(ind_2).get(2));
		
		
		City city_from = path_cities.get(path_cities.size()-1);
		City city_to = new City(cityName, secondCityCoordinate1, secondCityCoordinate2);
		
		path_cities.add(city_to);
		
		double firstCityCoordinate1 = city_from.getFirstCordiante();
		double firstCityCoordinate2 = city_from.getSecondCordiante();
	
		Point firstCityLocation = 
				GeometryEngine.project(firstCityCoordinate1,
								firstCityCoordinate2, 
									   mapSR);
		
		Point secondCityLocation = 
				GeometryEngine.project(secondCityCoordinate1,
						secondCityCoordinate2, 
						   mapSR);
		

	    SimpleLineSymbol symbol = new SimpleLineSymbol(Color.blue, 4);
	    symbol.setStyle(SimpleLineSymbol.Style.DASH);
	    
	    
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
	    
	    
	    SimpleMarkerSymbol blackTriangle = new SimpleMarkerSymbol(Color.BLUE,
	            16, Style.TRIANGLE);
	    blackTriangle.setAngle((float)(degrees));
	    
	    
	    Polyline polyline = new Polyline();
	    polyline.startPath(firstCityLocation);
	    polyline.lineTo(secondCityLocation);
	    
	    
	    graphicsLayer.addGraphic(new Graphic(secondCityLocation, symbol1));
		graphicsLayer.addGraphic(new Graphic(polyline, symbol));
		graphicsLayer.addGraphic(new Graphic(secondCityLocation, blackTriangle));
	    
		
	}
	
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
