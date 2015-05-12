/* Copyright 2014 Esri
 
All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.client.toolkit.attachments;

import java.awt.Component;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JProgressBar;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import com.esri.client.toolkit.utilities.FormatUtils;
import com.esri.core.map.AttachmentInfo;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.Graphic;
import com.esri.map.ArcGISFeatureLayer;
import com.esri.map.FeatureLayer;

/**
 * This class provides a UI component to display a list of attachments for a given 
 * feature in an ArcGISFeatureLayer; used by the {@link JAttachmentEditor} component.
 * @deprecated From 10.2.3, use {@link com.esri.toolkit.attachments.JAttachmentList} instead.
 * This new package supports {@link FeatureLayer} that was introduced in 10.2.
 */
@Deprecated
public class JAttachmentList extends JList {
  AttachmentInfo[] _attachmentInfos;
  Graphic _graphic;
  ArcGISFeatureLayer _featureLayer;
  JProgressBar _progressBar;
  private AttachmentActionListener _queryActionListener;
  private final AttachmentQueryCallback _queryAttachmentCallback = new AttachmentQueryCallback();

  private final class AttachmentQueryCallback implements CallbackListener<AttachmentInfo[]> {

    /* (non-Javadoc)
     * @see com.esri.core.map.CallbackListener#onError(java.lang.Throwable)
     */
    @Override
    public void onError(Throwable e) {
      if (_queryActionListener != null) {
        _queryActionListener.onError(e);
      } else {
        e.printStackTrace();
      }
    }

    /* (non-Javadoc)
     * @see com.esri.core.map.CallbackListener#onCallback(java.lang.Object)
     */
    @Override
    public void onCallback(AttachmentInfo[] objs) {
      _attachmentInfos = objs;
      if (_queryActionListener != null) {
        _queryActionListener.onSuccess(null);
      }
      SwingUtilities.invokeLater(new Runnable() {	
        @Override
       public void run() {
        setListData(_attachmentInfos);	
       }
      });
    }
  }

  /**
   * The Class AttachmentInfoListBoxRenderer.
   */
  class AttachmentInfoListBoxRenderer extends JLabel implements ListCellRenderer {

    private static final long serialVersionUID = 1L;

    /* (non-Javadoc)
     * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
     */
    @Override
    public Component getListCellRendererComponent(JList list, Object value,
        int index, boolean isSelected, boolean cellHasFocus) {
      if (isSelected) {
        setBackground(list.getSelectionBackground());
        setForeground(list.getSelectionForeground());
        setBorder(new LineBorder(list.getSelectionBackground(), 2));
      } else {
        setBackground(list.getBackground());
        setForeground(list.getForeground());
        setBorder(new LineBorder(list.getBackground(), 2));
      }

      setFont(list.getFont());

      AttachmentInfo af = (AttachmentInfo)value;
      setText(af.getName());
      return this;
    }
  }

  /**
   * Instantiates a new list.
   */
  public JAttachmentList(JProgressBar progressBar) {
    setCellRenderer(new AttachmentInfoListBoxRenderer());
    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
    _progressBar = progressBar;
  }
  
  /**
   * Instantiates a new list.
   * @param listener attachment action listener. This listener does not have a {@link AttachmentActionEvent}.
   * @since 10.2
   */
  public JAttachmentList(AttachmentActionListener listener) {
    this((JProgressBar) null);
    this._queryActionListener = listener;
  }
  
  /* 
   * Override that prevents incompatible selection mode being set
   * 
   * (non-Javadoc)
   * @see javax.swing.JList#setSelectionMode(int)
   */
  @Override
  public void setSelectionMode(int selectionMode) {
    if (selectionMode != ListSelectionModel.SINGLE_SELECTION) {
      throw new RuntimeException("Only single selection mode is supported");
    }
    super.setSelectionMode(selectionMode);
  }
  
  /* (non-Javadoc)
   * @see javax.swing.JComponent#processMouseEvent(java.awt.event.MouseEvent)
   * 
   * Override to clear selection when click is not over a list item.
   */
  @Override
  protected void processMouseEvent(MouseEvent e) {
    if (e.getClickCount() > 0) {
      int index = locationToIndex(e.getPoint());
      if (getCellBounds(index, index) != null && getCellBounds(index, index).contains(e.getPoint()))
      {
        setSelectedIndex(index);
      } else {
        clearSelection();
      } 
    } else {
      super.processMouseEvent(e);
    }
  }

  /* (non-Javadoc)
   * @see javax.swing.JList#getToolTipText(java.awt.event.MouseEvent)
   */
  @Override
  public String getToolTipText(MouseEvent e)
  {
    String tooltip = null;

    int index = locationToIndex(e.getPoint ());

    if (index > -1)
    {
      if (getCellBounds(index, index).contains(e.getPoint())) {
        ListModel model = getModel();
        AttachmentInfo af = (AttachmentInfo)model.getElementAt(index);
        int oid = (Integer)_graphic.getAttributeValue(_featureLayer.getObjectIdField());
        
        tooltip = "<html>Name: " + af.getName() 
          + "<br>Size: " + FormatUtils.formatMemory(af.getSize()) 
          + "<br>URI: " + _featureLayer.getAttachmentURL(oid, (int)af.getId()) + "<br>" 
          + af.getContentType() + "</html>";
      }
    }
    
    return tooltip;
  }
  
  /**
   * Gets the feature layer.
   *
   * @return the feature layer
   */
  public ArcGISFeatureLayer getFeatureLayer() {
    return _featureLayer;
  }
  
  /**
   * Gets the graphic.
   *
   * @return the graphic
   */
  public Graphic getGraphic() {
    return _graphic;
  }
  
  /**
   * Populates the list.
   *
   * @param graphic the graphic
   * @param featureLayer the feature layer
   */
  public void populateList(Graphic graphic, ArcGISFeatureLayer featureLayer) {
    _graphic = graphic;
    _featureLayer = featureLayer;
    
    int oid = (Integer)_graphic.getAttributeValue(_featureLayer.getObjectIdField());
    
    if (_queryActionListener != null) {
      _queryActionListener.onStart();
    }
    
    _featureLayer.queryAttachmentInfos(oid, _queryAttachmentCallback);
  }
  
  public void refresh() {
    int oid = (Integer)_graphic.getAttributeValue(_featureLayer.getObjectIdField());
    
    if (_queryActionListener != null) {
      _queryActionListener.onStart();
    }
    
    _featureLayer.queryAttachmentInfos(oid, _queryAttachmentCallback);
  }
  
  private static final long serialVersionUID = 1L;
}
