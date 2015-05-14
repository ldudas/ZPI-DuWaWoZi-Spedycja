package dataModels;

public class Transporter 
{
	
	private int id_trans;
	private SizeCategory sizeCategory; //kategoria rozmiaru
	private int number_of_orders; //liczba zamowien
	private double cost; //koszt
	private int capacity; //ładowność
	private int volume;//pojemość
	private int delay; //opoznienia
	private double executed; //stosunek niezreal/zreal
	
	
	public Transporter(int id_trans,SizeCategory sizeCategory, int number_of_orders, double cost, int capacity, int volume, int delay, double executed)
	{
		this.id_trans=id_trans;
		this.sizeCategory=sizeCategory;
		this.number_of_orders = number_of_orders;
		this.cost = cost;
		this.capacity=capacity;
		this.volume=volume;
		this.delay = delay;
		this.executed = executed;
	}
	
	public int getId_trans() 
	{
		return id_trans;
	}
	public void setId_trans(int id_trans) 
	{
		this.id_trans = id_trans;
	}
	public SizeCategory getSizeCategory()
	{
		return sizeCategory;
	}
	public void setSizeCategory(SizeCategory sizeCategory) 
	{
		this.sizeCategory = sizeCategory;
	}
	public int getNumber_of_orders() 
	{
		return number_of_orders;
	}
	public void setNumber_of_orders(int number_of_orders) 
	{
		this.number_of_orders = number_of_orders;
	}
	public double getCost() 
	{
		return cost;
	}
	public void setCost(double cost) 
	{
		this.cost = cost;
	}
	public int getCapacity() 
	{
		return capacity;
	}
	public void setCapacity(int capacity) 
	{
		this.capacity = capacity;
	}
	public int getVolume() 
	{
		return volume;
	}
	public void setVolume(int volume) 
	{
		this.volume = volume;
	}
	public int getDelay() 
	{
		return delay;
	}
	public void setDelay(int delay) 
	{
		this.delay = delay;
	}
	public double getExecuted() 
	{
		return executed;
	}
	public void setExecuted(double executed) 
	{
		this.executed = executed;
	}
	
	public String toString(){
		return id_trans+ " - " + capacity;
	}
	
	public static int compareByCapacity(Transporter t1, Transporter t2)
	{
		return Integer.compare(t2.getCapacity(), t1.getCapacity());
	}
	
}
