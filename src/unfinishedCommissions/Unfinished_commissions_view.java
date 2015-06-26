package unfinishedCommissions;

import java.text.ParseException;
import java.util.ArrayList;
import javax.swing.JFrame;
import jpanel.UnfinishedCommissions.Selected_commission_jpanel;
import jpanel.UnfinishedCommissions.Unfinished_commissions_jpanel;
import dataModels.Commission;

public class Unfinished_commissions_view {
	
	private Unfinished_commissions_presenter presenter;
	private JFrame frame;
	private Unfinished_commissions_jpanel unfinished_commissionsJPanel;
	private Selected_commission_jpanel selected_commission_JPanel;
	
	
	public void initialize(JFrame mainFrame, ArrayList<Commission> res)
	{
		frame = mainFrame; 
		frame.setTitle("Edycja danych");
		unfinished_commissionsJPanel = new Unfinished_commissions_jpanel(res,this);
		frame.add(unfinished_commissionsJPanel);
		frame.setBounds(1, 150, 1350, 450);
		frame.invalidate();
		frame.validate();
	}
	
	public void change_to_selected_commission(int selected, ArrayList<Commission> result ) throws ParseException{
		frame.remove(unfinished_commissionsJPanel);
		selected_commission_JPanel=new Selected_commission_jpanel(selected, result,this);
		frame.add(selected_commission_JPanel);
		frame.invalidate();
		frame.validate();
	}
	

	public void setPresenter(final Unfinished_commissions_presenter presenter){
		this.presenter = presenter;
	}

	public void save_change(){
		presenter.save_change();
	}
	
	public void save_change_with_result(ArrayList <Commission> res){
		selected_commission_JPanel.save_change(res);
	}
	
	public void save_to_dataBase(int selected, boolean if_end){
		presenter.save_to_dataBase(selected,if_end);
	}
	
	public JFrame getFrame(){
		return frame;
	}
	
	public void clearUnfinishedCommissionsFrame()
	{
		if(unfinished_commissionsJPanel != null)
			frame.remove(unfinished_commissionsJPanel);
		if(selected_commission_JPanel != null )
			frame.remove(selected_commission_JPanel);
	}
	
	public void change_one_commission_to_many(){
		frame.remove(selected_commission_JPanel);
		frame.add(unfinished_commissionsJPanel);
		frame.invalidate();
		frame.validate();
	}
	
	public void update(boolean flag){
		presenter.update(flag);
	}
	
	public void update_with_data(ArrayList<Commission> res){
		unfinished_commissionsJPanel.update_with_data(res);
	}
	
	public void filtr(String route_name){
		presenter.filtr(route_name);
	}
	
	public void filtr_with_data(ArrayList<Commission> res, String route_name){
		unfinished_commissionsJPanel.filtr(res, route_name);
	}
	
	public void delete_filtr(){
		presenter.delete_filtr();
	}
	
	public void delete_filtr_with_data(ArrayList<Commission> res){
		unfinished_commissionsJPanel.update_with_data(res);
	}
	
	public void change_flag(){
		unfinished_commissionsJPanel.change_flag();
	}
}
