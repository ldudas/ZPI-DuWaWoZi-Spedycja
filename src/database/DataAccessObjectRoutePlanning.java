package database;

import java.util.ArrayList;
import java.util.stream.Collectors;

import dataModels.Order;
import exceptions.DatabaseConnectionExeption;

public class DataAccessObjectRoutePlanning 
{

	private DatabaseConnector databaseConnector;
	
	public DataAccessObjectRoutePlanning()
	{
	}
	
	/**
	 * Ustawienie danych do połączenia z zewnętrzną bazą danych aktualnego użytkownika.
	 * @param serverAddress adres serwera zewnętrzengo
	 * @param serverPort port serwera zewnętrznego
	 * @param databaseName nazwa bazy danych na serwerze
	 * @param databaseLogin login do bazy danych na serwerze
	 * @param databasePassword hasło do bazy danych na serwerze
	 * @author Kamil Zimny
	 */
	public void setExternalDatabaseConnectionProperty(String serverAddress,String serverPort,String databaseName,String databaseLogin,String databasePassword)
	{
		databaseConnector = new DatabaseConnector(serverAddress, serverPort, databaseName, databaseLogin, databasePassword);
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
		final String query = "SELECT dlugosc,szerokosc FROM Miasta WHERE nazwa_miasta = '"+ cityName +"';";
		
		if(cityName.isEmpty())
			return null;
		
		String [] coordinates = null;
		
		ArrayList<ArrayList<Object>> resultOfQuery = null;
		try 
		{
			resultOfQuery = databaseConnector.getResultOfMySqlQuery(query);
			coordinates = new String[2];
		} 
		catch (DatabaseConnectionExeption e) 
		{
			e.printStackTrace();
		}
		
		if( resultOfQuery != null && coordinates != null && resultOfQuery.size() > 0)
		{
			coordinates[0] = (String)resultOfQuery.get(0).get(0);
			coordinates[1] = (String)resultOfQuery.get(0).get(1);
		}
		
		return coordinates;
	}
	
	/**
	 * Zwraca kolekcje wszystkich nazw miast z zewnętrznej bazy danych.
	 * @return ArrayList<String>
	 * @author Kamil Zimny
	 */
	public ArrayList<String> getAllCityNames()
	{
		final String query = "SELECT nazwa_miasta FROM Miasta ORDER BY nazwa_miasta;";
		
		ArrayList<ArrayList<Object>> resultOfQuery = null;
		try 
		{
			resultOfQuery = databaseConnector.getResultOfMySqlQuery(query);
		} 
		catch (DatabaseConnectionExeption e) 
		{
			e.printStackTrace();
		}
		
		if( resultOfQuery != null && resultOfQuery.size() > 0)
		{
			return resultOfQuery.stream()
					.map(c -> (String) c.get(0) )
					.collect(Collectors.toCollection(ArrayList::new));
		}
		
		return new ArrayList<String>();
		
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
	public void saveOrdersToDatabase(String route_name, ArrayList<Order> ordersData,String idTrans) throws DatabaseConnectionExeption,Exception,RuntimeException
	{
		ordersData.stream().forEach( order -> 
		{ 
			order.setTransporterID(idTrans);
			
			if( order.getIdManufacturer() != null && !order.getIdManufacturer().equals("") )
			{
				String queryCityId = "SELECT id_miasta FROM Miasta WHERE nazwa_miasta = '" + order.getCityFrom().getCityName() + "';";				
				ArrayList<ArrayList<Object>> resultOfQuery = null;

				try {
					resultOfQuery = databaseConnector.getResultOfMySqlQuery(queryCityId);
				} catch (Exception e1) 
				{
					throw new RuntimeException(e1);
				}

				
				String cityFromID = resultOfQuery.get(0).get(0).toString();
				
				queryCityId = "SELECT id_miasta FROM Miasta WHERE nazwa_miasta = '" + order.getCityTo().getCityName() + "';";			
				resultOfQuery = null;

				try {
					resultOfQuery = databaseConnector.getResultOfMySqlQuery(queryCityId);
				} catch (Exception e1) {
					throw new RuntimeException(e1);
				}
	
				String cityToID = resultOfQuery.get(0).get(0).toString();
				
				String insertRoute_name = "INSERT INTO Trasy_przewozinikow (nazwa_trasy) VALUES('" + route_name + "');";
				
				try 
				{
					databaseConnector.saveDataToDatabase(insertRoute_name);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				
				String getRoute_id = "SELECT id_trasy FROM Trasy_przewozinikow WHERE nazwa_trasy='" + route_name + "';";
				resultOfQuery = null;

				try {
					resultOfQuery = databaseConnector.getResultOfMySqlQuery(getRoute_id) ;
				} catch (Exception e1) {
					throw new RuntimeException(e1);
				}
				
				String routeID = resultOfQuery.get(0).get(0).toString();
				
				String query = "INSERT INTO Zlecenia (data_rozp_plan, data_zak_plan, id_prod, z_miasta, do_miasta, id_przew, id_trasy) "
						+ "VALUES('"+ order.getStartDate() +"','" +  order.getFinishDate() +"','" + order.getIdManufacturer() +"','"
						+  cityFromID +"','"  + cityToID +"','"+ order.getIdTransporter() + "','"+ routeID +"');";	

				try 
				{
					databaseConnector.saveDataToDatabase(query);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		} );
	}
	
	/**
	 * Zwraca liczbe wystąpień w bazie danych nazwy trasy podanej 
	 * w parametrze metody.
	 * @param route_name nazwa trasy.
	 * @return int liczba tras o nazwie podanej w parametrze
	 * @author Łukasz Dudaszek
	 */
	public int getNumberOfRoutesWithName(String route_name)
	{
		final String query = "SELECT count(id_trasy) FROM Trasy_przewoznikow WHERE nazwa_trasy= '"+route_name+"';";
		
		ArrayList<ArrayList<Object>> resultOfQuery = null;
		try 
		{
			resultOfQuery = databaseConnector.getResultOfMySqlQuery(query);
		} 
		catch (DatabaseConnectionExeption e) 
		{
			e.printStackTrace();
		}
		
		if( resultOfQuery != null)
		{
			return (int)((long)resultOfQuery.get(0).get(0));
		}
		
		return 1;
	}
}
