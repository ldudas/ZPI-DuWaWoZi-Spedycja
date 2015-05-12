/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.client.toolkit.overlays;

import java.awt.AWTEvent;

import com.esri.client.toolkit.overlays.HitTestOverlay;

/**
 * The Class HitTestEvent.
 * @deprecated From 10.2.3, use {@link com.esri.toolkit.overlays.HitTestEvent} instead.
 */
@Deprecated
public class HitTestEvent extends AWTEvent {
  private static final long serialVersionUID = 1L;

  public static final int FEATURE_HIT = AWTEvent.RESERVED_ID_MAX + 1;
  
  public HitTestEvent(HitTestOverlay source, int id) {
    super(source, id);
  }
  
  /**
   * Helper method to make it easier to access the overlay that causes this event.
   * @return the overlay
   * @since 10.2.3
   */
  public HitTestOverlay getOverlay() {
    return (HitTestOverlay)getSource();
  }
}
