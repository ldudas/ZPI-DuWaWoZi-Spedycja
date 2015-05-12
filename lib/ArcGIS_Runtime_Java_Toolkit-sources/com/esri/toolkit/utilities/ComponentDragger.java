/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.toolkit.utilities;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * This class can be used to make a Swing component draggable by the mouse. 
 * Once a component has been made draggable using this class as shown below, 
 * the user can drag the component by pressing the mouse, dragging the component 
 * to the desired location, then releasing the mouse. When the component is 
 * being dragged, the component is re-drawn at the latest mouse location, and 
 * the component is displayed with an alternate background. Optionally, set this 
 * alternate background using the constructor {@link #ComponentDragger(Component, Color)}.
 * <p>
 * @usage
 * To make a AWT/Swing component draggable, add an instance of this class to
 * the MouseListener and the MouseMotionListener:
 * <p>
 * <code>
 * ComponentDragger componentDragger = new ComponentDragger(component); <br>
 * addMouseMotionListener(componentDragger);<br>
 * addMouseListener(componentDragger);<br>
 * </code>
 * @since 10.2.3
 */
public class ComponentDragger implements MouseMotionListener, MouseListener {

  private Component component;

  private Point initialMouseLocation;
  private Point initialComponentLocation;

  private Color background;
  private Color backgroundOnDrag;
  
  private Cursor moveCursor = new Cursor(Cursor.MOVE_CURSOR);
  private Cursor currCursor = new Cursor(Cursor.DEFAULT_CURSOR);

  /**
   * Creates an instance to drag a component.
   * @param component component that will be dragged along with mouse movement.
   * @param backgroundOnDrag component background when drag is happening.
   */
  public ComponentDragger(Component component, Color backgroundOnDrag) {
    this.component = component;
    this.backgroundOnDrag = backgroundOnDrag;
  }

  public ComponentDragger(Component component) {
    this(component, null);
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    handleDrag(e);
  }

  @Override
  public void mouseMoved(MouseEvent e) {
  }

  @Override
  public void mouseClicked(MouseEvent e) {
  }
  
  @Override
  public void mouseEntered(MouseEvent e) {
    component.setCursor(moveCursor);
  }

  @Override
  public void mouseExited(MouseEvent arg0) {
    component.setCursor(currCursor);
  }

  @Override
  public void mousePressed(MouseEvent e) {
    // save current background so that it can be restored when mouse released
    background = component.getBackground();
    
    // by default, make component transparent on drag
    if (backgroundOnDrag == null) {
      backgroundOnDrag = new Color(background.getRed(), background.getGreen(), background.getBlue(), 50);
    }
    
    // change background
    component.setBackground(backgroundOnDrag);

    // record current locations of mouse and component
    initialMouseLocation = e.getLocationOnScreen();
    initialComponentLocation = component.getLocation();
  }

  @Override
  public void mouseReleased(MouseEvent arg0) {
    // restore background
    component.setBackground(background);
  }

  protected void handleDrag(MouseEvent event) {
    // work out how far the mouse has moved
    Point currMouseLocation = event.getLocationOnScreen();
    double deltaX = currMouseLocation.getX() - initialMouseLocation.getX();
    double deltaY = currMouseLocation.getY() - initialMouseLocation.getY();

    // move the component to the new location
    component.setLocation(
        (int) (initialComponentLocation.getX() + deltaX), 
        (int) (initialComponentLocation.getY() + deltaY));		
  }
  
  /**
   * Sets the cursor to be used when the component is dragged.
   * @param moveCursor cursor to be used when the component is dragged.
   * @since 10.2.3
   */
  public void setMoveCursor(Cursor moveCursor) {
    if (moveCursor == null) {
      throw new NullPointerException("Input cursor should not be null.");
    }
    this.moveCursor = moveCursor;
  }
}
