package interfacesTest;

import static org.junit.Assert.*;
import interfaces.RoutePlanningModel;

import org.junit.Test;

/**
 * Klasa testująca poprawność zwracanych współrzędnych
 * metoda getCityCoordinates klasy RoutePlanningModel
 * @author Tomasz
 *
 */
public class RoutePlanningModelTest {

	RoutePlanningModel rpm = new RoutePlanningModel();
	
	final static String CITY_NAME = "Kielce";
	final static String[] COORDINATES = {"20°37'E","50°53'N"};
	
	/**
	 * Test metody getCityCoordinates z pustym stringiem w argumencie reprezentującym nazwę miasta
	 */
	@Test
	public void testNullCityCoordinates() {
		assertNull(rpm.getCityCoordinates(""));
	}
	
	/**
	 * Test metody getCityCoordinates zwracającej liczbę współrzędnych z poprawnymi danymi
	 */
	@Test
	public void testGetProperNumberOfCityCoordinates() {
		assertEquals(2, rpm.getCityCoordinates(CITY_NAME).length);
	}
	
	/**
	 * Test metody getCityCoordinates z poprawnymi danymi
	 */
	@Test
	public void testGetProperCityCoordinates() {
		assertArrayEquals(COORDINATES, rpm.getCityCoordinates(CITY_NAME));
	}

}
