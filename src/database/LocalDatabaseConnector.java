package database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import exceptions.DatabaseConnectionExeption;


public class LocalDatabaseConnector 
{
	private String database_urlConnector;
	private Connection database_Connector;
	private Properties props;
	
	/**
	 * Konstruktor klasy LocalDatabaseConnector
	 * ustawia właściwości połączenia z lokalną bazą danych
	 */
	public LocalDatabaseConnector() 
	{	
    	File file = new File( System.getProperty("user.dir") + "\\AccountDatabase.accdb");
        props = new Properties();
    	props.setProperty("JackcessOpener","database.CryptCodecOpener");
    	props.setProperty("password", "DecisionSupport"); 
    	props.setProperty("Showschema","false");
    	props.setProperty("Memory","true");

		database_urlConnector = "jdbc:ucanaccess://" + file;
		database_Connector = null;
	}

	/** 
	 * Sprawdza poprawność połączenia, w przypadku potwierdzenia
	 * wykonuje podane zapytanie, ( UPDATE, INSERT ,DELETE) 
	 *  @author Kamil Zimny
	 * @throws DatabaseConnectionExeption 
	 */
	public void executeUpdateMySqlQuery(final String mySqlQuery) throws DatabaseConnectionExeption
	{
		if( !connectToDatabase() )
			throw new DatabaseConnectionExeption("Blad: Brak polaczenia z z baza danych.");
		Statement statment = null;
		try 
        {		
    		Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
    	
            statment = database_Connector.createStatement();
            statment.executeUpdate(mySqlQuery);
            statment.close();
        }
		catch (SQLException e)
		{
			throw new DatabaseConnectionExeption("Blad: "+e.getMessage());
		}
		catch(ClassNotFoundException e)
		{
			throw new DatabaseConnectionExeption("Blad: "+e.getMessage());
		}
		finally
		{			
			if( !closeConnectToDatabase() )
				throw new DatabaseConnectionExeption("Blad: Nie udalo sie zakonczyc polaczenia z baza danych.");
		}
		
	}
	
	/** 
	 * Sprawdza poprawność połączenia, w przypadku potwierdzenia
	 * zwraca wyniki zapytania przekazanego w parametrze mysSqlQuery, czyli
	 * arrayListe wierszy, arrayLista wierszy zawiera krotki wynikowe
	 * @return ArrayList<ArrayList<Object>> res : 
	 *  <br>res.get(i) -> i-ty wiersz  
	 *  <br>res.get(i).get(j) -> j-ta kolumna w i-tym wierszu 
	 *  @author Kamil Zimny
	 * @throws DatabaseConnectionExeption 
	 */
	@SuppressWarnings("finally")
	public ArrayList<ArrayList<Object>> getResultOfMySqlQuery(final String mySqlQuery) throws DatabaseConnectionExeption 
		//	throws DatabaseConnectionExeption
	{
		
		if( !connectToDatabase() )
			throw new DatabaseConnectionExeption("Blad: Brak polaczenia z z baza danych.");
		
		ResultSet resultOfQuery = null;
		Statement statment = null;
		ArrayList<ArrayList<Object>> queryResult = new ArrayList<ArrayList<Object>>();
		
		try 
        {		
    		Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
    	
            statment = database_Connector.createStatement();
			resultOfQuery = statment.executeQuery(mySqlQuery);
			int indexOfnextArray = 0;
			int numberOfResultColumns = resultOfQuery.getMetaData().getColumnCount();
			while (resultOfQuery.next()) 
			{
			   queryResult.add(new ArrayList<Object>());
			   for(int i=1; i<=numberOfResultColumns; i++)
				   queryResult.get(indexOfnextArray).add(resultOfQuery.getObject(i));
			   indexOfnextArray++;	   
			}
			statment.close();
		} 		
		catch (SQLException e)
		{
			throw new DatabaseConnectionExeption("Blad: "+e.getMessage());
		}
		catch(ClassNotFoundException e)
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
	 * Łączy się z bazą danych i zwraca wartość logiczną potwierdzajacą
	 * lub zaprzeczajacą połączenie z bazą danych na serwerze
	 * @return boolean : 
	 * <br>true -> udało się 
	 * <br>false -> nie udało się
	 * @author Kamil Zimny
	 */
	private boolean connectToDatabase()
	{		       
	     try 
	     {
	    	 database_Connector = DriverManager.getConnection(database_urlConnector,props);
	    	 return true;
	     }
	     catch(Exception ex) 
	     {
	       return false;
	     }
	}
	
	/**
	 * Zamyka połączenie z bazą danych i zwraca wartość logiczną
	 * czy udało się poprawnie zamknąć połączenie czy nie
	 * Zamknięcie nie stworzonego połączenia zwraca false.
	 * @return boolean :
	 * <br>true -> udało się 
	 * <br>false -> nie udało się
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
