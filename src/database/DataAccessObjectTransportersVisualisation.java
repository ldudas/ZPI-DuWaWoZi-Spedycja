package database;

import java.util.ArrayList;
import java.util.stream.Collectors;

import dataModels.Transporter;
import exceptions.DatabaseConnectionExeption;

public class DataAccessObjectTransportersVisualisation 
{

	private DatabaseConnector databaseConnector;
	
	
	/***
	 * Metoda zwracajaca liczbę przejazdów między miastami przewoznika o podanym id
	 * @param id_trans
	 * @return NULL OR ArrayList<ArrayList<String>> res : 
	 *  <br>res.get(0) -> pierwsza para miast
	 * 	<br>res.get(0).get(0) -> miasto poczatkowe
	 *  <br>res.get(0).get(1) -> miasto koncowe
	 *  <br>res.get(0).get(2) -> liczba zlecen pomiedzy miastami
	 * @author Łukasz Dudaszek
	 */
	public ArrayList<ArrayList<Object>> getTransporterRoutes(final int id_trans)
	{
		final String query = "SELECT m1.nazwa_miasta z_m, m2.nazwa_miasta do_m ,count(*) FROM Zlecenia z join Miasta m1 on z.z_miasta = m1.id_miasta join Miasta m2 on z.do_miasta = m2.id_miasta where id_przew="
							+ id_trans + " group by z_m, do_m";
		
		ArrayList<ArrayList<Object>> resultOfQuery = null;
		
		try 
		{
			resultOfQuery =  databaseConnector.getResultOfMySqlQuery(query);
		} 
		catch (DatabaseConnectionExeption e) 
		{
			e.printStackTrace();
		}
		
		return resultOfQuery;
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

	public void setExternalDatabaseConnectionProperty(String serverAddress,String serverPort,String databaseName,String databaseLogin,String databasePassword)
	{
		databaseConnector = new DatabaseConnector(serverAddress, serverPort, databaseName, databaseLogin, databasePassword);
	}
	
	/**
	 * @return kolekcja wszystkich nazw miast w bazie danych
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
}
