package builders;
import java.util.ArrayList;

import dataModels.ConverterCoordinations;
import dataModels.Manufacturer;

public class ManufacturersCollectionBuilder 
{
	
	public ManufacturersCollectionBuilder()
	{}
	
	/**
	 * Tworzenie kolekcji obiektów Manufacturer producentów,
	 * ma podstawie dannych z bazy.
	 * @param manufacturerDetails - dane dotyczące producentów
	 * @param manufacturerActivityInEachMonth - aktywności producentów w każdym miesiącu
	 * @param manufacturerCostInEachMonth - zarobek na producencie w każdym miesiącu
	 * @return ArrayList<Manufacturer> kolekcja producentow
	 * @author Kamil Zimny
	 */
	public ArrayList<Manufacturer> buildManufacturersCollection
						( 
							final ArrayList<ArrayList<String>> manufacturerDetails,
							final ArrayList<ArrayList<String>> manufacturerActivityInEachMonth,
							final ArrayList<ArrayList<String>> manufacturerCostInEachMonth
						)
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
				
				for( ArrayList<String> manAct :  manufacturerActivityInEachMonth )
					if ( manInfo.get(8).equals(manAct.get(0)) )
					{   
						manufacturers.get(manufacturers.size() - 1).setMonthsActivity(manAct);
						break;
					}
				
				for( ArrayList<String> manCost :  manufacturerCostInEachMonth )
					if ( manInfo.get(8).equals(manCost.get(0)) )
					{   
						manufacturers.get(manufacturers.size() - 1).setMonthsCost(manCost);
						break;
					}
					
			}
		}
		catch(NullPointerException ex)
		{
			return null;
		}
		
		return manufacturers;
	}
}
