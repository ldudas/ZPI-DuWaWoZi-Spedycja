package databaseTest;

import static org.junit.Assert.*;

import org.junit.Test;

import database.DataAccessObjectRoutePlanning;

public class DataAccessObjectRoutePlanningTest {

	DataAccessObjectRoutePlanning dao = new DataAccessObjectRoutePlanning();
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
