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
 * @since 10.2.3
 */
public interface FeatureEditCompleteListener extends EventListener {
  void featureEditComplete(FeatureEditCompleteEvent event);
  void featureDeleteComplete(FeatureEditCompleteEvent event);
}
