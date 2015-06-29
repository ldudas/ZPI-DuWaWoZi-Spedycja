package databaseTest;

import static org.junit.Assert.*;

import org.junit.Test;

import database.DataAccessObjectManufacturersVisualisation;

/**
 * Klasa testująca poprawność pobierania współrzędnych z bazy
 * metoda getCityCoordinates klasy DataAccessObjectManufacturersVisualisation
 * @author Tomasz
 *
 */
public class DataAccessObjectManufacturersVisualisationTest {

	DataAccessObjectManufacturersVisualisation dao = new DataAccessObjectManufacturersVisualisation();
	final static String CITY_NAME = "Kielce";
	final static String[] COORDINATES = {"20°37'E","50°53'N"};
	
	/**
	 * Test metody getCityCoordinates z pustym stringiem zamiast nazwy miasta w argumencie metody
	 */
	@Test
	public void testNullCityCoordinates() {
		assertNull(dao.getCityCoordinates(""));
	}
	
	/**
	 * Test pobierania właściwej liczby współrzędnych metody getCityCoordinates 
	 * z poprawną nazwą miasta w argumencie metody
	 */
	@Test
	public void testGetProperNumberOfCityCoordinates() {
		assertEquals(2, dao.getCityCoordinates(CITY_NAME).length);
	}
	
	/**
	 * Test metody getCityCoordinates z poprawną nazwą miasta w argumencie metody
	 */
	@Test
	public void testGetProperCityCoordinates() {
		assertArrayEquals(COORDINATES, dao.getCityCoordinates(CITY_NAME));
	}

}
