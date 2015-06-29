package database;
import java.util.ArrayList;

import shared.exceptions.DatabaseConnectionExeption;

/**
 * Data Access Object dla wizualizacji trasy
 * @author Kamil Zimny
 *
 */
public class DataAccessObjectPathVisualisation
{
	
	private DatabaseConnector databaseConnector;
	
	public DataAccessObjectPathVisualisation()
	{}
	
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
	public ArrayList<ArrayList<Object>> getCitiesCoordinates()
	{
		String query = "SELECT nazwa_miasta,dlugosc,szerokosc FROM Miasta";
		ArrayList<ArrayList<Object>> resultOfQuery = null;
		try 
		{
			resultOfQuery = databaseConnector.getResultOfMySqlQuery(query);
		} 
		catch (DatabaseConnectionExeption e) 
		{
			e.printStackTrace();
		}
		
		
		return resultOfQuery;
	}


}
