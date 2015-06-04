package databaseTest;

import static org.junit.Assert.*;

import org.junit.Test;

import database.DatabaseConnector;
import exceptions.DatabaseConnectionExeption;

public class DatabaseConnectorTest {

	DatabaseConnector db = new DatabaseConnector("powerful.nazwa.pl","3307","powerful","powerful","qweDSA12");
	final static int NUMBER_OF_CITIES = 692;
	final static int NUMBER_OF_CITY_COLUMNS = 3;
	
	@Test
	public void testReturnZeroTuple() throws DatabaseConnectionExeption {
		assertEquals(0, db.getResultOfMySqlQuery("SELECT id_miasta FROM Miasta WHERE nazwa_miasta = 'Bielawa' and id_miasta = 4").size());
	}
	
	@Test
	public void testReturnOneTuple() throws DatabaseConnectionExeption {
		assertEquals(1, db.getResultOfMySqlQuery("SELECT id_miasta FROM Miasta WHERE nazwa_miasta = 'Bielawa'").size());
	}
	
	@Test
	public void testReturnMoreThanOneTuple() throws DatabaseConnectionExeption {
		assertEquals(2, db.getResultOfMySqlQuery("SELECT id_miasta FROM Miasta WHERE nazwa_miasta = 'Bielawa' or id_miasta = 4").size());
	}
	
	@Test
	public void testReturnAllTuples() throws DatabaseConnectionExeption {
		assertEquals(NUMBER_OF_CITIES, db.getResultOfMySqlQuery("SELECT * FROM Miasta").size());
	}

}
