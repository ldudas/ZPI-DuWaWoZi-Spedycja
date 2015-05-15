package databaseTest;

import static org.junit.Assert.*;

import org.junit.Test;

import database.DatabaseConnector;
import exceptions.DatabaseConnectionExeption;

public class DatabaseConnectorTest {

	DatabaseConnector db = new DatabaseConnector();
	final static int LICZBA_MIAST = 692;
	final static int ILOSC_KOLUMN_MIASTA = 3;
	
	@Test
	public void testReturnZeroTuple() throws DatabaseConnectionExeption {
		assertEquals(0, db.getResultOfMySqlQuery("SELECT id_miasta FROM Miasta WHERE nazwa_miasta = 'Bielawa' and id_miasta = 4", 1).size());
	}
	
	@Test
	public void testReturnOneTuple() throws DatabaseConnectionExeption {
		assertEquals(1, db.getResultOfMySqlQuery("SELECT id_miasta FROM Miasta WHERE nazwa_miasta = 'Bielawa'", 1).size());
	}
	
	@Test
	public void testReturnMoreThanOneTuple() throws DatabaseConnectionExeption {
		assertEquals(2, db.getResultOfMySqlQuery("SELECT id_miasta FROM Miasta WHERE nazwa_miasta = 'Bielawa' or id_miasta = 4", 1).size());
	}
	
	@Test
	public void testReturnAllTuples() throws DatabaseConnectionExeption {
		assertEquals(LICZBA_MIAST, db.getResultOfMySqlQuery("SELECT * FROM Miasta", ILOSC_KOLUMN_MIASTA).size());
	}

}
