/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.client.toolkit.overlays;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

import javax.swing.Action;
import javax.swing.JButton;

/**
 * This class implements a button that is displayed as a {@link java.awt.Shape
 * Shape}. The Shape will be drawn as a white line with a black border. The size
 * of the button will be determined by the bounds of the shape. Note that if the
 * shape is not closed, it will only accept clicks on its outline.
 * @deprecated From 10.2.3
 */
@Deprecated
public class ShapeButton extends JButton {
  private static final long serialVersionUID = 1L;
  public static final int MARGIN = 5;

  private ShapeButtonUI _buttonUI;
  private float _alpha = 1f;

  /**
   * Instantiates a new shape button with the given Shape.
   * 
   * @param shape
   *            the shape the button will have
   */
  public ShapeButton(Shape shape) {
    super();

    setupUI(shape);
  }

  /**
   * Instantiates a new shape button with the given shape and action.
   * 
   * @param shape
   *            the shape the button will have
   * @param a
   *            the action performed on clicking the button
   */
  public ShapeButton(Shape shape, Action a) {
    super(a);

    setupUI(shape);
  }

  /**
   * Gets the shape.
   * 
   * @return the shape
   */
  public Shape getShape() {
    return _buttonUI.getShape();
  }

  /**
   * Sets the shape. Note that this may change the size of the button. The
   * shape's coordinates will be taken relative to the button.
   * 
   * @param shape
   *            the new shape
   */
  public void setShape(Shape shape) {
    setupUI(shape);
  }

  /**
   * Gets the overall alpha of the button.
   * 
   * @return the alpha value from zero to one
   */
  public float getAlpha() {
    return _alpha;
  }

  /**
   * Sets the alpha.
   * 
   * @param alpha
   *            the new alpha value between zero and one
   */
  public void setAlpha(float alpha) {
    _alpha = alpha;
  }

  /*
   * Overridden here to stop the border from being painted, all we want drawn
   * is the shape.
   * 
   * (non-Javadoc)
   * 
   * @see javax.swing.AbstractButton#paintBorder(java.awt.Graphics)
   */
  @Override
  protected void paintBorder(Graphics g) {
  }

  /**
   * Creates a new {@link com.esri.client.toolkit.overlays.ShapeButtonUI
   * ShapeButtonUI} instance and sets the button shape. This will also set the
   * size of the button large enough to fit the given shape and its darker
   * outline.
   * 
   * @param shape
   *            the new up ui
   */
  protected void setupUI(Shape shape) {
    _buttonUI = (ShapeButtonUI) ShapeButtonUI.createUI(this);
    _buttonUI.setShape(AffineTransform.getTranslateInstance(MARGIN, MARGIN)
        .createTransformedShape(shape));
    setUI(_buttonUI);
    Rectangle shapeBounds = shape.getBounds();
    shapeBounds.grow(MARGIN, 5);
    setSize((int) shapeBounds.getWidth(), (int) shapeBounds.getHeight());
  }

}
