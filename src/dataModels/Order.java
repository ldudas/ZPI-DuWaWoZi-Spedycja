package dataModels;

/**
 * Klasa Zamówienia tworzonego 
 * podczas działania aplikacji
 * @author Kamil Zimny
 *
 */
public class Order 
{
	/**
	 * Data rozpoczęcia zamówienia/zlecenia
	 */
	private String startDate;
	
	/**
	 * Data zakończenia zamówienia/zlecenia
	 */
	private String finishDate;
	
	/**
	 * Identyfikator producenta
	 */
	private String idManufacturer;
	
	/**
	 * Identyfikator przewoźnika
	 */
	private String idTrasnporter;
	
	/**
	 * Miasto z którego zaczynamy zlecenie
	 */
	private City cityFrom;
	
	/**
	 * Miasto w którym kończymy zlecenie
	 */
	private City cityTo;
	
	public Order(String cityNameFrom, String cityNameTo,String startDate, String finishDate,String idManufacturer,String idTrasnporter)
	{
		this.startDate = startDate;
		this.finishDate = finishDate;
		this.idManufacturer = idManufacturer;
		this.idTrasnporter = idTrasnporter;
	}
	
	public Order( City cityFrom, City cityTo ,String startDate, String finishDate)
	{
		this.cityFrom = cityFrom;
		this.cityTo = cityTo;
		this.startDate = startDate;
		this.finishDate = finishDate;
	}
	
	public String toString()
	{
		return startDate + " " + cityFrom.getCityName() + " " + finishDate + " " + cityTo.getCityName() + " " + idManufacturer + " " + idTrasnporter;
	}
	
	
	public void setManufacturerId(String idMan)
	{
		idManufacturer = idMan;
	}
	
	public void setTransporterID(String idTrans)
	{
		idTrasnporter = idTrans;
	}
	
	public String getStartDate()
	{
		return startDate;
	}
	
	public String getFinishDate()
	{
		return finishDate;
	}
	
	public String getIdManufacturer()
	{
		return idManufacturer;
	}
	
	public String getIdTransporter()
	{
		return idTrasnporter;
	}
	
	public City getCityTo()
	{
		return cityTo;
	}
	
	public City getCityFrom()
	{
		return cityFrom;
	}
	

}
