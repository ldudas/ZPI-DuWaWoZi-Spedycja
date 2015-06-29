package buildersTest;

import static org.junit.Assert.*;

import org.junit.Test;

import shared.builders.CityBuilder;

/**
 * Klasa testująca poprawność budowania miast w programie
 * metoda buildCity klasy CityBuilder
 * @author Tomasz
 *
 */
public class CityBuilderTest {

	CityBuilder cb = new CityBuilder();
	final static String CITY_NAME = "Kielce";
	final static String[] COORDINATES = {"20°37'E","50°53'N"};
	
	/**
	 * Test metody buildCity z pustą wartością nazwy miasta
	 */
	@Test
	public void testNullCity() {
		assertNull(cb.buildCity("", COORDINATES[0], COORDINATES[1]));
	}
	
	/**
	 * Test metody buildCity z poprawnymi danymi,
	 * nazwa miasta i współrzędne odpowiadają sobie
	 */
	@Test
	public void testBuildProperCity() {
		assertEquals(CITY_NAME,cb.buildCity(CITY_NAME, COORDINATES[0], COORDINATES[1]).getCityName());
	}
	
	/**
	 * Test metody buildCity z niepoprawnymi danymi,
	 * nazwa miasta nie zgodna ze współrzędnymi
	 */
	@Test
	public void testBuildIncorrectCity() {
		assertNotEquals("Warszawa",cb.buildCity(CITY_NAME, COORDINATES[0], COORDINATES[1]).getCityName());
	}

}
