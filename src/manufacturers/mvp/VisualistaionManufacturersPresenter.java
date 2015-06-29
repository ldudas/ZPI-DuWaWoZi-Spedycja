package manufacturers.mvp;


import java.awt.Color;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import shared.dataModels.Manufacturer;
import shared.dataModels.User;
import manufacturers.decorators.VisualisationManufactureDecorator;

import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.map.GraphicsLayer;










import interfaces.mvp.RoutePlanningPresenter;

/**
 * Presenter wizualizacji Producentów.
 * @author Kamil Zimny
 *
 */
public class VisualistaionManufacturersPresenter 
{
	
	private VisualisationManufacturersModel model_ManufacturersVis;
	private VisualistaionManufacturersView view_ManufacturersVis;
	private RoutePlanningPresenter route_planning_presenter;
	
	public VisualistaionManufacturersPresenter(final VisualistaionManufacturersView view, final VisualisationManufacturersModel model)
	{
		model_ManufacturersVis = model;
		view_ManufacturersVis = view;
	}
	
	public void set_route_presenter(final RoutePlanningPresenter presenter)
	{
		route_planning_presenter = presenter;
	}
	
	/**
	 * Metoda ustawiajaca widokowi mapę pobraną z modelu.
	 * @param cityName - nazwa miasta
	 * @author Kamil Zimny
	 */
	public void startManufacturersVisualisation(final String cityName)
	{
		view_ManufacturersVis.set_tab(route_planning_presenter.getTabWithMaps());
		view_ManufacturersVis.add_map_to_tab(model_ManufacturersVis.getMapWithVisualisationManufacturersInCity(cityName),cityName);
	}
	
	/**
	 * Usuwa wszystkie dane zapisane w modelu.
	 * @author Kamil Zimny
	 */
	public void clearDataInModel()
	{
		model_ManufacturersVis.clearData();
	}
	
	/**
	 * Metoda zwracająca obiekt Producenta zwierający atrybuty go charakteryzujące.
	 * @return Manufacturer - obiekt producenta
	 * @author Kamil Zimny
	 */
	public Manufacturer getAttributeOfSelectedManufacturers()
	{
		int [] id_ofSelectedManufacturers = null;
		Manufacturer manufacturer = null;

		GraphicsLayer layer = model_ManufacturersVis.getGraphicsLayerWithManufacturers();
		id_ofSelectedManufacturers = layer.getSelectionIDs(); //pobieramy indeksy zaznaczonych obiektow
		
		if( id_ofSelectedManufacturers.length == 1 )//jesli zaznaczony tylko jeden obiekt to pobierz jego atrybuty
		{
			Graphic graphic = layer.getGraphic(id_ofSelectedManufacturers[0]);
			Map<String, Object>  attributes = graphic.getAttributes();		
			String ID = (String) attributes.get("ID");
			manufacturer = model_ManufacturersVis.getManufacturerByID(ID);
		}			
			
		return manufacturer;
	}
	
	/**
	 * Metoda zmieniająca symbol na mapie na czarny krzyż oznaczający, 
	 * że dany producent nie odpowiada naszym wymagania.
	 * @author Kamil Zimny
	 */
	public void markAsUnsuitable()
	{	
		GraphicsLayer layer = model_ManufacturersVis.getGraphicsLayerWithManufacturers();
		int idOfGraphic = layer.getSelectionIDs()[0];
		Graphic graphic =  layer.getGraphic( idOfGraphic );
		SimpleMarkerSymbol symbol = (SimpleMarkerSymbol)  graphic.getSymbol() ;			
		symbol.setColor(Color.BLACK);
		symbol.setAngle(45);
		symbol.setSize(30);
		symbol.setStyle(SimpleMarkerSymbol.Style.CROSS);
		
		layer.updateGraphic(idOfGraphic, symbol);
	}
	
