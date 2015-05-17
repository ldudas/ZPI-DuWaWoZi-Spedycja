package interfaces;

import java.util.ArrayList;

import dataModels.Order;
import database.DataAccessObjectRoutePlanning;


public class RoutePlanningModel 
{
	private ArrayList<Order> ordersData;
	private DataAccessObjectRoutePlanning dao_routePlanning;
	
	public RoutePlanningModel()
	{
		ordersData = new ArrayList<Order>();
		dao_routePlanning = new DataAccessObjectRoutePlanning();
	}
	
	
	public ArrayList<String> getAllCityNames()
	{
		return dao_routePlanning.getAllCityNames();
	}
	
	public void addNewOrder(Order order)
	{
		ordersData.add(order);
	}
	
	public Order getLastOrder()
	{
		return ordersData.get(ordersData.size() - 1);
	}
	
	public String [] getCityCoordinates(final String cityName)
	{
		return dao_routePlanning.getCityCoordinates(cityName);
	}
	
	/**
	 * Usuwa ostatnio dodane zlecenie, nie bierze pod uwage pierwszego zlecenia ( nie usuwa go)
	 * @author Kamil Zimny
	 */
	public void removeLastOrder()
	{
		if( ordersData.size() > 0 )
			ordersData.remove(ordersData.size() - 1);
	}
}