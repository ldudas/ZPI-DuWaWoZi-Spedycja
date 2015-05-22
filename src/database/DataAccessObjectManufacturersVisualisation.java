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
	 *  <br>res.get(0).get(8) -> identyfikator producenta
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
		
		String query = "SELECT nazwa_prod, P.dlugosc, P.szerokosc, MAX(Z.data_zak_plan), "
				+ "COUNT(*) , SUM(Z.wartosc_zlec) , SUM(DATEDIFF(Z.data_zak_plan,Z.data_rozp_plan)), P.telefon, P.id_prod "
				+ "FROM Zlecenia Z JOIN Producenci P ON Z.id_prod = P.id_prod "
				+ "JOIN Miasta M ON Z.z_miasta = M.id_miasta "
				+ "WHERE M.nazwa_miasta = '"+ cityName +"' " + additionQueryOfSinceTime + additionQueryOfTimeAgo
				+ "GROUP BY P.id_prod;";
		
		ArrayList<ArrayList<Object>> resultOfQuery = null;
		ArrayList<ArrayList<String>> resultInString = null;
		int coutOfResultColumns = 9;
		try 
		{
			resultOfQuery =  databaseConnector.getResultOfMySqlQuery(query,coutOfResultColumns);
			resultInString = new ArrayList<ArrayList<String>>();
		} 
		catch (DatabaseConnectionExeption e) 
		{
			e.printStackTrace();
		}
		finally
		{
			if( resultOfQuery != null && resultInString != null && resultOfQuery.size() > 0)
			{
				for(int i=0;i<resultOfQuery.size();i++)
				{
					resultInString.add(new ArrayList<String>());
					for(int j=0;j<coutOfResultColumns;j++)
					{
						resultInString.get(i).add( resultOfQuery.get(i).get(j).toString()  );
					}
						
				}
			}
			return resultInString;	
		}

	}
	
	/**
	 * Metoda zwracajaca tablice wspolrzednych geograficznych miasta o nazwie
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
