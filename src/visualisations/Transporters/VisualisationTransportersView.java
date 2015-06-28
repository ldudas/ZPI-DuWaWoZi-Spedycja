package visualisations.Transporters;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import dataModels.*;

import com.esri.map.JMap;

import jpanels.TransportersVisualisation.TransporterInfo.TransporterDetailsJPanel;

/**
 * Widok wizualizacji przewoźników
 * @author Łukasz Dudaszek
 *
 */
public class VisualisationTransportersView 
{
	/**
	 * Prezenter
	 */
	private VisualisationTransportersPresenter trans_presenter;
	
	/**
	 * Pozycja (x i y) okna szczegółów przewoźnika
	 */
	private int windowPos;
	
	/**
	 * Okno główne
	 */
	private JFrame carrierVisualizationFrame;
	
	/**
	 * główny JPanel umieszczany w oknie głównym
	 */
	private TransVisInterfaceJPanel transVisInterfaceJPanel;
	
	/**
	 * flaga czy wizualizacja rozpoczęta
	 */
	private boolean isVisualisationStarted;
	
	/**
	 * kolekcja wyświetlalych przewoźników
	 */
	private ArrayList<Transporter> transporters;
	
	/**
	 *  zapamiętana maksymalna liczba zleceń (wśród wyświetlanych przewoźników)
	 */
	private double max_num_of_orders;
	
	/**
	 *  zapamiętana minimalna liczba zleceń (wśród wyświetlanych przewoźników)
	 */
	private double min_num_of_orders;
	
	/**
	 *  zapamiętany maksymalny koszt (wśród wyświetlanych przewoźników)
	 */
	private double max_cost;
	
	/**
	 *  zapamiętany minimalny koszt (wśród wyświetlanych przewoźników)
	 */
	private double min_cost;
	
	/**
	 *  zapamiętany maksymalna objętość (wśród wyświetlanych przewoźników)
	 */
	private double max_volume;
	
	/**
	 *  zapamiętana maksymalna ładowność (wśród wyświetlanych przewoźników)
	 */
	private double max_capacity;



	public VisualisationTransportersView() 
	{
		isVisualisationStarted = false;
		windowPos = 50;
	}
	
	/**
	 * Ustawianie prezentera
	 * @param pres prezenter
	 * @author Łukasz Dudaszek
	 */
	public void setPresenter(VisualisationTransportersPresenter pres)
	{
		trans_presenter = pres;
	}
	
	/**
	 * Czyszczenie okna głównego
	 * @author Łukasz Dudaszek
	 */
	public void clearCarrierVisualizationFrame()
	{
		if(transVisInterfaceJPanel != null)
			carrierVisualizationFrame.remove(transVisInterfaceJPanel);
		isVisualisationStarted = false;
	}

	
	/**
	 * Inicjalizacja widoku
	 * @param carrierV referencja do okna głównego
	 * @author Łukasz Dudaszek
	 */
	public void initialize( JFrame carrierV) 
	{		
		this.carrierVisualizationFrame = carrierV;
		carrierVisualizationFrame.setTitle("Przewo\u017Anicy");

	    transVisInterfaceJPanel = new TransVisInterfaceJPanel(this,trans_presenter.getOpeningFlag());
	    carrierVisualizationFrame.add(transVisInterfaceJPanel);
	    
		carrierVisualizationFrame.setBounds(250, 30, 825, 725);
		carrierVisualizationFrame.invalidate();
		carrierVisualizationFrame.validate();
	}
	
	/**
	 * Rysowanie wizualizacji przewoźników
	 * @param transporters kolekcja przewoźników do naniesienia na wizualizację
	 */
	 public void drawTransporters(ArrayList<Transporter> transporters)
	    {
		 	if(!isVisualisationStarted)
		 	{
		 		transVisInterfaceJPanel.removeStartVisualisationPanel();
		 		transVisInterfaceJPanel.addVisualisation();
				isVisualisationStarted = true;
		 	}
		 	
	    	this.transporters = transporters;
	    	
	    	max_num_of_orders = Double.MIN_VALUE;
	    	min_num_of_orders = Double.MAX_VALUE;
	    	max_cost = Double.MIN_VALUE;
	    	min_cost = Double.MAX_VALUE;
	    	max_volume = Double.MIN_VALUE;
	    	max_capacity = Double.MIN_VALUE;
	    	
	    	transporters.stream().forEach( (Transporter tr) -> {
	    		if(tr.getNumber_of_orders()<min_num_of_orders) min_num_of_orders = tr.getNumber_of_orders();
	    		if(tr.getNumber_of_orders()>max_num_of_orders) max_num_of_orders = tr.getNumber_of_orders();
	    		if(tr.getCost()>max_cost) max_cost = tr.getCost();
	    		if(tr.getCost()<min_cost) min_cost = tr.getCost();
	    		if(tr.getVolume()>max_volume) max_volume = tr.getVolume();
	    		if(tr.getCapacity()>max_capacity) max_capacity = tr.getCapacity();	
	    	});
	    	
	    	transVisInterfaceJPanel.repaintVisualisation();
	    }
	 
