package dataModels;

/**
 * Reprezetuje przewoźnika i przechowuje dane dotyczące jego przeszłe transakcje
 * @author Łukasz Dudaszek
 *
 */
public class Transporter 
{
	/**
	 * id przewoźnika
	 */
	private int id_trans;
	
	/**
	 * kategoria rozmiaru
	 */
	private SizeCategory sizeCategory; 
	
	/**
	 * liczba zamowien
	 */
	private int number_of_orders; 
	
	/**
	 * koszt
	 */
	private double cost; 
	
	/**
	 * ładowność
	 */
	private int capacity; 
	
	/**
	 * pojemość
	 */
	private int volume;
	
	/**
	 * stosuenk dni_opoznienia/dni_przewozów
	 */
	private double delay;
	
	/**
	 * stosunek niezreal/zreal
	 */
	private double executed;
	
	/**
	 * nazwa przewoznika
	 */
	private String name;
	
	/**
	 * numer przewoznika
	 */
	private int phone_num;
	
	
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

	@Override
	public String toString()
	{
		return name+" :: koszt - "+cost+" :: l. zleceń - "+number_of_orders;
	}
	
	/**
	 * Porównianie ze względu na ładowność
	 * @param t1 pierwszy przewoźnik do porównania
	 * @param t2 drugi przewoźnik do porównania
	 * @return 0 jeśli t1.capacity == t2.capcity; wartość mniejszą od 0 jeśli t1.capacity < t2.capacity; and wartość większą 0 jeśli t1.capacity > t2.capacity
	 */
	public static int compareByCapacity(Transporter t1, Transporter t2)
	{
		return Integer.compare(t2.getCapacity(), t1.getCapacity());
	}
	
	/**
	 * Porównianie ze względu na liczbę zleceń
	 * @param t1 pierwszy przewoźnik do porównania
	 * @param t2 drugi przewoźnik do porównania
	 * @return 0 jeśli t1.number_of_orders == t2.number_of_orders; wartość mniejszą od 0 jeśli t1.number_of_orders < t2.number_of_orders; and wartość większą 0 jeśli t1.number_of_orders > t2.number_of_orders
	 */
	public static int compareByNumbrOfOrders(Transporter t1, Transporter t2)
	{
		return Integer.compare(t1.getNumber_of_orders(), t2.getNumber_of_orders());
	}
	
	/**
	 * Porównianie ze względu na koszt
	 * @param t1 pierwszy przewoźnik do porównania
	 * @param t2 drugi przewoźnik do porównania
	 * @return 0 jeśli t1.cost == t2.cost; wartość mniejszą od 0 jeśli t1.cost < t2.cost; and wartość większą 0 jeśli t1.cost > t2.cost
	 */
	public static int compareByCost(Transporter t1, Transporter t2)
	{
		return Double.compare(t1.getCost(), t2.getCost());
	}
	
	
	/**
	 * Porównianie ze względu na objętość
	 * @param t1 pierwszy przewoźnik do porównania
	 * @param t2 drugi przewoźnik do porównania
	 * @return 0 jeśli t1.volume == t2.volume; wartość mniejszą od 0 jeśli t1.volume < t2.volume; and wartość większą 0 jeśli t1.volume > t2.volume
	 */
	public static int compareByVolume(Transporter t1, Transporter t2)
	{
		return Integer.compare(t1.getVolume(), t2.getVolume());
	}
	
	/**
	 * Porównianie ze względu na opóźnienie
	 * @param t1 pierwszy przewoźnik do porównania
	 * @param t2 drugi przewoźnik do porównania
	 * @return 0 jeśli t1.delay == t2.delay; wartość mniejszą od 0 jeśli t1.delay < t2.delay; and wartość większą 0 jeśli t1.delay > t2.delay
	 */
	public static int compareByDelay(Transporter t1, Transporter t2)
	{
		return Double.compare(t1.getDelay(), t2.getDelay());
	}
	
	/**
	 * Porównianie ze względu na stosunek niezreal/zreal
	 * @param t1 pierwszy przewoźnik do porównania
	 * @param t2 drugi przewoźnik do porównania
	 * @return 0 jeśli t1.executed == t2.executed; wartość mniejszą od 0 jeśli t1.executed < t2.executed; and wartość większą 0 jeśli t1.executed > t2.executed
	 */
	public static int compareByExecuted(Transporter t1, Transporter t2)
	{
		return Double.compare(t1.getExecuted(), t2.getExecuted());
	}
	
}