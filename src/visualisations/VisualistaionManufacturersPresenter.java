package visualisations;


import java.util.Map;

import com.esri.core.map.Graphic;
import com.esri.map.GraphicsLayer;
import com.esri.map.JMap;
import com.esri.map.Layer;
import com.esri.map.LayerList;

import interfaces.RoutePlanningPresenter;

public class VisualistaionManufacturersPresenter 
{
	
	private VisualisationManufacturersModel model_ManufacturersVis;
	private VisualistaionManufacturersView view_ManufacturersVis;
	private RoutePlanningPresenter route_planning_presenter;
	
	public VisualistaionManufacturersPresenter(final VisualistaionManufacturersView view,final VisualisationManufacturersModel model)
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
		view_ManufacturersVis.set_tab(route_planning_presenter.return_view().returnMap().return_tab());
		view_ManufacturersVis.add_map_to_tab(model_ManufacturersVis.getMapWithVisualisationManufacturersInCity(cityName));
	}
	
	/**
	 * Metoda zwracajaca atrybuty charakteryzujace danego Producenta.
	 * @return Map<String, Object> atrybutyZaznaczonegoObiektu
	 * @author Kamil Zimny
	 */
	public Map<String, Object> getIdOfSelectedManufacturers()
	{
		JMap map = model_ManufacturersVis.getMap();

		LayerList layerlist = map.getLayers();

		int [] id_ofSelectedManufacturers = null;
		Map<String, Object> attributes = null;
		for( Layer layer : layerlist)
		{
			if( layer.getName().toString() == "Manufacturers graphics" )//Znajdujemy odpowiedzni layer
			{
				id_ofSelectedManufacturers = ((GraphicsLayer) layer).getSelectionIDs(); //pobieramy indeksy zaznaczonych obiektow
				
				if( id_ofSelectedManufacturers.length == 1 )//jesli zaznaczony tylko jeden obiekt to pobierz jego atrybuty
				{
					Graphic graphic = ((GraphicsLayer) layer).getGraphic(id_ofSelectedManufacturers[0]);
					attributes = graphic.getAttributes();
				}			
				break;
			}
		}
		
		return attributes;
	}

	
	
}
