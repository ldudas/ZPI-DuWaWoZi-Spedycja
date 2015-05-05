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
 * An event fired when the drawing of a feature is complete.
 *
 * @since 10.2.3
 */
public class DrawingCompleteEvent extends AWTEvent {

  private static final long serialVersionUID = 1L;

  public static final int DRAWING_COMPLETED = AWTEvent.RESERVED_ID_MAX + 1;
  
  public DrawingCompleteEvent(DrawingOverlay source, int id) {
    super(source, id);
  }

}
