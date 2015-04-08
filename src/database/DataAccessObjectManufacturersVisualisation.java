/**
 * @author[Kamil Zimny]
 */
package database;

import java.util.ArrayList;

import exceptions.DatabaseConnectionExeption;

public class DataAccessObjectManufacturersVisualisation 
{
	private DatabaseConnector databaseConnector;
	
	public DataAccessObjectManufacturersVisualisation()
	{
		databaseConnector = new DatabaseConnector();
	}

	/**
	 * Metoda pobierajaca dane o producencie z bazy danych.
	 * @return NULL OR ArrayList<ArrayList<String>> res : 
	 *  <br>res.get(0) -> producent pierwszy 
	 * 	<br>res.get(0).get(0) -> nazwa
	 *  <br>res.get(0).get(1) -> dlugosc geograficzna
	 *  <br>res.get(0).get(2) -> szerokosc geograficzna
	 *  <br>res.get(0).get(3) -> ostatnia aktywnosc  
	 *  <br>res.get(0).get(4) -> liczba zlecen
	 *  <br>res.get(0).get(5) -> suma wartosci zlecen
	 *  <br>res.get(0).get(6) -> suma dni wykonanych zlecen
	 *  <br>res.get(0).get(7) -> telefon
	 * @author Kamil Zimny
	 */
	public ArrayList<ArrayList<String>> getDataAboutManufacturerToVizualization(final String cityName)
	{
		String query = "SELECT nazwa_prod, P.dlugosc, P.szerokosc, MAX(Z.data_zak_plan), "
				+ "COUNT(*) , SUM(Z.wartosc_zlec) , SUM(DATEDIFF(Z.data_zak_plan,Z.data_rozp_plan)),P.telefon "
				+ "FROM Zlecenia Z JOIN Producenci P ON Z.id_prod = P.id_prod "
				+ "JOIN Miasta M ON Z.z_miasta = M.id_miasta "
				+ "WHERE M.nazwa_miasta = '"+ cityName +"' "
				+ "GROUP BY P.id_prod;";
		
		ArrayList<ArrayList<Object>> resultOfQuery = null;
		ArrayList<ArrayList<String>> resultInString = null;
		try 
		{
			resultOfQuery =  databaseConnector.getResultOfMySqlQuery(query,8);
			resultInString = new ArrayList<ArrayList<String>>();
		} 
		catch (DatabaseConnectionExeption e) 
		{
			e.printStackTrace();
		}
		
		if( resultOfQuery != null && resultInString != null && resultOfQuery.size() > 0)
		{
			for(int i=0;i<resultOfQuery.size();i++)
			{
				resultInString.add(new ArrayList<String>());
				for(int j=0;j<8;j++)
				{
					resultInString.get(i).add( resultOfQuery.get(i).get(j).toString()  );
				}
					
			}
		}
		return resultInString;	
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
		String query = "SELECT dlugosc,szerokosc FROM Miasta WHERE nazwa_miasta = '"+ cityName +"';";
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
