package database;

import java.util.ArrayList;
import java.util.stream.Collectors;

import exceptions.DatabaseConnectionExeption;

public class DataAccessObjectUserAccount 
{
	private LocalDatabaseConnector localDatabaseConnector;
	
	public DataAccessObjectUserAccount()
	{
		localDatabaseConnector = new LocalDatabaseConnector();
	}
	
	
	/**
	 * Zapisuje do lokalnej bazy danych, informacje o koncie u�ytkownika,
	 * oraz dane serwera zewnetrznego zawierajacego baze danych.
	 * @param login
	 * @param password
	 * @param serverA
	 * @param serverP
	 * @param databaseN
	 * @param databaseL
	 * @param databaseP
	 * @author Kamil Zimny
	 */
	public void saveNewAccout(String login,String password,String serverA,String serverP, String databaseN, String databaseL,
			String databaseP)
	{
		String mySqlQuery = "INSERT INTO Accounts(Login, Password, ServerAddress, ServerPort, DatabaseName, "
				+ "DatabaseLogin, DatabasePassword) VALUES ('"+ login +"','"+password+"','"+ serverA +"','"+ serverP +"','" 
				+ databaseN +"','"+ databaseL + "','" + databaseP +"')";
		
		try 
		{
			localDatabaseConnector.executeUpdateMySqlQuery(mySqlQuery);
		} catch (DatabaseConnectionExeption e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Metoda sprawdzajaca czy dany login jest ju� zapisany w bazie
	 * @param login
	 * @return true je�li login jest w bazie || false je�li loginu nie ma
	 * @author Kamil Zimny
	 */
	public boolean isThisLoginAlreadyInDatabase(final String login)
	{	
		String mySqlQuery = "SELECT COUNT(Login) FROM Accounts WHERE Login = '" + login + "'";
		ArrayList<ArrayList<Object>> resultOfQuery = null;
		try 
		{
			resultOfQuery = localDatabaseConnector.getResultOfMySqlQuery(mySqlQuery);
		} catch (DatabaseConnectionExeption e) {

			e.printStackTrace();
		}
		
		if( resultOfQuery != null && resultOfQuery.size() > 0)
			return Integer.parseInt(resultOfQuery.get(0).get(0).toString()) > 0;
		
		return false;
	}
	
	public boolean confirmLoginAndPassword(final String login, final String password)
	{
		String mySqlQuery = "SELECT Password FROM Accounts WHERE Login = '" + login + "'";
		ArrayList<ArrayList<Object>> resultOfQuery = null;
		try 
		{
			resultOfQuery = localDatabaseConnector.getResultOfMySqlQuery(mySqlQuery);
		} catch (DatabaseConnectionExeption e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if( resultOfQuery != null && resultOfQuery.size() > 0)
			return resultOfQuery.get(0).get(0).toString().equals(password);
		
		return false; 
	}
	
	/**
	 * Metoda zwracająca dane użytkownika związane z serwerem zewnętrzynm na 
	 * podstawie podanego w parametrze loginu
	 * @param login
	 * @return ArrayList<String> 
	 * <br> get(0) -> ServerAddress
	 * <br> get(1) -> ServerPort
	 * <br> get(2) -> DatabaseName
	 * <br> get(3) -> DatabaseLogin
	 * <br> get(4) -> DatabasePassword
	 * @author Kamil Zimny
	 */
	public ArrayList<String> getLoggedUserData(final String login)
	{
		String mySqlQuery = "SELECT ServerAddress, ServerPort, DatabaseName, "
				+ "DatabaseLogin, DatabasePassword FROM Accounts WHERE Login = '" + login + "'";
		
		ArrayList<ArrayList<Object>> resultOfQuery = null;
		try 
		{
			resultOfQuery = localDatabaseConnector.getResultOfMySqlQuery(mySqlQuery);
		} catch (DatabaseConnectionExeption e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if( resultOfQuery != null && resultOfQuery.size() > 0)
		{
			return resultOfQuery.get(0).stream()
					.map(c -> (String) c )
					.collect(Collectors.toCollection(ArrayList::new));
		}
		
		return new ArrayList<String>();	
	}
	
}
