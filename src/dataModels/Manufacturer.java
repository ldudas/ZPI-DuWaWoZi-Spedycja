package dataModels;

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
	
	

}