	/**
	 * Metoda ustawiająca wybranemu producentowi pole dodatkowe inforamcje na 
	 * wartość podaną w parametrze info.
	 * @param manufacturer - producent któremu dodajemy notatke
	 * @param info - notatka dodawana do producenta
	 * @author Kamil Zimny
	 */
	public void setManufacturerAttribut_additionInfo(Manufacturer manufacturer,String info)
	{
		model_ManufacturersVis.getManufacturerByID(manufacturer.getID()).setInfoAboutManufacturer(info);
	}
	
	
	/**
	 * Metoda filtrująca producentów na mapie pod względem ich aktywności,
	 * ilość producentów wyświetlanych będzie co najwyżej równa parametrowi int.
	 * @param numberOfMostActive - ilość producentów do wyswietlenia
	 * @author Kamil Zimny
	 */
	public void filterCountOfMostActiveManufacturers(int numberOfMostActive)
	{
		VisualisationManufactureDecorator decorator = new VisualisationManufactureDecorator();
		GraphicsLayer layer = model_ManufacturersVis.getGraphicsLayerWithManufacturers();
		layer.removeAll();
		
		decorator.addManufacturerGraphicOnMap(model_ManufacturersVis.getMap().getSpatialReference(),  layer, 
				model_ManufacturersVis.getNumberOfManufacturerFromBegin(numberOfMostActive));
	}
	
	/**
	 * Metoda zwracająca początkowy stan producnetów na mapie.
	 * Wszyscy producenci z danego miasta.
	 * @author Kamil Zimny
	 */
	public void filterManfacturersShowAll()
	{
		VisualisationManufactureDecorator decorator = new VisualisationManufactureDecorator();
		GraphicsLayer layer = model_ManufacturersVis.getGraphicsLayerWithManufacturers();
		layer.removeAll();
		
		decorator.addManufacturerGraphicOnMap(model_ManufacturersVis.getMap().getSpatialReference(),  layer, 
				model_ManufacturersVis.getAllManufacturerInCurrentCity());
	}
	
	/**
	 * Metoda filtrująca producentów pod względem aktywności po określonej dacie w parametrze.
	 * @param numberOfDays - liczba dni od daty bierzącej
	 * @author Kamil Zimny
	 */
	public void filterManfacturersBySinceDate(int numberOfDays)
	{
		Calendar calendar = new GregorianCalendar();
		calendar.add(Calendar.DAY_OF_YEAR, -numberOfDays);

		VisualisationManufactureDecorator decorator = new VisualisationManufactureDecorator();
			
		GraphicsLayer layer = model_ManufacturersVis.getGraphicsLayerWithManufacturers();
		layer.removeAll();
				
		decorator.addManufacturerGraphicOnMap(model_ManufacturersVis.getMap().getSpatialReference(),  layer, 
						model_ManufacturersVis.getManufacturersWhoseLastActiviteIsBeforeDate(calendar.getTime()));
				
	}
	
	/**
	 * Metoda filtrujaca producentów na mapie, których ostatnia aktywnosc znajduje sie pomiedzy obliczonymi
	 * datami na podstawie parametrow.
	 * @param numberOfDaysAgo - liczba dni do cofnięcia od bierzącej daty
	 * @param numberOfDayTolerance - liczba dni które stanowią przesunięcie daty w obie strony
	 * @author Kamil Zimny
	 */
	public void filterManufacturersBetweenDate(int numberOfDaysAgo, int numberOfDayTolerance)
	{
		Calendar calendar = new GregorianCalendar();
		calendar.add(Calendar.DAY_OF_YEAR, -(numberOfDaysAgo+numberOfDayTolerance));
		Date dateFrom = calendar.getTime();
		calendar.add(Calendar.DAY_OF_YEAR, 2*(numberOfDaysAgo+numberOfDayTolerance) );
		Date dateTo = calendar.getTime();
		
		VisualisationManufactureDecorator decorator = new VisualisationManufactureDecorator();
		GraphicsLayer layer = model_ManufacturersVis.getGraphicsLayerWithManufacturers();
		layer.removeAll();
		
		decorator.addManufacturerGraphicOnMap(model_ManufacturersVis.getMap().getSpatialReference(),  layer, 
				model_ManufacturersVis.getManufacturersWhoseLastActiviteIsBetween(dateFrom, dateTo));
		
	}
	
	/**
	 * W modelu wizualizacji producentów ustawienia połączenia 
	 * do serwera na podstawie danych zalogowanego użytkownika 
	 * @param currentLoggedUser - obecnie zalogowany uzytkownik
	 * @throws Exception
	 */
	public void setExternalDatabaseConnectionProperty(User currentLoggedUser) throws Exception
	{
		model_ManufacturersVis.setExternalDatabaseConnectionProperty(currentLoggedUser);
	}
	

	
	
}
