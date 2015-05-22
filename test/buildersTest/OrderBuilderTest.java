package buildersTest;

import static org.junit.Assert.*;

import org.junit.Test;

import dataModels.City;
import builders.OrderBuilder;

public class OrderBuilderTest {

	OrderBuilder ob = new OrderBuilder();
	final static City CITY_FROM = new City("Kielce",2037,5053);
	final static City CITY_TO = new City("Gniezno",1736,5232);
	final static String START_DATE = "02-10-2013";
	final static String FINISH_DATE = "04-10-2013";
	
	@Test
	public void testNullCity() {
		assertNull(ob.buildOrder(null, CITY_FROM, START_DATE, FINISH_DATE));
	}
	
	@Test
	public void testNullDate() {
		assertNull(ob.buildOrder(CITY_TO, CITY_FROM, "", ""));
	}
	
	@Test
	public void testBuildProperOrder() {
		assertNotNull(ob.buildOrder(CITY_TO, CITY_FROM, START_DATE, FINISH_DATE));
	}
	
	@Test
	public void testCreateProperCity() {
		assertEquals(CITY_FROM.getCityName(), ob.buildOrder(CITY_TO, CITY_FROM, START_DATE, FINISH_DATE).getCityFrom().getCityName());
	}
	
}
