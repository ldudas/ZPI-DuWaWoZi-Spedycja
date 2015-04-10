package views;

import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.BorderLayout;

import presenters.PresenterManufacturersVisualisation;

import com.esri.map.JMap;


public class ViewManufacturersVisualisation 
{
	@SuppressWarnings("unused")
	private PresenterManufacturersVisualisation presenter_ManufacturersVis;
	
	
	public JFrame frame;
	public ViewManufacturersVisualisation_Route route;
	public ViewManufacturersVisualisation_Map map;
	public ViewManufacturersVisualisation_Start start;
	public ViewManufacturersVisualisation_Manufacturer manufacturer;



	/**
	 * Launch the application.
	 */
	/*
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ViewManufacturersVisualisation window = new ViewManufacturersVisualisation();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	*/

	/**
	 * Create the application.
	 */
	public ViewManufacturersVisualisation() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 650, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		route = new ViewManufacturersVisualisation_Route();
		map = new ViewManufacturersVisualisation_Map();
		start = new ViewManufacturersVisualisation_Start();
		manufacturer = new ViewManufacturersVisualisation_Manufacturer();
		frame.add(start);
		}
	 
	/**
	 * Metoda ustawiajaca presentera podanego w parametrze.
	 * @author Kamil Zimny
	 */
	public void setPresenter(final PresenterManufacturersVisualisation presenter)
	{
		presenter_ManufacturersVis = presenter;
	}
	
	/**
	 * Metoda dodajaca mape do okienka.
	 * @author Kamil Zimny
	 */
	public void addMapToWindow(final JMap map)
	{		
		frame.getContentPane().add(map, BorderLayout.CENTER);//to we views
	}
	
	public void change_start_to_map(){
		frame.remove(start);
		presenter_ManufacturersVis.ustawMape();
		frame.add(map);
		frame.invalidate();
		frame.validate();
	}
	
	public void setPresenters(){
		start.setPresenter(presenter_ManufacturersVis);
		map.setPresenter(presenter_ManufacturersVis);
		route.setPresenter(presenter_ManufacturersVis);
		manufacturer.setPresenter(presenter_ManufacturersVis);
	}
	
	public String city_to(){
		return start.get_city_to();
	}

	public void dupa(JMap M){
		map.startuj(M);
	}

	
}