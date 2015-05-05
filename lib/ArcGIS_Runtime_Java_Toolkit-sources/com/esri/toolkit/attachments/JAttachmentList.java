/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.toolkit.attachments;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.map.AttachmentInfo;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.Feature;
import com.esri.map.FeatureLayer;
import com.esri.toolkit.utilities.ExceptionHandler;
import com.esri.toolkit.utilities.FormatUtils;

/**
 * This class provides a UI component to display a list of attachments for a given 
 * feature in a FeatureLayer; used by the {@link JAttachmentEditor} component.
 * @since 10.2.3
 */
public class JAttachmentList extends JList {
  private List<AttachmentInfo> _attachmentInfos;
  private Feature feature;
  private FeatureLayer featureLayer;
  private AttachmentActionListener _queryActionListener;
  private final AttachmentQueryCallback _queryAttachmentCallback = new AttachmentQueryCallback();

  private final class AttachmentQueryCallback implements CallbackListener<List<AttachmentInfo>> {

    /* (non-Javadoc)
     * @see com.esri.core.map.CallbackListener#onError(java.lang.Throwable)
     */
    @Override
    public void onError(Throwable e) {
      if (_queryActionListener != null) {
        _queryActionListener.onError(e);
      } else {
        ExceptionHandler.handleException(JAttachmentList.this, e);
      }
    }

    /* (non-Javadoc)
     * @see com.esri.core.map.CallbackListener#onCallback(java.lang.Object)
     */
    @Override
    public void onCallback(final List<AttachmentInfo> objs) {
      _attachmentInfos = objs;
      if (_queryActionListener != null) {
        _queryActionListener.onSuccess(null);
      }
      SwingUtilities.invokeLater(new Runnable() {  
        @Override
       public void run() {
         if (objs != null) {
          setListData(_attachmentInfos.toArray());
         } else {
           setListData(new AttachmentInfo[]{});
         }
       }
      });
    }
  }

  /**
   * A custom list cell renderer for feature attachments. 
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
   * Instantiates a new attachment list.
   */
  public JAttachmentList() {
    setCellRenderer(new AttachmentInfoListBoxRenderer());
    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
  }
  
  /**
   * Instantiates a new attachment list.
   * @param listener attachment action listener. This listener does not have a {@link AttachmentActionEvent}.
   */
  public JAttachmentList(AttachmentActionListener listener) {
    this();
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
        AttachmentInfo attachmentInfo = (AttachmentInfo) model.getElementAt(index);
        
        tooltip = "<html>Name: " + attachmentInfo.getName() 
          + "<br>Size: " + FormatUtils.formatMemory(attachmentInfo.getSize()) +
          "</html>";
      }
    }
    
    return tooltip;
  }
  
  /**
   * Gets the feature layer.
   *
   * @return the feature layer
   */
  public FeatureLayer getFeatureLayer() {
    return featureLayer;
  }
  
  /**
   * Gets the feature.
   *
   * @return the feature
   */
  public Feature getFeature() {
    return feature;
  }
  
  /**
   * Populates the list of attachments for the given feature.
   *
   * @param feature the feature
   * @param featureLayer a feature layer whose feature table is of type
   * {@link GeodatabaseFeatureTable}, cannot be null.
   * @throws NullPointerException if any of the inputs is null.
   * @throws RuntimeException if the type of feature table of the feature 
   * layer is not {@link GeodatabaseFeatureTable}.
   */
  public void populateList(Feature feature, FeatureLayer featureLayer) {
    if (feature == null) {
      throw new NullPointerException("Input feature should not be null.");
    }
    if (featureLayer == null) {
      throw new NullPointerException("Input feature layer should not be null.");
    }
    if (!(featureLayer.getFeatureTable() instanceof GeodatabaseFeatureTable)) {
      throw new RuntimeException("The feature table of the input feature layer " +
        "should be of type GeodatabaseFeatureTable.");
    }
    this.feature = feature;
    this.featureLayer = featureLayer;
    refresh();
  }
  
  public void refresh() {
    Thread t = new Thread() {
      @Override
      public void run() {
        boolean successful = false;
        try {
          if (_queryActionListener != null) {
            _queryActionListener.onStart();
          }
          GeodatabaseFeatureTable featureTable = (GeodatabaseFeatureTable) featureLayer.getFeatureTable();
          featureTable.queryAttachmentInfos(feature.getId(), _queryAttachmentCallback);
          successful = true;
        } catch (Exception ex) {
          if (_queryActionListener != null) {
            _queryActionListener.onError(ex);
          }
        }
        if (successful && _queryActionListener != null) {
          _queryActionListener.onSuccess(null);
        }
      }
    };
    t.start();
  }
  
  private static final long serialVersionUID = 1L;
}
