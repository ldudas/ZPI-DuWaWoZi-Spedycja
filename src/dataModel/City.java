package dataModel;

public class City {

	private String city_name;
	double cor_1;
	double cor_2;
	
	public City(String city_name, double cor_1, double cor_2)
	{
		this.city_name=city_name;
		this.cor_1=cor_1;
		this.cor_2=cor_2;
	}
	
	public String getCityName()
	{
		return city_name;
	}
	
	public double getFirstCordiante()
	{
		return cor_1;
	}
	
	public double getSecondCordiante()
	{
		return cor_2;
	}
	
	@Override
	public boolean equals(Object o)
	{
		return ((City) o).getCityName().equals(city_name);
	}
	
}
