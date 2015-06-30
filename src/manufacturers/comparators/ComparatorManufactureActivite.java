package manufacturers.comparators;

import java.io.Serializable;
import java.util.Comparator;

import shared.dataModels.Manufacturer;

/**
 * Komparator ze względu na aktywność producentax	
 * @author Kamil Zimny
 *
 */
public class ComparatorManufactureActivite implements Comparator<Manufacturer> , Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Komparator porównujący dwóch producnetów, najpierw po zarobku dziennym na 
	 * producencie, w przypadku równości po ilości wykonanych zamówień.
	 * @author Kamil Zimnys
	 */
	@Override
	public int compare(Manufacturer man1, Manufacturer man2) 
	{
		// TODO Auto-generated method stub
		return man1.getRankOfDailyProfit() != man2.getRankOfDailyProfit() ? (int)(man1.getSumOfOrdersValue()/man1.getSumOfDays()) 
				- (int)(man2.getSumOfOrdersValue()/man2.getSumOfDays()) : 
			   man1.getRankOfNumberOfOrders() - man2.getRankOfNumberOfOrders();
	}
}