	 /**
	  * Rysowanie wizualizacji przewoźników dla podanych parametrów
	  * @param city_from Miasto początkowe
	  * @param city_to Miasto docelowe
	  * @param sc Keteoria rozmiaru pojazdu
	  * @author Łukasz Dudaszek
	  */
	 public void drawTransporters(String city_from, String city_to, SizeCategory sc)
	 {
		 trans_presenter.drawTransporters(city_from, city_to, sc);
	 }
	 
	 /**
	  * Otworzenie okna ze szczegółami przewoźnika
	  * @param t przewoźnik do wyświetlenia
	  * @param routes mapa z łańcuchem dostaw przewoźnika
	  * @return okno ze szczegółowymi danymi przewoźnika
	  */
	 public JFrame showTransporterDetailsWindow(Transporter t,JMap routes)
	 {
				
				JFrame detailsWindow = new JFrame(t.getName() + " - " + (t.getSizeCategory() == SizeCategory.SMALL?"Małe":
																t.getSizeCategory() == SizeCategory.MEDIUM?"Średnie":
																											"Duże"));
				detailsWindow.setResizable(false);
				detailsWindow.setBounds(windowPos, windowPos, 910, 350);
				detailsWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				
				TransporterDetailsJPanel transDetailsPanel = new TransporterDetailsJPanel();
				transDetailsPanel.setInfoAboutTransporterInToList(t);
				transDetailsPanel.setRoutesMap(routes);
				detailsWindow.getContentPane().add(transDetailsPanel);
				detailsWindow.setVisible(true);
				return detailsWindow;
	 }
	 

		/**
		 * Ustaw wybranego przewoźnika
		 * @param t Wybrany przewoźnik
		 */
		public void setChosenTransporter(Transporter t)
		{
			transVisInterfaceJPanel.setChosenTransporter(t);
			trans_presenter.setChosenTransporter(t);
		}
		
		/**
		 * Pobierz wybranego przewoźnika
		 * @return Wybrany przewoźnik
		 */
		public Transporter getChosenTransporter()
		{
			return trans_presenter.getChosenTransporter();
		}
		
		/**
		 * @return Kolekcja nazw miast z bazy danych
		 */
		public ArrayList<String> getAllCityNames()
		{
			return trans_presenter.getAllCityNames();
		}
		
		/**
		 * Zapisane trasy do bazy danych
		 */
		public void saveOrdersToDatabase()
		{
			trans_presenter.saveOrdersToDatabase();
		}
	 
		/**
		 * Pokazanie okna szczegółowych danych przewożnika
		 * @param id_trans
		 * @ okno ze szczegółowymi danymi przewoźnika
		 */
	 public JFrame showTransporterDetails(int id_trans)
	 {
		 return trans_presenter.showTransporterDetails(id_trans);
	 }
	 
	 /**
	  * Pokazanie okna dacyzji czy wprowadzić nazwę trasy raz jeszcze
	  * @return true - tak, false - nie
	  */
	 public boolean showRepeatInsertionDialog()
	 {
		 return (JOptionPane.showConfirmDialog (null, "Nie wprowadzono nazwy trasy.\nCzy chcesz ponowić wprowadzenie?","Brak nazwy trasy",JOptionPane.YES_NO_OPTION)) == JOptionPane.YES_OPTION
				 ? true : false;
	 }

	 
	public double getMax_num_of_orders()
	{
		return max_num_of_orders;
	}

	public double getMin_num_of_orders()
	{
		return min_num_of_orders;
	}

	public double getMax_cost()
	{
		return max_cost;
	}

	public double getMin_cost()
	{
		return min_cost;
	}

	public double getMax_volume()
	{
		return max_volume;
	}

	public double getMax_capacity()
	{
		return max_capacity;
	}

	public ArrayList<Transporter> getTransporters()
	{
		return transporters;
	}
	
	/**
	 * Pokaż okno z informacją o błędzie zapisu trasy do bazy danych
	 */
	public void showDatabaseSaveError()
	{
		JOptionPane.showMessageDialog(null, "Nie można zapisać trasy do bazy danych", "Błąd zapisu", 
				JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Pokaż okno z informacją o sukcesie zapisu trasy do bazy danych
	 */
	public void showSaveSuccessDialog()
	{
		JOptionPane.showMessageDialog(null, "Pomyślnie zapisano trasę do bazy", "Zapisano", 
				JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Pokaż okno do wprowadzenia nazyw trasy
	 * @return nazwa trasy
	 */
	public String showRouteNameDialog()
	{
		return JOptionPane.showInputDialog(null, "Podaj nazwę trasy: ","Nazwa trasy",JOptionPane.PLAIN_MESSAGE);
	}
	
	/**
	 * Pokaż okno z informacją o istniejącej już w bazie nazwie trasy
	 */
	public void showNonUniqueRouteNameDialog()
	{
		JOptionPane.showMessageDialog(null, "Istnieje już w systemie trasa o podanej nazwie\nProszę wprowadzić inną.", "Nieunikalna nazwa trasy", 
				JOptionPane.ERROR_MESSAGE);
	}
	 
	    
}




	

