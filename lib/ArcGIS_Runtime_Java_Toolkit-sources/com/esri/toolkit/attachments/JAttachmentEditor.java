/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.toolkit.attachments;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.map.AttachmentInfo;
import com.esri.core.map.Feature;
import com.esri.map.FeatureLayer;
import com.esri.map.JMap;
import com.esri.toolkit.overlays.HitTestEvent;
import com.esri.toolkit.overlays.HitTestListener;
import com.esri.toolkit.overlays.HitTestOverlay;
import com.esri.toolkit.utilities.ExceptionHandler;

/**
 * This class provides a UI component to edit feature attachments in a <code>FeatureLayer</code>. 
 * A clicked feature is selected and highlighted using a {@link HitTestOverlay}. The attachment editor 
 * displays a list of existing attachments for the selected feature and provides the options to:
 * <ul>
 * <li>add an attachment
 * <li>view an attachment
 * <li>delete an attachment
 * </ul>
 * @usage
 * <code><pre>
 * JMap jMap = new JMap();
 * FeatureLayer featureLayer = ...
 * JAttachmentEditor attachmentEditor = new JAttachmentEditor(jMap, featureLayer);
 * // or:
 * // JAttachmentEditor attachmentEditor = new JAttachmentEditor();
 * // attachmentEditor.setParameters(jMap, featureLayer); 
 * </pre></code>
 * @since 10.2.3
 */
public class JAttachmentEditor extends JComponent {

    private JMap map;
    private FeatureLayer featureLayer;
    private Feature currentFeature;
    private HitTestOverlay hitTestOverlay = null;
    
    private JAttachmentList attachmentList;
    private JButton btnView;
    private JButton btnAdd;
    private JButton btnDelete;
    private ViewAttachmentAction viewAction;
    private AddAttachmentAction addAction;
    private DeleteAttachmentAction deleteAction;
    
    private JProgressBar progressBar;
    
    private class Strings {
      private final static String DEFAULT_TITLE = "Attachments";
      private final static String VIEW     = "View";
      private final static String VIEWING  = "Fetching attachment...";
      private final static String QUERYING = "Fetching list of attachments...";
      private final static String ADD      = "Add";
      private final static String ADDING   = "Adding attachment...";
      private final static String DELETE   = "Delete";
      private final static String DELETING = "Deleting attachment...";
    }
    
    private enum ActionType {
      QUERY(Strings.QUERYING),
      VIEW(Strings.VIEWING),
      ADD(Strings.ADDING),
      DELETE(Strings.DELETING);
      
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
     * Instantiates a new JAttachmentEditor. Assumes that the feature layer has been initialized.
     *
     * @param map the map, should not be null.
     * @param featureLayer a feature layer whose feature table is of type
     * {@link GeodatabaseFeatureTable}, cannot be null.
     * @throws NullPointerException if any of the inputs is null.
     * @throws RuntimeException if the type of feature table of the feature 
     * layer is not {@link GeodatabaseFeatureTable}.
     */
    public JAttachmentEditor(JMap map, FeatureLayer featureLayer) {
        setParameters(map, featureLayer);
        initialize();
    }
    
    /**
     * Associates the given map and feature layer with this attachment editor.
     * 
     * @param map the map, cannot be null. A {@link HitTestOverlay}
     * will be added to this map and associated to the input feature layer.
     * @param featureLayer a feature layer whose feature table is of type
     * {@link GeodatabaseFeatureTable}, cannot be null.
     * @throws NullPointerException if any of the inputs is null.
     * @throws RuntimeException if the type of feature table of the feature 
     * layer is not {@link GeodatabaseFeatureTable}.
     */
    public void setParameters(final JMap map, final FeatureLayer featureLayer) {
      if (map == null) {
        throw new NullPointerException("Input map should not be null.");
      }
      if (featureLayer == null) {
        throw new NullPointerException("Input feature layer should not be null.");
      }
      if (!(featureLayer.getFeatureTable() instanceof GeodatabaseFeatureTable)) {
        throw new RuntimeException("The feature table of the input feature layer " +
            "should be of type GeodatabaseFeatureTable.");
      }
      GeodatabaseFeatureTable featureTable = (GeodatabaseFeatureTable) featureLayer.getFeatureTable();

      if (!featureTable.hasAttachments()) {
        throw new RuntimeException("Input feature layer does not support attachments.");
      }
      if (hitTestOverlay != null && this.map != null) {
        this.map.removeMapOverlay(hitTestOverlay);
      }

      hitTestOverlay = new HitTestOverlay(featureLayer);
      hitTestOverlay.addHitTestListener(new HitTestListener() {
        @Override
        public void featureHit(HitTestEvent event) {
          int topFeatureIndex = hitTestOverlay.getHitFeatures().size() - 1;
          currentFeature = hitTestOverlay.getHitFeatures().get(topFeatureIndex);
          featureLayer.clearSelection();
          featureLayer.selectFeature(currentFeature.getId());
          attachmentList.populateList(currentFeature, featureLayer);
          updateButtons();
          addAction.setParameters(
              attachmentList.getFeature(),
              attachmentList.getFeatureLayer());
        }
      });
      map.addMapOverlay(hitTestOverlay);

      this.map = map;
      this.featureLayer = featureLayer;
    }

