package visualisations.Path;

import javax.swing.JTabbedPane;

import com.esri.map.JMap;

public class VisualisationPathView 
{
	
	/**
	 * Referencja do panelu zakladek w ktorym widok umieszcza wizualizacje
	 */
  private JTabbedPane tab;
	
  	/**
	 * Ustaw panel zakladek widokowi
	 * @param  tabP Panel zakladek 
	 * @author Łukasz Dudaszek
	 */
	public void set_tab(JTabbedPane tabP)
	{
		tab=tabP;
	}
	
	
	/**
	 * Dodaj mape(wizualizacje) do panelu zakladek
	 * @param  map mapa do dodania
	 * @author Łukasz Dudaszek
	 */
	public void add_map_to_tab(JMap map)
	{
		tab.addTab("Trasa",map);
	}
  

}
