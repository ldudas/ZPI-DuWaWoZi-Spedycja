/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.toolkit.overlays;

import java.util.EventListener;

/**
 * The listener interface for receiving drawing complete events.
 * The class that is interested in processing a {@link DrawingCompleteEvent}
 * implements this interface, and the object created with that class 
 * is registered with a component using the <code>addDrawingCompleteListener</code>
 * method. When the drawing complete event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see DrawingOverlay
 * @since 10.2.3
 */
public interface DrawingCompleteListener extends EventListener {
  void drawingCompleted(DrawingCompleteEvent event);
}
