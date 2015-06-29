package visualisations.Transporters;

import java.awt.Dimension;
import java.awt.Toolkit;
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
	 * Okno główne
	 */
	private JFrame carrierVisualizationFrame;
	
	/**
	 * główny JPanel umieszczany w oknie głównym
	 */
	private TransVisInterfaceJPanel transVisInterfaceJPanel;
	
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
	 */
	 public void drawTransporters()
	    {
		 		transVisInterfaceJPanel.removeStartVisualisationPanel();
		 		transVisInterfaceJPanel.addVisualisation();
		 		transVisInterfaceJPanel.repaintVisualisation();
	    }
	 
	 /**
	  * Rysowanie wizualizacji przewoźników dla podanych parametrów
	  * @param city_from Miasto początkowe
	  * @param city_to Miasto docelowe
	  * @param sc Ketegoria rozmiaru pojazdu
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
				
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				double width = screenSize.getWidth();
				double height = screenSize.getHeight();
				
				
				detailsWindow.setBounds((int)(width/2 - 455), (int)(height/2 - 175), 910, 350);
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
	 public JFrame showTransporterDetails(int id_trans, SizeCategory sc)
	 {
		 return trans_presenter.showTransporterDetails(id_trans, sc);
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

	 
	

	public ArrayList<Transporter> getTransporters()
	{
		return trans_presenter.getFilteredTransporters();
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
	
	/**
	 * Pokaż okno z informacją o takich samych miastach - początkowym i docelowym.
	 */
	public void showBeginEndCitiesEqualInfo()
	{
		JOptionPane.showMessageDialog(null, "Miasta początkowe oraz docelowe powinny być różne", "Błędne dane", 
				JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void addWaitingPanel()
	{
		transVisInterfaceJPanel.addWaitingPanel();
	}
	
	public void removeStartPanel()
	{
		transVisInterfaceJPanel.removeStartVisualisationPanel();
	}
	
	public double getMax_num_of_orders()
	{
		return trans_presenter.getMax_num_of_orders();
	}

	public double getMin_num_of_orders()
	{
		return trans_presenter.getMin_num_of_orders();
	}

	public double getMax_cost()
	{
		return trans_presenter.getMax_cost();
	}

	public double getMin_cost()
	{
		return trans_presenter.getMin_cost();
	}

	public double getMax_volume()
	{
		return trans_presenter.getMax_volume();
	}

	public double getMax_capacity()
	{
		return trans_presenter.getMax_capacity();
	}
	
	
	 
	    
}




	

