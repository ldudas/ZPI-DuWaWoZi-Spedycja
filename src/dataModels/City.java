package dataModels;

public class City 
{
	/**
	 * Nazwa miasta
	 */
	private String city_name;
	/**
	 * Szerokość geograficzna miasta
	 */
	private double latitude;
	/**
	 * Długość geograficzna miasta
	 */
	private double longtitude;
	
	public City(String city_name, double latitude, double longtitude)
	{
		this.city_name = city_name;
		this.latitude = latitude;
		this.longtitude = longtitude;
	}
	
	public String getCityName()
	{
		return city_name;
	}
	
	public double getLatitude()
	{
		return latitude;
	}
	
	public double getLongtitude()
	{
		return longtitude;
	}
	
	public boolean isEquals(City city)
	{
		return ( city != null ) ? city.city_name.equals(city_name) : false;
	}
	
}
