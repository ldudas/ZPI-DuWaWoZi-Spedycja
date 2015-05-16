package interfaces;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JTabbedPane;

import builders.CityBuilder;
import builders.OrderBuilder;
import dataModels.City;
import dataModels.Manufacturer;
import dataModels.Order;
import visualisations.*;
import visualisations.Manufacturers.VisualistaionManufacturersPresenter;
import visualisations.Path.VisualisationPathPresenter;


public class RoutePlanningPresenter 
{
	private RoutePlanningModel route_planning_model;
	private RoutePlanningView route_planning_view;
	private VisualistaionManufacturersPresenter manu_presenter;
	private VisualisationPathPresenter path_presenter;
	
	public RoutePlanningPresenter(final RoutePlanningView view,final RoutePlanningModel model,final VisualistaionManufacturersPresenter map, VisualisationPathPresenter path_p)
	{
		route_planning_model = model;
		route_planning_view = view;
		manu_presenter = map;
		path_presenter = path_p;
	}
	
	
	/**
	 * Zmienia widok ze starowego okna na okno z wizualizacja producentow 
	 * oraz wizulaizacja trasy 
	 */
	public void changeStart_to_manufacturerVisualization()
	{
		startManuVisualisation();
		route_planning_view.change_start_to_manufacturerVisualization();
	}
	
	/**
	 * Metoda zmieniajaca widok z informacji o producencie do 
	 * na okno zwiazane z wykonaiem zleceniem wybranego producenta
	 * @author Kamil Zimny
	 */
	public void changeManufacturerInfoFrame_manufacturerOrderData()
	{
		try 
		{
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			route_planning_view.setCalendareDate_StartNewOrder(dateFormat.parse(route_planning_model.getLastOrder().getFinishDate()));
		} 
		catch (ParseException e) {}
		route_planning_view.change_manufactruerDetails_to_manufacturerOrderData();
	}
	
	/**
	 * Metoda zmieniajaca widok z okno zwiazane z wykonywanie(miasto,daty) do 
	 * na zleceniem informacji o wybranym producencie 
	 * @author Kamil Zimny
	 */
	public void changeManufacturerOrderData_manufacturerInfoFrame()
	{
		route_planning_view.change_manufacturerOrderData_to_manufactruerDetails();
	}
	
	/**
	 * W zaleznosci od stanu, wyswietla informacje o wybranym producencie w nowym oknie lub pokazuje informacje
	 * o bledzie zaznaczenia producentow( zaznaczono wiecej niz jednego)
	 * @author Kamil Zimny
	 */
	public void showManufacturerInfo()
	{
		Manufacturer manufacturer = manu_presenter.getAttributeOfSelectedManufacturers();

		if( manufacturer != null )
		{
			route_planning_view.show_manfacturerInfo(manufacturer);
			
		}
		else
			route_planning_view.show_ErrorMessage();			
	}
	
	public void removeLastCity()
	{
		path_presenter.removeLastCity();
	}
	
	public void addCityToPath()
	{
		String cityName = route_planning_view.getNextCityTo();
		CityBuilder cityBuilder = new CityBuilder();
		String [] coordinations = route_planning_model.getCityCoordinates(cityName);
		
		City city = cityBuilder.buildCity(cityName, coordinations[0], coordinations[1]);
		path_presenter.addCityToPath(city);
	}
	
	
	/**
	 * Pokaz wizualizacje trasy     
	 * @author Łukasz Dudaszek
	 */
	public void showPathMap()
	{
		path_presenter.startPathVisualisation();
	}
	
	/**
	 * Zamyka okno informajci o producencie
	 * @author Kamil Zimny
	 */
	public void closeManufacturerInfo()
	{
		route_planning_view.closeManufacturerInfoFrame();
	}
	 
	/**
	 * Uruchamia metodę prezentera wizualizacji producentów 
	 * startującą wizualizację producentów
	 */
	public void startManuVisualisation()
	{
		manu_presenter.startManufacturersVisualisation(route_planning_view.city_to());
	}
	
	public void send_nextCityNameAfterConfirm()
	{
		manu_presenter.startManufacturersVisualisation(route_planning_view.city_nextCityAfterComfirm());
		
	}
	
