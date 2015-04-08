/**
 * @author[Kamil Zimny]
 */
package views;
import java.awt.BorderLayout;

import javax.swing.JFrame;

import presenters.PresenterManufacturersVisualisation;

import com.esri.map.JMap;


public class ViewManufacturersVisualisation 
{
	@SuppressWarnings("unused")
	private PresenterManufacturersVisualisation presenter_ManufacturersVis;
	private JFrame window;
	
	public ViewManufacturersVisualisation() 
	{
		window = new JFrame();
		
		window.setSize(800, 600);
		window.setLocationRelativeTo(null); // center on screen
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.getContentPane().setLayout(new BorderLayout());
		window.setVisible(true);
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
		window.getContentPane().add(map, BorderLayout.CENTER);//to we views
	}

	
}
