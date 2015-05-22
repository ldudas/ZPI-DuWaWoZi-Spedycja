package interfacesTest;

import static org.junit.Assert.*;
import interfaces.RoutePlanningModel;

import org.junit.Test;


public class RoutePlanningModelTest {

	RoutePlanningModel rpm = new RoutePlanningModel();
	
	final static String CITY_NAME = "Kielce";
	final static String[] COORDINATES = {"20°37'E","50°53'N"};
	
	@Test
	public void testNullCityCoordinates() {
		assertNull(rpm.getCityCoordinates(""));
	}
	
	@Test
	public void testGetProperNumberOfCityCoordinates() {
		assertEquals(2, rpm.getCityCoordinates(CITY_NAME).length);
	}
	
	@Test
	public void testGetProperCityCoordinates() {
		assertArrayEquals(COORDINATES, rpm.getCityCoordinates(CITY_NAME));
	}

}
