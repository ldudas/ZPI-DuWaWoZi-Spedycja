package unfinishedCommissions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import database.DataAccessObjectCommissions;
import database.DataAccessObjectFactory;
import dataModels.Commission;
import dataModels.User;

public class Unfinished_commissions_model {
	
	private ArrayList<Commission> result;
	private DataAccessObjectCommissions dao;
	SimpleDateFormat sdfr;
	boolean flag=false;
	
	public Unfinished_commissions_model(){
		result = new ArrayList<Commission>();
		DataAccessObjectFactory factory = new DataAccessObjectFactory();
		dao = factory.getDataAccessObjectCommissions();
		sdfr = new SimpleDateFormat("yyyy-MM-dd");
	}
	
	public ArrayList<Commission> getResult(){
		if(flag){
			result.clear();
		}
		ArrayList<ArrayList<Object>> result = dao.getUnfinishedCommissions();
		for(ArrayList<Object> row: result){
			this.result.add(new Commission((int)row.get(0),sdfr.format(row.get(1)),sdfr.format(row.get(2)),sdfr.format(row.get(3)),
					sdfr.format(row.get(4)),(double)row.get(5),(double)row.get(6),(int)row.get(7),(int)row.get(8),(String)row.get(9),
					(String)row.get(10),(String)row.get(11),(String)row.get(12),(String)row.get(13)));
			}	
		flag=true;
		return this.result;
		}
	
	public ArrayList<Commission> returnResult(){
		return result;
	}
	
	public void setExternalDatabaseConnectionProperty(User currentLoggedUser) throws Exception
	{
		if( currentLoggedUser != null )
		{
			dao.setExternalDatabaseConnectionProperty(currentLoggedUser.getServerAddress(), 
				currentLoggedUser.getServerPort(),currentLoggedUser.getDatabaseName(), 
				currentLoggedUser.getDatabaseLogin(), currentLoggedUser.getDatabasePassword());
		}
		else
			throw new Exception("Użytkownik nie został zalogowany."); //nie powinno się zdarzyć.
	}
	
	public void clearData()
	{
		if (result != null)
			result.clear();
	}
	
	public void save_to_dataBase(int selected, boolean if_end){
		dao.saveCommission(result.get(selected),if_end);
	}
	
}