package buildersTest;

import static org.junit.Assert.*;

import org.junit.Test;

import dataModels.City;
import builders.CityBuilder;

public class CityBuilderTest {

	CityBuilder cb = new CityBuilder();
	final static String NAZWA_MIASTA = "Kielce";
	final static String[] WSPOLRZEDNE = {"20°37'E","50°53'N"};
	
	@Test
	public void testNullCity() {
		assertNull(cb.buildCity("", WSPOLRZEDNE[0], WSPOLRZEDNE[1]));
	}
	
	@Test
	public void testBuildProperCity() {
		assertEquals(NAZWA_MIASTA,cb.buildCity(NAZWA_MIASTA, WSPOLRZEDNE[0], WSPOLRZEDNE[1]).getCityName());
	}
	
	@Test
	public void testBuildIncorrectCity() {
		assertNotEquals("Warszawa",cb.buildCity(NAZWA_MIASTA, WSPOLRZEDNE[0], WSPOLRZEDNE[1]).getCityName());
	}

}
