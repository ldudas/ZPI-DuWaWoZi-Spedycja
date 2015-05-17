package interfaces;

import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import dataModels.Manufacturer;
import dataModels.Order;
import jpanels.ManufacturerVisualization.ManufacturerVisuzalizationJPanel;
import jpanels.ManufacturerVisualization.ManufactureInfo.ManufacturerDetailsJPanel;
import jpanels.ManufacturerVisualization.ManufactureInfo.ManufacturerOrderDataJPanel;
import jpanels.startWindow.StartJPanel;


public class RoutePlanningView 
{
	private RoutePlanningPresenter route_planning_presenter;
	private JFrame mainFrame;
	private ManufacturerVisuzalizationJPanel manufacturerVisualizationWithMapJPanel;
	private StartJPanel startJPanel;
	private ManufacturerOrderDataJPanel manufacturerOrderDataJPanel;
	private ManufacturerDetailsJPanel manufacturerDetailsJPanel;
	
	private JFrame manufacturerFrame;
	/**
	 * Create the application.
	 */
	public RoutePlanningView() 
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() 
	{
		mainFrame = new JFrame();
		mainFrame.setResizable(false);
		mainFrame.setBounds(300, 100, 630, 500);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		manufacturerVisualizationWithMapJPanel = new ManufacturerVisuzalizationJPanel();
		startJPanel = new StartJPanel();
		manufacturerOrderDataJPanel = new ManufacturerOrderDataJPanel();
		manufacturerDetailsJPanel = new ManufacturerDetailsJPanel();
		mainFrame.add(startJPanel);
	}
	 
	/**
	 * Metoda ustawiajaca presentera podanego w parametrze.
	 * @author Kamil Zimny
	 */
	public void setPresenter(final RoutePlanningPresenter presenter)
	{
		route_planning_presenter = presenter;
	}
	
	
	/**
	 * Zmienia widok ze startowego do widoku wizualizacji producentow na mapie
	 */
	public void change_start_to_manufacturerVisualization()
	{
		mainFrame.remove(startJPanel);
		mainFrame.setBounds(50, 50, 1120, 600);
		mainFrame.add(manufacturerVisualizationWithMapJPanel);
		mainFrame.invalidate();
		mainFrame.validate();
	}
	
	/**
	 * Metoda zmieniajaca widok z informacji o producencie do 
	 * na okno zwiazane z wykonaiem zleceniem wybranego producenta
	 * @author Kamil Zimny
	 */
	public void change_manufactruerDetails_to_manufacturerOrderData()
	{
		manufacturerFrame.remove(manufacturerDetailsJPanel);
		manufacturerFrame.setBounds(100, 100, 658, 475);
		manufacturerFrame.add(manufacturerOrderDataJPanel);
		manufacturerFrame.invalidate();
		manufacturerFrame.validate();
	}
	
	/**
	 * Metoda zmieniajaca widok z okno zwiazane z wykonywanie(miasto,daty) do 
	 * na zleceniem informacji o wybranym producencie 
	 * @author Kamil Zimny
	 */
	public void change_manufacturerOrderData_to_manufactruerDetails()
	{
		manufacturerFrame.remove(manufacturerOrderDataJPanel);
		manufacturerFrame.setBounds(100, 100, 665, 452);
		manufacturerFrame.add(manufacturerDetailsJPanel);
		manufacturerFrame.invalidate();
		manufacturerFrame.validate();
	}
	
	/**
	 * Tworzy nowe okno z danymi producenta i opcjami które możemy wykonać.
	 * @author Kamil Zimny
	 */
	public void show_manfacturerInfo(Manufacturer manufacturer)
	{
		if( manufacturerFrame != null)
			manufacturerFrame.dispose();
		
		manufacturerFrame = new JFrame();
		manufacturerFrame.setResizable(false);
		manufacturerFrame.setBounds(100, 100, 665, 452);
		manufacturerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		manufacturerDetailsJPanel.setInfoAboutManufacturerInToList(manufacturer);
		manufacturerFrame.add(manufacturerDetailsJPanel);
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
	 * Zamyka okno informacji o producencie
	 * @author Kamil Zimny
	 */
	public void closeManufacturerInfoFrame()
	{
		manufacturerFrame.dispose();
	}
	
	public void setPresenters()
	{
		startJPanel.setPresenter(route_planning_presenter);
		manufacturerVisualizationWithMapJPanel.setPresenter(route_planning_presenter);
		manufacturerOrderDataJPanel.setPresenter(route_planning_presenter);
		manufacturerDetailsJPanel.setPresenter(route_planning_presenter);
	}
	
	public String city_nextCityAfterComfirm()
	{
		return manufacturerOrderDataJPanel.getNextCityName();
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
		return manufacturerOrderDataJPanel.getStartDate();
	}
	
	public String getNextFinishDate()
	{
		return manufacturerOrderDataJPanel.getFinishDate();
	}
	
	public String getNextCityTo()
	{
		return manufacturerOrderDataJPanel.getNextCityName();
	}
	
	public void setJFrameVisibility(boolean vis)
	{
		mainFrame.setVisible(vis);;
	}	
	
	public void setVisibleOfManagementJPanels(boolean vis)
	{
		manufacturerVisualizationWithMapJPanel.setManagementJPanelVisibility(vis);
	}
	
	public int getTabSelectedIndex()
	{
		return manufacturerVisualizationWithMapJPanel.getTabWithMaps().getSelectedIndex();
	}
	
	public JTabbedPane getTabWithMaps()
	{
		return manufacturerVisualizationWithMapJPanel.getTabWithMaps();
	}
	
	public void addOrderToTab(final Order order)
	{
		manufacturerVisualizationWithMapJPanel.addOrderToMap(order);
	}
	
	public void setCalendareDate_StartNewOrder(Date date)
	{
		manufacturerOrderDataJPanel.setStartDateOn(date);
	}
	
	public void removeLastOrderFromTab()
	{
		manufacturerVisualizationWithMapJPanel.removeLastOrderFromTab();
	}
	
	
	
}
