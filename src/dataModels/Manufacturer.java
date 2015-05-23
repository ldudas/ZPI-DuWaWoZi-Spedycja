package dataModels;

import java.awt.Color;
import java.util.ArrayList;

public class Manufacturer 
{
	
	private String ID;
	private String name;
	private String phone;
	private String lastActivity;
	private double latitude;
	private double longitude;
	private double sumOfOrdersValue;
	private int sumOfDays;
	private int numberOfOrders;
	
	private int rankOfNumberOfOrders;
	private int rankOfDailyProfit;
	
	private String additionInfo;
	
	private ArrayList<Integer> monthActivity;
	private ArrayList<Color> monthActivityColors;
	
	
	public Manufacturer(String name,  double longitude, double latitude, String lastActivity,int numberOfOrders,
			double sumOfOrdersValue, int sumOfDays, String phone, String ID)
	{
		this.name = name;
		this.longitude = longitude;
		this.latitude = latitude;
		this.lastActivity = lastActivity;
		this.numberOfOrders = numberOfOrders;
		this.sumOfOrdersValue = sumOfOrdersValue;
		this.sumOfDays = sumOfDays;
		this.phone = phone;
		this.ID = ID;
		this.additionInfo = "Brak";
		this.monthActivity = new ArrayList<Integer>(12);
		this.monthActivityColors = new ArrayList<Color>(12);
	}
	
	
	public String getID()
	{
		return ID;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getPhone()
	{
		return phone;
	}
	
	public String getLastActivity()
	{
		return lastActivity;
	}
	
	public double getLatitude()
	{
		return latitude;
	}
	
	public double getLongtitude()
	{
		return longitude;
	}
	
	public double getSumOfOrdersValue()
	{
		return sumOfOrdersValue;
	}
	
	public int getSumOfDays()
	{
		return sumOfDays;
	}
	
	public int getNumberOfOrders()
	{
		return numberOfOrders;
	}
	
	public int getRankOfNumberOfOrders()
	{
		return rankOfNumberOfOrders;
	}
	
	public void setRankOfNumberOfOrders(int rank)
	{
		rankOfNumberOfOrders = rank;
	}
	
	public int getRankOfDailyProfit()
	{
		return rankOfDailyProfit;
	}
	
	public void setRankOfDailyProfit(int rank)
	{
		rankOfDailyProfit = rank;
	}
	
	public String getAdditionInfo()
	{
		return additionInfo;
	}
	
	public void setInfoAboutManufacturer(String info)
	{
		additionInfo = info;
	}
	
	public void setMonthsActivity(ArrayList<String> monthA)
	{
		for(int i=1;i<monthA.size(); i++)
		{
			monthActivity.add( Integer.parseInt(monthA.get(i)) );
		}
	}
	
	public int getMonthActivity(int monthNum)
	{ 
		return (monthNum > -1 && monthNum < 12) ? monthActivity.get(monthNum) : -1;
	}
	
	public void setMonthsActivityColors(ArrayList<Color> monthA)
	{
		System.out.println(monthActivity);
		for(int i=0;i<monthA.size(); i++)
		{
			monthActivityColors.add( monthA.get(i) );
		}
	}
	
	public Color getMonthActivityColor(int monthNum)
	{ 
		return (monthNum > -1 && monthNum < 12) ? monthActivityColors.get(monthNum) : null;
	}
	
	

}
