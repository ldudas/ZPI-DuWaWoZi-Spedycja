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
 * The listener interface for receiving Hit test events.
 * The class that is interested in processing a hit tested
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addHitTestListener<code> method. When
 * the hit test event occurs, that object's appropriate
 * method is invoked.
 *
 * @see HitTestEvent
 * @deprecated From 10.2.3, use {@link com.esri.toolkit.overlays.HitTestListener} instead.
 */
@Deprecated
public interface HitTestListener extends EventListener {
  void featureHit(HitTestEvent event);
}
