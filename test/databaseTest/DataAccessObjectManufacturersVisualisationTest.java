package databaseTest;

import static org.junit.Assert.*;

import org.junit.Test;

import database.DataAccessObjectManufacturersVisualisation;


public class DataAccessObjectManufacturersVisualisationTest {

	DataAccessObjectManufacturersVisualisation dao = new DataAccessObjectManufacturersVisualisation();
	final static String CITY_NAME = "Kielce";
	final static String[] COORDINATES = {"20°37'E","50°53'N"};
	
	@Test
	public void testNullCityCoordinates() {
		assertNull(dao.getCityCoordinates(""));
	}
	
	@Test
	public void testGetProperNumberOfCityCoordinates() {
		assertEquals(2, dao.getCityCoordinates(CITY_NAME).length);
	}
	
	@Test
	public void testGetProperCityCoordinates() {
		assertArrayEquals(COORDINATES, dao.getCityCoordinates(CITY_NAME));
	}

}
