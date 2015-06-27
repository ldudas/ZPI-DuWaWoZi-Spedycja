package visualisations.Transporters;

import interfaces.RoutePlanningPresenter;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.esri.map.JMap;

import dataModels.SizeCategory;
import dataModels.Transporter;
import dataModels.User;

/**
 * Prezenter wizualizacji przewoźników
 * @author Łukasz Dudaszek
 *
 */
public class VisualisationTransportersPresenter 
{
	
	/**
	 * model wizualizacji przewoznikow
	 */
	private VisualisationTransportersModel model_transporters;
	
	/**
	 * widok wizualizacji przewoznikow
	 */		
	private VisualisationTransportersView view_transporters;
	
	
	/**
	 * prezenter nadrzędny
	 */
	private RoutePlanningPresenter route_planning_presenter;
	
	
	public VisualisationTransportersPresenter(final VisualisationTransportersView view, final VisualisationTransportersModel model)
	{
		model_transporters = model;
		view_transporters = view;
	}
	
	 /**
	  * Rysowanie wizualizacji przewoźników dla podanych parametrów
	  * @param city_from Miasto początkowe
	  * @param city_to Miasto docelowe
	  * @param sc Ktegoria rozmiaru pojazdu
	  * @author Łukasz Dudaszek
	  */
	public void drawTransporters(String city_from, String city_to, SizeCategory sc)
	{
		ArrayList<Transporter> transporters = model_transporters.getFilteredTransporters(city_from,city_to,sc);
		
		
		view_transporters.drawTransporters(transporters);
	}
	
	/**
	 * Pokaż okno ze szczegółowymi danycmi przewoźnika
	 * @param id_trans id przewoźnika
	 */
	public void showTransporterDetails(int id_trans)
	{
		Transporter t = model_transporters.getTransporter(id_trans);
		JMap routes_of_transporter = model_transporters.getTransporterRoutesMap(id_trans) ;
		view_transporters.showTransporterDetailsWindow(t,routes_of_transporter);
	}
	
	/**
	 * Uruchom wizualizację przewoźników
	 * @param mainFrame Okno, w którym będzie uruchomiona
	 */
	public void startTransportersVisualization_inNewFrame(JFrame mainFrame)
	{
		view_transporters.initialize(mainFrame);
	}
	
	/**
	 * Ustaw prezentera
	 * @param presenter prezenter
	 */
	public void set_route_presenter(RoutePlanningPresenter presenter)
	{
		route_planning_presenter = presenter;
	}

	/**
	 * Ustawianie danych użytkownika z bazy loklanej
	 * @param currentLoggedUser zalogowany użytkownik
	 * @throws Exception
	 */
	public void setExternalDatabaseConnectionProperty(User currentLoggedUser) throws Exception
	{
		model_transporters.setExternalDatabaseConnectionProperty(currentLoggedUser);
	}
	
	/**
	 * Ustaw wybranego przewoźnika z wizualizacji
	 * @param t wybrany przewoźnik
	 */
	public void setChosenTransporter(Transporter t)
	{
		model_transporters.setChosenTransporter(t);
	}
	
	/**
	 * Pobierz wybranego przewoźnika
	 * @return wybrany przewoźnik
	 */
	public Transporter getChosenTransporter()
	{
		return model_transporters.getChosenTransporter();
	}
	
	/**
	 * Wyczyść okno główne
	 */
	public void clearTransportersFrame()
	{
		view_transporters.clearCarrierVisualizationFrame();
	}
	
	/**
	 * Wyczyść dane w modelu
	 */
	public void clearDataInModel()
	{
		model_transporters.clearData();
	}
	
	/**
	 * Zapisz ułożoną trasę do bazy danych.
	 * Metoda pobiera od użytkownika nazwę trasy.
	 */
	public void saveOrdersToDatabase()
	{
		int id_trans = model_transporters.getChosenTransporter().getId_trans();
		System.out.println(" id " + id_trans);
		if(id_trans != -1)
		{
			try 
			{
				boolean repeat_insertion = true;
				
				while(repeat_insertion)
				{
					String route_name = view_transporters.showRouteNameDialog();
					
					if(route_name!=null && !route_name.equals(""))
					{
						if(route_planning_presenter.isRouteNameUnique(route_name))
						{
							route_planning_presenter.saveOrdersToDatabase(route_name, String.valueOf(id_trans));
							view_transporters.showSaveSuccessDialog();
							route_planning_presenter.change_to_startPanel();
							repeat_insertion = false;
						}
						else
						{
							view_transporters.showNonUniqueRouteNameDialog();
						}
					}
					else
					{
						repeat_insertion =  view_transporters.showRepeatInsertionDialog();
					}
				}
			} 
			catch (Exception e) 
			{
				view_transporters.showDatabaseSaveError();
				System.out.println(e.getMessage());
			}
			
			
		}
		
	}
	
	/**
	 * Ustaw tryb otwarcia wizualiazcji przewoźników
	 * @param flag flaga otwarcia: -1 - tylko przegladanie przewoznikow, 1 -konczaca sie zapisem trasy do bazy
	 */
	public void setOpeningFlag(int flag)
	{
		model_transporters.setOpeningFlag(flag);
	}
	
	/**
	 * @return flaga otwarcia wizualizacji
	 */
	public int getOpeningFlag()
	{
		return model_transporters.getOpeningFlag();
	}
	
	/**
	 * @return Kolekcja nazw miast z bazy danych
	 */
	public ArrayList<String> getAllCityNames()
	{
		return model_transporters.getAllCityNames();
	}


}
