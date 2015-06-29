package manufacturers.jPanels.visualisation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import interfaces.mvp.RoutePlanningPresenter;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import shared.dataModels.Order;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;


public class PathManagementJPanel extends JPanel 
{

	private static final long serialVersionUID = 1L;
	private RoutePlanningPresenter presenter_route_planning;
	private JTable table_routeDiscription;
	private DefaultTableModel model;
	
	public PathManagementJPanel() 
	{
		setBackground(SystemColor.inactiveCaption);
		setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.inactiveCaptionText);
		panel.setBounds(0, -1, 372, 502);
		add(panel);
		panel.setLayout(null);
		
		JButton btn_showTransporters = new JButton("Wybierz przewoźnika");
		btn_showTransporters.setBounds(162, 435, 200, 45);
		panel.add(btn_showTransporters);
		btn_showTransporters.setBackground(SystemColor.activeCaption);
		btn_showTransporters.setForeground(SystemColor.desktop);
		btn_showTransporters.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		
		table_routeDiscription = new JTable();
		table_routeDiscription.setOpaque(true);
		table_routeDiscription.setFillsViewportHeight(true);
        
		table_routeDiscription.setForeground(SystemColor.desktop);
		table_routeDiscription.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		table_routeDiscription.setBackground(SystemColor.inactiveCaption);
		
		table_routeDiscription.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Miasto z", "Wyjazd", "Miasto do", "Przyjazd"
			}
		) {

			private static final long serialVersionUID = 1L;
			boolean[] columnEditables = new boolean[] {
				false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		
		table_routeDiscription.getTableHeader().setBackground(SystemColor.inactiveCaptionText);
		table_routeDiscription.getTableHeader().setForeground(new Color(255, 204, 0));
		
		table_routeDiscription.getTableHeader().setFont( new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		table_routeDiscription.getColumnModel().getColumn(1).setPreferredWidth(109);
		table_routeDiscription.getColumnModel().getColumn(3).setPreferredWidth(100);
		
		model = (DefaultTableModel) table_routeDiscription.getModel();
		
		JScrollPane scroll = new JScrollPane(table_routeDiscription);
		scroll.setBounds(10, 25, 357, 253);
		panel.add(scroll);
		
		JButton btn_removeLast = new JButton("Usuń ostatnie miasto");
		btn_removeLast.setBounds(162, 289, 200, 31);
		panel.add(btn_removeLast);
		btn_removeLast.setBackground(SystemColor.activeCaption);
		btn_removeLast.setForeground(SystemColor.desktop);
		btn_removeLast.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		btn_removeLast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				if(  model.getRowCount() > 1  )
				{
					int dialogResult = JOptionPane.showConfirmDialog(PathManagementJPanel.this, "Czy na pewno usunąć poniższy wpis:"
							+ "\n\nData wyjazdu: " + presenter_route_planning.getLastOrder().getFinishDate()
							+ "\nMiasto z: " + presenter_route_planning.getLastOrder().getCityFrom().getCityName()
							+ "\nData przyjazdu: " + presenter_route_planning.getLastOrder().getFinishDate()
							+ "\nMiasto do: " +presenter_route_planning.getLastOrder().getCityTo().getCityName() +"\n\n", 
							"Potwierdzenie", JOptionPane.YES_NO_OPTION);
					if( dialogResult == JOptionPane.YES_OPTION)
					{
						presenter_route_planning.removeLastCity();
						presenter_route_planning.set_priviousCityToVisualisation();
						presenter_route_planning.removeLastOrder();
						presenter_route_planning.removeLastOrderFromTab();
						JOptionPane.showMessageDialog(PathManagementJPanel.this, "Ostatnio dodane zlecenie zostało usunięte...", "Usunięto", 
								JOptionPane.INFORMATION_MESSAGE);	
					}
					
				}
				else
					JOptionPane.showMessageDialog(PathManagementJPanel.this, "Nie można usunąć pierwszego zlecenia...", "Błąd usuwania", 
							JOptionPane.ERROR_MESSAGE);	
				
			}
		});
		btn_showTransporters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				int num_of_cities = presenter_route_planning.getNumberOfCitiesInPathVis();
				
				if(num_of_cities>2)
				{
					int dialogResult = JOptionPane.showConfirmDialog(PathManagementJPanel.this, "Czy na pewno akceptujesz trasę\n"+
																							    " i chcesz przejść do wyboru przewoźnika?", 
																							    "Potwierdzenie edycji trasy", JOptionPane.YES_NO_OPTION);
					if(dialogResult == JOptionPane.YES_OPTION) 
					{
						 presenter_route_planning.changeManufacurerVisualization_to_transportVisualization(1);
					} 
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Nie można przejść dalej tylko z początkową trasą", "Błąd", 
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

	}
	
	public void setPresenter(final RoutePlanningPresenter presenter)
	{
		presenter_route_planning = presenter;
	}
	
	public void addOrderToTab(final Order order)
	{
		model.addRow(new Object [] { order.getCityFrom().getCityName() , order.getStartDate(), order.getCityTo().getCityName() , order.getFinishDate()  });
	}
	
	public void removeLastOrderFromTab()
	{	
		model.removeRow(model.getRowCount() - 1);
	}
	
	public void clearOrderTab()
	{
		int numberOfElements = model.getRowCount();
		for(int i=0; i < numberOfElements; i++)
			model.removeRow(0);
	}
	

}
