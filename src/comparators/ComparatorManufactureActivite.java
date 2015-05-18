package comparators;

import java.util.Comparator;

import dataModels.Manufacturer;

public class ComparatorManufactureActivite implements Comparator<Manufacturer>
{
	@Override
	public int compare(Manufacturer man1, Manufacturer man2) 
	{
		// TODO Auto-generated method stub
		return man1.getRankOfDailyProfit() != man2.getRankOfDailyProfit() ? man1.getRankOfDailyProfit() - man2.getRankOfDailyProfit() : 
			   man1.getRankOfNumberOfOrders() - man2.getRankOfNumberOfOrders();
	}
}
