package database;

public class DataAccessObjectTransportersVisualisation 
{

	private DatabaseConnector databaseConnector;
	
	public DataAccessObjectTransportersVisualisation()
	{}
	
	public void setExternalDatabaseConnectionProperty(String serverAddress,String serverPort,String databaseName,String databaseLogin,String databasePassword)
	{
		databaseConnector = new DatabaseConnector(serverAddress, serverPort, databaseName, databaseLogin, databasePassword);
	}
}
