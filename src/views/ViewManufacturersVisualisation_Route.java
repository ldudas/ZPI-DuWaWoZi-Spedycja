package views;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JList;
import javax.swing.JButton;

import presenters.PresenterManufacturersVisualisation;

public class ViewManufacturersVisualisation_Route extends JPanel {

	/**
	 * Create the panel.
	 */
	
	private PresenterManufacturersVisualisation presenter_ManufacturersVis;

	public ViewManufacturersVisualisation_Route() {
		setLayout(null);
		
		JTabbedPane route_tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		route_tabbedPane.setBounds(361, 45, 227, 210);
		add(route_tabbedPane);
		
		JList route_list = new JList();
		route_list.setBounds(87, 45, 200, 50);
		add(route_list);
		
		JButton route_saveButton = new JButton("Zapisz tras\u0119");
		route_saveButton.setBounds(129, 132, 119, 23);
		add(route_saveButton);

	}
	
	public void setPresenter(final PresenterManufacturersVisualisation presenter)
	{
		presenter_ManufacturersVis = presenter;
	}

}
