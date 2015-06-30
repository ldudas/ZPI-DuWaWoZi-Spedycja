package manufacturers.jPanels.visualisation;

import interfaces.mvp.RoutePlanningPresenter;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import java.awt.SystemColor;

import javax.swing.JPanel;

import shared.dataModels.Order;

import java.awt.Font;
import java.awt.CardLayout;


/**
 * Panel zawierający i zmieniający się pomiędzy wizualizacją
 * producentów a wizuwalizacją aktualnie stworzonej trasy.
 * @author Kamil Zimny
 *
 */
public class ManufacturerVisuzalizationJPanel extends JComponent 
{

	private static final long serialVersionUID = 1L;
	
	private JTabbedPane map_tabbedPane;
	private RoutePlanningPresenter presenter_route_planning;
	private JPanel panel_background;
	private ManufacturerManagementJPanel man_managmentJPanel;
	private PathManagementJPanel path__managmentJPanel;
	private JPanel panel_menagment;

	public ManufacturerVisuzalizationJPanel() 
	{
		setForeground(SystemColor.inactiveCaption);
		setBackground(SystemColor.inactiveCaption);
		setLayout(null);
		path__managmentJPanel = new PathManagementJPanel();
		
		panel_background = new JPanel();
		panel_background.setBackground(SystemColor.inactiveCaption);
		panel_background.setBounds(0, 0, 1186, 577);
		add(panel_background);
		panel_background.setLayout(null);
		
		map_tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		map_tabbedPane.setForeground(SystemColor.desktop);
		map_tabbedPane.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));
		map_tabbedPane.setBackground(SystemColor.inactiveCaption);
		map_tabbedPane.setBounds(395, 11, 709, 500);
		panel_background.add(map_tabbedPane);
		
		man_managmentJPanel = new ManufacturerManagementJPanel();
		panel_background.add(man_managmentJPanel);
		
		man_managmentJPanel.setBounds(10, 11, 375, 500);
		
		panel_menagment = new JPanel();
		panel_menagment.setBounds(12, 11, 375, 500);
		panel_background.add(panel_menagment);
		panel_menagment.setBackground(SystemColor.inactiveCaptionText);
		panel_menagment.setLayout(new CardLayout(0, 0));
		
		map_tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) 
			{
				presenter_route_planning.tabChanged();
			}
		});
	}
	
	public void setPresenter(final RoutePlanningPresenter presenter)
	{
		presenter_route_planning = presenter;
		man_managmentJPanel.setPresenter(presenter);
		path__managmentJPanel.setPresenter(presenter);
	}
	
	public JTabbedPane getTabWithMaps()
	{
		return map_tabbedPane;
	}
	
	public void clearTabbedWithMaps()
	{
		map_tabbedPane.removeAll();
	}
	
	public void setManagementJPanelVisibility(boolean vis)
	{
		if(vis)
		{
			panel_menagment.remove(path__managmentJPanel);
			panel_menagment.add(man_managmentJPanel);
		}
		else
		{
			panel_menagment.remove(man_managmentJPanel);
			panel_menagment.add(path__managmentJPanel);
		}
	}
	
	public void setCurrentTabOfMap()
	{
		map_tabbedPane.setSelectedIndex(map_tabbedPane.getTabCount()-1);
	}
	
	/**
	 * Metoda dodająca kolejne zlecenie/zamówienie do tabeli wyświetlanej w panelu
	 * @param order - nowe zlecenie dodawane do tabeli.
	 * @author Kamil Zimny
	 */
	public void addOrderToMap(final Order order)
	{
		path__managmentJPanel.addOrderToTab(order);
	}
	
	/**
	 * Metoda usuwająca dane dotyczące ostatniego zamówienia z tabeli wyświetlanej w panelu.
	 * @author Kamil Zimny
	 */
	public void removeLastOrderFromTab()
	{
		path__managmentJPanel.removeLastOrderFromTab();
	}
	
	/**
	 * Metoda usuwająca wszystkie zamówienia z tableli wyświetlanej w panelu.
	 * @author Kamil Zimny
	 */
	public void clearOrderTab()
	{
		path__managmentJPanel.clearOrderTab();
	}
}
