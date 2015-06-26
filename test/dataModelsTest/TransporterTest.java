package dataModelsTest;

import static org.junit.Assert.*;

import org.junit.Test;

import dataModels.SizeCategory;
import dataModels.Transporter;

/**
 * Klasa testująca poprawność porównywania przewoźników
 * metody compareByCapacity, compareByNumberOfOrders, compareByCost, compareByVolume
 * compareByDelay, compareByExecuted klasy Transporter
 * @author Tomasz
 *
 */
public class TransporterTest {

	Transporter t1 = new Transporter(1, SizeCategory.MEDIUM, 10, 2000, 100, 20, 1, 100, "RosiekAndRosiek", 791621444);
	Transporter t2 = new Transporter(2, SizeCategory.BIG, 50, 12000, 200, 50, 2, 50, "NextTir", 791621555);
	
	/**
	 * Test porównywania przewoźników metodą compareByCapacity
	 */
	@Test
	public void testCompareByCapacity() {
		assertEquals(1, Transporter.compareByCapacity(t1, t2));
	}
	
	/**
	 * Test porównywania przewoźników metodą compareByNumberOfOrders
	 */
	@Test
	public void testCompareByNumberOfOrders() {
		assertEquals(-1, Transporter.compareByNumbrOfOrders(t1, t2));
	}
	
	/**
	 * Test porównywania przewoźników metodą compareByCost
	 */
	@Test
	public void testCompareByCost() {
		assertEquals(-1, Transporter.compareByCost(t1, t2));
	}
	
	/**
	 * Test porównywania przewoźników metodą compareByVolume
	 */
	@Test
	public void testCompareByVolume() {
		assertEquals(-1, Transporter.compareByVolume(t1, t2));
	}

	/**
	 * Test porównywania przewoźników metodą compareByDelay
	 */
	@Test
	public void testCompareByDelay() {
		assertEquals(-1, Transporter.compareByDelay(t1, t2));
	}
	
	/**
	 * Test porównywania przewoźników metodą compareByExecuted
	 */
	@Test
	public void testCompareByExecuted() {
		assertEquals(1, Transporter.compareByExecuted(t1, t2));
	}
}