	public RoutePlanningView return_view()
	{
		return route_planning_view;
	}
	
	
	/**
	 * Utwórz w modelu path model mape z pierwszymi dwoma miastami   
	 * @author Łukasz Dudaszek
	 */
	public void createInitialPathMap()
	{
		//pobierz miasto starowe z widoku
		String city_to = route_planning_view.city_to();
		//pobierz miasto docelowe z widoku
		String city_from = route_planning_view.city_from();
		//Utwórz w modelu path model mape z pierwszymi dwoma miastami  
		path_presenter.createInitialMap(city_from, city_to);
	}
	
	/**
	 * Metoda zaznaczajaca na mapie grafika producenta jako odrzuconego
	 * @author Kamil Zimny
	 */
	public void markAsUnsuitable()
	{
		manu_presenter.markAsUnsuitable();
	}
	
	/**
	 * Metoda ustawiajaca wybranemu producentowi pole dodatkowe inforamcje na 
	 * wartosc podana w parametrze info
	 * @param manufacturer
	 * @param info
	 * @author Kamil Zimny
	 */
	public void setManufacturerAttribut_additionInfo(Manufacturer manufacturer,String info)
	{
		manu_presenter.setManufacturerAttribut_additionInfo(manufacturer, info);
	}
	
	/**
	 * Metoda pokazujaca odpowiedni JPanel z zarzadzaniem okna
	 * 
	 */
	public void tabChanged()
	{
		int index = route_planning_view.getTabSelectedIndex();
		if ( index == 0 )
			setVisibleOfManagementJPanels(true);
		else
			setVisibleOfManagementJPanels(false);			
	}
		

	private void setVisibleOfManagementJPanels(boolean visibilityOfZeroTab)
	{
		route_planning_view.setVisibleOfManagementJPanels(visibilityOfZeroTab);
	}
	
	/**
	 * Dodaje pierwsze zlecenie do kolekcji zleceń na obecnie wyznaczanej trasie
	 * @author Kamil Zimny
	 */
	public void addFirstOrder()
	{
		//pobierz miasto starowe z widoku
		String city_to = route_planning_view.city_to();
		//pobierz miasto docelowe z widoku
		String city_from = route_planning_view.city_from();
		
		String startDate = route_planning_view.getStartDate();
		String finishDate = route_planning_view.getFinishDate();
		
		CityBuilder cityBuilder = new CityBuilder();
		String [] coordinations = route_planning_model.getCityCoordinates(city_to);		
		City cityTo = cityBuilder.buildCity(city_to, coordinations[0], coordinations[1]);
		
		coordinations = route_planning_model.getCityCoordinates(city_from);
		City cityFrom = cityBuilder.buildCity(city_from, coordinations[0], coordinations[1]);
		
		OrderBuilder orderBuilder = new OrderBuilder();
		route_planning_model.addNewOrder(orderBuilder.buildOrder(cityTo, cityFrom, startDate, finishDate));
	}
	
	/**
	 * Dodaje kolejne zlecenie do kolekcji zleceń na obecnie wyznaczanej trasie
	 * @author Kamil Zimny
	 */
	public void addNextOrder()
	{
		//pobierz miasto starowe z widoku
		String city_to = route_planning_view.getNextCityTo();
		
		String startDate = route_planning_view.getNextStartDate();
		String finishDate = route_planning_view.getNextFinishDate();
		
		CityBuilder cityBuilder = new CityBuilder();
		String [] coordinations = route_planning_model.getCityCoordinates(city_to);		
		City cityTo = cityBuilder.buildCity(city_to, coordinations[0], coordinations[1]);
		
		
		OrderBuilder orderBuilder = new OrderBuilder();
		route_planning_model.addNewOrder(orderBuilder.buildOrder(cityTo, route_planning_model.getLastOrder().getCityTo()
														, startDate, finishDate));
	}
	
	/**
	 * Zwraca ostatnie zlecenie z kolekcji zleceń dokonanych podczas pojedyńczej trasy
	 * @return Order 
	 * @author Kamil Zimny
	 */
	public Order getLastOrder()
	{
		return route_planning_model.getLastOrder();
	}
	
	/**
	 * Dodaje kolejne zlecenie do tableli wyswietlajacej podstawowe dane zlecenia
	 * kolejne = ostatnio zaakceptowane zlecenie.
	 * @author Kamil Zimny
	 */
	public void addOrderToTab()
	{
		route_planning_view.addOrderToTab( route_planning_model.getLastOrder() );
	}
	
