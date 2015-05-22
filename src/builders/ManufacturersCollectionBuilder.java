package builders;
import java.util.ArrayList;

import dataModels.ConverterCoordinations;
import dataModels.Manufacturer;

public class ManufacturersCollectionBuilder 
{
	
	public ManufacturersCollectionBuilder(  )
	{
	}
	
	public ArrayList<Manufacturer> buildManufacturersCollection( final ArrayList<ArrayList<String>> manufacturerDetails )
	{
		ArrayList<Manufacturer> manufacturers = new ArrayList<Manufacturer>();
		ConverterCoordinations converterCoordinatrion = new ConverterCoordinations();
		try
		{
			for( ArrayList<String>  manInfo :  manufacturerDetails )
			{
	
				manufacturers.add(new Manufacturer(manInfo.get(0), converterCoordinatrion.parseCoordinate(manInfo.get(1)), 
						converterCoordinatrion.parseCoordinate(manInfo.get(2)), manInfo.get(3), 
						Integer.parseInt(manInfo.get(4)), Double.parseDouble(manInfo.get(5)), 
						Integer.parseInt(manInfo.get(6)), manInfo.get(7), manInfo.get(8) ));
			}
		}
		catch(NullPointerException ex)
		{
			return null;
		}
		
		return manufacturers;
	}
}
