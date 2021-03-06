package interfaces.mvp;

import interfaces.jPanels.startWindow.AboutJPanel;
import interfaces.jPanels.startWindow.LogJPanel;
import interfaces.jPanels.startWindow.MenuJPanel;
import interfaces.jPanels.startWindow.RegistryJPanel;
import interfaces.jPanels.startWindow.StartApplicationJPanel;
import interfaces.jPanels.startWindow.WelcomeJPanel;

import java.awt.CardLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import shared.dataModels.Manufacturer;
import shared.dataModels.Order;
import manufacturers.jPanels.info.ManufacturerDetailsJPanel;
import manufacturers.jPanels.info.ManufacturerOrderDataJPanel;
import manufacturers.jPanels.visualisation.ManufacturerVisuzalizationJPanel;
import manufacturers.jPanels.visualisation.StartPlanningJPanel;
import manufacturers.jPanels.visualisation.WaitingJPanel;

/**
 * Główny widok aplikacji.
 * @author Kamil
 *
 */
public class RoutePlanningView 
{
	private final String APPLICATION_NAME = "FORWARDer";
	private RoutePlanningPresenter route_planning_presenter;

	private ManufacturerVisuzalizationJPanel manufacturerVisualizationWithMapJPanel;
	private StartPlanningJPanel startPlanningJPanel;
	private ManufacturerOrderDataJPanel manufacturerOrderDataJPanel;
	private ManufacturerDetailsJPanel manufacturerDetailsJPanel;
	private MenuJPanel menuJPanel;
	private LogJPanel logJPanel;
	private RegistryJPanel registryJPanel;
	private StartApplicationJPanel startApplicationJPanel;
	private WelcomeJPanel welcomeJPanel;
	private AboutJPanel aboutJPanel;
	
	private JFrame mainFrame;
	private JFrame manufacturerFrame;
	private JFrame aboutFream;
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
		initializeFrameMenu();
		
		manufacturerVisualizationWithMapJPanel = new ManufacturerVisuzalizationJPanel();
		startPlanningJPanel = new StartPlanningJPanel();
		manufacturerOrderDataJPanel = new ManufacturerOrderDataJPanel();
		manufacturerDetailsJPanel = new ManufacturerDetailsJPanel();
		menuJPanel = new MenuJPanel();
		logJPanel = new LogJPanel();
		registryJPanel = new RegistryJPanel();
		startApplicationJPanel = new StartApplicationJPanel();
		welcomeJPanel = new WelcomeJPanel();
		aboutJPanel = new AboutJPanel();
		
