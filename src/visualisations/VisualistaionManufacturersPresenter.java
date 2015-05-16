package visualisations;


import java.awt.Color;
import java.util.Map;

import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.map.GraphicsLayer;
import com.esri.map.JMap;
import com.esri.map.Layer;
import com.esri.map.LayerList;


import dataModels.Manufacturer;
import interfaces.RoutePlanningPresenter;

public class VisualistaionManufacturersPresenter 
{
	
	private VisualisationManufacturersModel model_ManufacturersVis;
	private VisualistaionManufacturersView view_ManufacturersVis;
	private RoutePlanningPresenter route_planning_presenter;
	
	public VisualistaionManufacturersPresenter(final VisualistaionManufacturersView view, final VisualisationManufacturersModel model)
	{
		model_ManufacturersVis = model;
		view_ManufacturersVis = view;
	}
	
	public void set_route_presenter(final RoutePlanningPresenter presenter)
	{
		route_planning_presenter = presenter;
	}
	
	/**
	 * Metoda ustawiajaca widokowi mape pobrana z modelu.
	 * @author Kamil Zimny
	 */
	public void startManufacturersVisualisation(final String cityName)
	{
		view_ManufacturersVis.set_tab(route_planning_presenter.getTabWithMaps());
		view_ManufacturersVis.add_map_to_tab(model_ManufacturersVis.getMapWithVisualisationManufacturersInCity(cityName),cityName);
	}
	
	
	/**
	 * Metoda zwracajaca atrybuty charakteryzujace danego Producenta.
	 * @return Map<String, Object> atrybutyZaznaczonegoObiektu
	 * @author Kamil Zimny
	 */
	public Manufacturer getAttributeOfSelectedManufacturers()
	{
		JMap map = model_ManufacturersVis.getMap();
		LayerList layerlist = map.getLayers();

		int [] id_ofSelectedManufacturers = null;
		Manufacturer manufacturer = null;
		for( Layer layer : layerlist)
		{
			if( layer.getName().toString().equals("Manufacturers graphics") )//Znajdujemy odpowiedzni layer
			{
				id_ofSelectedManufacturers = ((GraphicsLayer) layer).getSelectionIDs(); //pobieramy indeksy zaznaczonych obiektow
				
				if( id_ofSelectedManufacturers.length == 1 )//jesli zaznaczony tylko jeden obiekt to pobierz jego atrybuty
				{
					Graphic graphic = ((GraphicsLayer) layer).getGraphic(id_ofSelectedManufacturers[0]);
					Map<String, Object>  attributes = graphic.getAttributes();		
					String ID = (String) attributes.get("ID");
					manufacturer = model_ManufacturersVis.getManufacturerByID(ID);
				}			
				break;
			}
			
		}
		return manufacturer;
	}
	
	/**
	 * Metoda ustawiajaca symbol na mapie na czarny krzyz oznaczajacy ze dany producent nie odpowiada naszym 
	 * wymagania spedytora.
	 * @author Kamil Zimny
	 */
	public void markAsUnsuitable()
	{
		JMap map = model_ManufacturersVis.getMap();
		LayerList layerlist = map.getLayers();

		for( Layer layer : layerlist)
		{
			if( layer.getName().toString().equals( "Manufacturers graphics" ) )//Znajdujemy odpowiedzni layer
			{		
				int idOfGraphic = ((GraphicsLayer) layer).getSelectionIDs()[0];
				Graphic graphic = ((GraphicsLayer) layer).getGraphic( idOfGraphic );
				SimpleMarkerSymbol symbol = (SimpleMarkerSymbol)  graphic.getSymbol() ;			
				symbol.setColor(Color.BLACK);
				symbol.setAngle(45);
				symbol.setSize(30);
				symbol.setStyle(SimpleMarkerSymbol.Style.CROSS);
				
				((GraphicsLayer) layer).updateGraphic(idOfGraphic, symbol);
				break;
			}
		}
	}
	
	/**
	 * Metoda ustawiajaca wybranemu producentowi pole dodatkowe inforamcje na 
	 * wartosc podana w parametrze info
	 * @param manufacturer
	 * @param info
	 * @author Kamil Zimny
	 */
	public void setManufacturerAttribut_additionInfo(Manufacturer manufacturer,String info)
	{
		model_ManufacturersVis.getManufacturerByID(manufacturer.getID()).setInfoAboutManufacturer(info);
	}

	
	
}
