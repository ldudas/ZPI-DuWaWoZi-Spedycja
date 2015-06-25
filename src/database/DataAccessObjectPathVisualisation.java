package database;
import java.util.ArrayList;

import exceptions.DatabaseConnectionExeption;


public class DataAccessObjectPathVisualisation
{
	
	private DatabaseConnector databaseConnector;
	
	public DataAccessObjectPathVisualisation()
	{}
	
	public void setExternalDatabaseConnectionProperty(String serverAddress,String serverPort,String databaseName,String databaseLogin,String databasePassword)
	{
		databaseConnector = new DatabaseConnector(serverAddress, serverPort, databaseName, databaseLogin, databasePassword);
	}
	
	/**
	 * @return kolekcja danych dotyczących położenia geograficznego miast
	 *  <br>res.get(0) -> miasto pierwsze
	 * 	<br>res.get(0).get(0) -> nazwa
	 *  <br>res.get(0).get(1) -> dlugosc geograficzna
	 *  <br>res.get(0).get(2) -> szerokosc geograficzna
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
