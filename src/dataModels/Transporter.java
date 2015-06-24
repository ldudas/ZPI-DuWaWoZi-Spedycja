package dataModels;

public class Transporter 
{
	
	private int id_trans;
	private SizeCategory sizeCategory; //kategoria rozmiaru
	private int number_of_orders; //liczba zamowien
	private double cost; //koszt
	private int capacity; //ładowność
	private int volume;//pojemość
	private double delay; //stosuenk dni_opoznienia/dni_przewozów
	private double executed; //stosunek niezreal/zreal
	private String name; // nazwa przewoznika
	private int phone_num; //numer przewoznika
	
	
	public Transporter(int id_trans,SizeCategory sizeCategory, int number_of_orders, double cost, int capacity, int volume, double delay, double executed, String name, int phone_num)
	{
		this.id_trans=id_trans;
		this.sizeCategory=sizeCategory;
		this.number_of_orders = number_of_orders;
		this.cost = cost;
		this.capacity=capacity;
		this.volume=volume;
		this.delay = delay;
		this.executed = executed;
		this.name = name;
		this.phone_num = phone_num;
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
	public double getDelay() 
	{
		return delay;
	}
	public void setDelay(double delay) 
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
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPhone_num() {
		return phone_num;
	}

	public void setPhone_num(int phone_num) {
		this.phone_num = phone_num;
	}

	public String toString(){
		return name+" :: koszt - "+cost+" :: l. zleceń - "+number_of_orders;
	}
	
	public static int compareByCapacity(Transporter t1, Transporter t2)
	{
		return Integer.compare(t2.getCapacity(), t1.getCapacity());
	}
	
	public static int compareByNumbrOfOrders(Transporter t1, Transporter t2)
	{
		return Integer.compare(t1.getNumber_of_orders(), t2.getNumber_of_orders());
	}
	
	public static int compareByCost(Transporter t1, Transporter t2)
	{
		return Double.compare(t1.getCost(), t2.getCost());
	}
	
	public static int compareByVolume(Transporter t1, Transporter t2)
	{
		return Integer.compare(t1.getVolume(), t2.getVolume());
	}
	
	public static int compareByDelay(Transporter t1, Transporter t2)
	{
		return Double.compare(t1.getDelay(), t2.getDelay());
	}
	
	public static int compareByExecuted(Transporter t1, Transporter t2)
	{
		return Double.compare(t1.getExecuted(), t2.getExecuted());
	}
	
}
