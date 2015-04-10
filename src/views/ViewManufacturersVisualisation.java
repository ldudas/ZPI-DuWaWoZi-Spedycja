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
	
	
	private JFrame frame;
	public ViewManufacturersVisualisation_Route route;
	public ViewManufacturersVisualisation_Map map;
	public ViewManufacturersVisualisation_Start start;


	/**
	 * Launch the application.
	 */
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
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		route = new ViewManufacturersVisualisation_Route();
		map = new ViewManufacturersVisualisation_Map();
		start = new ViewManufacturersVisualisation_Start();
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
	
	public void change(){
		frame.remove(start);
		frame.add(map);
		frame.repaint();
	}
	
	public void setPresenters(){
		start.setPresenter(presenter_ManufacturersVis);
		map.setPresenter(presenter_ManufacturersVis);
		route.setPresenter(presenter_ManufacturersVis);
	}

	
}