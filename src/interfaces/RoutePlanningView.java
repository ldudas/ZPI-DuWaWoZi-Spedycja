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
	private RouteJPanel route;
	private MapJPanel map;
	private StartJPanel start;
	private ManufacturerJPanel manufacturer;
	
	
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
	
	public void change_start_to_map()
	{
		frame.remove(start);
		frame.setBounds(50, 50, 1120, 600);
		frame.add(map);
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
		
		manufacturer.setInfoAboutManufacturerInToList(attributes);
		manufacturerFrame.add(manufacturer);
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
		start.setPresenter(route_planning_presenter);
		map.setPresenter(route_planning_presenter);
		route.setPresenter(route_planning_presenter);
		manufacturer.setPresenter(route_planning_presenter);
	}
	
	public String city_to()
	{
		return start.get_city_to();
	}
	
	public JFrame returnJFrame()
	{
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
