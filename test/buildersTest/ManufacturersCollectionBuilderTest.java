package buildersTest;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import shared.builders.ManufacturersCollectionBuilder;

/**
 * Klasa testująca poprawność budowania kolekcji producentów
 * metoda buildManufacturersCollection klasy ManufacturersCollectionBuilder
 * @author Tomasz
 *
 */
public class ManufacturersCollectionBuilderTest {

	ManufacturersCollectionBuilder mcb = new ManufacturersCollectionBuilder();
	
	/**
	 * Test metody buildManufacturersCollection z pustymi wartościami argumentów
	 */
	@Test
	public void testNullManufacturersCollection() {
		assertNull(mcb.buildManufacturersCollection(null,null,null));
	}
	
	/**
	 * Test metody buildManufacturersCollection z poprawnymi wartościami argumentów
	 */
	@Test
	public void testBuildProperManufacturersCollection() {
		ArrayList<ArrayList<String>> all = new ArrayList<ArrayList<String>>();
		ArrayList<String> prod1 = new ArrayList<String>();	
		prod1.add("Nestle");
		prod1.add("51°45'27?N");
		prod1.add("18°04'48?E");
		prod1.add(" ");
		prod1.add("4");
		prod1.add("2000");
		prod1.add("6");
		prod1.add("791621432");
		prod1.add("1");
		all.add(prod1);
		assertEquals(1, mcb.buildManufacturersCollection(all,null,null).size());
	}
}
