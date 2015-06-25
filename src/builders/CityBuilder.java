package builders;

import dataModels.City;
import dataModels.ConverterCoordinations;

public class CityBuilder 
{
	
	public CityBuilder()
	{
	}
	
	/**
	 * Tworzy obiekt miasta.
	 * @param name - nazwa miasta
	 * @param longtitude - długość gegraficzna miasta
	 * @param latitude - szerokość geograficzna miasta
	 * @return City stworzony obiekt miasta.
	 * @author Kamil Zimny
	 */
	public City buildCity(String name, String longtitude , String latitude)
	{
		if(name.isEmpty())
			return null;
		
		ConverterCoordinations converterCoordination = new ConverterCoordinations();
		
		return new City(name, converterCoordination.parseCoordinate(latitude),  converterCoordination.parseCoordinate(longtitude));
	}
	
}
