/**
 * @author[Kamil Zimny]
 */
package database;

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
		database_urlConnector = "jdbc:mysql://powerful.nazwa.pl:3307/powerful?user=powerful&password=qweDSA12";
		database_Connector = null;
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
	@SuppressWarnings("finally")
	public ArrayList<ArrayList<Object>> getResultOfMySqlQuery(final String mySqlQuery,final int numberOfResultColumns) 
			throws DatabaseConnectionExeption
	{
		if( !checkInternetConnection() )
			throw new DatabaseConnectionExeption("Blad: Brak polaczenia z internetem.");
		if( !connectToDatabase() )
			throw new DatabaseConnectionExeption("Blad: Brak polaczenia z z baza danych.");
		
		ResultSet resultOfQuery = null;
		Statement statment = null;
		ArrayList<ArrayList<Object>> queryResult = new ArrayList<ArrayList<Object>>();
		
		try 
        {		
            Class.forName("com.mysql.jdbc.Driver");
            statment = database_Connector.createStatement();
			resultOfQuery = statment.executeQuery(mySqlQuery);
			int indexOfnextArray = 0;
			
			while (resultOfQuery.next()) 
			{
			   queryResult.add(new ArrayList<Object>());
			   for(int i=1; i<=numberOfResultColumns; i++)
				   queryResult.get(indexOfnextArray).add(resultOfQuery.getObject(i));
			   indexOfnextArray++;	   
			}
			statment.close();

		} 		
		catch (SQLException | ClassNotFoundException e) 
		{
			throw new DatabaseConnectionExeption("Blad: "+e.getMessage());
		}	
		finally
		{			
			if( !closeConnectToDatabase() )
				throw new DatabaseConnectionExeption("Blad: Nie udalo sie zakonczyc polaczenia z baza danych.");
			return queryResult ;
		}
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
               
}
