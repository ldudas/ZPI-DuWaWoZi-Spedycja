package visualisations;

import com.esri.client.toolkit.overlays.InfoPopupOverlay;
import com.esri.map.JMap;


@SuppressWarnings("deprecation")
public class VisualisationManufacturersModel 
{	
	private JMap map;
	public VisualisationManufacturersModel()
	{}
	VisualisationManufactureDecorator dec;
		
	/**
	 * Metoda tworzaca mape z przyblizeniem na okreslone miasto.
	 * Na mape nanoszone sa obiekty w ktorych zawarte sa dane o producentach
	 * ich aktywnosci i przydatnosci wyboru.
	 * @return JMap
	 * @author Kamil Zimny
	 */
	public JMap getMapWithVisualisationManufacturersInCity(final String cityName)
	{
		dec = new VisualisationManufactureDecorator(cityName);

		map =  dec;
		return map;
	}
	

	public InfoPopupOverlay del()
	{
		return dec.infoPopupOverlay;
	}
	
	public JMap getMap()
	{
		return map;
	}
	
	 

	
}