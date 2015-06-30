package interfaces.mvp;

import java.util.ArrayList;

import database.DataAccessObjectRoutePlanning;
import database.DataAccessObjectUserAccount;
import shared.dataModels.Manufacturer;
import shared.dataModels.Order;
import shared.dataModels.User;
import shared.exceptions.DatabaseConnectionExeption;


/**
 * Główny model aplikacji.
 * @author Kamil
 *
 */
public class RoutePlanningModel 
{
	private ArrayList<Order> ordersData;
	private DataAccessObjectRoutePlanning dao_routePlanning;
	private DataAccessObjectUserAccount dao_userAccount;
	
	private User currentLoggedUser;
	private Manufacturer currentManufacturer;
	
	public RoutePlanningModel()
	{
		ordersData = new ArrayList<Order>();
		dao_routePlanning = new DataAccessObjectRoutePlanning();
		dao_userAccount = new DataAccessObjectUserAccount();
	}
	
	/**
	 * Metoda zwracająca wszystkie nazwy miast zapisane w bazie danych 
	 * umieszczone w kolekcji.
	 * @return ArrayList<String>
	 * @author Kamil Zimny 
	 */
	public ArrayList<String> getAllCityNames()
	{
		return dao_routePlanning.getAllCityNames();
	}
	
	/**
	 * Metoda dodająca nowe zlecenie/zamówienie do kolekcji zamównień 
	 * podczas tworzenia nowej trasy.
	 * @param order - zamównie do zapisania w kolekcji
	 * @author Kamil Zimny
	 */
	public void addNewOrder(Order order)
	{
		ordersData.add(order);
	}
	
	/**
	 * Metoda zwracająca ostatnio dodane zamównie z 
	 * kolekcji zamówień.
	 * @return Order
	 * @author Kamil Zimny
	 */
	public Order getLastOrder()
	{
		return ordersData.get(ordersData.size() - 1);
	}
	
	/**
	 * Metoda zwracająca tablicę współrzędnych geograficznych miasta o nazwie
	 * podanej w parametrze.
	 * @return NULL OR String [] tab :
	 * <br>tab[0] - dlugosc geograficzna 
	 * <br>tab[1] - szerokosc geograficzna
	 * @author Kamil Zimny
	 */
	public String [] getCityCoordinates(final String cityName)
	{
		return dao_routePlanning.getCityCoordinates(cityName);
	}
	
	/**
	 * Usuwa ostatnio dodane zlecenie, nie bierze pod uwage pierwszego zlecenia ( nie usuwa go)
	 * @author Kamil Zimny
	 */
	public void removeLastOrder()
	{
		if( ordersData.size() > 0 )
			ordersData.remove(ordersData.size() - 1);
	}
	
	/**
	 * Metoda czyszcząca/usuwająca wszystkie dane zapisane w modelu. 
	 * Wszystkie zapisane zmaówienia.
	 * @author Kamil Zimny
	 */
	public void clearData()
	{
		if( ordersData != null)
			ordersData.clear();
	}
	
	/**
	 * Metoda ustawiająca na podstawie hasła i loginu aktualnego 
	 * użytkownika zalogowanego do bazy.
	 * @param login 
	 * @param password
	 * @author Kamil Zimny
	 */
	public void setCurrentUser(String login, String password)
	{
		ArrayList<String> userData = dao_userAccount.getLoggedUserData(login);
		currentLoggedUser = new User(login, password, userData.get(0), userData.get(1), userData.get(2), userData.get(3), userData.get(4));
	}
	
	/**
	 * Metoda ustawiająca aktualnie wybranego producenta.
	 * @param currentMan
	 * @author Kamil Zimny
	 */
	public void setCurrentManufacturer(Manufacturer currentMan)
	{
		currentManufacturer = currentMan;
	}
	
	/**
	 * Metoda pobierająca aktualnie wybranego producenta.
	 * @return  Manufacturer
	 * @author Kamil Zimny
	 */
	public Manufacturer getCurrentManufacturer()
	{
		return currentManufacturer;
	}
	
	/**
	 * Metoda pobierająca aktualnie zalogowanego użytkownika.
	 * @return User
	 * @author Kamil Zimny
	 */
	public User getCurrentUser()
	{
		return currentLoggedUser;
	}
	
