package dataModelsTest;

import static org.junit.Assert.*;

import org.junit.Test;

import dataModels.ConverterCoordinations;

public class ConverterCoordinationsTest {

	ConverterCoordinations cc = new ConverterCoordinations();
	final static String COORDINATE = "51°45'27?N";
	final static String PARSE_COORDINATE = "51.7575";
	final static String WRONG_PARSE_COORDINATE = "55.55";

	@Test
	public void testEmptyCoordinate() {
		assertEquals(String.valueOf(-1.0), String.valueOf(cc.parseCoordinate("")));
	}
	
	@Test
	public void testProperParseCoordinate() {
		assertEquals(PARSE_COORDINATE, String.valueOf(cc.parseCoordinate(COORDINATE)));
	}
	
	@Test
	public void testWrongParseCoordinate() {
		assertNotEquals(WRONG_PARSE_COORDINATE, String.valueOf(cc.parseCoordinate(COORDINATE)));
	}
}
