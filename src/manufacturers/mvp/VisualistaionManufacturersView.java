package manufacturers.mvp;

import javax.swing.JTabbedPane;

import com.esri.map.JMap;

/**
 * Widok wizualizacji Producentów.
 * @author Kamil Zimny
 *
 */
public class VisualistaionManufacturersView 
{
	/**
	 * Obszar zakładek, wizualizacji producentów i 
	 * aktualnie stworzonej trasy
	 */
	private JTabbedPane tab;
	
	public VisualistaionManufacturersView()
	{
	}
	
	public void set_tab(JTabbedPane tab_)
	{
		tab=tab_;
	}
	
	/**
	 * Dodaje obiekt mapy do Obszaru zakładek.
	 * Nazwa zakładki to nazwa miasta z którego 
	 * zostali pobrani producenci.
	 * @param map - mapa z producentami ( wizualizacja )
	 * @param cityName - nazwa miasta
	 * @author Kamil Zimny
	 */
	public void add_map_to_tab(JMap map, String cityName)
	{
		if( tab.getTabCount() < 2 )
			tab.addTab(cityName,map);
		else			
		{
			tab.removeTabAt(0);
			tab.insertTab(cityName, null, map, null, 0);
		}
	}

}
