/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.toolkit.attachments;

import java.awt.event.ActionEvent;
import java.util.List;

import com.esri.core.geodatabase.GeodatabaseEditError;
import com.esri.core.geodatabase.GeodatabaseFeatureServiceTable;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.map.AttachmentInfo;
import com.esri.core.map.Feature;
import com.esri.map.FeatureLayer;

/**
 * The action to delete an existing attachment from a particular feature in a <code>FeatureLayer</code>.
 * @since 10.2.3
 */
public class DeleteAttachmentAction extends AttachmentAction {

  private Feature feature;
  private FeatureLayer featureLayer;
  private AttachmentInfo attachmentInfo;
  
  /**
   * Sets the parameters required to delete an attachment.
   *
   * @param feature feature
   * @param featureLayer the feature layer
   * @param attachmentInfo the attachment info
   */
  public void setParameters(Feature feature, FeatureLayer featureLayer, AttachmentInfo attachmentInfo) {
    this.feature = feature;
    this.featureLayer = featureLayer;
    this.attachmentInfo = attachmentInfo;
  }
  
  /* (non-Javadoc)
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    if (feature != null && featureLayer != null && attachmentInfo != null)  {
      deleteAttachment(feature, featureLayer, attachmentInfo);
    }
  }
  
  private void deleteAttachment(final Feature f, final FeatureLayer fl,
    final AttachmentInfo attachmentInfo) {
    Thread t = new Thread() {
      @Override
      public void run() {
        boolean successful = true;
        try {
          fireAttachmentActionStart();
          GeodatabaseFeatureTable featureTable = (GeodatabaseFeatureTable) fl.getFeatureTable();
          featureTable.deleteAttachment(f.getId(), attachmentInfo.getId());
          if (featureTable instanceof GeodatabaseFeatureServiceTable) {
            List<GeodatabaseEditError> errors = ((GeodatabaseFeatureServiceTable)featureTable).applyAttachmentEdits(null).get();
            if (!errors.isEmpty()) {
              fireAttachmentActionError(new RuntimeException("Failed to apply edit to service"));
              successful = false;
            }
          }
        } catch (Exception ex) {
          successful = false;
          fireAttachmentActionError(ex);
        }
        if (successful) {
          fireAttachmentActionSuccessful();
        }
      }
    };
    t.start();
  }
  
  private static final long serialVersionUID = 1L;
}
