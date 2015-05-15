package interfaces;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import dataModels.Manufacturer;
import dataModels.Order;
import jpanels.ManufacturerVisualization.MapJPanel;
import jpanels.ManufacturerVisualization.ManufactureInfo.ManufacturerJPanel;
import jpanels.startWindow.StartJPanel;


public class RoutePlanningView 
{
	private RoutePlanningPresenter route_planning_presenter;
	private JFrame frame;
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
		frame.setResizable(false);
		frame.setBounds(300, 100, 630, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
	 * Tworzy nowe okno z danymi producenta i opcjami które możemy wykonać.
	 * @author Kamil Zimny
	 */
	public void show_manfacturerInfo(Manufacturer manufacturer)
	{
		manufacturerFrame = new JFrame();
		manufacturerFrame.setResizable(false);
		manufacturerFrame.setBounds(100, 100, 600, 345);
		manufacturerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		manufacturerJPanel.setInfoAboutManufacturerInToList(manufacturer);
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
		JOptionPane.showMessageDialog(null,"Aby wyświetlić dane o producencie należy na mapie zaznaczyć tylko jednego producenta, a następnie ponownie kliknąć przycisk wybierz.",
				"Zaznaczono nie poprawną liczbę poducentów",JOptionPane.ERROR_MESSAGE);
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
		manufacturerJPanel.setPresenter(route_planning_presenter);
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
	
	public String getStartDate()
	{
		return startJPanel.getStartDate();
	}
	
	public String getFinishDate()
	{
		return startJPanel.getFinishDate();
	}
	
	public String getNextStartDate()
	{
		return manufacturerJPanel.getStartDate();
	}
	
	public String getNextFinishDate()
	{
		return manufacturerJPanel.getFinishDate();
	}
	
	public String getNextCityTo()
	{
		return manufacturerJPanel.getNextCityName();
	}
	
	public void setJFrameVisibility(boolean vis)
	{
		frame.setVisible(vis);;
	}
	
	public String getNameOfNextCity()
	{
		return manufacturerJPanel.getNextCityName();
	}
	
	
	public void setVisibleOfManagementJPanels(boolean vis)
	{
		mapJPanel.setManagementJPanelVisibility(vis);
	}
	
	public int getTabSelectedIndex()
	{
		return mapJPanel.getTabWithMaps().getSelectedIndex();
	}
	
	public JTabbedPane getTabWithMaps()
	{
		return mapJPanel.getTabWithMaps();
	}
	
	public void addOrderToTab(final Order order)
	{
		mapJPanel.addOrderToMap(order);
	}
	
}
