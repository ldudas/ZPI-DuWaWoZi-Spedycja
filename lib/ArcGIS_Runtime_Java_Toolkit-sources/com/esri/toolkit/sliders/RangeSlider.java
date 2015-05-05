/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.toolkit.sliders;

import javax.swing.JSlider;
import javax.swing.plaf.SliderUI;

/**
 * A custom {@link JSlider} used by {@link JTimeSlider}.
 * @since 10.2.3
 */
public class RangeSlider extends JSlider {
  public enum RangeMode{
    CumulativeFromStart,
    Range,
    SingleValue
  }
  
  private static final long serialVersionUID = 1L;
  private RangeMode _rangeMode = RangeMode.SingleValue;

  public RangeMode getRangeMode() {
    return _rangeMode;
  }
  
  public void setRangeMode(RangeMode mode){
    _rangeMode = mode;
    
    if(_rangeMode == RangeMode.CumulativeFromStart){
      setValue(0);
    }
    
    SliderUI ui = getUI();
    if(ui instanceof BasicRangeSliderUI){
      ((BasicRangeSliderUI)ui).setRangeMode(_rangeMode);
    }
  }

  /* (non-Javadoc)
   * @see javax.swing.JSlider#getValue()
   */
  @Override
  public int getValue() {
    if(_rangeMode != RangeMode.CumulativeFromStart){
      return super.getValue();
    }else{
      return getMinimum();
    }
  }

  /* (non-Javadoc)
   * @see javax.swing.JSlider#setValue(int)
   */
  @Override
  public void setValue(int n) {
    if(_rangeMode != RangeMode.CumulativeFromStart){
      if(_rangeMode == RangeMode.SingleValue){
        super.setExtent(0);
      }
      super.setValue(n);
    }else{
      super.setValue(getMinimum());
    }
  }

  /* (non-Javadoc)
   * @see javax.swing.JSlider#setExtent(int)
   */
  @Override
  public void setExtent(int extent) {
    if(_rangeMode == RangeMode.SingleValue){
      super.setExtent(0);
    }else{
      super.setExtent(extent);
    }
  }

}
