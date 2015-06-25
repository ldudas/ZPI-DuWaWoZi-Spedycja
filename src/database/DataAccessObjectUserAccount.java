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
	 * Metoda sprawdzająca czy dany login jest już zapisany w lokalnej bazie danych.
	 * @param login
	 * @return boolean 
	 * <br> true - login jest w bazie 
	 * <br> false - loginu nie ma 
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
	 * @param login - login użytkownika
	 * @return ArrayList<String> 
	 * <br> get(0) - ServerAddress adres serwera
	 * <br> get(1) - ServerPort port serwera
	 * <br> get(2) - DatabaseName nazwa bazy danych
	 * <br> get(3) - DatabaseLogin login do bazy danych
	 * <br> get(4) - DatabasePassword hasło do bazy danych
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
