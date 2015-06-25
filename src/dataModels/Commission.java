package dataModels;

public class Commission {
	
	private int commissionId;
	private String startDatePlan;
	private String startDateReal;
	private String finishDatePlan;
	private String finishDateReal;
	private double transporterCost;
	private double commissionValue;
	private int vehicleCapacity;
	private int vehicleCapacity2;
	private String Manufacturer;
	private String cityA;
	private String cityB;
	private String transporter;
	private String routeName;
	
	public Commission(int commissionId, String startDatePlan,String startDateReal,String finishDatePlan, 
			String finishDateReal, double transporterCost, double commissionValue, int vehicleCapacity, 
			int vehicleCapacity2, String Manufacturer, String cityA,
			String cityB, String transporter, String routeName){
			
			this.commissionId = commissionId;
			this.startDatePlan = startDatePlan;
			this.startDateReal = startDateReal;
			this.finishDatePlan = finishDatePlan;
			this.finishDateReal = finishDateReal;
			this.transporterCost = transporterCost;
			this.commissionValue = commissionValue;
			this.vehicleCapacity = vehicleCapacity;
			this.vehicleCapacity2 = vehicleCapacity2;
			this.Manufacturer = Manufacturer;
			this.cityA = cityA;
			this.cityB = cityB;
			this.transporter = transporter;
			this.routeName = routeName;
		
	}
	
	public int getId(){
		return commissionId;
	}
	
	public String getStartDatePlan(){
		return startDatePlan;
	}
	
	public String getFinishDatePlan(){
		return finishDatePlan;
	}
	
	public double getTransporterCost(){
		return transporterCost;
	}
	
	public double getCommissionValue(){
		return commissionValue;
	}
	
	public int getVehicleCapacity(){
		return vehicleCapacity;
	}
	
	public int getVehcicleCapacity2(){
		return vehicleCapacity2;
	}
	
	public String getManufacturer(){
		return Manufacturer;
	}
	
	public String getCityA(){
		return cityA;
	}
	
	public String getCityB(){
		return cityB;
	}
	
	public String getTransporter(){
		return transporter;
	}
	
	public String getRouteName(){
		return routeName;
	}
	
	public String getStartDateReal(){
		return startDateReal;
	}
	
	public String getFinishDateReal(){
		return finishDateReal;
	}
	
	public void setId(int id){
		commissionId=id;
	}
	
	public void setStartDatePlan(String date){
		startDatePlan=date;
	}
	
	public void setFinishDatePlan(String date){
		finishDatePlan=date;
	}
	
	public void setTransporterCost(double cost){
		transporterCost=cost;
	}
	
	public void setCommissionValue(double value){
		commissionValue=value;
	}
	
	public void setVehicleCapacity(int capacity){
		 vehicleCapacity=capacity;
	}
	
	public void setVehcicleCapacity2(int capacity){
		 vehicleCapacity2=capacity;
	}
	
	public void setManufacturer(String manufacturer){
		 Manufacturer=manufacturer;
	}
	
	public void setCityA(String city){
		 cityA=city;
	}
	
	public void setCityB(String city){
		cityB=city;
	}
	
	public void setTransporter(String transporter){
		this.transporter=transporter;
	}
	
	public void setRouteName(String name){
		 routeName=name;
	}
	
	public void setStartDateReal(String date){
		startDateReal=date;
	}
	
	public void setFinishDateReal(String date){
		finishDateReal=date;
	}

}
