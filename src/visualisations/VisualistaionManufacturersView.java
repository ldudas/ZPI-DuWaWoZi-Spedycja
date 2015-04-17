package visualisations;

import javax.swing.JTabbedPane;

import com.esri.map.JMap;

public class VisualistaionManufacturersView {
	
	JTabbedPane tab;
	
	public VisualistaionManufacturersView()
	{
		
		
	}
	
	public void set_tab(JTabbedPane tab_)
	{
		tab=tab_;
	}
	
	public void add_map_to_tab(JMap map, String cityName)
	{
		tab.addTab(cityName,map);
	}

}
