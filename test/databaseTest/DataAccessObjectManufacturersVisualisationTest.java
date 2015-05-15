package databaseTest;

import static org.junit.Assert.*;

import org.junit.Test;

import database.DataAccessObjectManufacturersVisualisation;


public class DataAccessObjectManufacturersVisualisationTest {

	DataAccessObjectManufacturersVisualisation dao = new DataAccessObjectManufacturersVisualisation();
	final static String NAZWA_MIASTA = "Kielce";
	final static String[] WSPOLRZEDNE = {"20°37'E","50°53'N"};
	
	@Test
	public void testNullCityCoordinates() {
		assertNull(dao.getCityCoordinates(""));
	}
	
	@Test
	public void testGetProperNumberOfCityCoordinates() {
		assertEquals(2, dao.getCityCoordinates(NAZWA_MIASTA).length);
	}
	
	@Test
	public void testGetProperCityCoordinates() {
		assertArrayEquals(WSPOLRZEDNE, dao.getCityCoordinates(NAZWA_MIASTA));
	}

}
