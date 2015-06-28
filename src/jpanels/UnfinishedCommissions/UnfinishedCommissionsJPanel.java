package jpanels.UnfinishedCommissions;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import dataModels.Commission;

import javax.swing.JButton;

import unfinishedCommissions.UnfinishedCommissionsView;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JComboBox;

public class UnfinishedCommissionsJPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Create the panel.
	 */
	
	private JTable table_routeDiscription;
	@SuppressWarnings("unused")
	private UnfinishedCommissionsView view;
	private JComboBox<Object> route_name_filtr_combo;
	private boolean if_filtr = false;
	private ArrayList<Commission> temp;
	
	public UnfinishedCommissionsJPanel(ArrayList<Commission> result, UnfinishedCommissionsView view) {
		
		this.view = view;
		setBounds(1,1,1350,400);
		setBackground(SystemColor.inactiveCaption);
		setLayout(null);
		

		
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.inactiveCaptionText);
		panel.setBounds(1, 20, 1350, 400);
		add(panel);
		panel.setLayout(null);
		
		Object[][] commissions = new Object [result.size()][14];
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
			commissions[i][13]=result.get(i).getId();
			}
		
		table_routeDiscription = new JTable();
		table_routeDiscription.setOpaque(true);
		table_routeDiscription.setFillsViewportHeight(true);
        table_routeDiscription.setForeground(SystemColor.desktop);
		table_routeDiscription.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		table_routeDiscription.setBackground(SystemColor.inactiveCaption);
		table_routeDiscription.setSelectionBackground(Color.LIGHT_GRAY);
		table_routeDiscription.setModel(new DefaultTableModel(
				commissions,
			new String[] {
						
					"Nazwa trasy", "Data rozp. plan.", "Data rozp. rzecz",
					"Data zak. plan.", "Data zak. rzecz.","Koszt","Wartość",
					"Poj.","Ładow.", "Producent", "Miasto początkowe",
					"Miasto końcowe", "Przewoźnik", "ID"
			}
		) {

			private static final long serialVersionUID = 1L;
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false, false, false, false, false, false, false,false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		
		table_routeDiscription.getTableHeader().setBackground(SystemColor.inactiveCaptionText);
		table_routeDiscription.getTableHeader().setForeground(new Color(255, 204, 0));
		table_routeDiscription.getTableHeader().setFont( new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		
		JScrollPane scroll = new JScrollPane(table_routeDiscription);
		scroll.setBounds(12, 10, 1333, 300);
		panel.add(scroll);
		
		JButton btnWybierz = new JButton("Wybierz");
		btnWybierz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int selected_row = table_routeDiscription.getSelectedRow();
				if(selected_row==-1){
					JOptionPane.showMessageDialog(panel,"Nie wybrałeś żadnego zlecenia.","Powiadomienie",1);
				}
				else if(table_routeDiscription.getSelectedRowCount()>1){
					JOptionPane.showMessageDialog(panel,"Możesz wybrać tylko jedno zlecenie","Powiadomienie",1);
				}
				else{ 
					try {
						boolean condition = false;
						if(if_filtr){
							int index=0;
							while(!condition){
								if(temp.get(selected_row).getId() == result.get(index).getId()){
									condition = true;
								}
								else{
									index++;
								}
								}
							selected_row = index;
						}
						
						view.change_to_selected_commission(selected_row,result);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
		btnWybierz.setBackground(SystemColor.activeCaption);
		btnWybierz.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		btnWybierz.setBounds(1135, 316, 171, 56);
		panel.add(btnWybierz);
		
		JLabel lblNazwaTrasy = new JLabel("Nazwa trasy");
		lblNazwaTrasy.setForeground(new Color(255, 204, 0));
		lblNazwaTrasy.setBackground(new Color(255, 204, 0));
		lblNazwaTrasy.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblNazwaTrasy.setBounds(29, 316, 86, 14);
		panel.add(lblNazwaTrasy);
		
		JButton btnFiltruj = new JButton("Filtruj");
		btnFiltruj.setBackground(SystemColor.activeCaption);
		btnFiltruj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(route_name_filtr_combo.getSelectedIndex() == -1 ){
					JOptionPane.showMessageDialog(panel," Wybierz nazwę trasy do filtrowania ","Powiadomienie",1);
				}
				else{
					view.filtr((String)route_name_filtr_combo.getSelectedItem());
					if_filtr=true;
				}
				
			}
		});
		btnFiltruj.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		btnFiltruj.setBounds(198, 316, 171, 56);
		panel.add(btnFiltruj);
		
		JButton deletFiltr_btn = new JButton("Usuń filtr");
		deletFiltr_btn.setBackground(SystemColor.activeCaption);
		deletFiltr_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view.delete_filtr();
				if_filtr=false;
			}
		});
		deletFiltr_btn.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		deletFiltr_btn.setBounds(407, 316, 171, 56);
		panel.add(deletFiltr_btn);
		
		HashSet<String> set = new HashSet<String>();
		for(int i=0;i<result.size();i++){
			set.add(result.get(i).getRouteName());
		}
		String[] names_to_combo = set.toArray(new String[0]);
		
	    route_name_filtr_combo = new JComboBox<Object>(names_to_combo);
		route_name_filtr_combo.setBounds(28, 344, 130, 28);
		panel.add(route_name_filtr_combo);
		
	}
	
	public void update_with_data(ArrayList<Commission> result){
		Object[][] commissions = new Object [result.size()][14];
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
			commissions[i][13]=result.get(i).getId();
			}
		
		table_routeDiscription.setModel(new DefaultTableModel(
				commissions,
			new String[] {
						
					"Nazwa trasy", "Data rozp. plan.", "Data rozp. rzecz",
					"Data zak. plan.", "Data zak. rzecz.","Koszt","Wartość",
					"Poj.","Ładow.", "Producent", "Miasto początkowe",
					"Miasto końcowe", "Przewoźnik","ID"
			}
		) {

			private static final long serialVersionUID = 1L;
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false, false, false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		
	}
	
	public void filtr(ArrayList<Commission> result,String route_name){
		int count=0;
		for(int i=0;i<result.size();i++){
			if(result.get(i).getRouteName().compareTo(route_name) == 0){
				count++;
			}
		}
		int y=0;
		Object[][] commissions = new Object [count][14];
		temp = new ArrayList<Commission>(count);
		for(int i=0;i<result.size();i++){
			if(result.get(i).getRouteName().compareTo(route_name) == 0){
				commissions[y][0]=result.get(i).getRouteName();
				commissions[y][1]=result.get(i).getStartDatePlan();
				commissions[y][2]=result.get(i).getStartDateReal();
				commissions[y][3]=result.get(i).getFinishDatePlan();
				commissions[y][4]=result.get(i).getFinishDateReal();
				commissions[y][5]=result.get(i).getTransporterCost();
				commissions[y][6]=result.get(i).getCommissionValue();
				commissions[y][7]=result.get(i).getVehicleCapacity();
				commissions[y][8]=result.get(i).getVehcicleCapacity2();
				commissions[y][9]=result.get(i).getManufacturer();
				commissions[y][10]=result.get(i).getCityA();
				commissions[y][11]=result.get(i).getCityB();
				commissions[y][12]=result.get(i).getTransporter();
				commissions[y][13]=result.get(i).getId();
				temp.add(result.get(i));
				y++;
				}
			}
		
		
		
		table_routeDiscription.setModel(new DefaultTableModel(
				commissions,
			new String[] {
						
					"Nazwa trasy", "Data rozp. plan.", "Data rozp. rzecz",
					"Data zak. plan.", "Data zak. rzecz.","Koszt","Wartość",
					"Poj.","Ładow.", "Producent", "Miasto początkowe",
					"Miasto końcowe", "Przewoźnik", "ID"
			}
		) {

			private static final long serialVersionUID = 1L;
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false, false, false, false, false, false, false,false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
	}
	
	public void change_flag(){
		if_filtr = false;
	}
}
