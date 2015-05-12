/* Copyright 2014 Esri
 
All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.client.toolkit.attachments;

import java.util.EventListener;

import com.esri.map.FeatureLayer;

/**
 * Listener interface for objects that want to receive attachment action events 
 * fired by attachment actions.
 * 
 * @deprecated From 10.2.3, use {@link com.esri.toolkit.attachments.AttachmentActionListener} instead.
 * This new package supports {@link FeatureLayer} that was introduced in 10.2.
 */
@Deprecated
public interface AttachmentActionListener extends EventListener {
  /**
   * Invoked just before the actions starts.
   * @since 10.2
   */
  void onStart();

  /**
   * Invoked on successful completion.
   * @param event event.
   */
  void onSuccess(AttachmentActionEvent event);

  /**
   * Invoked on failures.
   * @param e cause of failure.
   * @since 10.2
   */
  void onError(Throwable e);
}
