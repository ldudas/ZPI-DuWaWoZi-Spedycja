package buildersTest;

import static org.junit.Assert.*;

import org.junit.Test;

import shared.builders.OrderBuilder;
import shared.dataModels.City;

/**
 * Klasa testująca poprawność budowania zleceń w programie
 * metody buildOrder, getCityName klasy OrderBuilder
 * @author Tomasz
 *
 */
public class OrderBuilderTest {

	OrderBuilder ob = new OrderBuilder();
	final static City CITY_FROM = new City("Kielce",2037,5053);
	final static City CITY_TO = new City("Gniezno",1736,5232);
	final static String START_DATE = "02-10-2013";
	final static String FINISH_DATE = "04-10-2013";
	
	/**
	 * Test metody buildOrder z pustą wartością argumentu reprezentującego nazwę miasta
	 */
	@Test
	public void testNullCity() {
		assertNull(ob.buildOrder(null, CITY_FROM, START_DATE, FINISH_DATE));
	}
	
	/**
	 * Test metody buildOrder z pustymi wartościami argumentów reprezentujących daty
	 */
	@Test
	public void testNullDate() {
		assertNull(ob.buildOrder(CITY_TO, CITY_FROM, "", ""));
	}
	
	/**
	 * Test metody buildOrder z poprawnymi argumentami metody
	 */
	@Test
	public void testBuildProperOrder() {
		assertNotNull(ob.buildOrder(CITY_TO, CITY_FROM, START_DATE, FINISH_DATE));
	}
	
	/**
	 * Test metody getCityName z poprawnymi argumentami metody
	 */
	@Test
	public void testCreateProperCity() {
		assertEquals(CITY_FROM.getCityName(), ob.buildOrder(CITY_TO, CITY_FROM, START_DATE, FINISH_DATE).getCityFrom().getCityName());
	}
	
}
