package interfaces;

import java.awt.CardLayout;
import java.awt.SystemColor;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;






import dataModels.Manufacturer;
import dataModels.Order;
import jpanels.ManufacturerVisualization.ManufacturerVisuzalizationJPanel;
import jpanels.ManufacturerVisualization.ManufactureInfo.ManufacturerDetailsJPanel;
import jpanels.ManufacturerVisualization.ManufactureInfo.ManufacturerOrderDataJPanel;
import jpanels.startWindow.LogJPanel;
import jpanels.startWindow.MenuJPanel;
import jpanels.startWindow.RegistryJPanel;
import jpanels.startWindow.StartJPanel;


public class RoutePlanningView 
{
	private RoutePlanningPresenter route_planning_presenter;
	private JFrame mainFrame;
	private ManufacturerVisuzalizationJPanel manufacturerVisualizationWithMapJPanel;
	private StartJPanel startJPanel;
	private ManufacturerOrderDataJPanel manufacturerOrderDataJPanel;
	private ManufacturerDetailsJPanel manufacturerDetailsJPanel;
	private MenuJPanel menuJPanel;
	private LogJPanel logJPanel;
	private RegistryJPanel registryJPanel;
	
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
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		manufacturerVisualizationWithMapJPanel = new ManufacturerVisuzalizationJPanel();
		startJPanel = new StartJPanel();
		manufacturerOrderDataJPanel = new ManufacturerOrderDataJPanel();
		manufacturerDetailsJPanel = new ManufacturerDetailsJPanel();
		menuJPanel = new MenuJPanel();
		logJPanel = new LogJPanel();
		registryJPanel = new RegistryJPanel();
		
		mainFrame.setBounds(250, 150, 290, 400);
		mainFrame.setTitle("Menu");
		mainFrame.add(menuJPanel);
		mainFrame.invalidate();
		mainFrame.validate();
	}
	 
	/**
	 * Metoda ustawiajaca presentera podanego w parametrze.
	 * @author Kamil Zimny
	 */
	public void setPresenter(final RoutePlanningPresenter presenter)
	{
		route_planning_presenter = presenter;
	}
	
	public void change_menu_to_startPlanning()
	{
		mainFrame.remove(menuJPanel);
		mainFrame.setBounds(300, 100, 630, 500);
		mainFrame.setTitle("Dane zlecenia");
		mainFrame.add(startJPanel);
		mainFrame.invalidate();
		mainFrame.validate();
	}
	
	public void change_menu_to_registryUser()
	{
		mainFrame.remove(menuJPanel);
		mainFrame.setBounds(300, 100, 415, 468);
		mainFrame.setTitle("Rejestracja");
		mainFrame.add(registryJPanel);
		mainFrame.invalidate();
		mainFrame.validate();
	}
	
	public void change_registryUser_to_menu()
	{
		mainFrame.remove(registryJPanel);
		mainFrame.setBounds(250, 150, 290, 400);
		mainFrame.setTitle("Menu");
		mainFrame.add(menuJPanel);
		mainFrame.invalidate();
		mainFrame.validate();
	}
	
	public void change_menu_to_loginUser()
	{
		mainFrame.remove(menuJPanel);
		mainFrame.setBounds(300, 100, 275, 240);
		mainFrame.setTitle("Logowanie");
		mainFrame.add(logJPanel);
		mainFrame.invalidate();
		mainFrame.validate();
	}
	
	public void change_loginUser_to_menu()
	{
		mainFrame.remove(logJPanel);
		mainFrame.setBounds(250, 150, 290, 400);
		mainFrame.setTitle("Menu");
		mainFrame.add(menuJPanel);
		mainFrame.invalidate();
		mainFrame.validate();
	}
	
	
	/**
	 * Zmienia widok ze startowego do widoku wizualizacji producentow na mapie
	 */
	public void change_start_to_manufacturerVisualization()
	{
		ImageIcon loading = new ImageIcon("images/ajax-loader.gif");		
		JLabel loadingLabel = new JLabel("", loading, JLabel.CENTER);
		JPanel background = new JPanel();
		background.setLayout(new CardLayout(0, 0));
		background.setBounds(0, 0, 500, 300);
		background.setBackground(SystemColor.inactiveCaption);
		background.add(loadingLabel);
		
		mainFrame.remove(startJPanel);
		mainFrame.setBounds(300, 100, 500, 300);
		mainFrame.setTitle("Ładowanie");
		mainFrame.add(background);
		mainFrame.invalidate();
		mainFrame.validate();
		
		Timer timer = new Timer();
		
		timer.schedule(new TimerTask() 
		{	
			@Override
			public void run() 
			{
				mainFrame.remove(background);
				mainFrame.setBounds(50, 50, 1120, 600);
				mainFrame.setTitle("Producenci");
				mainFrame.add(manufacturerVisualizationWithMapJPanel);	
				mainFrame.invalidate();
				mainFrame.validate();
			}
		}, 5000);
		
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
		manufacturerFrame.setTitle("Kolejna trasa");
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
		manufacturerFrame.setTitle("Dane producenta");
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
		manufacturerFrame.setTitle("Dane producenta");
		
		manufacturerDetailsJPanel.setInfoAboutManufacturerInToList(manufacturer);
		manufacturerDetailsJPanel.setChartActivityColor(manufacturer);
		
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
		menuJPanel.setPresenter(route_planning_presenter);
		logJPanel.setPresenter(route_planning_presenter);
		registryJPanel.setPresenter(route_planning_presenter);
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
	
	public void closeMainFrame_ManufacturerVisualization()
	{
		mainFrame.dispose();
	}
	
	public void setNewLoggedUser(String login)
	{
		menuJPanel.setNewLoggedUser(login);
	}
	
	public void setNotLoggedUser()
	{
		menuJPanel.setNotLoggedUser();
	}
	
	public String getLogin()
	{
		return registryJPanel.getLogin();
	}
	
	/**
	 * Metoda zwracajaca podane przez u�ytkownika has�a.
	 * @return [] String
	 * <br> [0] -> password
	 * <br> [1] -> repeat password
	 * @author Kamil Zimny
	 */
	public String [] getPasswords()
	{
		return registryJPanel.getPasswords();
	}
	
	public String getServerAddress()
	{
		return registryJPanel.getServerAddress();
	}
	
	public String getServerPort()
	{
		return registryJPanel.getServerPort();
	}
	
	public String getDatabaseName()
	{
		return registryJPanel.getDatabaseName();
	}
	
	public String getDatabaseLogin()
	{
		return registryJPanel.getDatabaseLogin();
	}
	
	public String getDatabasePassword()
	{
		return registryJPanel.getDatabasePassword();
	}
	
	public String getLogin_Login()
	{
		return logJPanel.getLogin();
	}
	
	public String getPassword_Login()
	{
		return logJPanel.getPassword();
	}
	
	public void setEnableButtonsToUserAction(boolean flag)
	{
		menuJPanel.setEnableButtonsToUserAction(flag);
	}
	
	public void addAllCityToList()
	{
		startJPanel.addAllCityToList();
		manufacturerOrderDataJPanel.addAllCityToList();
	}
	
}
