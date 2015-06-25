package jpanel.UnfinishedCommissions;

import javax.swing.JPanel;

import java.awt.SystemColor;

import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Font;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;

import unfinishedCommissions.Unfinished_commissions_view;
import jpanel.calendare.JCalendar;
import dataModels.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class Selected_commission_jpanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField cost_textField;
	private JTextField value_textField;
	private JTextField capacity_textField;
	private JTextField capacity2_textField;
	private JCalendar startDate;
	private JCalendar endDate;
	private JTextField startDatePlan_textField;
	private JTextField endDatePlan_textField;
	private JTextArea commissionID_textArea;
	private JTextArea producer_textArea;
	private JTextArea cityA_textArea;
	private JTextArea cityB_textArea;
	private JTextArea transporter_textArea;
	private JTextArea name_textArea;
	private Unfinished_commissions_view view;
	private int selected;

	/**
	 * Create the panel.
	 * @throws ParseException 
	 */
	public Selected_commission_jpanel(int choosen, ArrayList<Commission> result,Unfinished_commissions_view view) throws ParseException {
		
		this.view = view;
		view.getFrame().setBounds(1, 1, 970, 550);
		selected = choosen;
		setBackground(SystemColor.inactiveCaptionText);
		setLayout(null);
		setBounds(0,0,970,550);
		
		
		JPanel panel_startDate = new JPanel();	
		panel_startDate.setBackground(SystemColor.inactiveCaptionText);
		panel_startDate.setBounds(312, 10, 297, 361);
		add(panel_startDate);
		panel_startDate.setLayout(null);
		
		startDate = new JCalendar();
		startDate.getMonthChooser().getComboBox().setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 12));
		startDate.getYearChooser().getSpinner().setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		startDate.getYearChooser().getSpinner().setForeground(SystemColor.desktop);
		startDate.getYearChooser().getSpinner().setBackground(SystemColor.activeCaption);
		startDate.getDayChooser().getDayPanel().setBackground(SystemColor.inactiveCaptionText);
		startDate.setBounds(10, 83, 280, 220);
		startDate.setLocale(new Locale("pl", "PL"));
		panel_startDate.add(startDate);
		
		JLabel startDate_l = new JLabel("Data rozp. rzecz.:");
		startDate_l.setBounds(10, 67, 120, 14);
		panel_startDate.add(startDate_l);
		startDate_l.setForeground(new Color(255, 204, 0));
		startDate_l.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		
		JLabel startDatePlanL = new JLabel("Data rozp. plan.:");
		startDatePlanL.setBounds(10, 11, 120, 14);
		panel_startDate.add(startDatePlanL);
		startDatePlanL.setForeground(new Color(255, 204, 0));
		startDatePlanL.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		
		startDatePlan_textField = new JTextField();
		startDatePlan_textField.setEditable(false);
		startDatePlan_textField.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		startDatePlan_textField.setForeground(new Color(255, 204, 0));
		startDatePlan_textField.setBackground(SystemColor.inactiveCaptionText);
		startDatePlan_textField.setBounds(10, 36, 120, 20);
		panel_startDate.add(startDatePlan_textField);
		startDatePlan_textField.setColumns(10);
		
		
		
		JPanel panel_endDate = new JPanel();	
		panel_endDate.setBackground(SystemColor.inactiveCaptionText);
		panel_endDate.setBounds(657, 10, 297, 361);
		add(panel_endDate);
		panel_endDate.setLayout(null);
		
		endDate = new JCalendar();
		endDate.getMonthChooser().getComboBox().setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 12));
		endDate.getYearChooser().getSpinner().setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		endDate.getYearChooser().getSpinner().setForeground(SystemColor.desktop);
		endDate.getYearChooser().getSpinner().setBackground(SystemColor.activeCaption);
		endDate.getDayChooser().getDayPanel().setBackground(SystemColor.inactiveCaptionText);
		endDate.setBounds(10, 83, 280, 220);
		endDate.setLocale(new Locale("pl", "PL"));
		panel_endDate.add(endDate);
		
		JLabel endDate_l = new JLabel("Data zak. plan.:");
		endDate_l.setBounds(10, 11, 120, 14);
		panel_endDate.add(endDate_l);
		endDate_l.setForeground(new Color(255, 204, 0));
		endDate_l.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		
		JLabel endDatePlanL = new JLabel("Data zak. rzecz.:");
		endDatePlanL.setBounds(10, 67, 120, 14);
		panel_endDate.add(endDatePlanL);
		endDatePlanL.setForeground(new Color(255, 204, 0));
		endDatePlanL.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		
		endDatePlan_textField = new JTextField();
		endDatePlan_textField.setEditable(false);
		endDatePlan_textField.setForeground(new Color(255, 204, 0));
		endDatePlan_textField.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		endDatePlan_textField.setBackground(SystemColor.inactiveCaptionText);
		endDatePlan_textField.setBounds(10, 36, 120, 20);
		panel_endDate.add(endDatePlan_textField);
		endDatePlan_textField.setColumns(10);
		
		
		
		JLabel lblIdZlecenia_1 = new JLabel("ID zlecenia: ");
		lblIdZlecenia_1.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblIdZlecenia_1.setBackground(new Color(255, 204, 0));
		lblIdZlecenia_1.setForeground(new Color(255, 204, 0));
		lblIdZlecenia_1.setBounds(20, 10, 79, 14);
		add(lblIdZlecenia_1);
		
		
		this.commissionID_textArea = new JTextArea();
		commissionID_textArea.setBackground(SystemColor.inactiveCaptionText);
		commissionID_textArea.setForeground(new Color(255, 204, 0));
		commissionID_textArea.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		commissionID_textArea.setText("chuj");
		commissionID_textArea.setBounds(109, 9, 50, 18);
		add(commissionID_textArea);
		
		JLabel lblKosztPrzewozu = new JLabel("Koszt przewozu:");
		lblKosztPrzewozu.setForeground(new Color(255, 204, 0));
		lblKosztPrzewozu.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblKosztPrzewozu.setBounds(20, 50, 116, 14);
		add(lblKosztPrzewozu);
		
		JLabel lblWartoZlecenia = new JLabel("Warto\u015B\u0107 zlecenia: ");
		lblWartoZlecenia.setForeground(new Color(255, 204, 0));
		lblWartoZlecenia.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblWartoZlecenia.setBounds(20, 90, 120, 14);
		add(lblWartoZlecenia);
		
		JLabel lblPojemnoPojazdu = new JLabel("Pojemno\u015B\u0107 pojazdu:");
		lblPojemnoPojazdu.setForeground(new Color(255, 204, 0));
		lblPojemnoPojazdu.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblPojemnoPojazdu.setBounds(20, 130, 139, 14);
		add(lblPojemnoPojazdu);
		
		JLabel lbladownoPojazdu = new JLabel("\u0141adowno\u015B\u0107 pojazdu:");
		lbladownoPojazdu.setForeground(new Color(255, 204, 0));
		lbladownoPojazdu.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lbladownoPojazdu.setBounds(20, 170, 139, 14);
		add(lbladownoPojazdu);
		
		JLabel lblProducent = new JLabel("Producent:");
		lblProducent.setForeground(new Color(255, 204, 0));
		lblProducent.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblProducent.setBounds(20, 210, 79, 14);
		add(lblProducent);
		
		this.producer_textArea = new JTextArea();
		producer_textArea.setForeground(new Color(255, 204, 0));
		producer_textArea.setBackground(SystemColor.inactiveCaptionText);
		producer_textArea.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		producer_textArea.setText("chuj");
		producer_textArea.setBounds(109, 209, 137, 18);
		add(producer_textArea);
		
		JLabel lblMiastoPocztkowe = new JLabel("Miasto pocz\u0105tkowe: ");
		lblMiastoPocztkowe.setForeground(new Color(255, 204, 0));
		lblMiastoPocztkowe.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblMiastoPocztkowe.setBounds(20, 250, 139, 14);
		add(lblMiastoPocztkowe);
		
		this.cityA_textArea = new JTextArea();
		cityA_textArea.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		cityA_textArea.setForeground(new Color(255, 204, 0));
		cityA_textArea.setBackground(SystemColor.inactiveCaptionText);
		cityA_textArea.setText("chuj");
		cityA_textArea.setBounds(160, 249, 142, 18);
		add(cityA_textArea);
		
		JLabel lblMiastoKocowe = new JLabel("Miasto ko\u0144cowe: ");
		lblMiastoKocowe.setForeground(new Color(255, 204, 0));
		lblMiastoKocowe.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblMiastoKocowe.setBounds(20, 290, 116, 14);
		add(lblMiastoKocowe);
		
		this.cityB_textArea = new JTextArea();
		cityB_textArea.setText("chuj");
		cityB_textArea.setBackground(SystemColor.inactiveCaptionText);
		cityB_textArea.setForeground(new Color(255, 204, 0));
		cityB_textArea.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		cityB_textArea.setBounds(146, 289, 156, 18);
		add(cityB_textArea);
		
		JLabel lblPrzewonik = new JLabel("Przewo\u017Anik: ");
		lblPrzewonik.setForeground(new Color(255, 204, 0));
		lblPrzewonik.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblPrzewonik.setBounds(20, 330, 82, 14);
		add(lblPrzewonik);
		
		this.transporter_textArea = new JTextArea();
		transporter_textArea.setForeground(new Color(255, 204, 0));
		transporter_textArea.setBackground(SystemColor.inactiveCaptionText);
		transporter_textArea.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		transporter_textArea.setText("chuj");
		transporter_textArea.setBounds(109, 329, 193, 18);
		add(transporter_textArea);
		
		JLabel lblNazwaTrasy = new JLabel("Nazwa trasy:");
		lblNazwaTrasy.setForeground(new Color(255, 204, 0));
		lblNazwaTrasy.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblNazwaTrasy.setBounds(20, 370, 92, 14);
		add(lblNazwaTrasy);
		
		this.name_textArea = new JTextArea();
		name_textArea.setForeground(new Color(255, 204, 0));
		name_textArea.setBackground(SystemColor.inactiveCaptionText);
		name_textArea.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		name_textArea.setText("chuj");
		name_textArea.setBounds(120, 369, 182, 18);
		add(name_textArea);
		
		JButton btnZapisz = new JButton("Zapisz");
		btnZapisz.setBackground(SystemColor.activeCaption);
		btnZapisz.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(endDate.getDate().compareTo(startDate.getDate()) <= 0){
					JOptionPane.showMessageDialog(panel_endDate,"Data zakończnie musi być późniejsza niż rozpoczęcia");
				}
				else{
					view.save_change();
					view.save_to_dataBase(selected);
				}
			}
		});
		btnZapisz.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		btnZapisz.setBounds(410, 439, 150, 23);
		add(btnZapisz);
		
		cost_textField = new JTextField();
		cost_textField.setForeground(new Color(255, 204, 0));
		cost_textField.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		cost_textField.setBackground(SystemColor.inactiveCaptionText);
		cost_textField.setBounds(160, 47, 86, 20);
		add(cost_textField);
		cost_textField.setColumns(10);
		
		value_textField = new JTextField();
		value_textField.setForeground(new Color(255, 204, 0));
		value_textField.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		value_textField.setBackground(SystemColor.inactiveCaptionText);
		value_textField.setBounds(160, 87, 86, 20);
		add(value_textField);
		value_textField.setColumns(10);
		
		capacity_textField = new JTextField();
		capacity_textField.setForeground(new Color(255, 204, 0));
		capacity_textField.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		capacity_textField.setBackground(SystemColor.inactiveCaptionText);
		capacity_textField.setBounds(160, 127, 86, 20);
		add(capacity_textField);
		capacity_textField.setColumns(10);
		
		capacity2_textField = new JTextField();
		capacity2_textField.setForeground(new Color(255, 204, 0));
		capacity2_textField.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		capacity2_textField.setBackground(SystemColor.inactiveCaptionText);
		capacity2_textField.setBounds(160, 167, 86, 20);
		add(capacity2_textField);
		capacity2_textField.setColumns(10);
		
		commissionID_textArea.setText(Integer.toString(result.get(choosen).getId()));
		
		startDatePlan_textField.setText(result.get(choosen).getStartDatePlan());
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		startDate.setDate( formatter.parse(result.get(choosen).getStartDateReal()) );
		
		endDatePlan_textField.setText(result.get(choosen).getFinishDatePlan());
		
		endDate.setDate( formatter.parse(result.get(choosen).getFinishDateReal()) );
		
		cost_textField.setText(Double.toString(result.get(choosen).getTransporterCost()));
		
		value_textField.setText(Double.toString(result.get(choosen).getCommissionValue()));
		
		capacity_textField.setText(Integer.toString(result.get(choosen).getVehicleCapacity()));
		
		capacity2_textField.setText(Integer.toString(result.get(choosen).getVehcicleCapacity2()));
		
		producer_textArea.setText(result.get(choosen).getManufacturer());
		
		cityA_textArea.setText(result.get(choosen).getCityA());
		
		cityB_textArea.setText(result.get(choosen).getCityB());
		
		transporter_textArea.setText(result.get(choosen).getTransporter());
		
		name_textArea.setText(result.get(choosen).getRouteName());
		
	}
	
	public void save_change(ArrayList<Commission> res){
		
		res.get(selected).setVehcicleCapacity2(Integer.parseInt(capacity2_textField.getText()));
		res.get(selected).setVehicleCapacity(Integer.parseInt(capacity_textField.getText()));
		res.get(selected).setCommissionValue(Double.parseDouble(value_textField.getText()));
		res.get(selected).setTransporterCost(Double.parseDouble(cost_textField.getText()));
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		res.get(selected).setStartDateReal(formatter.format(startDate.getDate()));
		res.get(selected).setFinishDateReal(formatter.format(endDate.getDate()));
		
	}
}
