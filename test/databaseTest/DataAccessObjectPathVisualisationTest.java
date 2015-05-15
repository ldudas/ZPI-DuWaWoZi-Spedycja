package databaseTest;

import static org.junit.Assert.*;

import org.junit.Test;

import database.DataAccessObjectPathVisualisation;

public class DataAccessObjectPathVisualisationTest {

	DataAccessObjectPathVisualisation dao = new DataAccessObjectPathVisualisation();
	final static int LICZBA_MIAST = 692;
	
	@Test
	public void testGetProperNumberOfCities() {
		assertEquals(LICZBA_MIAST, dao.getCitiesCoordinates().size());
	}

}
