package databaseTest;

import static org.junit.Assert.*;

import org.junit.Test;

import shared.database.DataAccessObjectPathVisualisation;

/**
 * Klasa testująca poprawność pobierania liczby miast dostępnych w bazie
 * metoda getCitiesCoordinates klasy DataAccessObjectPathVisualisation
 * @author Tomasz
 *
 */
public class DataAccessObjectPathVisualisationTest {

	DataAccessObjectPathVisualisation dao = new DataAccessObjectPathVisualisation();
	final static int NUMBER_OF_CITIES = 692;
	
	/**
	 * Test pobierania właściwej liczby miast metodą getCitiesCoordinates 
	 */
	@Test
	public void testGetProperNumberOfCities() {
		assertEquals(NUMBER_OF_CITIES, dao.getCitiesCoordinates().size());
	}
	
	/**
	 * Test pobierania niewłaściwej liczby miast metodą getCitiesCoordinates 
	 */
	@Test
	public void testGetWrongNumberOfCities() {
		assertNotEquals(NUMBER_OF_CITIES+1, dao.getCitiesCoordinates().size());
	}

}
