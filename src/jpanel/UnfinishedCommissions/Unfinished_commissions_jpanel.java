package jpanel.UnfinishedCommissions;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import dataModels.Commission;

import javax.swing.JButton;

import unfinishedCommissions.Unfinished_commissions_view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Unfinished_commissions_jpanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Create the panel.
	 */
	
	private JTable table_routeDiscription;
	private DefaultTableModel model;
	private Unfinished_commissions_view view;
	
	public Unfinished_commissions_jpanel(ArrayList<Commission> result, Unfinished_commissions_view view) {
		
		this.view = view;
		setBounds(1,1,1350,400);
		setBackground(SystemColor.inactiveCaption);
		setLayout(null);
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.inactiveCaptionText);
		panel.setBounds(1, 1, 1350, 400);
		add(panel);
		panel.setLayout(null);
		
		Object[][] commissions = new Object [result.size()][13];
		for(int i=0;i<result.size();i++){
			commissions[i][0]=result.get(i).getRouteName();
			commissions[i][1]=result.get(i).getStartDatePlan();
			commissions[i][2]=result.get(i).getStartDateReal();
			commissions[i][3]=result.get(i).getFinishDatePlan();
			commissions[i][4]=result.get(i).getFinishDateReal();
			commissions[i][5]=result.get(i).getTransporterCost();
			commissions[i][6]=result.get(i).getCommissionValue();
			commissions[i][7]=result.get(i).getVehicleCapacity();
			commissions[i][8]=result.get(i).getVehcicleCapacity2();
			commissions[i][9]=result.get(i).getManufacturer();
			commissions[i][10]=result.get(i).getCityA();
			commissions[i][11]=result.get(i).getCityB();
			commissions[i][12]=result.get(i).getTransporter();
			}
		
		table_routeDiscription = new JTable();
		table_routeDiscription.setOpaque(true);
		table_routeDiscription.setFillsViewportHeight(true);
        
		table_routeDiscription.setForeground(SystemColor.desktop);
		table_routeDiscription.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		table_routeDiscription.setBackground(SystemColor.inactiveCaption);
		
		table_routeDiscription.setModel(new DefaultTableModel(
				commissions,
			new String[] {
						
					"Nazwa trasy", "Data rozp. plan.", "Data rozp. rzecz",
					"Data zak. plan.", "Data zak. rzecz.","Koszt","Wartość",
					"Poj.","Ładow.", "Producent", "Miasto początkowe",
					"Miasto końcowe", "Przewoźnik"
			}
		) {

			private static final long serialVersionUID = 1L;
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false, false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		
		table_routeDiscription.getTableHeader().setBackground(SystemColor.inactiveCaptionText);
		table_routeDiscription.getTableHeader().setForeground(new Color(255, 204, 0));
		
		table_routeDiscription.getTableHeader().setFont( new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		table_routeDiscription.getColumnModel().getColumn(0).setWidth(10);
		table_routeDiscription.getColumnModel().getColumn(1).setWidth(10);
		table_routeDiscription.getColumnModel().getColumn(2).setWidth(10);
		table_routeDiscription.getColumnModel().getColumn(3).setWidth(10);
		table_routeDiscription.getColumnModel().getColumn(4).setWidth(10);
		table_routeDiscription.getColumnModel().getColumn(5).setWidth(1);
		table_routeDiscription.getColumnModel().getColumn(6).setWidth(1);
		table_routeDiscription.getColumnModel().getColumn(7).setWidth(1);
		table_routeDiscription.getColumnModel().getColumn(8).setWidth(1);
		table_routeDiscription.getColumnModel().getColumn(9).setWidth(10);
		table_routeDiscription.getColumnModel().getColumn(10).setWidth(10);
		table_routeDiscription.getColumnModel().getColumn(11).setWidth(10);
		table_routeDiscription.getColumnModel().getColumn(12).setWidth(10);
		
		model = (DefaultTableModel) table_routeDiscription.getModel();
		
		JScrollPane scroll = new JScrollPane(table_routeDiscription);
		scroll.setBounds(1, 1, 1350, 300);
		panel.add(scroll);
		
		
		
		JButton btnWybierz = new JButton("Wybierz");
		btnWybierz.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int selected_row = table_routeDiscription.getSelectedRow();
				if(selected_row==-1){
					JOptionPane.showMessageDialog(panel,"Nie wybrałeś żadnego zlecenia.");
				}
				else{
					try {
						view.change_to_selected_commission(selected_row,result);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		btnWybierz.setBounds(1224, 337, 89, 23);
		panel.add(btnWybierz);
		
	}
}
