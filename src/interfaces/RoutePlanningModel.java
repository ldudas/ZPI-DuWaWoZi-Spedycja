package interfaces;

import java.util.ArrayList;

import dataModels.Manufacturer;
import dataModels.Order;
import dataModels.User;
import database.DataAccessObjectRoutePlanning;
import database.DataAccessObjectUserAccount;
import exceptions.DatabaseConnectionExeption;



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
	
	
	public ArrayList<String> getAllCityNames()
	{
		return dao_routePlanning.getAllCityNames();
	}
	
	public void addNewOrder(Order order)
	{
		ordersData.add(order);
	}
	
	public Order getLastOrder()
	{
		return ordersData.get(ordersData.size() - 1);
	}
	
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
	
	public void clearData()
	{
		if( ordersData != null)
			ordersData.clear();
	}
	
	public void setCurrentUser(String login, String password)
	{
		ArrayList<String> userData = dao_userAccount.getLoggedUserData(login);
		currentLoggedUser = new User(login, password, userData.get(0), userData.get(1), userData.get(2), userData.get(3), userData.get(4));
	}
	
	public void setCurrentManufacturer(Manufacturer currentMan)
	{
		currentManufacturer = currentMan;
	}
	
	public Manufacturer getCurrentManufacturer()
	{
		return currentManufacturer;
	}
	
	public User getCurrentUser()
	{
		return currentLoggedUser;
	}
	
	public void saveNewAccout(String login,String password,String serverA,String serverP, String databaseN, String databaseL,
			String databaseP)
	{
		dao_userAccount.saveNewAccout(login, password, serverA, serverP, databaseN, databaseL, databaseP);
	}
	
	public boolean isThisLoginAlreadyInDatabase(final String login)
	{
		return dao_userAccount.isThisLoginAlreadyInDatabase(login);
	}
	
	public boolean confirmLoginAndPassword(final String login,final String password)
	{
		return dao_userAccount.confirmLoginAndPassword(login, password);
	}
	
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
	
	public void saveAllOrdersInDatabase(String idTrans) throws DatabaseConnectionExeption, RuntimeException, Exception
	{
		dao_routePlanning.saveOrdersToDatabase(ordersData,idTrans);
	}
}