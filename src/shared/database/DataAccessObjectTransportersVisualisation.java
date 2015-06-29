package shared.database;

import java.util.ArrayList;
import java.util.stream.Collectors;

import shared.dataModels.SizeCategory;
import shared.exceptions.DatabaseConnectionExeption;

/**
 * Data Access Object dla wizualizacji przewoźników
 * @author Łukasz Dudaszek
 *
 */
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
	
	
	public ArrayList<ArrayList<Object>> getTranspoters(String city_from, String city_to, SizeCategory size)
	{	
		
		ArrayList<ArrayList<Object>> resultOfQuery = null;
		String query;
		
		try 
		{
			if(size == SizeCategory.SMALL){
				query = "SELECT Z.id_przew, COUNT(Z.id_przew), AVG(Z.koszt_przew), AVG(Z.ladownosc_poj), AVG(Z.pojemnosc_poj), MAX(P.nazwa_przew), MAX(P.telefon), IFNULL(COUNT(CASE WHEN czy_zrealizowano = 0 THEN 1 ELSE NULL END)/COUNT(CASE WHEN czy_zrealizowano = 1 THEN 1 ELSE NULL END),0),(IFNULL(SUM(CASE WHEN Z.data_rozp_rzecz > Z.data_rozp_plan THEN DATEDIFF(Z.data_rozp_rzecz,Z.data_rozp_plan) ELSE NULL END),0) + IFNULL(SUM(CASE WHEN Z.data_zak_rzecz > Z.data_zak_plan THEN DATEDIFF(Z.data_zak_rzecz,Z.data_zak_plan) ELSE NULL END),0)) /SUM(DATEDIFF(Z.data_zak_plan,Z.data_rozp_plan)), M.nazwa_miasta, N.nazwa_miasta FROM Zlecenia Z JOIN Przewoznicy P ON Z.id_przew = P.id_przew JOIN Miasta M ON Z.z_miasta = M.id_miasta JOIN Miasta N ON Z.do_miasta = N.id_miasta WHERE M.nazwa_miasta = '"
						+city_from+"' AND N.nazwa_miasta = '"+city_to+"' AND Z.ladownosc_poj <= 3.5 AND Z.pojemnosc_poj <= 10 GROUP BY Z.id_przew;";
				resultOfQuery = databaseConnector.getResultOfMySqlQuery(query);
				
			}
			else if (size == SizeCategory.MEDIUM){
				query = "SELECT Z.id_przew, COUNT(Z.id_przew), AVG(Z.koszt_przew), AVG(Z.ladownosc_poj), AVG(Z.pojemnosc_poj), MAX(P.nazwa_przew), MAX(P.telefon), IFNULL(COUNT(CASE WHEN czy_zrealizowano = 0 THEN 1 ELSE NULL END)/COUNT(CASE WHEN czy_zrealizowano = 1 THEN 1 ELSE NULL END),0),(IFNULL(SUM(CASE WHEN Z.data_rozp_rzecz > Z.data_rozp_plan THEN DATEDIFF(Z.data_rozp_rzecz,Z.data_rozp_plan) ELSE NULL END),0) + IFNULL(SUM(CASE WHEN Z.data_zak_rzecz > Z.data_zak_plan THEN DATEDIFF(Z.data_zak_rzecz,Z.data_zak_plan) ELSE NULL END),0)) /SUM(DATEDIFF(Z.data_zak_plan,Z.data_rozp_plan)), M.nazwa_miasta, N.nazwa_miasta FROM Zlecenia Z JOIN Przewoznicy P ON Z.id_przew = P.id_przew JOIN Miasta M ON Z.z_miasta = M.id_miasta JOIN Miasta N ON Z.do_miasta = N.id_miasta WHERE M.nazwa_miasta = '"
						+city_from+"' AND N.nazwa_miasta = '"+city_to+"' AND Z.ladownosc_poj > 3.5 AND Z.ladownosc_poj <=10  AND Z.pojemnosc_poj >10 AND Z.pojemnosc_poj <= 100 GROUP BY Z.id_przew;";
				
				resultOfQuery = databaseConnector.getResultOfMySqlQuery(query);
			}
			else if (size == SizeCategory.BIG){

				query = "SELECT Z.id_przew, COUNT(Z.id_przew), AVG(Z.koszt_przew), AVG(Z.ladownosc_poj), AVG(Z.pojemnosc_poj), MAX(P.nazwa_przew), MAX(P.telefon), IFNULL(COUNT(CASE WHEN czy_zrealizowano = 0 THEN 1 ELSE NULL END)/COUNT(CASE WHEN czy_zrealizowano = 1 THEN 1 ELSE NULL END),0),(IFNULL(SUM(CASE WHEN Z.data_rozp_rzecz > Z.data_rozp_plan THEN DATEDIFF(Z.data_rozp_rzecz,Z.data_rozp_plan) ELSE NULL END),0) + IFNULL(SUM(CASE WHEN Z.data_zak_rzecz > Z.data_zak_plan THEN DATEDIFF(Z.data_zak_rzecz,Z.data_zak_plan) ELSE NULL END),0)) /SUM(DATEDIFF(Z.data_zak_plan,Z.data_rozp_plan)), M.nazwa_miasta, N.nazwa_miasta FROM Zlecenia Z JOIN Przewoznicy P ON Z.id_przew = P.id_przew JOIN Miasta M ON Z.z_miasta = M.id_miasta JOIN Miasta N ON Z.do_miasta = N.id_miasta WHERE M.nazwa_miasta = '"
								+city_from+"' AND N.nazwa_miasta = '"+city_to+"' AND Z.ladownosc_poj > 10 AND Z.pojemnosc_poj >100 GROUP BY Z.id_przew;";
						
				resultOfQuery = databaseConnector.getResultOfMySqlQuery(query);
			}
			
		} 
		catch (DatabaseConnectionExeption e) 
		{
			e.printStackTrace();
		}
		
		
		return resultOfQuery;
	}
}