		openWelcomeView();
	}

	/**
	 * Metoda tworząca pasek menu głównego okna aplikacji.
	 * @author Kamil Zimny
	 */
	private void initializeFrameMenu()
	{
		JMenuBar menuBar;
		JMenu menu;
		JMenuItem menuItem;

		//Create the menu bar.
		menuBar = new JMenuBar();

		//Build the first menu.
		menu = new JMenu("Menu");
		menu.setMnemonic(KeyEvent.VK_A);
		menuBar.add(menu);
		menu.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));

		menuItem = new JMenuItem("Wyloguj",
		                         KeyEvent.VK_T);
		menuItem.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if( menuJPanel.isUserLogged() )
				{
					int dialogResult = JOptionPane.showConfirmDialog(mainFrame, "Czy na pewno chcesz się wylogować?", 
							"Wyloguj", JOptionPane.YES_NO_OPTION);
					if(dialogResult == JOptionPane.YES_OPTION) 
					{
						route_planning_presenter.logOutUser();
						
						menuJPanel.setNotLoggedUser();
						menuJPanel.setEnableButtonsToUserAction(false);
						menuJPanel.setEnableButtonsFirstAction(true);
						startApplicationJPanel.removeLogicJPanel();
						openWelcomeView();
					}
				}
			};
		});
		menuItem.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		menu.add(menuItem);
		

		menuItem = new JMenuItem("Okno powitania",
		                         KeyEvent.VK_T);
		menuItem.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				int dialogResult = JOptionPane.showConfirmDialog(mainFrame, "Czy na pewno chcesz przejść do okna powitania?", 
						"Powitanie", JOptionPane.YES_NO_OPTION);
				if(dialogResult == JOptionPane.YES_OPTION) 
				{
					change_to_startingPanel();
					//prepareFrameAfterChangeView(mainFrame);
				}
			};
		});
		menuItem.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		menu.add(menuItem);
		
		menu.addSeparator();
		
		menuItem = new JMenuItem("Zakończ",
                KeyEvent.VK_T);
		menuItem.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				int dialogResult = JOptionPane.showConfirmDialog(mainFrame, "Czy na pewno chcesz zakończyć działanie aplikacji?",
						"Zakończ", JOptionPane.YES_NO_OPTION);
				if(dialogResult == JOptionPane.YES_OPTION) 
				{
					mainFrame.dispose();			
				}
				
			};
		});
		menuItem.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		menu.add(menuItem);
		
		menu = new JMenu("Pomoc");
		menu.setMnemonic(KeyEvent.VK_A);
		menuBar.add(menu);
			
		menuItem = new JMenuItem("Instrukcja", KeyEvent.VK_T);
		menuItem.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		menuItem.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				route_planning_presenter.openInstructionToApplication();
			};
		});
		menu.add(menuItem);
		menu.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		menu.addSeparator();
		
		menuItem = new JMenuItem("O programie",
                KeyEvent.VK_T);
		menuItem.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if( aboutFream != null)
					aboutFream.dispose();
				
				aboutFream = new JFrame();
				aboutFream.setResizable(false);
				aboutFream.setBounds(500, 200, 562, 440);
				aboutFream.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				aboutFream.setTitle("O programie");
				aboutFream.add(aboutJPanel);
				aboutFream.setVisible(true);		
			};
		});
		menuItem.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		menu.add(menuItem);
		
		menu.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));

		mainFrame.setJMenuBar(menuBar);
	}
	
	/**
	 * Metoda czyszcząca główne okno aplikacji.
	 */
	public void clearMainFrame()
	{
		manufacturerVisualizationWithMapJPanel.clearTabbedWithMaps();
		mainFrame.remove(startApplicationJPanel);
		mainFrame.remove(manufacturerVisualizationWithMapJPanel);
	}
	
	
	/**
	 * Metoda ustawiająca panel powitania jak aktualny widok.
	 * @author Kamil Zimny
	 */
	private void openWelcomeView()
	{
		mainFrame.setBounds(250, 150, 891, 534);
		mainFrame.setTitle(APPLICATION_NAME);
		startApplicationJPanel.addControlJPanel(menuJPanel);
		startApplicationJPanel.addLogicJPanel(welcomeJPanel);
		mainFrame.add(startApplicationJPanel);
		prepareFrameAfterChangeView(mainFrame);
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
	 * Metoda zmieniająca widok z menu startowego do panelu rozpoczynającego planowanie.
	 * @author Kamil Zimny
	 */
	public void change_menu_to_startPlanning()
	{
		startApplicationJPanel.removeLogicJPanel();
		mainFrame.setTitle("Dane zlecenia");
		startApplicationJPanel.addLogicJPanel(startPlanningJPanel);
		startPlanningJPanel.clearData();
		prepareFrameAfterChangeView(mainFrame);
	}
	
	/**
	 * Metoda zmieniająca widok z menu startowego do panelu rejestracji nowego użytkownika.
	 * @author Kamil Zimny
	 */
	public void change_menu_to_registryUser()
	{
		startApplicationJPanel.removeLogicJPanel();
		mainFrame.setTitle("Rejestracja");
		registryJPanel.clearText();
		startApplicationJPanel.addLogicJPanel(registryJPanel);
		prepareFrameAfterChangeView(mainFrame);
	}
	
	/**
	 * Metoda zmieniająca widok z panelu rejestracji nowego użytkownika do menu startowego.
	 * @author Kamil Zimny
	 */
	public void change_registryUser_to_menu()
	{
		startApplicationJPanel.removeLogicJPanel();
		mainFrame.setTitle( APPLICATION_NAME );
		startApplicationJPanel.addLogicJPanel(welcomeJPanel);
		prepareFrameAfterChangeView(mainFrame);
	}
	
	/**
	 * Metoda zmieniająca widok z menu startowego do panelu logownaia użytkownika.
	 * @author Kamil Zimny
	 */
	public void change_menu_to_loginUser()
	{
		startApplicationJPanel.removeLogicJPanel();
		mainFrame.setTitle("Logowanie");
		logJPanel.clearTexts();
		startApplicationJPanel.addLogicJPanel(logJPanel);
		prepareFrameAfterChangeView(mainFrame);
	}
	
	/**
	 * Metoda zmieniająca widok z panelu logowania użytkownika do menu startowego.
	 * @author Kamil Zimny
	 */
	public void change_loginUser_to_menu()
	{
		startApplicationJPanel.removeLogicJPanel();
		mainFrame.setTitle(APPLICATION_NAME);
		startApplicationJPanel.addLogicJPanel(welcomeJPanel);
		prepareFrameAfterChangeView(mainFrame);
	}
	
	/**
	 * Metoda ustawiająca widok na startowy.
	 */
	public void change_to_startingPanel()
	{
		route_planning_presenter.logOutUser();
		
		startApplicationJPanel.removeLogicJPanel();
		openWelcomeView();
		if( menuJPanel.isUserLogged() )
			menuJPanel.setEnableAllButtons(true);
		else
		{
			menuJPanel.setEnableButtonsToUserAction(false);
			menuJPanel.setEnableButtonsFirstAction(true);
		}
	}
	
	
	/**
	 * Metoda zmieniająca widok ze startowego do widoku wizualizacji producentow na mapie.
	 * @author Kamil Zimny
	 */
	public void change_startPlanning_to_manufacturerVisualization()
	{
		
				startApplicationJPanel.removeLogicJPanel();
				mainFrame.remove(startApplicationJPanel);
				mainFrame.setBounds(100, 100, 1123, 580);
				mainFrame.setTitle("Producenci");
				mainFrame.add(manufacturerVisualizationWithMapJPanel);
				prepareFrameAfterChangeView(mainFrame);
		
	}
	
	/**
	 * Metoda zmieniająca widok z wyboru danych pierwszego zlecenia 
	 * do widoku oczekiwania na załadowanie danych aplikacji.
	 */
	public void change_startPlanning_to_Waiting()
	{
		    	JPanel background = new WaitingJPanel();
				background.setLayout(new CardLayout(0, 0));
				background.setBounds(0, 0, 500, 300);
				background.setBackground(SystemColor.inactiveCaptionText);
				menuJPanel.setEnableAllButtons(false);
				
				startApplicationJPanel.removeLogicJPanel();
				startApplicationJPanel.addLogicJPanel(background);
				prepareFrameAfterChangeView(mainFrame);
		
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
		prepareFrameAfterChangeView(manufacturerFrame);
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
		prepareFrameAfterChangeView(manufacturerFrame);
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
	 * Metoda pomocnicza odświerzająca głowne okno aplikacji po zmienie widoku.
	 * @param frame
	 */
	private void prepareFrameAfterChangeView(JFrame frame)
	{
		frame.invalidate();
		frame.validate();
		frame.repaint();
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
	
	/**
	 * Zamyka dodatkowe okno z danymi o producencie.
	 */
	public void closeAboutFrame()
	{
		aboutFream.dispose();
	}
	
	public void setPresenters()
	{
		menuJPanel.setPresenter(route_planning_presenter);
		logJPanel.setPresenter(route_planning_presenter);
		registryJPanel.setPresenter(route_planning_presenter);
		startPlanningJPanel.setPresenter(route_planning_presenter);
		manufacturerVisualizationWithMapJPanel.setPresenter(route_planning_presenter);
		manufacturerOrderDataJPanel.setPresenter(route_planning_presenter);
		manufacturerDetailsJPanel.setPresenter(route_planning_presenter);
		aboutJPanel.setPresenter(route_planning_presenter);
	}
	
	/**
	 * Metoda ustawiająca datę podaną w parametrze w kalendarzu daty rozpoczęcia 
	 * oraz datę podaną w parametrze plus jedne dzień w kalendarzu daty zakończenia.
	 * @param date
	 * @author Kamil Zimny
	 */
	public String city_nextCityAfterComfirm()
	{
		return manufacturerOrderDataJPanel.getNextCityName();
	}
	
	public String city_to()
	{
		return startPlanningJPanel.get_city_to();
	}
	
	public String city_from()
	{
		return startPlanningJPanel.get_city_from();
	}
	
	public String getStartDate()
	{
		return startPlanningJPanel.getStartDate();
	}
	
	public String getFinishDate()
	{
		return startPlanningJPanel.getFinishDate();
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
	
	/**
	 * Metoda ustawiająca datę podaną w parametrze w kalendarzu daty rozpoczęcia 
	 * oraz datę podaną w parametrze plus jedne dzień w kalendarzu daty zakończenia.
	 * @param date
	 * @author Kamil Zimny
	 */
	public void setCalendareDate_StartNewOrder(Date date)
	{
		manufacturerOrderDataJPanel.setStartDateOn(date);
	}
	
	/**
	 * Metoda usuwająca dane dotyczące ostatniego zamówienia z tabeli wyświetlanej w panelu.
	 * @author Kamil Zimny
	 */
	public void removeLastOrderFromTab()
	{
		manufacturerVisualizationWithMapJPanel.removeLastOrderFromTab();
	}
	
	/**
	 * Metoda usuwająca wszystkie zamówienia z tableli wyświetlanej w panelu.
	 * @author Kamil Zimny
	 */
	public void clearOrderTab()
	{
		manufacturerVisualizationWithMapJPanel.clearOrderTab();
	}
	
	/**
	 * Metoda zamykająca główne okno aplikacji.
	 */
	public void closeMainFrame_ManufacturerVisualization()
	{
		mainFrame.dispose();
	}
	
	/**
	 * Metoda pokazująca w menu login aktualnego użytkownika.
	 * @param login
	 * @author Kamil Zimny.
	 */
	public void setNewLoggedUser(String login)
	{
		menuJPanel.setNewLoggedUser(login);
	}
	
	/**
	 * Metoda pokazująca w menu niezalogowane użytkownika.
	 * @param login
	 * @author Kamil Zimny.
	 */
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
		startPlanningJPanel.addAllCityToList();
		manufacturerOrderDataJPanel.addAllCityToList();
	}
	
	public JFrame getMainFrame()
	{
		clearMainFrame();
		return mainFrame;
	}

	
}