	/**
	 * Metoda sprawdzajaca poprawnosc wprowadzonych danych 
	 * podczas wybierania kolejnej trasy zlecenia.
	 * @return opis błedu || null jesli wszystko ok
	 * @author Kamil Zimny
	 */
	public String checkCorrectnessOfData_nextOrders()
	{
		if( route_planning_view.getNextCityTo() == null || route_planning_view.getNextCityTo().equals("") )
			return "Nie wybrano miasta docelowego...";
		if( route_planning_view.getNextCityTo().equals(route_planning_model.getLastOrder().getCityTo().getCityName()) )
			return "Miasto docelowe jest miastem w którym się obecnie znajdujemy...";
		if( route_planning_view.getNextStartDate() == null || route_planning_view.getNextStartDate().equals("") ) 
			return "Nie wybrano daty wyjazdu...";
		if( route_planning_view.getNextFinishDate() == null || route_planning_view.getNextFinishDate().equals("") ) 
			return "Nie wybrano daty przyjazdu...";
		
		SimpleDateFormat sDateFormat; 
		try 
		{
			sDateFormat  = new SimpleDateFormat("yyyy-MM-dd");
			
			if( sDateFormat.parse(route_planning_view.getNextStartDate()).after
				(sDateFormat.parse(route_planning_view.getNextFinishDate())) ) 
				return "Data przyjazdu jest wcześniejsza niż data wyjazdu...";
			if( sDateFormat.parse(route_planning_view.getNextStartDate()).before( 
					sDateFormat.parse( route_planning_model.getLastOrder().getFinishDate())) )
				return "Data wyjazdu jest wcześniejsza niż przewidywana data dotarcia...";		
		} 
		catch (ParseException e) 
		{
			return "Problem z datami...";
		}
		
		return null;
	}
	
	/**
	 * Zwraca zakladke z mapa producentow
	 * @return JTabbenPane z mapa producentow
	 * @author Kamil Zimny
	 */
	public JTabbedPane getTabWithMaps()
	{
		return route_planning_view.getTabWithMaps();
	}
	
	/**
	 * Sprawdzanie porpawnosci wprowadzanych danych w oknie startowym
	 * @return Opis bledu || null jesli wszystko ok.
	 * @author Kamil Zimny
	 */
	public String checkCorrectnessOfData_firstOrder()
	{
		if( route_planning_view.city_from() == null || route_planning_view.city_from().equals("") ) 
			return "Nie wybrano miasta startowego...";
		if( route_planning_view.city_to() == null || route_planning_view.city_to().equals("") ) 
			return "Nie wybrano miasta docelowego...";
		if( route_planning_view.city_from().equals(route_planning_view.city_to()) )
			return "Miasto startowe jest takie samo jak miasto docelowe...";
		if( route_planning_view.getStartDate() == null || route_planning_view.getStartDate().equals("") ) 
			return "Nie wybrano daty wyjazdu...";
		if( route_planning_view.getFinishDate() == null || route_planning_view.getFinishDate().equals("") ) 
			return "Nie wybrano daty przyjazdu...";
		SimpleDateFormat sDateFormat; 
		try 
		{
			sDateFormat  = new SimpleDateFormat("yyyy-MM-dd");
			
			if( sDateFormat.parse(route_planning_view.getStartDate()).after
				(sDateFormat.parse(route_planning_view.getFinishDate()))  ) 
				return "Data przyjazdu jest wcześniejsza niż data wyjazdu...";
			if( sDateFormat.parse(route_planning_view.getStartDate()).before
					( sDateFormat.parse(sDateFormat.format(new Date())) )  )
				return "Data wyjazdu jest wcześniejsza niż obecna data...";
			
		} 
		catch (ParseException e) 
		{
			return "Problem z datami...";
		}
		
		return null;
	}
	
	/**
	 * Metoda dodajaca wszystkie miasta do podanych w parametrze comoboBox'ow
	 * @param comboBox
	 * @param comboBoxSecond
	 * @author Kamil Zimny
	 */
	public void addAllCityToList(JComboBox<String> comboBox,JComboBox<String> comboBoxSecond)
	{
		for ( String cityName : route_planning_model.getAllCityNames() )
		{
			if(comboBox != null)
				comboBox.addItem(cityName);
			if(comboBoxSecond != null)
				comboBoxSecond.addItem(cityName);
		}
	}
	
	
	

	
}
