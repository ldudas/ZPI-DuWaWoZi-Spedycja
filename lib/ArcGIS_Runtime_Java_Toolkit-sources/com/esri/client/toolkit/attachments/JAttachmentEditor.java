/* Copyright 2014 Esri
 
All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.client.toolkit.attachments;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.esri.client.toolkit.overlays.HitTestEvent;
import com.esri.client.toolkit.overlays.HitTestListener;
import com.esri.client.toolkit.overlays.HitTestOverlay;
import com.esri.core.map.AttachmentInfo;
import com.esri.core.map.Graphic;
import com.esri.map.ArcGISFeatureLayer;
import com.esri.map.FeatureLayer;
import com.esri.map.JMap;

/**
 * This provides a UI to work with feature attachments in an <code>ArcGISFeatureLayer</code>. 
 * The following options are available on the selected feature:
 * <ul>
 * <li>display a list of attachments.</li>
 * <li>add an attachment.</li>
 * <li>view an attachment.</li>
 * <li>delete an attachment.</li>
 * </ul>
 * @deprecated From 10.2.3, use {@link com.esri.toolkit.attachments.JAttachmentEditor} instead.
 * This new package supports {@link FeatureLayer} that was introduced in 10.2.
 */
@Deprecated
public class JAttachmentEditor extends JComponent {

    JMap _map;
    ArcGISFeatureLayer _featureLayer;
    HitTestOverlay _hitTestOverlay = null;
    JAttachmentList _attachmentList;
    JButton _btnView;
    JButton _btnAdd;
    JButton _btnDelete;
    ViewAttachmentAction _viewAction;
    AddAttachmentAction _addAction;
    DeleteAttachmentAction _deleteAction;
    Graphic _currentGraphic;

    JProgressBar _progressBar;
    private final static String DEFAULT_TITLE = "Attachments";
    
    private enum ActionType {
      QUERY("Querying attachments..."),
      ADD("Adding attachment..."),
      DELETE("Deleting attachment...");
      
      private String text;
      
      private ActionType(String text) {
        this.text = text;
      }
      
      public String getText() {
        return text;
      }
    }

    /**
     * Instantiates a new JAttachmentEditor.
     */
    public JAttachmentEditor() {
        initialize();
    }

    /**
     * Instantiates a new JAttachmentEditor.
     *
     * @param map the map
     */
    public JAttachmentEditor(JMap map) {
        _map = map;
        initialize();
    }

    /**
     * Instantiates a new JAttachmentEditor. Assumes that the feature layer has been initialized.
     *
     * @param map the map
     * @param featureLayer the feature layer
     */
    public JAttachmentEditor(JMap map, ArcGISFeatureLayer featureLayer) {
        _map = map;
        initialize();
        setFeatureLayer(featureLayer);
    }

    /**
     * Sets the feature layer. Assumes that the feature layer has been initialized.
     *
     * @param featureLayer the new feature layer
     */
    public void setFeatureLayer(final ArcGISFeatureLayer featureLayer) {
        if (featureLayer == null) {
            return;
        }

        if (!featureLayer.hasAttachments()) {
            throw new RuntimeException("Feature layer does not support attachments.");
        }
        if (_hitTestOverlay != null) {
            _map.removeMapOverlay(_hitTestOverlay);
        }
        _hitTestOverlay = new HitTestOverlay(featureLayer);
        _hitTestOverlay.addHitTestListener(new HitTestListener() {

            @Override
            public void featureHit(HitTestEvent event) {
                _currentGraphic = _hitTestOverlay.getHitGraphics().get(0);
                ((ArcGISFeatureLayer) _hitTestOverlay.getLayer()).clearSelection();
                ((ArcGISFeatureLayer) _hitTestOverlay.getLayer()).select(_currentGraphic.getUid());
                _attachmentList.populateList(_currentGraphic, _featureLayer);
                enableButtons();
                _addAction.setParameters(
                    _attachmentList.getGraphic(),
                    _attachmentList.getFeatureLayer());
            }
        });

        _map.addMapOverlay(_hitTestOverlay);

        _featureLayer = featureLayer;
        enableButtons();
    }

    /**
     * Gets the feature layer.
     *
     * @return the feature layer
     */
    public ArcGISFeatureLayer getFeatureLayer() {
        return _featureLayer;
    }

