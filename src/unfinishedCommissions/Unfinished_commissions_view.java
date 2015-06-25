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
	
	public Unfinished_commissions_view()
	{
	}
	
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
	
	public void change_to_selected_commission(int selected, ArrayList<Commission> result) throws ParseException{
		frame.getContentPane().removeAll();
		selected_commission_JPanel=new Selected_commission_jpanel(selected, result,this);
		frame.getContentPane().add(selected_commission_JPanel);
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
	
	public void save_to_dataBase(int selected){
		presenter.save_to_dataBase(selected);
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
	
	
}
