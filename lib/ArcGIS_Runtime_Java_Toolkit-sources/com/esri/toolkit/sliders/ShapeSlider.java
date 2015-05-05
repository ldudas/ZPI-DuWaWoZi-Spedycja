/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.toolkit.sliders;

import java.awt.Shape;

import javax.swing.BoundedRangeModel;
import javax.swing.JSlider;

/**
 * This class implements a slider control with a simple line for the track and a
 * given shape for the thumb.
 * @since 10.2.3
 */
public class ShapeSlider extends JSlider {
  private static final long serialVersionUID = 1L;
  private ShapeSliderUI _sliderUI;
  private float _alpha = 1f;

  /**
   * Instantiates a new shape slider.
   * 
   * @param shape
   *            the shape to use for the thumb
   */
  public ShapeSlider(Shape shape) {
    super();
    init(shape);
  }

  /**
   * Instantiates a new shape slider.
   * 
   * @param shape
   *            the shape
   * @param brm
   *            the bounded range model to define the slider's range
   */
  public ShapeSlider(Shape shape, BoundedRangeModel brm) {
    super(brm);
    init(shape);
  }

  /**
   * Instantiates a new shape slider.
   * 
   * @param shape
   *            the shape to use for the thumb
   * @param orientation
   *            the orientation
   * @param min
   *            the minimum value
   * @param max
   *            the maximum value
   * @param value
   *            the current value
   */
  public ShapeSlider(Shape shape, int orientation, int min, int max, int value) {
    super(orientation, min, max, value);
    init(shape);
  }

  /**
   * Instantiates a new shape slider.
   * 
   * @param shape
   *            the shape to use for the thumb
   * @param min
   *            the minimum value
   * @param max
   *            the maximum value
   * @param value
   *            the current value
   */
  public ShapeSlider(Shape shape, int min, int max, int value) {
    super(min, max, value);
    init(shape);
  }

  /**
   * Instantiates a new shape slider.
   * 
   * @param shape
   *            the shape to use for the thumb
   * @param min
   *            the minimum value
   * @param max
   *            the maximum value
   */
  public ShapeSlider(Shape shape, int min, int max) {
    super(min, max);
    init(shape);
  }

  /**
   * Instantiates a new shape slider.
   * 
   * @param shape
   *            the shape to use for the thumb
   * @param orientation
   *            the orientation
   */
  public ShapeSlider(Shape shape, int orientation) {
    super(orientation);
    init(shape);
  }

  /**
   * Gets the shape currently being used for the thumb.
   * 
   * @return the shape to use for the thumb
   */
  public Shape getShape() {
    return _sliderUI.getShape();
  }

  /**
   * Sets the shape to use for the thumb.
   * 
   * @param shape
   *            the new shape to use for the thumb
   */
  public void setShape(Shape shape) {
    setupUI(shape);
  }

  /**
   * Gets the alpha.
   * 
   * @return the alpha value between zero and one
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

  /**
   * Initialise a new instance of this class by setting up the shape used to
   * draw the thumb.
   * 
   * @param shape
   */
  private void init(Shape shape) {
    setupUI(shape);
  }

  /**
   * Creates a new {@link com.esri.toolkit.sliders.ShapeSliderUI
   * ShapeSliderUI} instance and applies it to this slider instance.
   * 
   * @param shape
   *            the shape used to draw the thumb
   */
  private void setupUI(Shape shape) {
    _sliderUI = (ShapeSliderUI) ShapeSliderUI.createUI(this);
    _sliderUI.setShape(shape);
    setUI(_sliderUI);
  }

}
