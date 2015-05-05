/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.toolkit.overlays;

import java.awt.AWTEvent;

import com.esri.toolkit.overlays.FeatureEditOverlay.FeatureInfo;

/**
 * @since 10.2.3
 */
public class FeatureEditCompleteEvent extends AWTEvent {
  private static final long serialVersionUID = 1L;
  public static final int FEATUREEDITCOMPLETE_FIRST = AWTEvent.RESERVED_ID_MAX + 1;
  public static final int FEATUREEDITCOMPLETE_EDITED = FEATUREEDITCOMPLETE_FIRST;
  public static final int FEATUREEDITCOMPLETE_DELETED = FEATUREEDITCOMPLETE_EDITED + 1;
  public static final int FEATUREEDITCOMPLETE_LAST = FEATUREEDITCOMPLETE_DELETED;
  private FeatureInfo[] _editedFeatureInfos;

  public FeatureEditCompleteEvent(FeatureEditOverlay source, FeatureInfo[] featureInfos, int id) {
    super(source, id);
    _editedFeatureInfos = featureInfos;
  }

  public FeatureInfo[] getEditedFeatureInfos() {
    return _editedFeatureInfos;
  }

}
