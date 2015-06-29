package dataModelsTest;

import static org.junit.Assert.*;

import org.junit.Test;

import shared.dataModels.ConverterCoordinations;

/**
 * Klasa testująca poprawność parsowania współrzędnych
 * metoda parseCoordinate klasy ConverterCoordinations
 * @author Tomasz
 *
 */
public class ConverterCoordinationsTest {

	ConverterCoordinations cc = new ConverterCoordinations();
	final static String COORDINATE = "51°45'27?N";
	final static String PARSE_COORDINATE = "51.7575";
	final static String WRONG_PARSE_COORDINATE = "55.55";

	/**
	 * Test metody parseCoordinate z pustym argumentem reprezentującym współrzędną
	 */
	@Test
	public void testEmptyCoordinate() {
		assertEquals(String.valueOf(-1.0), String.valueOf(cc.parseCoordinate("")));
	}
	
	/**
	 * Test metody parseCoordinate z poprawną współrzędną i poprawnym wynikiem
	 */
	@Test
	public void testProperParseCoordinate() {
		assertEquals(PARSE_COORDINATE, String.valueOf(cc.parseCoordinate(COORDINATE)));
	}
	
	/**
	 * Test metody parseCoordinate z poprawną współrzędną i niepoprawnym wynikiem
	 */
	@Test
	public void testWrongParseCoordinate() {
		assertNotEquals(WRONG_PARSE_COORDINATE, String.valueOf(cc.parseCoordinate(COORDINATE)));
	}
}
