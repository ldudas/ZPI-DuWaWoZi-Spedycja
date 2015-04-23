package interfaces;

import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import jpanels.ManufacturerJPanel;
import jpanels.MapJPanel;
import jpanels.RouteJPanel;
import jpanels.StartJPanel;


public class RoutePlanningView 
{
	private RoutePlanningPresenter route_planning_presenter;
	private JFrame frame;
	private RouteJPanel routeJPanel;
	private MapJPanel mapJPanel;
	private StartJPanel startJPanel;
	private ManufacturerJPanel manufacturerJPanel;
	
	
	private JFrame manufacturerFrame;
	/**
	 * Create the application.
	 */
	public RoutePlanningView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() 
	{
		frame = new JFrame();
		frame.setBounds(100, 100, 650, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		routeJPanel = new RouteJPanel();
		mapJPanel = new MapJPanel();
		startJPanel = new StartJPanel();
		manufacturerJPanel = new ManufacturerJPanel();
		frame.add(startJPanel);
	}
	 
	/**
	 * Metoda ustawiajaca presentera podanego w parametrze.
	 * @author Kamil Zimny
	 */
	public void setPresenter(final RoutePlanningPresenter presenter)
	{
		route_planning_presenter = presenter;
	}
	
	public void change_start_to_map()
	{
		frame.remove(startJPanel);
		frame.setBounds(50, 50, 1120, 600);
		frame.add(mapJPanel);
		frame.invalidate();
		frame.validate();
	}
	
	/**
	 * Tworzy nowe okno z danymi producenta i opcjami które mo¿emy wykonaæ.
	 * @author Kamil Zimny
	 */
	public void show_manfacturerInfo(Map<String, Object> attributes)
	{
		manufacturerFrame = new JFrame();
		manufacturerFrame.setBounds(100, 100, 650, 500);
		manufacturerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		manufacturerJPanel.setInfoAboutManufacturerInToList(attributes);
		manufacturerFrame.add(manufacturerJPanel);
		manufacturerFrame.setVisible(true);
		
	}
	/**
	 * Gdy zaznaczymy wiecej niz jdenego producenta i zdecydujemy sie na wyswietlenie jego danych
	 * wtedy wyskauje okno bledu informujace o zbyt duzej liczbie wybranych producentow
	 * @author Kamil Zimny
	 */
	public void show_ErrorMessage()
	{
		JOptionPane.showMessageDialog(null,"Aby wyœwietliæ dane o producencie nale¿y na mapie zaznaczyæ tylko jednego producenta, a nastêpnie ponownie klikn¹æ przycisk wybierz.",
				"Zaznaczono nie poprawn¹ liczbê poducentów",JOptionPane.ERROR_MESSAGE);
	}
	/**
	 * Zamyka okno informajci o producencie
	 * @author Kamil Zimny
	 */
	public void closeManufacturerInfoFrame()
	{
		manufacturerFrame.dispose();
	}
	
	public void setPresenters()
	{
		startJPanel.setPresenter(route_planning_presenter);
		mapJPanel.setPresenter(route_planning_presenter);
		routeJPanel.setPresenter(route_planning_presenter);
		manufacturerJPanel.setPresenter(route_planning_presenter);
	}
	
	public void changeTabOfMap()
	{
		mapJPanel.setCurrentTabOfMap();
	}
	
	public String city_nextCityAfterComfirm()
	{
		return manufacturerJPanel.getNextCityName();
	}
	
	public String city_to()
	{
		return startJPanel.get_city_to();
	}
	
	public String city_from()
	{
		return startJPanel.get_city_from();
	}
	
	public JFrame returnJFrame()
	{
		return frame;
	}
	
	public RouteJPanel returnRouteJPanel(){
		return routeJPanel;
		}
	
	public MapJPanel returnMapJPanel(){
		return mapJPanel;
		}
	
	public StartJPanel returnStartJPanel(){
		return startJPanel;
		}
	
	public ManufacturerJPanel returnManufacturerJPanel(){
		return manufacturerJPanel;
		}
	}
