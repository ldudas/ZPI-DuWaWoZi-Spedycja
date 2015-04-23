package jpanels;

import interfaces.RoutePlanningPresenter;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JList;
import javax.swing.JButton;

public class RouteJPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Create the panel.
	 */
	
	private RoutePlanningPresenter presenter_ManufacturersVis;

	public RouteJPanel() {
		setLayout(null);
		
		JTabbedPane route_tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		route_tabbedPane.setBounds(361, 45, 227, 210);
		add(route_tabbedPane);
		
		JList route_list = new JList();
		route_list.setBounds(87, 45, 200, 76);
		add(route_list);
		
		JButton route_saveButton = new JButton("Zapisz tras\u0119");
		route_saveButton.setBounds(129, 132, 119, 23);
		add(route_saveButton);

	}
	
	public void setPresenter(final RoutePlanningPresenter presenter)
	{
		presenter_ManufacturersVis = presenter;
	}

}
