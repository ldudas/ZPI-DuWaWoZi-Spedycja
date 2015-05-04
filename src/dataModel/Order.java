package dataModel;

public class Order {
	
	private String cityNameFrom;
	private String cityNameTo;
	private String startDate;
	private String finishDate;
	private String idManufacturer;
	private String idTrasnporter;
	
	public Order(String cityNameFrom, String cityNameTo,String startDate, String finishDate,String idManufacturer,String idTrasnporter)
	{
		this.cityNameFrom = cityNameFrom;
		this.cityNameTo = cityNameTo;
		this.startDate = startDate;
		this.finishDate = finishDate;
		this.idManufacturer = idManufacturer;
		this.idTrasnporter = idTrasnporter;
	}
	
	public String getCityNameFrom(){
		return cityNameFrom;
	}
	
	public String getCityNameTo(){
		return cityNameFrom;
	}
	
	public String getStartDate(){
		return startDate;
	}
	
	public String getFinishDate(){
		return finishDate;
	}
	
	public String idManufacturer(){
		return finishDate;
	}
	
	public String idTransporter(){
		return idTrasnporter;
	}
	

}
