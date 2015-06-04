package database;

import java.util.ArrayList;

import dataModels.Transporter;
import exceptions.DatabaseConnectionExeption;

public class DataAccessObjectTransportersVisualisation 
{
	
	private DatabaseConnector databaseConnector;
	
	public DataAccessObjectTransportersVisualisation()
	{
		databaseConnector = new DatabaseConnector();
	}
	
	public ArrayList<Transporter> getTranspoters(String city_from, String city_to)
	{
		return null;
	}
	
	
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
	@SuppressWarnings("finally")
	public ArrayList<ArrayList<Object>> getTransporterRoutes(final int id_trans)
	{
		final String query = "SELECT m1.nazwa_miasta z_m, m2.nazwa_miasta do_m ,count(*) FROM Zlecenia z join Miasta m1 on z.z_miasta = m1.id_miasta join Miasta m2 on z.do_miasta = m2.id_miasta where id_przew="
							+ id_trans + " group by z_m, do_m";
		
		ArrayList<ArrayList<Object>> resultOfQuery = null;
		final int coutOfResultColumns = 3;
		
		try 
		{
			resultOfQuery =  databaseConnector.getResultOfMySqlQuery(query,coutOfResultColumns);
		} 
		catch (DatabaseConnectionExeption e) 
		{
			e.printStackTrace();
		}
		
		return resultOfQuery;
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
		
		
		return resultOfQuery;
	}

}
