/* Copyright 2014 Esrisri
 
All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.client.toolkit.attachments;

import java.awt.event.ActionEvent;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureEditResult;
import com.esri.core.map.Graphic;
import com.esri.map.ArcGISFeatureLayer;
import com.esri.map.FeatureLayer;

/**
 * The action to add an attachment to a particular feature in an <code>ArcGISFeatureLayer</code>.
 * @deprecated From 10.2.3, use {@link com.esri.toolkit.attachments.AddAttachmentAction} instead.
 * This new package supports {@link FeatureLayer} that was introduced in 10.2.
 */
@Deprecated
public class AddAttachmentAction extends AttachmentAction {

  Graphic _graphic;
  ArcGISFeatureLayer _featureLayer;
  
  /**
   * Sets the parameters required to add an attachment.
   *
   * @param g the graphic
   * @param f the feature layer
   */
  public void setParameters(Graphic g, ArcGISFeatureLayer f) {
    _graphic = g;
    _featureLayer = f;
  }
  
  /* (non-Javadoc)
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  @Override
  public void actionPerformed(ActionEvent arg0) {
    if (_graphic != null && _featureLayer != null) {
      JFileChooser chooser = new JFileChooser();
      FileNameExtensionFilter filter = new FileNameExtensionFilter(
          null, "png", "jpg", "gif", "txt", "xml");
      chooser.setFileFilter(filter);
      int returnVal = chooser.showOpenDialog(null);
      if(returnVal == JFileChooser.APPROVE_OPTION) {
        fireAttachmentActionStart();

        int oid = ((Integer)_graphic.getAttributeValue(_featureLayer.getObjectIdField())).intValue();
        _featureLayer.addAttachment(oid, chooser.getSelectedFile(), new CallbackListener<FeatureEditResult>() {

          /* (non-Javadoc)
           * @see com.esri.core.map.CallbackListener#onError(java.lang.Throwable)
           */
          @Override
          public void onError(Throwable e) {
            fireAttachmentActionError(e);
          }

          /**
           * Called when operation completes successfully.
           *
           * @param objs the result
           */
          @Override
          public void onCallback(FeatureEditResult objs) {
            fireAttachmentActionCompletedSelected();
          }
        });
      } 
    } 
  }
 
  private static final long serialVersionUID = 1L;
}
