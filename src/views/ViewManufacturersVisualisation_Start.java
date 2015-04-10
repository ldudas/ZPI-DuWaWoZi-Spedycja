package views;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import presenters.PresenterManufacturersVisualisation;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ViewManufacturersVisualisation_Start extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField start_from_textField;
	private JTextField start_to_textField;
	private JTextField start_tripDate_textField;
	private JTextField start_arrivalDate_textField;
	public PresenterManufacturersVisualisation presenter_ManufacturersVis;
	/**
	 * Create the panel.
	 */
	public ViewManufacturersVisualisation_Start() {
		setLayout(null);
		
		start_from_textField = new JTextField();
		start_from_textField.setBounds(129, 49, 86, 20);
		add(start_from_textField);
		start_from_textField.setColumns(10);
		
		start_to_textField = new JTextField();
		start_to_textField.setBounds(425, 49, 86, 20);
		add(start_to_textField);
		start_to_textField.setColumns(10);
		
		start_tripDate_textField = new JTextField();
		start_tripDate_textField.setBounds(129, 109, 86, 20);
		add(start_tripDate_textField);
		start_tripDate_textField.setColumns(10);
		
		start_arrivalDate_textField = new JTextField();
		start_arrivalDate_textField.setBounds(425, 109, 86, 20);
		add(start_arrivalDate_textField);
		start_arrivalDate_textField.setColumns(10);
		
		JLabel lblSkd = new JLabel("Sk\u0105d:");
		lblSkd.setBounds(129, 36, 46, 14);
		add(lblSkd);
		
		JLabel lblDokd = new JLabel("Dok\u0105d:");
		lblDokd.setBounds(427, 36, 46, 14);
		add(lblDokd);
		
		JLabel lblDataWyjazdu = new JLabel("Data wyjazdu:");
		lblDataWyjazdu.setBounds(129, 94, 86, 14);
		add(lblDataWyjazdu);
		
		JLabel lblDataPrzyjazdu = new JLabel("Data przyjazdu");
		lblDataPrzyjazdu.setBounds(425, 94, 86, 14);
		add(lblDataPrzyjazdu);
		
		JButton start_okButton = new JButton("OK");
		start_okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				presenter_ManufacturersVis.changeView();
			}
		});
		start_okButton.setBounds(276, 160, 89, 23);
		add(start_okButton);
		
	}
	 
	public void setPresenter(final PresenterManufacturersVisualisation presenter)
	{
		presenter_ManufacturersVis = presenter;
	}
	
}
