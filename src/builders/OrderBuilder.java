package builders;

import dataModels.City;
import dataModels.Order;

public class OrderBuilder 
{
	public OrderBuilder() {
		
	}
	
	public Order buildOrder(City cityTo, City cityFrom ,String startDate, String finishDate)
	{
		return new Order(cityFrom, cityTo, startDate, finishDate);
	}
}
