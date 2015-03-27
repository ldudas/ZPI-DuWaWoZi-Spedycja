/*
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
    /**
      * @param args
     */
	
	public DatabaseConnector()
	{
		database_urlConnector = "jdbc:mysql://powerfulyy.nazwa.pl:3307/powerfulyy?user=powerfulyy&password=qwePOI12";
		database_Connector = null;
	}
	
	/*
	 * sprawdza polaczenie z internetem
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
	
	/*
	 * Laczy sie z baza danych i zwraca wartosc logiczna potwierdzajaca
	 * lub zaprzeczajaca polaczenie z baza danych na serwerze
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
	
	/*
	 * Zamyka polacznie z baza danych i zwraca wartos logiczna
	 * czy udalo sie poprawnie zamknac polaczenie czy nie
	 * Zamkniecie nie stworzonego polaczenia zwraca false.
	 */
	private boolean closeConnectToDatabase()
	{
		if(database_Connector != null )
		{
			try 
			{
				database_Connector.close();
				return true;
			} 
			catch (SQLException e) 
			{
				//e.printStackTrace();
			}
		}	
		return false;
	}
	
	/*
	 * Sprawdza poprawnosc polaczenia, w przypadku potwierdzenia
	 * zwraca wyniki zapytania przekazanego w parametrze mysSqlQuery, czyli
	 * arrayListe wierszy, arrayLista wierszy zawiera krotki wynikowe, jest ich tyle
	 * co wynosi wartosc parametu numberOfResultColumns
	 * ************************WAZNE**************************************
	 * numberOfResultColumns musi byc mniejsza lub rowna ilosci zwracanych kolumn
	 * w przypadku mniejszej wartosci poprostu stracimy dane i nie bedziemy sie mogli do nich odwalac 
	 * w przypadku wiekszej wartosci pewnie rzuci jakims wyjatkiem.
	 * ----------------------------------------------------------------------------------------------
	 * Tomus to takie info dla Ciebie zeby nie bylo ze jak zaczales testowac to odrazu znalazlez blad
	 * i sie bedziesz cieszyc, Ciekawe kto to przeczyta do konca :) majo majo
	 * ----------------------------------------------------------------------------------------------
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
    	/*******************KORZYSTANIE Z KLASY DATABASECONNECTOR**************
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
    	 ***********************************************************************/
               
}
