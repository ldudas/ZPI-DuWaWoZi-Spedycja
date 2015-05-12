/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.toolkit.attachments;

import java.util.EventListener;

/**
 * Listener interface for objects that want to receive attachment action events 
 * fired by attachment actions.
 * 
 * @see AttachmentAction
 * @see AttachmentActionEvent
 * @since 10.2.3
 */
public interface AttachmentActionListener extends EventListener {
  /**
   * Invoked just before the actions starts.
   */
  void onStart();

  /**
   * Invoked on successful completion.
   * @param event attachment action event
   */
  void onSuccess(AttachmentActionEvent event);

  /**
   * Invoked on failures.
   * @param e cause of failure.
   */
  void onError(Throwable e);
}
