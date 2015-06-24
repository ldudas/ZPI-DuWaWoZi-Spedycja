package dataModelsTest;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;

import org.junit.Test;

import dataModels.Manufacturer;

public class ManufacturerTest {

	Manufacturer m1 = new Manufacturer("Nestle", 2037, 5053, " ", 4, 2000, 6, "791621432", "1");
	
	@Test
	public void testGetMonthActivity() {
		ArrayList<String> monthActivity = new ArrayList<String>();
		monthActivity.add("5");
		monthActivity.add("6");
		monthActivity.add("7");
		m1.setMonthsActivity(monthActivity);
		assertEquals(7, m1.getMonthActivity(1));
	}
	
	@Test
	public void testGetMonthActivityWrongMonth() {
		assertEquals(-1, m1.getMonthActivity(16));
	}
	
	@Test
	public void testGetMonthActivityColor() {
		ArrayList<Color> monthActivityColors = new ArrayList<Color>();
		monthActivityColors.add(new Color(0,0,255));
		monthActivityColors.add(new Color(255,255,255));
		monthActivityColors.add(new Color(232,10,232));
		m1.setMonthsActivityColors(monthActivityColors);
		assertEquals(new Color(255,255,255), m1.getMonthActivityColor(1));
	}
	
	@Test
	public void testGetMonthActivityColorWrongMonth() {
		assertEquals(new Color(0,0,0), m1.getMonthActivityColor(16));
	}
	
	@Test
	public void testGetQuarterActivitySpring() {
		ArrayList<String> monthActivity = new ArrayList<String>(12);
		monthActivity.add("1");
		monthActivity.add("2");
		monthActivity.add("3");
		monthActivity.add("4");
		monthActivity.add("5");
		monthActivity.add("6");
		m1.setMonthsActivity(monthActivity);
		assertEquals(15, m1.getQuarterActivity(0));
	}
	
	@Test
	public void testGetQuarterActivityWrongQuarter() {
		assertEquals(0, m1.getQuarterActivity(5));
	}

	@Test
	public void testGetQuarterActivityColor() {
		ArrayList<Color> monthActivityColors = new ArrayList<Color>();
		monthActivityColors.add(new Color(0,0,255));
		monthActivityColors.add(new Color(255,255,255));
		monthActivityColors.add(new Color(232,10,232));
		m1.setQuarterActivityColor(monthActivityColors);
		assertEquals(new Color(255,255,255), m1.getQuarterActivityColor(1));
	}
	
	@Test
	public void testGetQuarterActivityColorWrongQuarter() {
		assertEquals(new Color(0,0,0), m1.getQuarterActivityColor(5));
	}
	
}