    /**
     * Gets the map associated with this attachment editor.
     * 
     * @return the map.
     */
    public JMap getMap() {
      return map;
    }

    /**
     * Gets the feature layer associated with this attachment editor.
     *
     * @return the feature layer
     */
    public FeatureLayer getFeatureLayer() {
        return featureLayer;
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

        btnView = new JButton(Strings.VIEW);
        btnView.setFocusPainted(false);
        buttonsPanel.add(btnView);
        btnAdd = new JButton(Strings.ADD);
        btnAdd.setFocusPainted(false);
        buttonsPanel.add(btnAdd);
        btnDelete = new JButton(Strings.DELETE);
        btnDelete.setFocusPainted(false);
        buttonsPanel.add(btnDelete);
        
        viewAction = new ViewAttachmentAction();
        viewAction.addAttachmentActionListener(new GenericAttachmentActionListener(ActionType.VIEW));
        addAction = new AddAttachmentAction();
        addAction.addAttachmentActionListener(new GenericAttachmentActionListener(ActionType.ADD));
        deleteAction = new DeleteAttachmentAction();
        deleteAction.addAttachmentActionListener(new GenericAttachmentActionListener(ActionType.DELETE));

        btnView.addActionListener(viewAction);
        btnAdd.addActionListener(addAction);
        btnDelete.addActionListener(deleteAction);

        // irrespective of height specified, flow layout seems to
        // set the height based on its contents.
        buttonsPanel.setMaximumSize(new Dimension(32767, 2 * gap));
        
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setString(Strings.DEFAULT_TITLE);
        progressBar.setVisible(true);
        attachmentEditorPanel.add(progressBar);

        JScrollPane attachmentListPane = new JScrollPane();
        attachmentEditorPanel.add(attachmentListPane);

        AttachmentActionListener queryAttachmentActionListener = 
          new GenericAttachmentActionListener(ActionType.QUERY);
        attachmentList = new JAttachmentList(queryAttachmentActionListener);
        attachmentListPane.setViewportView(attachmentList);
        
        // Listen to list selection events to control button enabled/disabled
        // and set the action parameters.
        attachmentList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                if (!attachmentList.getValueIsAdjusting()) {
                    if (attachmentList.getSelectedValue() != null) {
                        viewAction.setParameters(
                            attachmentList.getFeature(),
                            attachmentList.getFeatureLayer(),
                            (AttachmentInfo) attachmentList.getSelectedValue());
                        deleteAction.setParameters(
                            attachmentList.getFeature(),
                            attachmentList.getFeatureLayer(),
                            (AttachmentInfo) attachmentList.getSelectedValue());
                    }
                    updateButtons();
                }
            }
        });
        updateButtons();
    }

    private void updateButtons() {
        Object obj = attachmentList.getSelectedValue();
        btnView.setEnabled(obj != null);
        btnAdd.setEnabled(attachmentList.getFeature() != null);
        btnDelete.setEnabled(obj != null);
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
            progressBar.setString(actionType.getText());
            progressBar.setIndeterminate(true);
          }
        });
      }

      @Override
      public void onSuccess(AttachmentActionEvent event) {
        if (actionType == ActionType.ADD || actionType == ActionType.DELETE) {
          attachmentList.refresh();
        }
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            progressBar.setString(Strings.DEFAULT_TITLE);
            progressBar.setIndeterminate(false);
          }
        });
      }

      @Override
      public void onError(final Throwable e) {
        String errMsg = "Failed to " + actionType + " attachment because: " + e.getMessage() +
            ". Check whether file type and size are supported.";
        ExceptionHandler.handleException(map, errMsg);
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            progressBar.setString(Strings.DEFAULT_TITLE);
            progressBar.setIndeterminate(false);
          }
        });
      }
    }
    
    private static final long serialVersionUID = 1L;
}
