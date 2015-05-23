package mapOverlay;

import com.esri.map.MapOverlay;

 public class MyOverlay extends MapOverlay 
 {
	  /**
	 * 
	 */
	  private static final long serialVersionUID = 1L;

	  @Override
	  public void onMouseMoved(java.awt.event.MouseEvent event) 
	  {
	        // TODO Auto-generated method stub
		  System.out.println("Move!");
		//  event.getComponent().
		  //super.onMouseDragged(event);
	  }
 
	  @Override
	  public void onMouseClicked(java.awt.event.MouseEvent event) 
	  {
		  // TODO Auto-generated method stub
		  System.out.println("Click!");
		  super.onMouseClicked(event);
	  }
}

