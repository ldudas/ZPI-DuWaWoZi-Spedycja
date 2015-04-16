package maps;

import javax.swing.JTabbedPane;

import com.esri.map.JMap;

public class VMVView {
	
	JTabbedPane tab;
	
	public VMVView(){
		
	}
	
	public void set_tab(JTabbedPane tab_){
		tab=tab_;
	}
	
	public void add_map_to_tab(JMap mapka){
		tab.addTab("Trasa",mapka);
	}

}
