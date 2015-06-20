package dataModels;

public class Order 
{
	
//	private String cityNameFrom;
//	private String cityNameTo;
	private String startDate;
	private String finishDate;
	private String idManufacturer;
	private String idTrasnporter;
	
	private City cityFrom;
	private City cityTo;
	
	public Order(String cityNameFrom, String cityNameTo,String startDate, String finishDate,String idManufacturer,String idTrasnporter)
	{
	//	this.cityNameFrom = cityNameFrom;
	//	this.cityNameTo = cityNameTo;
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
