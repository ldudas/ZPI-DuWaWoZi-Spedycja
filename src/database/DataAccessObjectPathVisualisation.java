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
