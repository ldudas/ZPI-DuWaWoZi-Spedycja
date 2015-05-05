/* Copyright 2014 Esri
 
All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.client.toolkit.attachments;

import java.awt.event.ActionEvent;

import com.esri.core.map.AttachmentInfo;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureEditResult;
import com.esri.core.map.Graphic;
import com.esri.map.ArcGISFeatureLayer;
import com.esri.map.FeatureLayer;

/**
 * The action to delete an existing attachment from a particular feature in an <code>ArcGISFeatureLayer</code>.
 * @deprecated From 10.2.3, use {@link com.esri.toolkit.attachments.DeleteAttachmentAction} instead.
 * This new package supports {@link FeatureLayer} that was introduced in 10.2.
 */
@Deprecated
public class DeleteAttachmentAction extends AttachmentAction {

  Graphic _graphic;
  ArcGISFeatureLayer _featureLayer;
  AttachmentInfo _attachmentInfo;
  
  /**
   * Sets the parameters required to delete an attachment.
   *
   * @param g the graphic
   * @param f the feature layer
   * @param af the attachment info
   */
  public void setParameters(Graphic g, ArcGISFeatureLayer f, AttachmentInfo af) {
    _graphic = g;
    _featureLayer = f;
    _attachmentInfo = af;
  }
  
  /* (non-Javadoc)
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    if (_graphic != null && _featureLayer != null && _attachmentInfo != null)  {
      int oid = (Integer)_graphic.getAttributeValue(_featureLayer.getObjectIdField());
      fireAttachmentActionStart();
      _featureLayer.deleteAttachments(oid, new int[]{(int)_attachmentInfo.getId()}, new CallbackListener<FeatureEditResult[]>() {

        /* (non-Javadoc)
         * @see com.esri.core.map.CallbackListener#onError(java.lang.Throwable)
         */
        @Override
        public void onError(Throwable e) {
          fireAttachmentActionError(e);
        }

        /**
         * Called when the operation completes successfully.
         *
         * @param objs the result
         */
        @Override
        public void onCallback(FeatureEditResult[] objs) {
          fireAttachmentActionCompletedSelected();
        }
      });
    }
  }
  
  private static final long serialVersionUID = 1L;
}
