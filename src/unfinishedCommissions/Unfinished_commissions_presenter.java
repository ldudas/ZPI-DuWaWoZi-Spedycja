package unfinishedCommissions;

import java.util.ArrayList;

import javax.swing.JFrame;

import dataModels.User;
import interfaces.RoutePlanningPresenter;
import dataModels.Commission;


public class Unfinished_commissions_presenter {
	
	private Unfinished_commissions_model model;
	private Unfinished_commissions_view view;
	//private RoutePlanningPresenter route;
	
	public Unfinished_commissions_presenter(final Unfinished_commissions_model model,Unfinished_commissions_view view){
		this.model = model;
		this.view = view;
	}
	
	//public void set_route_presenter(final RoutePlanningPresenter r){
//		this.route = r;
//	}
	
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
	
	public void save_to_dataBase(int selected){
		model.save_to_dataBase(selected);
	}
	
	public void clearDataInModel()
	{
		model.clearData();
	}
	
	public void clearUnfinishedCommissionsFrame()
	{
		view.clearUnfinishedCommissionsFrame();
	}
}
