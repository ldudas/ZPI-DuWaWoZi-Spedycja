/**
 * @author[Kamil Zimny]
 */
package uk.co.placona.helloWorld;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import exceptions.DatabaseConnectionExeption;
 
public class DatabaseConnector 
{
	
	private String database_urlConnector;
	private Connection database_Connector;
	
	public DatabaseConnector()
	{
		database_urlConnector = "jdbc:mysql://powerfulyy.nazwa.pl:3307/powerfulyy?user=powerfulyy&password=qwePOI12";
		database_Connector = null;
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
			resultOfQuery = getResultOfMySqlQuery(query,2);
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
	 * @author Kamil Zimny
	 */
	public ArrayList<ArrayList<String>> getDataAboutManufacturerToVizualization(final String cityName)
	{
		String query = "SELECT nazwa_prod, P.dlugosc, P.szerokosc, MAX(Z.data_zak_plan), "
				+ "COUNT(*) , SUM(Z.wartosc_zlec) , SUM(DATEDIFF(Z.data_zak_plan,Z.data_rozp_plan)) "
				+ "FROM Zlecenia Z JOIN Producenci P ON Z.id_prod = P.id_prod "
				+ "JOIN Miasta M ON Z.z_miasta = M.id_miasta "
				+ "WHERE M.nazwa_miasta = '"+ cityName +"' "
				+ "GROUP BY P.id_prod;";
		
		ArrayList<ArrayList<Object>> resultOfQuery = null;
		ArrayList<ArrayList<String>> resultInString = null;
		try 
		{
			resultOfQuery =  getResultOfMySqlQuery(query,7);
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
				for(int j=0;j<7;j++)
				{
					resultInString.get(i).add( resultOfQuery.get(i).get(j).toString()  );
				}
					
			}
		}
		return resultInString;	
	}
	
	/**
	 * Sprawdza polaczenie z internetem
	 * @return boolean : 
	 * <br>true -> udalo sie 
	 * <br>false -> nie udalo sie
	 * @author Kamil Zimny
	 */
	private boolean checkInternetConnection()
	{
		try 
		{
            URL url = new URL("http://www.google.com");
            HttpURLConnection urlConnect = (HttpURLConnection)url.openConnection();
            urlConnect.getContent();
            return true;
        } 
		catch (Exception e) 
		{   
            return false;
        }
		
	}
	
	/**
	 * Laczy sie z baza danych i zwraca wartosc logiczna potwierdzajaca
	 * lub zaprzeczajaca polaczenie z baza danych na serwerze
	 * @return boolean : 
	 * <br>true -> udalo sie 
	 * <br>false -> nie udalo sie
	 * @author Kamil Zimny
	 */
	private boolean connectToDatabase()
	{		       
	     try 
	     {
	    	 database_Connector = DriverManager.getConnection(database_urlConnector);
	    	 return true;
	     }
	     catch(Exception ex) 
	     {
	       return false;
	     }
	}
	
	/**
	 * Zamyka polacznie z baza danych i zwraca wartos logiczna
	 * czy udalo sie poprawnie zamknac polaczenie czy nie
	 * Zamkniecie nie stworzonego polaczenia zwraca false.
	 * @return boolean :
	 * <br>true -> udalo sie 
	 * <br>false -> nie udalo sie
	 * @author Kamil Zimny
	 */
	private boolean closeConnectToDatabase()
	{
		if(database_Connector != null )
		{
			try 
			{
				database_Connector.close();
			} 
			catch (SQLException e) 
			{
				return false;
			}
		}	
		return true;
	}
	
	/**
	 * Sprawdza poprawnosc polaczenia, w przypadku potwierdzenia
	 * zwraca wyniki zapytania przekazanego w parametrze mysSqlQuery, czyli
	 * arrayListe wierszy, arrayLista wierszy zawiera krotki wynikowe, jest ich tyle
	 * co wynosi wartosc parametu numberOfResultColumns
	 * ************WAZNE*****************
	 * numberOfResultColumns musi byc mniejsza lub rowna ilosci zwracanych kolumn
	 * w przypadku mniejszej wartosci poprostu stracimy dane i nie bedziemy sie mogli do nich odwalac 
	 * w przypadku wiekszej wartosci pewnie rzuci jakims wyjatkiem.
	 * @return ArrayList<ArrayList<Object>> res : 
	 *  <br>res.get(i) -> i-ty wiersz  
	 *  <br>res.get(i).get(j) -> j-ta kolumna w i-tym wierszu 
	 *  @author Kamil Zimny
	 */
	public ArrayList<ArrayList<Object>> getResultOfMySqlQuery(final String mySqlQuery,final int numberOfResultColumns) 
			throws DatabaseConnectionExeption
	{
		if( !checkInternetConnection() )
			throw new DatabaseConnectionExeption("Blad: Brak polaczenia z internetem.");
		if( !connectToDatabase() )
			throw new DatabaseConnectionExeption("Blad: Brak polaczenia z z baza danych.");
		
		ResultSet resultOfQuery = null;
		ArrayList<ArrayList<Object>> queryResult = new ArrayList<ArrayList<Object>>();
		
		try 
        {		
            Class.forName("com.mysql.jdbc.Driver");
            Statement statment = database_Connector.createStatement();
			resultOfQuery = statment.executeQuery(mySqlQuery);
			int indexOfnextArray = 0;
			
			while (resultOfQuery.next()) 
			{
			   queryResult.add(new ArrayList<Object>());
			   for(int i=1; i<=numberOfResultColumns; i++)
				   queryResult.get(indexOfnextArray).add(resultOfQuery.getObject(i));
			   indexOfnextArray++;	   
			}
			
			if( !closeConnectToDatabase() )
				throw new DatabaseConnectionExeption("Blad: Nie udalo sie zakonczyc polaczenia z baza danych.");
					
			return queryResult ;
		} 		
		catch (SQLException | ClassNotFoundException e) 
		{
			throw new DatabaseConnectionExeption("Blad: "+e.getMessage());
		}	
	}

	
	/*
	 * ******************KORZYSTANIE Z KLASY DATABASECONNECTOR**************
    	 **********************************************************************
    	String query = "SELECT id,nazwa FROM Miasta ";
    	int numberOfResultColumns = -2;
    	
    	DatabaseConnector databaseConnector = new DatabaseConnector();
		try 
		{
			ArrayList<ArrayList<Object>> result = databaseConnector.getResultOfMySqlQuery(query,numberOfResultColumns);
			
			for(int i=0;i<result.size();i++)
			{
				// Tu implementowac dzialanie na zwroconych krotkach
				// Nalezy najpier zrzutowac obiekt czyli np.
				// (String) result.get(i).get(0)
				// (int) result.get(i).get(1) itp.
				// Lub tez skorzystac z petli pod spodem gdy w kazdej
				// kolumnie sa obiekty tego samego typu.
				for(int j=0;j<numberOfResultColumns;j++)
				{
					System.out.print( result.get(i).get(j).toString() + " ");
				}
				System.out.println();
			}
		} 
		catch (DatabaseConnectionExeption e) 
		{
			e.printStackTrace();
		}
    	 ***********************************************************************
    	 **********************************************************************
    	 *
    */
               
}
