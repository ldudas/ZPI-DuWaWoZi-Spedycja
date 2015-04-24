package builders;

import dataModels.City;
import dataModels.ConverterCoordinations;

public class CityBuilder 
{
	
	public CityBuilder()
	{
	}
	
	public City buildCity(String name, String longtitude , String latitude)
	{
		ConverterCoordinations converterCoordination = new ConverterCoordinations();
		
		return new City(name, converterCoordination.parseCoordinate(latitude),  converterCoordination.parseCoordinate(longtitude));
	}
	
}
