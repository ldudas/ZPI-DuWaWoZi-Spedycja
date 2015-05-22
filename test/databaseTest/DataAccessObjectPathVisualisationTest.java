package databaseTest;

import static org.junit.Assert.*;

import org.junit.Test;

import database.DataAccessObjectPathVisualisation;

public class DataAccessObjectPathVisualisationTest {

	DataAccessObjectPathVisualisation dao = new DataAccessObjectPathVisualisation();
	final static int NUMBER_OF_CITIES = 692;
	
	@Test
	public void testGetProperNumberOfCities() {
		assertEquals(NUMBER_OF_CITIES, dao.getCitiesCoordinates().size());
	}

}
