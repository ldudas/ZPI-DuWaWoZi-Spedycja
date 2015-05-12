/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.client.toolkit.sliders;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSliderUI;

/**
 * This class defines the look and feel for the
 * {@link com.esri.client.toolkit.sliders.ShapeSlider ShapeSlider} class. It
 * draws the slider with a simple line for the track and a
 * {@link java.awt.Shape Shape} for the thumb.
 * @deprecated From 10.2.3, use {@link com.esri.toolkit.sliders.ShapeSliderUI} instead.
 */
@Deprecated
public class ShapeSliderUI extends BasicSliderUI {

  /**
   * The listener interface for receiving UIMouse events. The class that is
   * interested in processing a UIMouse event implements this interface, and
   * the object created with that class is registered with a component using
   * the component's <code>addUIMouseListener<code> method. When
   * the UIMouse event occurs, that object's appropriate
   * method is invoked.
   * 
   * @see UIMouseEvent
   */
  public class UIMouseListener implements MouseListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(MouseEvent e) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(MouseEvent e) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    /*
     * Overridden to allow us to stop displaying thumb mouse over feedback
     * when the mouse leaves the bounds of the control.
     * 
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseExited(MouseEvent e) {
      _mouseOverThumb = false;
    }

  }

  /**
   * The listener interface for receiving UIMouseMotion events. The class that
   * is interested in processing a UIMouseMotion event implements this
   * interface, and the object created with that class is registered with a
   * component using the component's
   * <code>addUIMouseMotionListener<code> method. When
   * the UIMouseMotion event occurs, that object's appropriate
   * method is invoked.
   * 
   * @see UIMouseMotionEvent
   */
  public class UIMouseMotionListener implements MouseMotionListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent
     * )
     */
    @Override
    public void mouseDragged(MouseEvent e) {
    }

    /*
     * Overridden to allow us to determine whether or not the mouse is over
     * the thumb so we can show mouse over feedback.
     * 
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent
     * )
     */
    @Override
    public void mouseMoved(MouseEvent e) {
      AffineTransform translate = AffineTransform.getTranslateInstance(
          thumbRect.x, thumbRect.y);
      _mouseOverThumb = translate.createTransformedShape(_uiShape)
          .contains(e.getPoint());
    }

  }

  private Shape _uiShape;
  private BasicStroke _controlStroke = new BasicStroke(2,
      BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
  private BasicStroke _haloStroke = new BasicStroke(3, BasicStroke.CAP_ROUND,
      BasicStroke.JOIN_ROUND);
  private UIMouseMotionListener _mouseMotionListener;
  private boolean _mouseOverThumb = false;
  private UIMouseListener _mouseListener;

  /**
   * Instantiates a new shape slider ui.
   * 
   * @param slider
   *            the ShapeSlider this instance will be the look and feel for.
   */
  public ShapeSliderUI(ShapeSlider slider) {
    super(slider);
    _mouseMotionListener = new UIMouseMotionListener();
  }

  /**
   * Creates the ui.
   * 
   * @param component
   *            the component whose look and feel we are setting
   * @return a new instance of this class
   */
  public static ComponentUI createUI(JComponent component) {
    return new ShapeSliderUI((ShapeSlider) component);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * javax.swing.plaf.basic.BasicSliderUI#installUI(javax.swing.JComponent)
   */
  @Override
  public void installUI(JComponent c) {
    super.installUI(c);
    c.addMouseMotionListener(_mouseMotionListener);
    c.addMouseListener(_mouseListener);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * javax.swing.plaf.basic.BasicSliderUI#uninstallUI(javax.swing.JComponent)
   */
  @Override
  public void uninstallUI(JComponent c) {
    super.uninstallUI(c);
    c.removeMouseMotionListener(_mouseMotionListener);
    c.removeMouseListener(_mouseListener);
  }

  /**
   * Gets the shape that defines the slider thumb.
   * 
   * @return the shape
   */
  public Shape getShape() {
    return _uiShape;
  }

  /**
   * Sets the shape that defines the slider thumb.
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
   * @see javax.swing.plaf.basic.BasicSliderUI#paintTrack(java.awt.Graphics)
   */
  @Override
  public void paintTrack(Graphics g) {
    if (slider instanceof ShapeSlider) {
      Graphics2D g2d = (Graphics2D) g;

      ShapeSlider shapeSlider = (ShapeSlider) slider;

      // Create a line to draw the slider track. This will run down
      // the centre of the trackRect.
      Line2D line = new Line2D.Double();
      if (slider.getOrientation() == JSlider.HORIZONTAL) {
        line.setLine(trackRect.getMinX(), trackRect.getCenterY(),
            trackRect.getMaxX(), trackRect.getCenterY());
      } else {
        line.setLine(trackRect.getCenterX(), trackRect.getMinY(),
            trackRect.getCenterX(), trackRect.getMaxY());
      }

      // Draw the line as white line over a thicker black line. Use the
      // set alpha value.
      Composite oldComposite = g2d.getComposite();
      g2d.setComposite(AlphaComposite.getInstance(
          AlphaComposite.SRC_OVER, shapeSlider.getAlpha()));
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
          RenderingHints.VALUE_ANTIALIAS_ON);
      g2d.setPaint(Color.gray);
      g2d.setStroke(_haloStroke);
      g2d.draw(line);
      g2d.setPaint(Color.white);
      g2d.setStroke(_controlStroke);
      g2d.draw(line);

      g2d.setComposite(oldComposite);
    } else {
      // Not applied to a ShapeSlider, draw a normal slider track.
      super.paintTrack(g);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.swing.plaf.basic.BasicSliderUI#paintThumb(java.awt.Graphics)
   */
  @Override
  public void paintThumb(Graphics g) {
    if (slider instanceof ShapeSlider) {
      Graphics2D g2d = (Graphics2D) g;

      ShapeSlider shapeSlider = (ShapeSlider) slider;

      // Translate graphics to thumb location
      g.translate(thumbRect.x, thumbRect.y);

      Composite oldComposite = g2d.getComposite();
      if (_mouseOverThumb) {
        // Mouse is over thumb, draw fully opaque with a radial
        // gradient under it.
        g2d.setComposite(AlphaComposite.getInstance(
            AlphaComposite.SRC_OVER, 1.0f));
        Rectangle bounds = _uiShape.getBounds();
        Rectangle2D gradientRect = new Rectangle2D.Double(0, 0,
            bounds.getWidth(), bounds.getHeight());
        g2d.setPaint(new RadialGradientPaint(gradientRect, new float[] {
            0f, 1f }, new Color[] { Color.white,
            new Color(255, 255, 255, 0) }, CycleMethod.NO_CYCLE));
        g2d.fill(gradientRect);
      } else {
        // Draw with the set alpha value.
        g2d.setComposite(AlphaComposite.getInstance(
            AlphaComposite.SRC_OVER, shapeSlider.getAlpha()));
      }

      // Draw the thumb with the set shape as a white outline over a
      // thicker
      // black outline.
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
          RenderingHints.VALUE_ANTIALIAS_ON);
      g2d.setPaint(Color.gray);
      g2d.setStroke(_haloStroke);
      g2d.draw(_uiShape);
      g2d.setPaint(Color.white);
      g2d.setStroke(_controlStroke);
      g2d.draw(_uiShape);

      g2d.setComposite(oldComposite);

      // Done so translate graphics back to where they were for
      // rest of drawing
      g.translate(-thumbRect.x, -thumbRect.y);
    } else {
      // Not applied to a ShapeSlider, draw a normal thumb
      super.paintThumb(g);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.swing.plaf.basic.BasicSliderUI#paintFocus(java.awt.Graphics)
   */
  @Override
  public void paintFocus(Graphics g) {
    if (slider instanceof ShapeSlider) {
      Graphics2D g2d = (Graphics2D) g;

      // Draw focus rectangle with same alpha setting as the rest of
      // the control.
      ShapeSlider shapeSlider = (ShapeSlider) slider;
      Composite oldComposite = g2d.getComposite();
      g2d.setComposite(AlphaComposite.getInstance(
          AlphaComposite.SRC_OVER, shapeSlider.getAlpha()));
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
          RenderingHints.VALUE_ANTIALIAS_ON);
      super.paintFocus(g);

      g2d.setComposite(oldComposite);
    } else {
      // Not applied to a ShapeSlider, draw a normal focus rectangle
      super.paintFocus(g);
    }
  }

  /*
   * Overridden to allow us to set the thumb size to be the bounds of the set
   * shape.
   * 
   * (non-Javadoc)
   * 
   * @see javax.swing.plaf.basic.BasicSliderUI#getThumbSize()
   */
  @Override
  protected Dimension getThumbSize() {
    Rectangle bounds = _uiShape.getBounds();
    return new Dimension((int) bounds.getWidth(), (int) bounds.getHeight());
  }

}
