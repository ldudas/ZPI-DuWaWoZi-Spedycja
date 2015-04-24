package database;

import java.util.ArrayList;

import exceptions.DatabaseConnectionExeption;

public class DataAccessObjectRoutePlanning 
{

	private DatabaseConnector databaseConnector;
	
	public DataAccessObjectRoutePlanning()
	{
		databaseConnector = new DatabaseConnector();
	}
	/**
	 * Metoda zwracajaca tablice wspolrzednych geograficznych miasta o nazwie
	 * podanej w parametrze.
	 * @return NULL OR String [] tab :
	 * <br>tab[0] -> dlugosc geograficzna 
	 * <br>tab[1] -> szerokosc geograficzna
	 * @author Kamil Zimny
	 */
	public String [] getCityCoordinates(final String cityName)
	{

		final String query = "SELECT dlugosc,szerokosc FROM Miasta WHERE nazwa_miasta = '"+ cityName +"';";
		
		String [] coordinates = null;
		ArrayList<ArrayList<Object>> resultOfQuery = null;
		try 
		{
			resultOfQuery = databaseConnector.getResultOfMySqlQuery(query,2);
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
}
