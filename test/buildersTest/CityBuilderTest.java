package buildersTest;

import static org.junit.Assert.*;

import org.junit.Test;

import dataModels.City;
import builders.CityBuilder;

public class CityBuilderTest {

	CityBuilder cb = new CityBuilder();
	final static String CITY_NAME = "Kielce";
	final static String[] COORDINATES = {"20°37'E","50°53'N"};
	
	@Test
	public void testNullCity() {
		assertNull(cb.buildCity("", COORDINATES[0], COORDINATES[1]));
	}
	
	@Test
	public void testBuildProperCity() {
		assertEquals(CITY_NAME,cb.buildCity(CITY_NAME, COORDINATES[0], COORDINATES[1]).getCityName());
	}
	
	@Test
	public void testBuildIncorrectCity() {
		assertNotEquals("Warszawa",cb.buildCity(CITY_NAME, COORDINATES[0], COORDINATES[1]).getCityName());
	}

}
