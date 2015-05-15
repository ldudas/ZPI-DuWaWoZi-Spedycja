package interfacesTest;

import static org.junit.Assert.*;
import interfaces.RoutePlanningModel;

import org.junit.Test;

import dataModels.City;
import dataModels.Order;

public class RoutePlanningModelTest {

	RoutePlanningModel rpm = new RoutePlanningModel();
	
	final static String NAZWA_MIASTA = "Kielce";
	final static String[] WSPOLRZEDNE = {"20°37'E","50°53'N"};
	
	@Test
	public void testGetOrderCollection()
	{
		assertNotNull(rpm.getOrdersCollection());
	}
	
	@Test
	public void testNullLastOrder()
	{
		rpm.getOrdersCollection().add(null);
		assertNull(rpm.getLastOrder());
	}
	
	@Test
	public void testNotNullLastOrder()
	{
		Order o = new Order(new City("Kielce",2037,5053), new City("Gniezno",1736,5232), "", "");
		rpm.getOrdersCollection().add(o);
		assertNotNull(rpm.getLastOrder());
	}
	
	@Test
	public void testNullCityCoordinates() {
		assertNull(rpm.getCityCoordinates(""));
	}
	
	@Test
	public void testGetProperNumberOfCityCoordinates() {
		assertEquals(2, rpm.getCityCoordinates(NAZWA_MIASTA).length);
	}
	
	@Test
	public void testGetProperCityCoordinates() {
		assertArrayEquals(WSPOLRZEDNE, rpm.getCityCoordinates(NAZWA_MIASTA));
	}

}
