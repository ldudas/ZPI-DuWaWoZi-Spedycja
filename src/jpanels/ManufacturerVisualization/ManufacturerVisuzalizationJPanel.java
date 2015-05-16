package jpanels.ManufacturerVisualization;

import interfaces.RoutePlanningPresenter;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import java.awt.Color;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import dataModels.Order;


public class ManufacturerVisuzalizationJPanel extends JComponent 
{

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	
	private ManufacturerManagementJPanel man_managmentJPanel;
	private PathManagementJPanel path__managmentJPanel;
	
	private JTabbedPane map_tabbedPane;
	private RoutePlanningPresenter presenter_route_planning;

	public ManufacturerVisuzalizationJPanel() 
	{
		setForeground(Color.WHITE);
		setBackground(Color.RED);
		setLayout(null);
		
		man_managmentJPanel = new ManufacturerManagementJPanel();
		man_managmentJPanel.setBounds(45, 21, 341, 458);
		add(man_managmentJPanel);
		
		path__managmentJPanel = new PathManagementJPanel();
		path__managmentJPanel .setBounds(10, 30, 380, 458);
		add( path__managmentJPanel );
		path__managmentJPanel.setVisible(false);
		
		map_tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		map_tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) 
			{
				presenter_route_planning.tabChanged();
			}
		});
		map_tabbedPane.setBounds(396, 40, 700, 480);
		add(map_tabbedPane);
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
	
	public void setManagementJPanelVisibility(boolean vis)
	{
		man_managmentJPanel.setVisible(vis);
		path__managmentJPanel.setVisible(!vis);
	}
	
	public void setCurrentTabOfMap()
	{
		map_tabbedPane.setSelectedIndex(map_tabbedPane.getTabCount()-1);
	}
	
	public void addOrderToMap(final Order order)
	{
		path__managmentJPanel.addOrderToTab(order);
	}
}
