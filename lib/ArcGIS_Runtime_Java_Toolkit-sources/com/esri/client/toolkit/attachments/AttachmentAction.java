/* Copyright 2014 Esri
 
All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.client.toolkit.attachments;

import javax.swing.AbstractAction;
import javax.swing.event.EventListenerList;

import com.esri.map.FeatureLayer;

/**
 * Base class for the actions to take when working with feature attachments, 
 * such as adding or deleting an attachment.
 * @deprecated From 10.2.3, use {@link com.esri.toolkit.attachments.AttachmentAction} instead.
 * This new package supports {@link FeatureLayer} that was introduced in 10.2.
 */
@Deprecated
public abstract class AttachmentAction extends AbstractAction {

  private static final long serialVersionUID = 1L;
  
  EventListenerList _listenerList = new EventListenerList();

  /**
   * Add listeners for action completed event
   * 
   * @param l
   * 		Listener to add
   */
  public void addAttachmentActionListener(AttachmentActionListener l) {
    _listenerList.add(AttachmentActionListener.class, l);
  }

  /**
   * Remove listeners for action completed event
   * 
   * @param l 
   * 		Listener to remove
   */
  public void removeAttachmentActionListener(AttachmentActionListener l) {
    _listenerList.remove(AttachmentActionListener.class, l);
  }
  
   /**
    * Fires a event when the action starts.
    * @since 10.2 
    */
    protected void fireAttachmentActionStart() {
     Object[] listeners = _listenerList.getListenerList();
      for (int i = listeners.length - 2; i >= 0; i -= 2) {
          if (listeners[i] == AttachmentActionListener.class) {
              ((AttachmentActionListener) listeners[i+1]).onStart();
          }
      }
    }
  
  /**
   * Fires a event when the action completes successfully
   */
  protected void fireAttachmentActionCompletedSelected() {
       Object[] listeners = _listenerList.getListenerList();
       for (int i = listeners.length - 2; i >= 0; i -= 2) {
           if (listeners[i] == AttachmentActionListener.class) {
               ((AttachmentActionListener)listeners[i+1]).onSuccess(new AttachmentActionEvent(this, AttachmentActionEvent.ACTION_COMPLETED));
           }
       }
  }

 /**
  * Fires a event when the action fails. 
  * @param e cause of failure.
  * @since 10.2
  */
  protected void fireAttachmentActionError(Throwable e) {
   Object[] listeners = _listenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == AttachmentActionListener.class) {
          ((AttachmentActionListener) listeners[i+1]).onError(e);
      }
    }
  }
}
