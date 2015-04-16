package interfaces;

import javax.swing.JFrame;
import jpanels.ManufacturerJPanel;
import jpanels.MapJPanel;
import jpanels.RouteJPanel;
import jpanels.StartJPanel;


public class RoutePlanningView 
{
	private RoutePlanningPresenter route_planning_presenter;
	private JFrame frame;
	private RouteJPanel route;
	private MapJPanel map;
	private StartJPanel start;
	private ManufacturerJPanel manufacturer;
	
	/**
	 * Create the application.
	 */
	public RoutePlanningView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 650, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		route = new RouteJPanel();
		map = new MapJPanel();
		start = new StartJPanel();
		manufacturer = new ManufacturerJPanel();
		frame.add(start);
		}
	 
	/**
	 * Metoda ustawiajaca presentera podanego w parametrze.
	 * @author Kamil Zimny
	 */
	public void setPresenter(final RoutePlanningPresenter presenter)
	{
		route_planning_presenter = presenter;
	}
	
	public void change_start_to_map(){
		frame.remove(start);
		frame.add(map);
		frame.invalidate();
		frame.validate();
	}
	
	public void setPresenters(){
		start.setPresenter(route_planning_presenter);
		map.setPresenter(route_planning_presenter);
		route.setPresenter(route_planning_presenter);
		manufacturer.setPresenter(route_planning_presenter);
	}
	
	public String city_to(){
		return start.get_city_to();
	}
	
	public JFrame returnJFrame(){
		return frame;
		}
	
	public RouteJPanel returnRoute(){
		return route;
		}
	
	public MapJPanel returnMap(){
		return map;
		}
	
	public StartJPanel returnStart(){
		return start;
		}
	
	public ManufacturerJPanel returnManufacturer(){
		return manufacturer;
		}

	
}