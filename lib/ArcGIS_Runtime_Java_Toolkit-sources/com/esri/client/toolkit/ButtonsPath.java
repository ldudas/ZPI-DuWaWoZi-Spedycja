/* Copyright 2014 Esri
 
All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.client.toolkit;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JButton;

/**
 * @deprecated From 10.2.3, use {@link com.esri.toolkit.utilities.PathBasedToolBar} instead.
 */
@Deprecated
public class ButtonsPath {
  private Vector<AbstractButton> _buttons = new Vector<AbstractButton>();
  private PathSegmentIterator _pathIterator;
  private boolean _isVisible = false;
  private Dimension _buttonSize;
  private boolean _autoArrangeButtons = false;
  private Path2D _path;
  private Container _parentContainer;
  private Rectangle _bounds;
  
  public ButtonsPath(Container parent, Path2D path, Dimension buttonSize) {
    _parentContainer = parent;
    _pathIterator = new PathSegmentIterator(path);
    _buttonSize = buttonSize;
    setPath(path);
  }
  
  public Path2D getPath() {
    return _path;
  }

  public void setPath(Path2D path) {
    _path = path;
    
    // Get the path's bounds
    _bounds = _path.getBounds();
    
    // Inflate the bounds by an amount that should incorporate
    // the size of the buttons. The largest dimension of a button
    // is its diagonal.
    int buttonDiagonal = (int) Math.sqrt((_buttonSize.width * _buttonSize.width) + (_buttonSize.height * _buttonSize.height));
    _bounds.grow(buttonDiagonal, buttonDiagonal);
  }

  public boolean isVisible(){
    return _isVisible;
  }
  public void setVisible(boolean visible){
    _isVisible = visible;
    
    for(AbstractButton curButton: _buttons){
      curButton.setVisible(visible);
    }
  }
  
  public boolean isAutoArrangeButtons() {
    return _autoArrangeButtons;
  }

  public void setAutoArrangeButtons(boolean autoArrangeButtons) {
    _autoArrangeButtons = autoArrangeButtons;
  }
  
  public void add(Action action){
    JButton newButton = new JButton(action);
    newButton.setOpaque(false);
    newButton.setSize(_buttonSize);
    _parentContainer.add(newButton);
    _buttons.add(newButton);
    
    if(_autoArrangeButtons){
      arrangeButtons();
    }
  }
  
  public void remove(AbstractButton button){
    _buttons.remove(button);
    _parentContainer.remove(button);
    
    if(_autoArrangeButtons){
      arrangeButtons();
    }
  }
  
  public void remove(Action action){
    AbstractButton foundButton = null;
    
    for(AbstractButton curButton: _buttons){
      if(curButton.getAction().equals(action)){
        foundButton = curButton;
        break;
      }
    }
    
    if(foundButton != null){
      remove(foundButton);
    }
  }
  
  public void arrangeButtons(){
    int buttonCount = _buttons.size();
    double buttonSpacing = _pathIterator.getPathLength() / (buttonCount - 1);
    _pathIterator.reset();
    
    for(int counter = 0; counter < buttonCount; ++counter){
      if(_pathIterator.next(counter * buttonSpacing)){
        double lengthOnCurSegment = _pathIterator.getDistanceAlongCurrentSegment();
        LineSegment curSegment = _pathIterator.getCurSegment();
        Point2D buttonPoint = curSegment.getPointOnLine(lengthOnCurSegment);
        placeButton(buttonPoint, _buttons.get(counter));
      }
    }
  }
  
  public Point getLocation(){
    return _path.getBounds().getLocation();
  }
  
  public void setLocation(int x, int y){
    Point curLocation = getLocation();
    move(x - curLocation.x, y - curLocation.y);
  }

  public void setLocation(java.awt.Point location) {
    setLocation(location.x, location.y);
  }
  
  public void move(int dx, int dy){
    _bounds.translate(dx, dy);
    _path.transform(AffineTransform.getTranslateInstance(dx, dy));
    for(AbstractButton curButton: _buttons){
      moveButton(curButton, dx, dy);
    }
  }

  private void moveButton(AbstractButton curButton, int dx, int dy) {
    Point curLocation = curButton.getLocation();
    curLocation.translate(dx, dy);
    curButton.setLocation(curLocation);
  }

  private void placeButton(Point2D buttonPoint, AbstractButton button) {
    button.setLocation((int)(buttonPoint.getX() - (_buttonSize.getWidth() / 2)), 
        (int)(buttonPoint.getY() - (_buttonSize.getHeight() / 2)));
  }

  public Rectangle getBounds() {
    return _bounds;
  }
}
