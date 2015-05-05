/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.client.toolkit.overlays;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * This class defines the look and feel of the
 * {@link com.esri.client.toolkit.overlays.ShapeButton ShapeButton} class. The
 * button shape is drawn as a white line on top of a thicker black line. A
 * circular gradient is drawn on mouse over.
 * @deprecated From 10.2.3
 */
@Deprecated
class ShapeButtonUI extends BasicButtonUI {

  /**
   * The listener interface for receiving buttonPropertyChange events. The
   * class that is interested in processing a buttonPropertyChange event
   * implements this interface, and the object created with that class is
   * registered with a component using the component's
   * <code>addButtonPropertyChangeListener<code> method. When
   * the buttonPropertyChange event occurs, that object's appropriate
   * method is invoked.
   * 
   * @see ButtonPropertyChangeEvent
   */
  public class ButtonPropertyChangeListener implements PropertyChangeListener {

    /*
     * We are looking for changes in the roll over enabled property so we
     * can determine whether or not to show feedback when the mouse is over
     * the button's shape.
     * 
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.
     * PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      if (evt.getPropertyName().equals(
          AbstractButton.ROLLOVER_ENABLED_CHANGED_PROPERTY)) {
        _rolloverEnabled = (Boolean) evt.getNewValue();
      }
    }

  }

  private Shape _uiShape;
  private ButtonPropertyChangeListener _propertyChangeListener;
  private boolean _rolloverEnabled;
  private BasicStroke _controlStroke = new BasicStroke(2);
  private BasicStroke _haloStroke = new BasicStroke(3);

  /**
   * Creates an instance of this to associate with the given component.
   * 
   * @param c
   *            the component to apply this look and feel to
   * @return an instance of this class
   */
  public static ComponentUI createUI(JComponent c) {
    return new ShapeButtonUI();
  }

  /**
   * Gets the shape currently defining the button.
   * 
   * @return the shape
   */
  public Shape getShape() {
    return _uiShape;
  }

  /**
   * Sets the shape that will define the button.
   * 
   * @param shape
   *            the new shape
   */
  public void setShape(Shape shape) {
    _uiShape = shape;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * javax.swing.plaf.basic.BasicButtonUI#installUI(javax.swing.JComponent)
   */
  @Override
  public void installUI(JComponent c) {
    super.installUI(c);
    c.setOpaque(false);
    c.addPropertyChangeListener(_propertyChangeListener);
    _rolloverEnabled = ((AbstractButton) c).isRolloverEnabled();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * javax.swing.plaf.basic.BasicButtonUI#uninstallUI(javax.swing.JComponent)
   */
  @Override
  public void uninstallUI(JComponent c) {
    super.uninstallUI(c);
    c.removePropertyChangeListener(_propertyChangeListener);
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.swing.plaf.basic.BasicButtonUI#paint(java.awt.Graphics,
   * javax.swing.JComponent)
   */
  @Override
  public void paint(Graphics g, JComponent c) {
    if (c instanceof ShapeButton) {
      ShapeButton button = (ShapeButton) c;
      ButtonModel model = button.getModel();

      Graphics2D g2d = (Graphics2D) g;

      Composite oldComposite = g2d.getComposite();

      // Is the mouse over the button and is rollover enabled?
      if (_rolloverEnabled && model.isRollover()) {

        // Draw button fully opaque with a radial gradient under it
        g2d.setComposite(AlphaComposite.getInstance(
            AlphaComposite.SRC_OVER, 1.0f));
        Rectangle bounds = button.getBounds();
        Rectangle2D gradientRect = new Rectangle2D.Double(0, 0,
            bounds.getWidth(), bounds.getHeight());
        g2d.setPaint(new RadialGradientPaint(gradientRect, new float[] {
            0f, 1f }, new Color[] { Color.white,
            new Color(255, 255, 255, 0) }, CycleMethod.NO_CYCLE));
        g2d.fill(gradientRect);
      } else {
        // No rollover, just draw the button with the currently
        // set alpha value.
        g2d.setComposite(AlphaComposite.getInstance(
            AlphaComposite.SRC_OVER, button.getAlpha()));
      }

      // Draw the button shape
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
          RenderingHints.VALUE_ANTIALIAS_ON);
      g2d.setPaint(Color.gray);
      g2d.setStroke(_haloStroke);
      g2d.draw(_uiShape);
      g2d.setPaint(Color.white);
      g2d.setStroke(_controlStroke);
      g2d.draw(_uiShape);

      g2d.setComposite(oldComposite);
    }
  }

  /*
   * Overridden to return true if the button's shape contains the given point.
   * Note that if the shape is not closed, this will only return true if the
   * given point falls on the shape's outline.
   * 
   * (non-Javadoc)
   * 
   * @see javax.swing.plaf.ComponentUI#contains(javax.swing.JComponent, int,
   * int)
   */
  @Override
  public boolean contains(JComponent c, int x, int y) {
    return _uiShape.contains(x, y);
  }

}