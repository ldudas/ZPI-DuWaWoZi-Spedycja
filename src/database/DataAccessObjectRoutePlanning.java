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
	
	public void setExternalDatabaseConnectionProperty(String serverAddress,String serverPort,String databaseName,String databaseLogin,String databasePassword)
	{
		databaseConnector = new DatabaseConnector(serverAddress, serverPort, databaseName, databaseLogin, databasePassword);
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
	
	public void saveOrdersToDatabase(String route_name, ArrayList<Order> ordersData,String idTrans) throws DatabaseConnectionExeption,Exception,RuntimeException
	{
		ordersData.stream().forEach( order -> 
		{ 
			order.setTransporterID(idTrans);
			
			if( order.getIdManufacturer() != null && !order.getIdManufacturer().equals("") )
			{
				String queryCityId = "SELECT id_miasta FROM Miasta WHERE nazwa_miasta = '" + order.getCityFrom().getCityName() + "';";
				
				ArrayList<ArrayList<Object>> resultOfQuery = null;

				try 
				{
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
								
				String query = "INSERT INTO Zlecenia (data_rozp_plan, data_zak_plan, id_prod, z_miasta, do_miasta, id_przew) "
						+ "VALUES('"+ order.getStartDate() +"','" +  order.getFinishDate() +"','" + order.getIdManufacturer() +"','"
						+  cityFromID +"','"  + cityToID +"','"+ order.getIdTransporter() + "');";	

				try 
				{
					databaseConnector.saveDataToDatabase(query);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		} );
	}
}
