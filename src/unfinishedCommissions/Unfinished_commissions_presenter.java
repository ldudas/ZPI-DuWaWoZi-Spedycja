package unfinishedCommissions;

import java.util.ArrayList;
import javax.swing.JFrame;
import dataModels.User;
import dataModels.Commission;


public class Unfinished_commissions_presenter {
	
	private Unfinished_commissions_model model;
	private Unfinished_commissions_view view;
	
	public Unfinished_commissions_presenter(final Unfinished_commissions_model model,Unfinished_commissions_view view){
		this.model = model;
		this.view = view;
	}

	public void startCommissionsEdition(JFrame mainFrame)
	{
		view.initialize(mainFrame,model.getResult());
		
	}
	
	public void setExternalDatabaseConnectionProperty(User currentLoggedUser) throws Exception
	{
		model.setExternalDatabaseConnectionProperty(currentLoggedUser);
	}
	
	public void save_change(){
		save_change_with_result(model.returnResult());
	}
	
	public void save_change_with_result(ArrayList <Commission> res){
		view.save_change_with_result(res);
	}
	
	public void save_to_dataBase(int selected, boolean if_end){
		model.save_to_dataBase(selected, if_end);
	}
	
	public void clearDataInModel()
	{
		model.clearData();
	}
	
	public void clearUnfinishedCommissionsFrame()
	{
		view.clearUnfinishedCommissionsFrame();
	}
	
	public void update(boolean flag){
		if(flag){
			view.update_with_data(model.getResult());
		}
		else{
			view.update_with_data(model.returnResult());
		}
	}
	
	public void filtr(String route_name){
		view.filtr_with_data(model.returnResult(),route_name);
	}
	
	public void delete_filtr(){
		view.delete_filtr_with_data(model.returnResult());
	}
	
}
