package visualisations;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import com.esri.map.JMap;

public class VisualisationPathView {

  private JFrame window;
  private JMap map;

  public VisualisationPathView() {
    window = new JFrame();
    window.setSize(800, 600);
    window.setLocationRelativeTo(null); // center on screen
    window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    window.getContentPane().setLayout(new BorderLayout(0, 0));

    // dispose map just before application window is closed.
    window.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent windowEvent) {
        super.windowClosing(windowEvent);
        map.dispose();
      }
    });

   
    
  }
  
  public void openWindowWithMap(JMap mapa){
	  
	  if(map!=null){
	  window.getContentPane().remove(map);
	  }
	  
	  map = mapa;
	  window.getContentPane().add(map);
	  window.setVisible(true);
  }
  

}
