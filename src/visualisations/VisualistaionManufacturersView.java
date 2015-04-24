package visualisations;

import javax.swing.JTabbedPane;

import com.esri.map.JMap;

public class VisualistaionManufacturersView 
{
	
	private JTabbedPane tab;
	
	public VisualistaionManufacturersView()
	{
		
		
	}
	
	public void set_tab(JTabbedPane tab_)
	{
		tab=tab_;
	}
	
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
