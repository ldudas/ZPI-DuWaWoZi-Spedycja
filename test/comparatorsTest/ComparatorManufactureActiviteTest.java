package comparatorsTest;

import static org.junit.Assert.*;

import java.awt.Color;

import manufacturers.comparators.ComparatorManufactureActivite;

import org.junit.Test;

import shared.dataModels.Manufacturer;

/**
 * Klasa testująca poprawność działania komparatora
 * metoda compare klasy ComparatorManufactureActivite
 * @author Tomasz
 *
 */
public class ComparatorManufactureActiviteTest {

	ComparatorManufactureActivite cma = new ComparatorManufactureActivite();
	
	/**
	 * Test porównywania dwóch takich samych producentów
	 */
	@Test
	public void testTheSameManufacturers() {
		Manufacturer m1 = new Manufacturer("Nestle", 2037, 5053, " ", 4, 2000, 6, "791621432", "1");
		m1.setRankOfDailyProfit(new Color(0,0,0));
		m1.setRankOfNumberOfOrders(12);
		assertTrue(cma.compare(m1, m1)==0);
	}
	
	/**
	 * Test porównywania dwóch różnych producentów
	 */
	@Test
	public void testDifferentManufacturers() {
		Manufacturer m1 = new Manufacturer("Nestle", 2037, 5053, " ", 4, 2000, 6, "791621432", "1");
		Manufacturer m2 = new Manufacturer("BP", 1736, 5232, " ", 6, 10000, 12, "791621333", "2");
		m1.setRankOfDailyProfit(new Color(0,0,0));
		m1.setRankOfNumberOfOrders(12);
		m2.setRankOfDailyProfit(new Color(255,0,0));
		m2.setRankOfNumberOfOrders(15);
		assertTrue(cma.compare(m1, m2)<0);
	}

}
