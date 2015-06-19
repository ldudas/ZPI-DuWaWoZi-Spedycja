package builders;

import dataModels.City;
import dataModels.Order;

public class OrderBuilder 
{
	public OrderBuilder() 
	{
		
	}
	
	public Order buildOrder(City cityTo, City cityFrom ,String startDate, String finishDate)
	{
		if(cityTo == null || cityFrom == null || startDate.isEmpty() || finishDate.isEmpty())
			return null;
		
		return new Order(cityFrom, cityTo, startDate, finishDate);
	}
	
	public Order buildOrder(City cityTo, City cityFrom ,String startDate, String finishDate, String manufactureID)
	{
		if(cityTo == null || cityFrom == null || startDate.isEmpty() || finishDate.isEmpty())
			return null;
		Order order = new Order(cityFrom, cityTo, startDate, finishDate);
		order.setManufacturerId(manufactureID);
		return order;
	}
}
