/* Copyright 2014 Esri
 
All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.client.toolkit.attachments;

import java.awt.event.ActionEvent;

import com.esri.client.toolkit.utilities.BrowserLauncher;
import com.esri.core.map.AttachmentInfo;
import com.esri.core.map.Graphic;
import com.esri.map.ArcGISFeatureLayer;
import com.esri.map.FeatureLayer;

/**
 * The action to view a particular feature attachment in an <code>ArcGISFeatureLayer</code>.
 * @deprecated From 10.2.3, use {@link com.esri.toolkit.attachments.ViewAttachmentAction} instead.
 * This new package supports {@link FeatureLayer} that was introduced in 10.2.
 */
@Deprecated
public class ViewAttachmentAction extends AttachmentAction {

    Graphic _graphic;
    ArcGISFeatureLayer _featureLayer;
    AttachmentInfo _attachmentInfo;

    /**
     * Sets the parameters required to view an attachment.
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
        if (_graphic != null && _featureLayer != null && _attachmentInfo != null) {
            BrowserLauncher.openURL(
                _featureLayer.getAttachmentURL(_graphic, (int)_attachmentInfo.getId()));
        }
    }

    private static final long serialVersionUID = 1L;
}
