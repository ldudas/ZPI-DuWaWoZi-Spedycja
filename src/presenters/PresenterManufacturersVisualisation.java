/**
 * @author Kamil Zimny
 */
/**
 * @author[Kamil Zimny]
 */
package presenters;

import views.ViewManufacturersVisualisation;
import models.ModelManufacturersVisualisation;
import com.esri.map.JMap;



public class PresenterManufacturersVisualisation 
{
	private ModelManufacturersVisualisation model_ManufacturersVis;
	private ViewManufacturersVisualisation view_ManufacturersVis;
	
	
	public PresenterManufacturersVisualisation(final ViewManufacturersVisualisation view,final ModelManufacturersVisualisation model)
	{
		model_ManufacturersVis = model;
		view_ManufacturersVis = view;
	}
	
	/**
	 * Metoda ustawiajaca widokowi mape pobrana z modelu.
	 * @author Kamil Zimny
	 */
	public void startManufacturersVisualisation(final String cityName)
	{
		view_ManufacturersVis.addMapToWindow(
				model_ManufacturersVis.getMapWithVisualisationManufacturersInCity(cityName));
		
	}
	
	public void changeView()
	{
		view_ManufacturersVis.change_start_to_map();
	}
	
	public void ustawMape(){
		view_ManufacturersVis.dupa(model_ManufacturersVis.getMapWithVisualisationManufacturersInCity(view_ManufacturersVis.city_to()));
	}
	
	
	
	
}
