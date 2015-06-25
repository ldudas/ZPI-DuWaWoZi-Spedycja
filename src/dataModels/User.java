package dataModels;

/**
 * Klasa Użytkownika tworzonego podczas 
 * rejestracji w systemie i zapisywanego 
 * w bazie lokalnej
 * @author Kamil
 *
 */
public class User 
{
	/**
	 * Login użytkownika tworzony podczas
	 * rejestracji nowego użytkownika
	 */
	private String login;
	
	/**
	 * Hasło tworzony podczas
	 * rejestracji nowego użytkownika 
	 */
	private String password;
	
	/**
	 * Adres serwera zewnętrznego
	 */
	private String serverAddress;
	
	/**
	 * Port serwera zewnętrznego
	 */
	private String serverPort;
	
	/**
	 * Nazwa bazy danej na serwerze
	 */
	private String databaseName;
	
	/**
	 * Login do bazy danych na serwerze 
	 */
	private String databaseLogin;
	
	/**
	 * Hasło do bazy danej na serwerze
	 */
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
