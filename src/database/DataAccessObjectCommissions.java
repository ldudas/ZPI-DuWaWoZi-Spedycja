package database;

import java.util.ArrayList;
import dataModels.Commission;
import exceptions.DatabaseConnectionExeption;

public class DataAccessObjectCommissions {
	
	private DatabaseConnector databaseConnector;
	
	public DataAccessObjectCommissions()
	{}
	
	public void setExternalDatabaseConnectionProperty(String serverAddress,String serverPort,String databaseName,String databaseLogin,String databasePassword)
	{
		databaseConnector = new DatabaseConnector(serverAddress, serverPort, databaseName, databaseLogin, databasePassword);
	}
	
	public ArrayList<ArrayList<Object>> getUnfinishedCommissions()
	{
		String query = "SELECT Z.id_zlecenia, Z.data_rozp_plan, Z.data_rozp_rzecz, Z.data_zak_plan, "
				+ "Z.data_zak_rzecz, Z.koszt_przew, Z.wartosc_zlec, Z.pojemnosc_poj, Z.ladownosc_poj, "
				+ "P.nazwa_prod, M.nazwa_miasta Z, M1.nazwa_miasta DO, PRZ.nazwa_przew, "
						+ "T.nazwa_trasy FROM Zlecenia Z LEFT JOIN Producenci P ON Z.id_prod = P.id_prod "
						+ "LEFT JOIN Miasta M ON Z.z_miasta = M.id_miasta LEFT JOIN "
						+ "Miasta M1 ON Z.do_miasta = M1.id_miasta "
						+ "LEFT JOIN Przewoznicy PRZ ON Z.id_przew = PRZ.id_przew "
						+ "LEFT JOIN Trasy_przewoznikow T ON Z.id_trasy = T.id_trasy "
						+ "WHERE Z.czy_zrealizowano = 0 ORDER BY Z.id_zlecenia;";		
		
		ArrayList<ArrayList<Object>> resultOfQuery = null;
		try 
		{
			resultOfQuery = databaseConnector.getResultOfMySqlQuery(query);
		} 
		catch (DatabaseConnectionExeption e) 
		{
			e.printStackTrace();
		}
		
		return resultOfQuery;
	}
	
	public void saveCommission(Commission item, boolean if_end)
	{
		
		
		String query = "UPDATE Zlecenia SET data_rozp_rzecz = '" + item.getStartDateReal() + "', data_zak_rzecz = '" + item.getFinishDateReal() + "', koszt_przew = '" + 
						item.getTransporterCost() + "', wartosc_zlec = '" + item.getCommissionValue() + "', pojemnosc_poj = '" + item.getVehicleCapacity() +  "', ladownosc_poj = '" + 
						item.getVehcicleCapacity2() +"' WHERE id_zlecenia = '" + item.getId() + "';";   
	
		String query2 = "UPDATE Zlecenia SET data_rozp_rzecz = '" + item.getStartDateReal() + "', data_zak_rzecz = '" + item.getFinishDateReal() + "', koszt_przew = '" + 
						item.getTransporterCost() + "', wartosc_zlec = '" + item.getCommissionValue() + "', pojemnosc_poj = '" + item.getVehicleCapacity() +  "', ladownosc_poj = '" + 
						item.getVehcicleCapacity2() +"', czy_zrealizowano = '" + 1 + "' WHERE id_zlecenia = '" + item.getId() + "';"; 
		try 
		{
			if(if_end){
				databaseConnector.saveDataToDatabase(query2);
			}
			else{
				databaseConnector.saveDataToDatabase(query);
			}
			
		} 
		catch (DatabaseConnectionExeption e) 
		{
			e.printStackTrace();
		}
		
		
	}

}
