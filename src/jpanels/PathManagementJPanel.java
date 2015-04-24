package jpanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import interfaces.RoutePlanningPresenter;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import dataModels.Order;

public class PathManagementJPanel extends JPanel 
{

	private static final long serialVersionUID = 1L;
	private RoutePlanningPresenter presenter_route_planning;
	private JTable table_routeDiscription;
	
	public PathManagementJPanel() 
	{
		setLayout(null);
		
		JButton btn_removeLast = new JButton("Usu\u0144 ostatnie");
		btn_removeLast.setBounds(201, 395, 132, 38);
		btn_removeLast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				presenter_route_planning.removeLastCity();
			}
		});
		add(btn_removeLast);
		
		JButton btn_saveRoute = new JButton("Zapisz tras\u0119");
		btn_saveRoute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				
			}
		});
		btn_saveRoute.setBounds(201, 346, 132, 38);
		add(btn_saveRoute);
		
		table_routeDiscription = new JTable();
		table_routeDiscription.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Miasto z", "Data rozpocz\u0119cia", "Miasto do", "Data zako\u0144czenia"
			}
		) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			boolean[] columnEditables = new boolean[] {
				false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table_routeDiscription.getColumnModel().getColumn(1).setPreferredWidth(109);
		table_routeDiscription.getColumnModel().getColumn(3).setPreferredWidth(100);

		JScrollPane scroll = new JScrollPane(table_routeDiscription);
		scroll.setBounds(10, 11, 357, 262);
		add(scroll);

	}
	
	public void setPresenter(final RoutePlanningPresenter presenter)
	{
		presenter_route_planning = presenter;
	}
	
	public void addOrderToTab(final Order order)
	{
		DefaultTableModel model = (DefaultTableModel) table_routeDiscription.getModel();
		model.addRow(new Object [] { order.getCityFrom().getCityName() , order.getStartDate(), order.getCityTo().getCityName() , order.getFinishDate()  });
	}
	

}
