/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.toolkit.attachments;

import javax.swing.AbstractAction;
import javax.swing.event.EventListenerList;

/**
 * Base class for the actions to take when working with feature attachments, 
 * such as adding or deleting an attachment.
 * @since 10.2.3
 */
public abstract class AttachmentAction extends AbstractAction {

  private static final long serialVersionUID = 1L;
  
  private EventListenerList _listenerList = new EventListenerList();

  /**
   * Adds a listener for attachment action events.
   * 
   * @param l
   * 		Listener to add
   */
  public void addAttachmentActionListener(AttachmentActionListener l) {
    _listenerList.add(AttachmentActionListener.class, l);
  }

  /**
   * Removes a listener for attachment action events.
   * 
   * @param l 
   * 		Listener to remove
   */
  public void removeAttachmentActionListener(AttachmentActionListener l) {
    _listenerList.remove(AttachmentActionListener.class, l);
  }
  
   /**
    * Fires an event when the attachment action starts.
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
   * Fires an event when the attachment action completes successfully.
   */
  protected void fireAttachmentActionSuccessful() {
       Object[] listeners = _listenerList.getListenerList();
       for (int i = listeners.length - 2; i >= 0; i -= 2) {
           if (listeners[i] == AttachmentActionListener.class) {
               ((AttachmentActionListener)listeners[i+1]).onSuccess(new AttachmentActionEvent(this, AttachmentActionEvent.ACTION_COMPLETED));
           }
       }
  }

 /**
  * Fires an event when the action fails.
  * 
  * @param e cause of failure.
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
