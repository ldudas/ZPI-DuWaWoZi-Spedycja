package builders;

import dataModels.City;
import dataModels.Order;

/**
 * Budowniczy dla klasy Order
 * @author Kamil Zimny
 *
 */
public class OrderBuilder 
{
	public OrderBuilder() 
	{
		
	}
	
	/**
	 * Tworzy obiekt Order zamówienia zlecenia na daną trasę.
	 * @param cityTo - miasto docelowe
	 * @param cityFrom - miasto startowe
	 * @param startDate - data rozpoczęcia
	 * @param finishDate - data zakończenia
	 * @return Order zamówienia
	 * @author Kamil Zimny
	 */
	public Order buildOrder(City cityTo, City cityFrom ,String startDate, String finishDate)
	{
		if(cityTo == null || cityFrom == null || startDate.isEmpty() || finishDate.isEmpty())
			return null;
		
		return new Order(cityFrom, cityTo, startDate, finishDate);
	}
	
	/**
	 * Tworzy obiekt Order zamówienia zlecenia na daną trasę.
	 * @param cityTo - miasto docelowe
	 * @param cityFrom - miasto startowe
	 * @param startDate - data rozpoczęcia
	 * @param finishDate - data zakończenia
	 * @param manufactureID - identyfikator producenta
	 * @return Order zamówienia
	 * @author Kamil Zimny
	 */
	public Order buildOrder(City cityTo, City cityFrom ,String startDate, String finishDate, String manufactureID)
	{
		if(cityTo == null || cityFrom == null || startDate.isEmpty() || finishDate.isEmpty())
			return null;
		Order order = new Order(cityFrom, cityTo, startDate, finishDate);
		order.setManufacturerId(manufactureID);
		return order;
	}
}
