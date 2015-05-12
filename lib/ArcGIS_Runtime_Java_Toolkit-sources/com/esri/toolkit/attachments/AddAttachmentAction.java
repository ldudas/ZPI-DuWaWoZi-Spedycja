/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.toolkit.attachments;

import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URLConnection;
import java.util.List;

import javax.swing.JFileChooser;

import com.esri.core.geodatabase.GeodatabaseEditError;
import com.esri.core.geodatabase.GeodatabaseFeatureServiceTable;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.map.Feature;
import com.esri.map.FeatureLayer;

/**
 * The action to add an attachment to a particular feature in a <code>FeatureLayer</code>.
 * @since 10.2.3
 */
public class AddAttachmentAction extends AttachmentAction {

  private Feature feature;
  private FeatureLayer featureLayer;
  
  /**
   * Sets the parameters required to add an attachment.
   *
   * @param feature the feature
   * @param featureLayer the feature layer
   */
  public void setParameters(Feature feature, FeatureLayer featureLayer) {
    this.feature = feature;
    this.featureLayer = featureLayer;
  }
  
  /**
   * Displays a file chooser dialog and when the user chooses a 
   * file, adds it as an attachment to the feature.
   */
  @Override
  public void actionPerformed(ActionEvent arg0) {
    if (feature != null && featureLayer != null) {
      JFileChooser chooser = new JFileChooser();
      int returnVal = chooser.showOpenDialog(null);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        addAttachment(feature, featureLayer, chooser.getSelectedFile());
      }
    } 
  }
  
  private void addAttachment(final Feature f, final FeatureLayer fL, final File file) {
    Thread t = new Thread() {
      @Override
      public void run() {
        boolean successful = true;
        try {
          fireAttachmentActionStart();
          GeodatabaseFeatureTable featureTable = (GeodatabaseFeatureTable) fL.getFeatureTable();
          String contentType = URLConnection.guessContentTypeFromName(file.getName());
          if (contentType == null) {
            int extensionIndex = file.getName().lastIndexOf(".");
            if (extensionIndex != -1) {
              contentType = file.getName().substring(extensionIndex + 1);
            }
          }
          long id = featureTable.addAttachment(f.getId(), file, contentType, file.getName());
          if (id == -1) {
            throw new RuntimeException("Failed to add attachment.");
          }
          
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
