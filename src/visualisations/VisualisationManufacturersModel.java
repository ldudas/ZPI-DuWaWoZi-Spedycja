package visualisations;

import com.esri.map.JMap;


@SuppressWarnings("deprecation")
public class VisualisationManufacturersModel 
{	
	private JMap map;
	public VisualisationManufacturersModel()
	{}
	
		
	/**
	 * Metoda tworzaca mape z przyblizeniem na okreslone miasto.
	 * Na mape nanoszone sa obiekty w ktorych zawarte sa dane o producentach
	 * ich aktywnosci i przydatnosci wyboru.
	 * @return JMap
	 * @author Kamil Zimny
	 */
	public JMap getMapWithVisualisationManufacturersInCity(final String cityName)
	{
		map = new VisualisationManufactureDecorator(cityName);
	
		return map;
	}
	
	public void selectTheBestManufacturersInRange(int range)
	{
		
	}
	
	public JMap getMap()
	{
		return map;
	}
	
	 

	
}