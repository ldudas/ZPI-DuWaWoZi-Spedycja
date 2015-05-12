/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.client.toolkit.overlays;

import java.util.EventListener;

/**
 * @deprecated From 10.2.3, use {@link com.esri.toolkit.overlays.DrawingCompleteListener} instead.
 */
@Deprecated
public interface DrawingCompleteListener extends EventListener {
  void drawingCompleted(DrawingCompleteEvent event);
}