    private void initialize() {
        // spacing around buttons
        int gap = 5;

        setPreferredSize(new Dimension(220, 300));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel attachmentEditorPanel = new JPanel();
        attachmentEditorPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        add(attachmentEditorPanel);
        attachmentEditorPanel.setLayout(new BoxLayout(attachmentEditorPanel, BoxLayout.Y_AXIS));

        JPanel buttonsPanel = new JPanel();
        attachmentEditorPanel.add(buttonsPanel);
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, gap, gap));

        _btnView = new JButton("View");
        buttonsPanel.add(_btnView);

        _btnAdd = new JButton("Add");
        buttonsPanel.add(_btnAdd);

        _btnDelete = new JButton("Delete");
        buttonsPanel.add(_btnDelete);

        // irrespective of height specified, flow layout seems to
        // set the height based on its contents.
        buttonsPanel.setMaximumSize(new Dimension(32767, 2 * gap));
        
        _progressBar = new JProgressBar();
        _progressBar.setStringPainted(true);
        _progressBar.setString(DEFAULT_TITLE);
        _progressBar.setVisible(true);
        attachmentEditorPanel.add(_progressBar);

        JScrollPane attachmentListPane = new JScrollPane();
        attachmentEditorPanel.add(attachmentListPane);

        AttachmentActionListener queryAttachmentActionListener = 
          new GenericAttachmentActionListener(ActionType.QUERY);
        _attachmentList = new JAttachmentList(queryAttachmentActionListener);
        attachmentListPane.setViewportView(_attachmentList);

        _viewAction = new ViewAttachmentAction();
        
        _addAction = new AddAttachmentAction();
        _addAction.addAttachmentActionListener(new GenericAttachmentActionListener(ActionType.ADD));

        _deleteAction = new DeleteAttachmentAction();
        _deleteAction.addAttachmentActionListener(new GenericAttachmentActionListener(ActionType.DELETE));

        _btnAdd.addActionListener(_addAction);
        _btnDelete.addActionListener(_deleteAction);
        _btnView.addActionListener(_viewAction);

        enableButtons();

        // Listen to list selection events to control button enabled/disabled
        // and set the action parameters.
        _attachmentList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                if (!_attachmentList.getValueIsAdjusting()) {
                    if (_attachmentList.getSelectedValue() != null) {
                        _viewAction.setParameters(
                            _attachmentList.getGraphic(),
                            _attachmentList.getFeatureLayer(),
                            (AttachmentInfo)_attachmentList.getSelectedValue());
                        _deleteAction.setParameters(
                            _attachmentList.getGraphic(),
                            _attachmentList.getFeatureLayer(),
                            (AttachmentInfo)_attachmentList.getSelectedValue());
                    }
                    enableButtons();
                }
            }
        });
    }

    private void enableButtons() {
        Object obj = _attachmentList.getSelectedValue();
        _btnView.setEnabled(obj != null);
        _btnAdd.setEnabled(_attachmentList.getGraphic() != null);
        _btnDelete.setEnabled(obj != null);
    }

    class GenericAttachmentActionListener implements AttachmentActionListener {
      
      private ActionType actionType;
      
      GenericAttachmentActionListener(ActionType actionType) {
        this.actionType = actionType;
      }

      @Override
      public void onStart() {
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            _progressBar.setString(actionType.getText());
            _progressBar.setIndeterminate(true);
          }
        });
      }

      @Override
      public void onSuccess(AttachmentActionEvent event) {
        if (actionType == ActionType.ADD || actionType == ActionType.DELETE) {
          _attachmentList.refresh();
        }
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            _progressBar.setString(DEFAULT_TITLE);
            _progressBar.setIndeterminate(false);
          }
        });
      }

      @Override
      public void onError(final Throwable e) {
        JOptionPane.showMessageDialog(
          _map.getParent(),
          "Failed to " + actionType + " attachment because: " + e.getMessage() +
          ". Check whether file type and size are supported.");
        e.printStackTrace();
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            _progressBar.setString(DEFAULT_TITLE);
            _progressBar.setIndeterminate(false);
          }
        });
      }
    }
    
    private static final long serialVersionUID = 1L;
}
