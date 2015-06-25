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
	}
	
	
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
	 * Metoda pobierająca dane o producencie z bazy danych.
	 * @return NULL OR ArrayList<ArrayList<String>> res : 
	 *  <br>res.get(0) -> producent pierwszy 
	 * 	<br>res.get(0).get(0) - nazwa
	 *  <br>res.get(0).get(1) - dlugosc geograficzna
	 *  <br>res.get(0).get(2) - szerokosc geograficzna
	 *  <br>res.get(0).get(3) - ostatnia aktywnosc  
	 *  <br>res.get(0).get(4) - liczba zlecen
	 *  <br>res.get(0).get(5) - suma wartosci zlecen
	 *  <br>res.get(0).get(6) - suma dni wykonanych zlecen
	 *  <br>res.get(0).get(7) - telefon
	 *  <br>res.get(0).get(8) - identyfikator producenta
	 * @author Kamil Zimny
	 */
	@SuppressWarnings("finally")
	public ArrayList<ArrayList<String>> getDataAboutManufacturerToVizualization(final String cityName, final Integer intervalValue,
			final String intervalType, final Integer intervalValue2, final String intervalType2)
	{
		if(cityName.isEmpty())
			return null;
		
		String additionQueryOfSinceTime = "";
		String additionQueryOfTimeAgo = "";
		
		if ( (intervalValue != null && intervalType != null) && (intervalValue2 == null || intervalType2 == null) )	
			additionQueryOfSinceTime = "AND Z.data_rozp_plan > date_sub(sysdate(),INTERVAL " + intervalValue + " " + intervalType + ")";
		
		if ( (intervalValue != null && intervalType != null) && (intervalValue2 != null && intervalType2 != null) )	
			additionQueryOfTimeAgo = "AND Z.data_rozp_plan < date_add( date_sub(sysdate(),INTERVAL "+ intervalValue + " " + intervalType + ")"+
										" , INTERVAL "+ intervalValue2 + " " + intervalType2 + ") "+
										"AND Z.data_rozp_plan > date_sub( date_sub(sysdate(),INTERVAL " + intervalValue + " " + intervalType + ")"+
										" , INTERVAL "+ intervalValue2 + " " + intervalType2 + ")";
		
		final String query = "SELECT nazwa_prod, P.dlugosc, P.szerokosc, MAX(Z.data_rozp_plan), "
				+ "COUNT(*) , SUM(Z.wartosc_zlec) , SUM(DATEDIFF(Z.data_zak_plan,Z.data_rozp_plan)), P.telefon, P.id_prod "
				+ "FROM Zlecenia Z JOIN Producenci P ON Z.id_prod = P.id_prod "
				+ "JOIN Miasta M ON Z.z_miasta = M.id_miasta "
				+ "WHERE M.nazwa_miasta = '"+ cityName +"' " + additionQueryOfSinceTime + additionQueryOfTimeAgo
				+ "GROUP BY P.id_prod;";
		
		ArrayList<ArrayList<Object>> resultOfQuery = null;
		try 
		{
			resultOfQuery =  databaseConnector.getResultOfMySqlQuery(query);
		} 
		catch (DatabaseConnectionExeption e) 
		{
			e.printStackTrace();
		}
		finally
		{
			return convertResultToString(resultOfQuery);	
		}
	}
	
	/**
	 * Metoda zwracająca aktywności producentów w każdym z miesiąców w danym mieście.
	 * @param cityName
	 * @return NULL OR ArrayList<ArrayList<String>> res : 
	 *  <br>res.get(0) -> producent pierwszy 
	 * 	<br>res.get(0).get(0) -> identyfikator producenta
	 *  <br>res.get(0).get(1) -> liczba aktywnosci w styczniu
	 *  <br>res.get(0).get(2) -> liczba aktywnosci w lutym
	 *  <br>[...]
	 *  <br>res.get(0).get(12) -> liczba aktywnosci w grudniu
	 * @author Kamil Zimny
	 */
	@SuppressWarnings("finally")
	public ArrayList<ArrayList<String>> getManufacturersActivityInEachMonth(final String cityName)
	{
		final String query = "SELECT "+
									  "Z.id_prod, " +
									  "sum(if(month(Z.data_rozp_plan) = 1, 1, 0)) ," +
									  "sum(if(month(Z.data_rozp_plan) = 2, 1, 0)) ," +
									  "sum(if(month(Z.data_rozp_plan) = 3, 1, 0)) ," +
									  "sum(if(month(Z.data_rozp_plan) = 4, 1, 0)) ," +
									  "sum(if(month(Z.data_rozp_plan) = 5, 1, 0)) ," +
									  "sum(if(month(Z.data_rozp_plan) = 6, 1, 0)) ," +
									  "sum(if(month(Z.data_rozp_plan) = 7, 1, 0)) ," +
									  "sum(if(month(Z.data_rozp_plan) = 8, 1, 0)) ," +
									  "sum(if(month(Z.data_rozp_plan) = 9, 1, 0))  ," +
									  "sum(if(month(Z.data_rozp_plan) = 10, 1, 0)) ," +
									  "sum(if(month(Z.data_rozp_plan) = 11, 1, 0)) ," +
									  "sum(if(month(Z.data_rozp_plan) = 12, 1, 0)) " +
									"FROM Zlecenia Z JOIN Miasta M ON Z.z_miasta = M.id_miasta " +
									"WHERE M.nazwa_miasta = '"+ cityName +"' " +
									"GROUP BY Z.id_prod;";
		
		ArrayList<ArrayList<Object>> resultOfQuery = null;
		try 
		{
			resultOfQuery =  databaseConnector.getResultOfMySqlQuery(query);
		} 
		catch (DatabaseConnectionExeption e) 
		{
			e.printStackTrace();
		}
		finally
		{
			return convertResultToString(resultOfQuery);	
		}
	}
	
	/***
	 * Metoda zwracajaca aktywnosci producentów w każdym z miesiąców w danym mieście.
	 * @param cityName
	 * @return NULL OR ArrayList<ArrayList<String>> res : 
	 *  <br>res.get(0) -> producent pierwszy 
	 * 	<br>res.get(0).get(0) -> identyfikator producenta
	 *  <br>res.get(0).get(1) -> dzienny zarobek w styczniu
	 *  <br>res.get(0).get(2) -> dzienny zarobek  w lutym
	 *  <br>[...]
	 *  <br>res.get(0).get(12) -> dzienny zarobek w grudniu
	 * @author Kamil Zimny
	 */
	@SuppressWarnings("finally")
	public ArrayList<ArrayList<String>> getManufacturersCostInEachMonth(final String cityName)
	{
		final String query = "SELECT "+
									  "Z.id_prod, " +
									  "sum(if(month(Z.data_rozp_plan) = 1, Z.wartosc_zlec / DATEDIFF(Z.data_zak_plan,Z.data_rozp_plan), 0)) ," +
									  "sum(if(month(Z.data_rozp_plan) = 2, Z.wartosc_zlec / DATEDIFF(Z.data_zak_plan,Z.data_rozp_plan), 0)) ," +
									  "sum(if(month(Z.data_rozp_plan) = 3, Z.wartosc_zlec / DATEDIFF(Z.data_zak_plan,Z.data_rozp_plan), 0)) ," +
									  "sum(if(month(Z.data_rozp_plan) = 4, Z.wartosc_zlec / DATEDIFF(Z.data_zak_plan,Z.data_rozp_plan), 0)) ," +
									  "sum(if(month(Z.data_rozp_plan) = 5, Z.wartosc_zlec / DATEDIFF(Z.data_zak_plan,Z.data_rozp_plan), 0)) ," +
									  "sum(if(month(Z.data_rozp_plan) = 6, Z.wartosc_zlec / DATEDIFF(Z.data_zak_plan,Z.data_rozp_plan), 0)) ," +
									  "sum(if(month(Z.data_rozp_plan) = 7, Z.wartosc_zlec / DATEDIFF(Z.data_zak_plan,Z.data_rozp_plan), 0)) ," +
									  "sum(if(month(Z.data_rozp_plan) = 8, Z.wartosc_zlec / DATEDIFF(Z.data_zak_plan,Z.data_rozp_plan), 0)) ," +
									  "sum(if(month(Z.data_rozp_plan) = 9, Z.wartosc_zlec / DATEDIFF(Z.data_zak_plan,Z.data_rozp_plan), 0))  ," +
									  "sum(if(month(Z.data_rozp_plan) = 10, Z.wartosc_zlec / DATEDIFF(Z.data_zak_plan,Z.data_rozp_plan), 0)) ," +
									  "sum(if(month(Z.data_rozp_plan) = 11, Z.wartosc_zlec / DATEDIFF(Z.data_zak_plan,Z.data_rozp_plan), 0)) ," +
									  "sum(if(month(Z.data_rozp_plan) = 12, Z.wartosc_zlec / DATEDIFF(Z.data_zak_plan,Z.data_rozp_plan), 0)) " +
									"FROM Zlecenia Z JOIN Miasta M ON Z.z_miasta = M.id_miasta " +
									"WHERE M.nazwa_miasta = '"+ cityName +"' " +
									"GROUP BY Z.id_prod;";
		
		ArrayList<ArrayList<Object>> resultOfQuery = null;
		try 
		{
			resultOfQuery =  databaseConnector.getResultOfMySqlQuery(query);
			
		} 
		catch (DatabaseConnectionExeption e) 
		{
			e.printStackTrace();
		}
		finally
		{
			return convertResultToString(resultOfQuery);	
		}
	}
	
	/**
	 * Metoda konwertująca otrzymaną w parametrze kolekcje ArrayList<ArrayList<Object>> 
	 * na kolekcję ArrayList<ArrayList<String>>
	 * @param resultOfQuery 
	 * @return ArrayList<ArrayList<String>>
	 * @author Kamil Zimny
	 */
	private ArrayList<ArrayList<String>> convertResultToString(ArrayList<ArrayList<Object>> resultOfQuery)
	{		
		ArrayList<ArrayList<String>> resultInString = new ArrayList<ArrayList<String>>();
		
		if( resultOfQuery != null && resultInString != null && resultOfQuery.size() > 0)
		{
			for(int i=0;i<resultOfQuery.size();i++)
			{
				resultInString.add(new ArrayList<String>());
				for(int j=0;j<resultOfQuery.get(i).size();j++)
				{
					resultInString.get(i).add( resultOfQuery.get(i).get(j).toString()  );
				}
					
			}
		}
		return resultInString;	
	}
	
	/**
	 * Metoda zwracająca tablicę współrzędnych geograficznych miasta o nazwie
	 * podanej w parametrze.
	 * @return NULL OR String [] tab :
	 * <br>tab[0] -> dlugosc geograficzna 
	 * <br>tab[1] -> szerokosc geograficzna
	 * @author Kamil Zimny
	 */
	@SuppressWarnings("finally")
	public String [] getCityCoordinates(final String cityName)
	{
		if(cityName.isEmpty())
			return null;
		
		final String query = "SELECT dlugosc,szerokosc FROM Miasta WHERE nazwa_miasta = '"+ cityName +"';";
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
		finally
		{
			if( resultOfQuery != null && coordinates != null && resultOfQuery.size() > 0)
			{
				coordinates[0] = (String)resultOfQuery.get(0).get(0);
				coordinates[1] = (String)resultOfQuery.get(0).get(1);
			}
			
			return coordinates;
		}
	}

}
