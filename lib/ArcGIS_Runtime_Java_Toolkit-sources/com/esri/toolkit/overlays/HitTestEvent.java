/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.toolkit.overlays;

import java.awt.AWTEvent;

/**
 * This class represents a hit test event, fired by an active hit test 
 * overlay when one or more features are 'hit' by a mouse click. 
 * 
 * @see HitTestOverlay
 * @since 10.2.3
 */
public class HitTestEvent extends AWTEvent {
  private static final long serialVersionUID = 1L;

  public static final int FEATURE_HIT = AWTEvent.RESERVED_ID_MAX + 1;
  
  public HitTestEvent(HitTestOverlay source, int id) {
    super(source, id);
  }
  
  /**
   * Helper method to make it easier to access the overlay that causes this event.
   * @return the overlay
   */
  public HitTestOverlay getOverlay() {
    return (HitTestOverlay)getSource();
  }
}
