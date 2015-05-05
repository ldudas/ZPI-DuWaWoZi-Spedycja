/* Copyright 2014 Esri
 
All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.client.toolkit;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.esri.client.toolkit.utilities.AWTUtilitiesWrapper;

/**
 * @deprecated From 10.2.3.
 */
@Deprecated
public class JToolDialog extends JDialog {
  private static final long serialVersionUID = 1L;
  
  public static void main(String[] args) {
    try {
      JToolDialog dialog = new JToolDialog();
      dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
      Container contentPane = dialog.getContentPane();
      contentPane.setLayout(new BorderLayout());
      contentPane.add(new JPanel(), BorderLayout.CENTER);
      dialog.setBounds(50, 50, 450, 300);
      dialog.setVisible(true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Class for handling mouse messages. Used to start dragging of the dialog
   * when a mouse down event is received. The mouse drag event is used to
   * update the drawing of the dialog.
   * 
   */
  public class ToolDialogMouseListener implements MouseMotionListener,
      MouseListener {

    @Override
    public void mouseDragged(MouseEvent e) {
      handleDrag(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
      handleMouseEnter(e);
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
      beginDrag(e);
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    }

  }

  protected ToolDialogMouseListener _mouseMotionListener = new ToolDialogMouseListener();
  private Point _previousDragPoint;
  protected JPanel _internalContentPanel;
  protected int _edgePadding = 10;
  private int _opacity = 200;
  protected int _doubleEdgePadding = _edgePadding * 2;
  private int _outCode;
  public JToolDialog() {
    super();
    init();
  }

  public JToolDialog(Frame owner) {
    super(owner);
    init();
  }

  public JToolDialog(Dialog owner) {
    super(owner);
    init();
  }

  public JToolDialog(Window owner) {
    super(owner);
    init();
  }

  public JToolDialog(Frame owner, boolean modal) {
    super(owner, modal);
    init();
  }

  public JToolDialog(Frame owner, String title) {
    super(owner, title);
    init();
  }

  public JToolDialog(Dialog owner, boolean modal) {
    super(owner, modal);
    init();
  }

  public JToolDialog(Dialog owner, String title) {
    super(owner, title);
    init();
  }

  public JToolDialog(Window owner, ModalityType modalityType) {
    super(owner, modalityType);
    init();
  }

  public JToolDialog(Window owner, String title) {
    super(owner, title);
    init();
  }

  public JToolDialog(Frame owner, String title, boolean modal) {
    super(owner, title, modal);
    init();
  }

  public JToolDialog(Dialog owner, String title, boolean modal) {
    super(owner, title, modal);
    init();
  }

  public JToolDialog(Window owner, String title, ModalityType modalityType) {
    super(owner, title, modalityType);
    init();
  }

  public JToolDialog(Frame owner, String title, boolean modal,
      GraphicsConfiguration gc) {
    super(owner, title, modal, gc);
    init();
  }

  public JToolDialog(Dialog owner, String title, boolean modal,
      GraphicsConfiguration gc) {
    super(owner, title, modal, gc);
    init();
  }

  public JToolDialog(Window owner, String title, ModalityType modalityType,
      GraphicsConfiguration gc) {
    super(owner, title, modalityType, gc);
    init();
  }

  /**
   * Get the size of the padding between the callout pane and the edge of the
   * dialog.
   * 
   * @return Gap between the callout pane and the dialog edge.
   */
  public int getEdgePadding() {
    return _edgePadding;
  }

  /**
   * Set the size of the padding between the callout pane and the edge of the
   * dialog.
   * 
   * @param edgePadding
   *            Gap between the callout pane and the dialog edge.
   */
  public void setEdgePadding(int edgePadding) {
    _edgePadding = edgePadding;
    _doubleEdgePadding = _edgePadding * 2;
  }

  /**
   * Get the current opacity setting for the area of the dialog outside the
   * callout pane (including the callout tail).
   * 
   * @return Current opacity setting for the dialog: a value from 0 (fully
   *         transparent) to 255 (fully opaque).
   */
  public float getOpacity() {
    return _opacity;
  }

  /**
   * Set the current opacity setting for the area of the dialog outside the
   * callout pane (including the callout tail).
   * 
   * @param opacity
   *            Opacity value between 0 (fully transparent) and 255 (fully
   *            opaque).
   */
  public void setOpacity(int opacity) {
    _opacity = opacity;
  }

  @Override
  public void setBounds(int x, int y, int width, int height) {
    super.setBounds(x, y, width, height);

    if (_internalContentPanel != null) {
      _internalContentPanel.setBounds(_edgePadding, _edgePadding, width
          - _doubleEdgePadding, height - _doubleEdgePadding);
    }
  }

  @Override
  public void setBounds(Rectangle rect) {
    super.setBounds(rect);

    if (_internalContentPanel != null) {
      _internalContentPanel.setBounds(new Rectangle(_edgePadding,
          _edgePadding, (int) (rect.getWidth() - _doubleEdgePadding),
          (int) (rect.getHeight() - _doubleEdgePadding)));
    }
  }

  /* (non-Javadoc)
   * @see javax.swing.JDialog#getContentPane()
   */
  @Override
  public Container getContentPane() {
    Container retVal = null;
  
    if (_internalContentPanel != null) {
      retVal = _internalContentPanel;
    } else {
      _internalContentPanel = new JPanel();
      Container contentPane = super.getContentPane();
      
      // Set this dialog's content pane to use absolute positioning. We need
      // to do this as the location of the callout panel is set explicitly to
      // give the illusion of a dialog being moved.
      contentPane.setLayout(null);
      contentPane.add(_internalContentPanel);
      retVal = _internalContentPanel;
    }
  
    return retVal;
  }

  /**
   * 
   */
  protected void init() {
    // Turn off dialog decoration otherwise setting the window shape
    // will not work.
    setUndecorated(true);
  
    // Use reflection to access methods for setting window shape and
    // opacity.	
    if(AWTUtilitiesWrapper.isTranslucencySupported(AWTUtilitiesWrapper.PERPIXEL_TRANSLUCENT) &&
        AWTUtilitiesWrapper.isTranslucencyCapable(getGraphicsConfiguration())){
      AWTUtilitiesWrapper.setWindowOpaque(this, false);
    }
  
    JComponent contentPane = (JComponent) getContentPane();
    int doubleEdgePadding = _edgePadding * 2;
    contentPane.setBounds(_edgePadding, _edgePadding, getWidth() - doubleEdgePadding, getHeight() - doubleEdgePadding);
    contentPane.setBorder(null);
    contentPane.setLayout(new BorderLayout(0, 0));
    
    addMouseListener(_mouseMotionListener);
    addMouseMotionListener(_mouseMotionListener);
  
  }

  /**
   * Handle mouse enter events by determining whether or not the mouse falls
   * in the dialog border area. If the dialog is not resizable and the cursor
   * falls within the border area, the cursor is changed to a move cursor to
   * indicate that a drag will move the dialog. If the dialog is resizable
   * and the cursor falls within the border area, the code will determine
   * which resize cursor to set based on which edge or corner the mouse is
   * over. 
   * 
   * @param event
   */
  protected void handleMouseEnter(MouseEvent event) {
    int outCode = _internalContentPanel.getBounds().outcode(event.getPoint());
    _internalContentPanel.setCursor(Cursor.getDefaultCursor());
    Cursor cursor = Cursor.getDefaultCursor();
    
    if(isResizable()){
      if ((outCode & Rectangle2D.OUT_LEFT) == Rectangle2D.OUT_LEFT) {
        if((outCode & Rectangle2D.OUT_TOP) == Rectangle2D.OUT_TOP){
          cursor = Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR);				
        }else if((outCode & Rectangle2D.OUT_BOTTOM) == Rectangle2D.OUT_BOTTOM){
          cursor = Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR);					
        }else{
          cursor = Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR);
        }
      }else if (((outCode & Rectangle2D.OUT_RIGHT) == Rectangle2D.OUT_RIGHT)) {
        if((outCode & Rectangle2D.OUT_TOP) == Rectangle2D.OUT_TOP){
          cursor = Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR);				
        }else if((outCode & Rectangle2D.OUT_BOTTOM) == Rectangle2D.OUT_BOTTOM){
          cursor = Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR);					
        }else{
          cursor = Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);
        }
        
      }else if (((outCode & Rectangle2D.OUT_TOP) == Rectangle2D.OUT_TOP)) {
        if((outCode & Rectangle2D.OUT_LEFT) == Rectangle2D.OUT_LEFT){
          cursor = Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR);				
        }else if((outCode & Rectangle2D.OUT_RIGHT) == Rectangle2D.OUT_RIGHT){
          cursor = Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR);					
        }else{
          cursor = Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);
        }
        
      }else if (((outCode & Rectangle2D.OUT_BOTTOM) == Rectangle2D.OUT_BOTTOM)) {
        if((outCode & Rectangle2D.OUT_LEFT) == Rectangle2D.OUT_LEFT){
          cursor = Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR);				
        }else if((outCode & Rectangle2D.OUT_RIGHT) == Rectangle2D.OUT_RIGHT){
          cursor = Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR);					
        }else{
          cursor = Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);
        }
      }
    }else{
      cursor = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
    }
    
    setCursor(cursor);
  }

  /**
   * Store the start point of our drag operation so we can determine the x and
   * y location changes. We also store the outcode for mouse location relative
   * to our content pane so we can determine whether we are dragging or resizing.
   * 
   * @param event
   *            Screen location of our drag start point.
   */
  protected void beginDrag(MouseEvent event) {
    Point locationOnScreen = event.getLocationOnScreen();
    _previousDragPoint = locationOnScreen;
    getLocation();
    _outCode = _internalContentPanel.getBounds().outcode(event.getPoint());
  }

  /**
   * Used to either resize the dialog or move it. If the mouse is over the
   * content pane, the dialog will be moved. If it is over the border, it
   * will be resized.
   * 
   * @param event
   *            Current location on the screen this is used along with our
   *            previous (or start) drag location to determine how far the
   *            mouse has moved.
   */
  protected void handleDrag(MouseEvent event) {
    // Work out how far we've moved
    Point screenPoint = event.getLocationOnScreen();
    double deltaX = screenPoint.getX() - _previousDragPoint.getX();
    double deltaY = screenPoint.getY() - _previousDragPoint.getY();
    
    if(_outCode == 0){
      // We're inside the content pane
      doDrag(deltaX, deltaY);
    }else if(isResizable()){
      // We're outside the content pane and the dialog is resizable
      doResize(_outCode, (int)deltaX, (int)deltaY);
    }else{
      // We're outside the content pane but the dialog is not resizable,
      // drag instead.
      doDrag(deltaX, deltaY);
    }
    
    _previousDragPoint = screenPoint;
  }

  private void doResize(int outcode, int deltaX, int deltaY) {
    int leftDelta = 0;
    int topDelta = 0;
    int rightDelta = 0;
    int bottomDelta = 0;
    
    if ((outcode & Rectangle2D.OUT_LEFT) == Rectangle2D.OUT_LEFT) {
      leftDelta = deltaX;
      rightDelta = deltaX;
    } 

    if (((outcode & Rectangle2D.OUT_RIGHT) == Rectangle2D.OUT_RIGHT)) {
      rightDelta = -deltaX;
    } 

    if (((outcode & Rectangle2D.OUT_TOP) == Rectangle2D.OUT_TOP)) {
      topDelta = deltaY;
      bottomDelta = deltaY;
    }

    if (((outcode & Rectangle2D.OUT_BOTTOM) == Rectangle2D.OUT_BOTTOM)) {
      bottomDelta = -deltaY;
    }
    
    setBounds(getX() + leftDelta, getY() + topDelta, getWidth() - rightDelta, getHeight() - bottomDelta);
  }

  /**
   * @param deltaX
   * @param deltaY
   */
  protected void doDrag(double deltaX, double deltaY) {
    Point location = getLocation();
    this.setLocation((int)(location.getX() + deltaX), (int)(location.getY() + deltaY));
  }

  @Override
  public void paint(Graphics arg0) {
    super.paint(arg0);
  
    Graphics2D graphics = (Graphics2D) arg0;
    graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));

    // Construct a rounded rectangular area for our opaque region.
    Rectangle bounds = _internalContentPanel.getBounds();

    // Create an areas for our opaque and semi-transparent regions.
    Area opaqueArea = new Area(bounds);
    Area transparentArea = new Area(new RoundRectangle2D.Double(
        0, 0, getWidth(), getHeight(), _edgePadding, _edgePadding));

    // Subtract the opaque area from the semi-transparent area. This
    // will give us a shape we can fill with the semi-transparent
    // colour that will leave our callout panel fully opaque.
    transparentArea.subtract(opaqueArea);

    // Get our current background colour.
    Color background = getContentPane().getBackground();

    // Set up our graphics to draw a semi-transparent version
    // of the background colour.
    graphics.setColor(new Color(background.getRed(), background
        .getGreen(), background.getBlue(), _opacity));
    graphics.fill(transparentArea);
  }

}