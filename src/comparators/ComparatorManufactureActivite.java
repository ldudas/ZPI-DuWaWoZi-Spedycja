package comparators;

import java.io.Serializable;
import java.util.Comparator;

import dataModels.Manufacturer;

public class ComparatorManufactureActivite implements Comparator<Manufacturer> , Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int compare(Manufacturer man1, Manufacturer man2) 
	{
		// TODO Auto-generated method stub
		return man1.getRankOfDailyProfit() != man2.getRankOfDailyProfit() ? (int)(man1.getSumOfOrdersValue()/man1.getSumOfDays()) 
				- (int)(man2.getSumOfOrdersValue()/man2.getSumOfDays()) : 
			   man1.getRankOfNumberOfOrders() - man2.getRankOfNumberOfOrders();
	}
}
