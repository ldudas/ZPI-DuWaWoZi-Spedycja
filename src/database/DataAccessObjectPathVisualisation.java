package database;
import java.util.ArrayList;

import exceptions.DatabaseConnectionExeption;


public class DataAccessObjectPathVisualisation
{
	
private DatabaseConnector databaseConnector;
	
	public DataAccessObjectPathVisualisation()
	{
		databaseConnector = new DatabaseConnector();
	}
	
	public ArrayList<ArrayList<Object>> getCitiesCoordinates()
	{
		String query = "SELECT nazwa_miasta,dlugosc,szerokosc FROM Miasta";
		ArrayList<ArrayList<Object>> resultOfQuery = null;
		try 
		{
			resultOfQuery = databaseConnector.getResultOfMySqlQuery(query,3);
		} 
		catch (DatabaseConnectionExeption e) 
		{
			e.printStackTrace();
		}
		
		/*if( resultOfQuery != null && resultOfQuery.size() > 0)
		{
			coordinates[0] = (String)resultOfQuery.get(0).get(0);
			coordinates[1] = (String)resultOfQuery.get(0).get(1);
		}*/
		
		return resultOfQuery;
	}


}
