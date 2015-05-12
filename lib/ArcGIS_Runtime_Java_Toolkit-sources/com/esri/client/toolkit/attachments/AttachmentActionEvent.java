/* Copyright 2014 Esri
 
All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.client.toolkit.attachments;

import java.awt.AWTEvent;

import com.esri.map.FeatureLayer;

/**
 * @deprecated From 10.2.3, use {@link com.esri.toolkit.attachments.AttachmentActionEvent} instead.
 * This new package supports {@link FeatureLayer} that was introduced in 10.2.
 */
@Deprecated
public class AttachmentActionEvent extends AWTEvent {

  private static final long serialVersionUID = 1L;

  public static final int ACTION_COMPLETED = AWTEvent.RESERVED_ID_MAX + 1;
  
  public AttachmentActionEvent(AttachmentAction source, int id) {
    super(source, id);
  }

}
