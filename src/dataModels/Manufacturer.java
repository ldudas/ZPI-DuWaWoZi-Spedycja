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
	private ArrayList<Color> quarterActivityColors;
	
	
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
		this.quarterActivityColors = new ArrayList<Color>(4); 
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
		for(int i=0;i<monthA.size(); i++)
		{
			monthActivityColors.add( monthA.get(i) );
		}
	}
	
	public Color getMonthActivityColor(int monthNum)
	{ 
		return (monthNum > -1 && monthNum < 12) ? monthActivityColors.get(monthNum) : new Color(0,0,0);
	}
	
	public void setQuarterActivityColor(ArrayList<Color> quarterA)
	{
		for(int i=0;i<quarterA.size(); i++)
		{
			quarterActivityColors.add( quarterA.get(i) );
		}
	}
	
	/***
	 * int quarter = 0 -> Wiosna
	 * int quarter = 1 -> Lato
	 * int quarter = 2 -> Jesien
	 * int quarter = 3 -> Zima
	 * @return Color of activity in quarter
	 * @author Kamil Zimny
	 */
	public Color getQuarterActivityColor(int quarter)
	{
		return (quarter > -1 && quarter < 4) ? quarterActivityColors.get(quarter) : new Color(0,0,0);
	}
	
	/***
	 * int quarter = 0 -> Wiosna
	 * int quarter = 1 -> Lato
	 * int quarter = 2 -> Jesien
	 * int quarter = 3 -> Zima
	 * @return int  activity in quarter
	 * @author Kamil Zimny
	 */
	public int getQuarterActivity(int quarter)
	{
		switch( quarter)
		{
		    case 0: //Wiosna
		    	return monthActivity.get(2) + monthActivity.get(3) + monthActivity.get(4); //Marzec,Kwiecen,Maj
		    case 1: //Lato
		    	return monthActivity.get(5) + monthActivity.get(6) + monthActivity.get(7); //Czerwiec,Lipiec, Sierpien
		    case 2: //Jesien
		    	return monthActivity.get(8) + monthActivity.get(9) + monthActivity.get(10); //Wrzesien,Pazdziernik, Listopad
		    case 3: //Zima
		    	return monthActivity.get(11) + monthActivity.get(0) + monthActivity.get(1); //Grudzien, Styczen, Luty
		    default : 
		    		return 0; //nie powinno sie zdarzyc
		}
	}
	

}
