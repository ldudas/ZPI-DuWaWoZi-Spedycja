package dataModels;

public class User 
{
	private String login;
	private String password;
	
	private String serverAddress;
	private String serverPort;
	private String databaseName;
	private String databaseLogin;
	private String databasePassword;
	
	public User(String log,String pass,String serAdd,String serPor,String datNam,String datLog,String datPass)
	{
		this.login = log;
		this.password = pass;
		this.serverAddress = serAdd;
		this.serverPort = serPor;
		this.databaseName = datNam;
		this.databaseLogin = datNam;
		this.databasePassword = datPass;
	}
	
	public String getLogin()
	{
		return login;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public String getServerAddress()
	{
		return serverAddress;
	}
	
	public String getServerPort()
	{
		return serverPort;
	}
	
	public String getDatabaseName()
	{
		return databaseName;
	}
	
	public String getDatabaseLogin()
	{
		return databaseLogin;
	}
	
	public String getDatabasePassword()
	{
		return databasePassword;
	}
	
}
