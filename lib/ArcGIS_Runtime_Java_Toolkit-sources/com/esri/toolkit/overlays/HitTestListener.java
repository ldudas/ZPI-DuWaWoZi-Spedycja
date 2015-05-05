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
 * The listener interface for receiving hit test events.
 * The class that is interested in processing a hit test
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addHitTestListener<code> method. When
 * the hit test event occurs, that object's appropriate
 * method is invoked.
 *
 * @see HitTestEvent
 * @see HitTestOverlay
 * @since 10.2.3
 */
public interface HitTestListener extends EventListener {
  void featureHit(HitTestEvent event);
}
