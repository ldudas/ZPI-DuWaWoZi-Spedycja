package databaseTest;

import static org.junit.Assert.*;

import org.junit.Test;

import shared.database.DataAccessObjectRoutePlanning;

/**
 * Klasa testująca poprawność pobierania współrzędnych z bazy
 * @author Tomasz
 *
 */
public class DataAccessObjectRoutePlanningTest {

	DataAccessObjectRoutePlanning dao = new DataAccessObjectRoutePlanning();
	final static String CITY_NAME = "Kielce";
	final static String[] COORDINATES = {"20°37'E","50°53'N"};
	
	/**
	 * Test metody getCityCoordinates z pustą wartością argumentu reprezentującego nazwę miasta
	 */
	@Test
	public void testNullCityCoordinates() {
		assertNull(dao.getCityCoordinates(""));
	}
	
	/**
	 * Test metody getCityCoordinates - pobierania poprawnej liczby współrzędnych 
	 * z poprawną wartością nazwy miasta jako argument 
	 */
	@Test
	public void testGetProperNumberOfCityCoordinates() {
		assertEquals(2, dao.getCityCoordinates(CITY_NAME).length);
	}
	
	/**
	 * Test metody getCityCoordinates z poprawną wartością nazwy miasta w argumencie metody
	 */
	@Test
	public void testGetProperCityCoordinates() {
		assertArrayEquals(COORDINATES, dao.getCityCoordinates(CITY_NAME));
	}

}
