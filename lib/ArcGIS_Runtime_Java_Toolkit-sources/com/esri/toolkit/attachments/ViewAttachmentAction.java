/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.toolkit.attachments;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.map.AttachmentInfo;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.Feature;
import com.esri.map.FeatureLayer;

/**
 * The action to view a particular feature attachment in a <code>FeatureLayer</code>.
 * @since 10.2.3
 */
public class ViewAttachmentAction extends AttachmentAction {

    private Feature feature;
    private FeatureLayer featureLayer;
    private AttachmentInfo attachmentInfo;

    /**
     * Sets the parameters required to view an attachment.
     *
     * @param feature the feature which has the attachment
     * @param featureLayer the feature layer containing the feature
     * @param attachmentInfo the attachment info specifying which attachment to view
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
        if (feature != null && featureLayer != null && attachmentInfo != null) {
          fireAttachmentActionStart();
          GeodatabaseFeatureTable featureTable = (GeodatabaseFeatureTable) featureLayer.getFeatureTable();
          featureTable.retrieveAttachment(
              feature.getId(), 
              attachmentInfo.getId(), 
              new CallbackListener<InputStream>() {
                  
                  @Override
                  public void onError(Throwable e) {
                    fireAttachmentActionError(e);
                  }
                  
                  @Override
                  public void onCallback(InputStream inputFileStream) {
                    try {
                      writeToFileAndOpen(inputFileStream);
                    } catch (Exception ex) {
                      fireAttachmentActionError(ex);
                    }
                    fireAttachmentActionSuccessful();
                  }
                }
           );
        }
    }
    
  private void writeToFileAndOpen(InputStream inputFileStream) throws Exception {
    OutputStream output = null;
    try {
      String tmpDir = System.getProperty("java.io.tmpdir");
      String filePath = tmpDir + File.separator + attachmentInfo.getName();
      output = new FileOutputStream(filePath);
      byte[] buffer = new byte[8 * 1024];
      int bytesRead;
      while ((bytesRead = inputFileStream.read(buffer)) != -1) {
        output.write(buffer, 0, bytesRead);
      }
      Desktop.getDesktop().open(new File(filePath));
    } catch (Exception ex) {
      throw ex;
    } finally {
      try {
        if (output != null) {
          output.close();
        }
        if (inputFileStream != null) {
          inputFileStream.close();
        }
      } catch (Exception ex) {
        throw ex;
      }
    }
  }
   
  private static final long serialVersionUID = 1L;
}