	/**
	 * Zapisuje do lokalnej bazy danych, informacje o koncie użytkownika,
	 * oraz dane serwera zewnetrznego zawierajacego baze danych.
	 * @param login - login użytkownika podawany podczas logowania
	 * @param password - hasło użytkownika podawany podczas logowania
	 * @param serverA - adres serwera zewnętrznego 
	 * @param serverP - port serwera zewnętrznego 
	 * @param databaseN - nazwa bazy danych na zewnętrznym serwerze
	 * @param databaseL - login do bazy danych na zewnętrznym serwerze
	 * @param databaseP - hasło do bazy danych na zewnętrznym serwerze
	 * @author Kamil Zimny
	 */
	public void saveNewAccout(String login,String password,String serverA,String serverP, String databaseN, String databaseL,
			String databaseP)
	{
		dao_userAccount.saveNewAccout(login, password, serverA, serverP, databaseN, databaseL, databaseP);
	}
	
	/**
	 * Metoda sprawdzająca czy dany login jest już zapisany w lokalnej bazie danych.
	 * @param login
	 * @return boolean 
	 * <br> true - login jest w bazie 
	 * <br> false - loginu nie ma 
	 * @author Kamil Zimny
	 */
	public boolean isThisLoginAlreadyInDatabase(final String login)
	{
		return dao_userAccount.isThisLoginAlreadyInDatabase(login);
	}
	
	/**
	 * Metoda sprawdzająca czy podane dane znajdują się i są poprawne w 
	 * porównaniu z danymi umieszczonymi w lokalnej bazie danych.
	 * @param login - login użytkownika 
	 * @param password - hasło użytkownika
	 * @return boolean
	 * <br> true - login i hasło znajdują się w lokalnej bazie i są poprawnę  
	 * <br> false - login i hasło nie znajdują się w lokalnej bazie i są nie poprawnę
	 * @author Kamil Zimny
	 */
	public boolean confirmLoginAndPassword(final String login,final String password)
	{
		return dao_userAccount.confirmLoginAndPassword(login, password);
	}
	
	/**
	 * Ustawienie danych do połączenia z zewnętrzną bazą danych aktualnego użytkownika.
	 * @param serverAddress adres serwera zewnętrzengo
	 * @param serverPort port serwera zewnętrznego
	 * @param databaseName nazwa bazy danych na serwerze
	 * @param databaseLogin login do bazy danych na serwerze
	 * @param databasePassword hasło do bazy danych na serwerze
	 * @throws Exception
	 * @author Kamil Zimny
	 */
	public void setExternalDatabaseConnectionProperty() throws Exception
	{
		if( currentLoggedUser != null )
		{
			dao_routePlanning.setExternalDatabaseConnectionProperty(currentLoggedUser.getServerAddress(), 
				currentLoggedUser.getServerPort(),currentLoggedUser.getDatabaseName(), 
				currentLoggedUser.getDatabaseLogin(), currentLoggedUser.getDatabasePassword());
		}
		else
			throw new Exception("Użytkownik nie został zalogowany."); //nie powinno się zdarzyć.
	}
	
	/**
	 * Zapisywanie danych tworzonej trasy w czasie wykonywania programu.
	 * @param route_name nazwa trasy podana przez użytkownika
	 * @param ordersData dane dotyczące zlecenia
	 * @param idTrans identyfikator przewoźnika
	 * @throws DatabaseConnectionExeption 
	 * @throws Exception 
	 * @throws RuntimeException
	 * @author Kamil Zimny
	 */
	public void saveAllOrdersInDatabase(String route_name, String idTrans) throws DatabaseConnectionExeption, RuntimeException, Exception
	{
		dao_routePlanning.saveOrdersToDatabase(route_name,ordersData,idTrans);
	}
	
	/**
	 * Zwraca liczbe wystąpień w bazie danych nazwy trasy podanej 
	 * w parametrze metody.
	 * @param route_name nazwa trasy.
	 * @return int liczba tras o nazwie podanej w parametrze
	 * @author Łukasz Dudaszek
	 */
	public boolean isRouteNameUnique(String route_name)
	{
		return dao_routePlanning.getNumberOfRoutesWithName(route_name) == 0?true:false;
	}
}