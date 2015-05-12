/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.toolkit.editing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.esri.toolkit.overlays.DrawingCompleteEvent;
import com.esri.toolkit.overlays.DrawingCompleteListener;
import com.esri.toolkit.overlays.DrawingOverlay;
import com.esri.toolkit.utilities.ExceptionHandler;
import com.esri.core.geodatabase.GeodatabaseEditError;
import com.esri.core.geodatabase.GeodatabaseFeatureServiceTable;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Geometry.Type;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.Feature;
import com.esri.core.map.Graphic;
import com.esri.core.table.FeatureTable;
import com.esri.core.table.TableException;
import com.esri.map.FeatureLayer;
import com.esri.map.JMap;
import com.esri.map.Layer;
import com.esri.map.Layer.LayerStatus;
import com.esri.map.LayerEvent;
import com.esri.map.LayerInitializeCompleteEvent;
import com.esri.map.LayerInitializeCompleteListener;
import com.esri.map.LayerListEventListenerAdapter;
import com.esri.map.MapEvent;
import com.esri.map.MapEventListenerAdapter;
import com.esri.map.popup.PopupDialog;
import com.esri.map.popup.PopupView;
import com.esri.map.popup.PopupViewEvent;
import com.esri.map.popup.PopupViewListener;
import com.esri.toolkit.PrototypeBuilder;
import com.esri.toolkit.PrototypeFeature;

/**
 * This class extends JList to provide a list for picking feature prototypes 
 * from one or more <code>FeatureLayer</code>s and adding features of the chosen 
 * prototype to the map via mouse clicks. Uses a {@link DrawingOverlay} for 
 * drawing features on the map.
 * 
 * @usage
 * <code><pre>
 * JTemplatePicker jPicker = new JTemplatePicker(jMap);
 * jPicker.setWatchMap(true);
 * // customize the picker 
 * jPicker.setIconWidth(36);
 * jPicker.setIconHeight(36);
 * jPicker.setShowNames(true);
 * // add the template picker to, for example, your JPanel
 * jPanel.add(jPicker, BorderLayout.WEST);
 * </pre></code>
 * 
 * @since 10.2.3
 */
public class JTemplatePicker extends JList {

  DrawingOverlay drawingOverlay = null;

  @Override
  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    if (isEnabled()) {
      if (drawingOverlay != null) {
        drawingOverlay.setActive(true);
      }
    } else {
      clearSelection();
      if (drawingOverlay != null) {
        drawingOverlay.clearFeature();
        drawingOverlay.setActive(false);
      }
    }
  }
  
  /*
   * Override that makes sure that selection only takes place when over an item
   *
   * (non-Javadoc)
   * @see javax.swing.JComponent#processMouseEvent(java.awt.event.MouseEvent)
   *
   */
  @Override
  protected void processMouseEvent(MouseEvent e) {
    if (isEnabled()) {
      if (e.getClickCount() > 0) {
        int index = locationToIndex(e.getPoint());
        if (index > -1) {
          if (getCellBounds(index, index).contains(e.getPoint()))
          {
            setSelectedIndex(index);
          }
        }
      } else {
        super.processMouseEvent(e);
      }
    }
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

  /**
   * Creates an instance that is not yet associated with a JMap. Associated this 
   * instance with a map using {@link #setMap(JMap)}.
   */
  public JTemplatePicker() {
    setLayout(new BorderLayout(0, 0));

    setCellRenderer(new PrototypeFeatureListBoxRenderer());
    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    setLayoutOrientation(JList.VERTICAL_WRAP);
    setVisibleRowCount(-1);
  }

  private JMap _map;
  private boolean _showNames = true;
  private boolean _showAttributeEditor = false;
  private int _iconWidth = 25;
  private int _iconHeight = 25;

  /**
   * Creates an instance and associates it with a JMap; use {@link #setWatchMap(boolean) setWatchMap} and/or
   * {@link #addLayersFromMap()} to complete setting up this object.
   * 
   * @param map the map this picker is going to work with
   */
  public JTemplatePicker(JMap map) {
    this();
    if (map == null) {
      throw new IllegalArgumentException("Map cannot be null");
    }
    _map = map;

    init();
  }

  /**
   * Internal initialization
   */
  private void init() {
    drawingOverlay = new DrawingOverlay();
    _map.addMapEventListener(new MapEventListenerAdapter() {

      /*
       * Initialization steps carried out when the associated JMap becomes ready
       *
       * (non-Javadoc)
       * @see com.esri.map.MapEventListener#mapReady(com.esri.map.MapEvent)
       */
      @Override
      public void mapReady(MapEvent event) {
        _map.addMapOverlay(drawingOverlay);

        drawingOverlay.addDrawingCompleteListener(new DrawingCompleteListener() {

          @Override
          public void drawingCompleted(final DrawingCompleteEvent event) {
            Feature newFeature = ((DrawingOverlay) event.getSource()).getFeature();
            
            final FeatureLayer featureLayer = ((PrototypeFeature) getSelectedValue()).getFeatureLayer();
            if (isShowAttributeEditor() && featureLayer.getFeatureTable() instanceof GeodatabaseFeatureTable) {
              PopupView editView = PopupView.createEditView("Edit attributes", featureLayer);
              editView.setFeature(_map, newFeature);
              final PopupDialog popup = _map.createPopup(new JComponent[] { editView }, newFeature);
              Envelope graphicBounds = new Envelope();
              newFeature.getGeometry().queryEnvelope(graphicBounds);
              com.esri.core.geometry.Point center = _map.toScreenPoint(graphicBounds.getCenter());
              popup.setAnchorPoint(new Point((int) center.getX(), (int) center.getY()));
              editView.addPopupViewListener(new PopupViewListener() {
                
                @Override
                public void onCommitEdit(PopupViewEvent popupViewEvent, Feature feature) {
                  popup.close();
                  addFeatureToLayer(feature, featureLayer);
                }
                
                @Override
                public void onCancelEdit(PopupViewEvent popupViewEvent, Feature feature) {
                  // user cancelled so abort the new feature
                  popup.close();
                  ((DrawingOverlay) event.getSource()).clearFeature();
                }
              });
              popup.setVisible(true);
            } else {
              addFeatureToLayer(newFeature, featureLayer);
            }
          }
        });

        addListSelectionListener(new ListSelectionListener() {

          /*
           * Listener for list selection which sets the selected item into the AddFeatureOverlay (non-Javadoc)
           * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
           */
          @Override
          public void valueChanged(ListSelectionEvent arg0) {
            if (getSelectedIndex() >= 0) {
              drawingOverlay.setUp((PrototypeFeature) getSelectedValue());
            }
          }
        });
      }
    });

    _map.getLayers().addLayerListEventListener(new LayerListEventListenerAdapter(){

      /*
       * If the JMap is being watched adds feature layers to the picker when they are
       * added to the JMap
       *
       * (non-Javadoc)
       * @see com.esri.map.LayerListEventListenerAdapter#layerAdded(com.esri.map.LayerEvent)
       */
      @Override
      public void layerAdded(LayerEvent event) {
        if (_watchMap) {
          Layer layer = event.getChangedLayer();
          if (layer instanceof FeatureLayer) {
            addLayer((FeatureLayer)layer);
          }
        }
      }

      /*
       * If the JMap is being watched adds feature layers to the picker when they are
       * added to the JMap
       */
      @Override
      public void multipleLayersAdded(LayerEvent event) {
        if (_watchMap) {                
          for (Layer layer : event.getChangedLayers().values()) {
            if (layer instanceof FeatureLayer) {
              addLayer((FeatureLayer)layer);
            } 
          }
        }
      }

      /*
       * If the JMap is being watched removes feature layers from the picker when they
       * are removed from the JMap
       *
       * (non-Javadoc)
       * @see com.esri.map.LayerListEventListenerAdapter#layerRemoved(com.esri.map.LayerEvent)
       */
      @Override
      public void layerRemoved(LayerEvent event) {
        if (_watchMap) {
          Layer layer = event.getChangedLayer();
          if (layer instanceof FeatureLayer) {
            removeLayer((FeatureLayer)layer);
          }
        }
      }

      @Override
      public void multipleLayersRemoved(LayerEvent event) {
        if(_watchMap){
          for(Layer curLayer: event.getChangedLayers().values()){
            if(curLayer instanceof FeatureLayer){
              removeLayer((FeatureLayer)curLayer);
            }
          }
        }
      }

    });
  }

  private boolean _watchMap = false;

  /**
   * Set to true to have the picker respond to map events such as layer added or removed
   *
   * @param watchMap
   */
  public void setWatchMap(boolean watchMap) {
    _watchMap = watchMap;
  }

  /**
   * @return true if the picker is watching map events
   */
  public boolean getWatchMap() {
    return _watchMap;
  }

  /**
   * Sets the map this picker will work with; use {@link #setWatchMap(boolean) setWatchMap} and/or
   * {@link #addLayersFromMap()} to complete setting up this object.
   * 
   * @param map the JMap the picker will work with
   */
  public void setMap(JMap map) {
    if (_map == null) {
      _map = map;
      init();
    } else {
      throw new IllegalArgumentException("Map has already been set");
    }
  }

  private HashMap<FeatureLayer, List<PrototypeFeature>> pgMap = new HashMap<FeatureLayer, List<PrototypeFeature>>();

  /**
   * Updates the picker list
   */
  private void updateList() {
    Vector<PrototypeFeature> pgVector = new Vector<PrototypeFeature>();
    for (List<PrototypeFeature> pgList : pgMap.values()) {
      pgVector.addAll(pgList);
    }

    setListData(pgVector);
  }

  private ArrayList<FeatureLayer> pending = new ArrayList<FeatureLayer>();

  /**
   * Populate the picker with the feature layers in the map
   */
  public void addLayersFromMap() {
    if (_map == null) {
      throw new NullPointerException("Map has not been set");
    }
    for (Layer layer : _map.getLayers()) {
      if (layer instanceof FeatureLayer) {
        FeatureLayer featureLayer = (FeatureLayer)layer;
        addLayer(featureLayer);
      }
    }
  }

  /**
   * Adds a feature layer to the picker. If the layer is not already initialized then the
   * layer will be added once it becomes ready.
   *
   * @param featureLayer
   * 		feature layer to add to the picker
   */
  public void addLayer(FeatureLayer featureLayer) {
    if (featureLayer == null) {
      throw new IllegalArgumentException("featurLayer cannot be null");
    }
    
    if (featureLayer.getStatus() != LayerStatus.INITIALIZED) {
      featureLayer.addLayerInitializeCompleteListener(new LayerInitializeCompleteListener() {

        @Override
        public void layerInitializeComplete(LayerInitializeCompleteEvent e) {
          FeatureLayer layer = (FeatureLayer)e.getLayer();
          // Handle exception to prevent layer initialization being failed
          try {
            internalAddLayer(layer);
          } catch (IllegalArgumentException ex) {
            ExceptionHandler.handleException(JTemplatePicker.this, ex);
          }
        }
      });
    } else {
      internalAddLayer(featureLayer);
    }
  }

  /**
   * Extracts prototype features from the layer and adds them to the picker.
   *
   * @param featureLayer
   */
  private void internalAddLayer(FeatureLayer featureLayer) {
    if (featureLayer.getStatus() == LayerStatus.ERRORED) {
      String name = featureLayer.getName();
      if (name != null && !name.isEmpty()) {
        throw new IllegalArgumentException("Feature layer (" + featureLayer.getName() + ") has status " + LayerStatus.ERRORED);
      } else {
        throw new IllegalArgumentException("Feature layer has status " + LayerStatus.ERRORED);
      }
    }
    
    FeatureTable featureTable = featureLayer.getFeatureTable();
    // check that new features can be created on this feature table
    if (featureTable instanceof GeodatabaseFeatureTable && ((GeodatabaseFeatureTable) featureTable).canCreate()) {
      PrototypeBuilder pb = new PrototypeBuilder(featureLayer);
      pgMap.put(featureLayer, pb.build());

      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          updateList();
        }
      });
    } else {
      throw new IllegalArgumentException("Feature layer (" + featureLayer.getName() + ") must allow new features to be created. ");
    }
  }

  /**
   * @param featureLayer
   * 		feature layer to remove from the picker
   */
  public void removeLayer(FeatureLayer featureLayer) {
    if (!pending.remove(featureLayer)) {
      if (pgMap.remove(featureLayer) != null)
      {
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            updateList();
          }
        });
      }
    }
  }

  /**
   * @return the width of the icon
   */
  public int getIconWidth() {
    return _iconWidth;
  }

  /**
   * @return the height of the icon
   */
  public int getIconHeight() {
    return _iconHeight;
  }

  /**
   * Set the width of the icon
   *
   * @param width
   */
  public void setIconWidth(int width) {
    _iconWidth = width;
    
    updateList();
  }

  /**
   * Set the height of the icon
   *
   * @param height
   */
  public void setIconHeight(int height) {
    _iconHeight = height;
    
    updateList();
  }

  /**
   * @return true if names of feature types/templates are to be shown in the picker
   */
  public boolean isShowNames() {
    return _showNames;
  }

  /**
   * @param showNames
   * 		true to show names of feature types/templates in the picker
   */
  public void setShowNames(boolean showNames) {
    _showNames = showNames;
    
    updateList();
  }

  /**
   * Sets whether the attribute editor dialog is shown when a feature is created.
   *
   * @param showAttributeEditor true if dialog is to be shown
   */
  public void setShowAttributeEditor(boolean showAttributeEditor) {
    _showAttributeEditor = showAttributeEditor;
  }

  /**
   * Checks if the attribute editor dialog is shown when a feature is created.
   *
   * @return true, if dialog is to be shown
   */
  public boolean isShowAttributeEditor() {
    return _showAttributeEditor;
  }

  /*
   * Override that provides a per item tool tip
   *
   * (non-Javadoc)
   * @see javax.swing.JList#getToolTipText(java.awt.event.MouseEvent)
   */
  @Override
  public String getToolTipText(MouseEvent e)
  {
    String tooltip = null;
    if (!_showNames) {
      int index = locationToIndex(e.getPoint ());

      if (index > -1)
      {
        if (getCellBounds(index, index).contains(e.getPoint())) {
          ListModel model = getModel();
          PrototypeFeature pg = (PrototypeFeature)model.getElementAt(index);

          tooltip = pg.getName();
        }
      }
    }

    return tooltip;
  }

  public DrawingOverlay getOverlay(){
    return drawingOverlay;
  }

  /**
   * This class provides a custom renderer for the list box
   *
   */
  class PrototypeFeatureListBoxRenderer extends JLabel implements ListCellRenderer {

    private static final long serialVersionUID = 1L;

    public PrototypeFeatureListBoxRenderer() {
      setOpaque(true);
    }

    /*
     * Renders a list item as an icon with an optional name string
     *
     * (non-Javadoc)
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

      if (_showNames) {
        setText(((PrototypeFeature)value).getName());
      } else {
        setText(null);
      }
      setIcon(((PrototypeFeature)value).getIcon(_iconWidth, _iconHeight));

      return this;
    }
  }

  private void addFeatureToLayer(Feature g, FeatureLayer featureLayer) {
    Graphic simplifiedGraphic = new Graphic(g.getGeometry(), g.getSymbol(), g.getAttributes());
    if (g.getGeometry().getType() != Type.POINT && g.getGeometry().getType() != Type.MULTIPOINT) {
      Geometry geom = GeometryEngine.simplify(g.getGeometry(), _map.getSpatialReference());
      simplifiedGraphic = new Graphic(geom, g.getSymbol(), g.getAttributes());
    }

    final FeatureTable featureTable = featureLayer.getFeatureTable();
    
    try {
      featureTable.addFeature(simplifiedGraphic);
      
      if (featureTable instanceof GeodatabaseFeatureServiceTable) {
        ((GeodatabaseFeatureServiceTable)featureTable).applyEdits(new CallbackListener<List<GeodatabaseEditError>>() {
          boolean error = false;
          
          @Override
          public void onError(Throwable e) {
            error = true;
            String errMsg = "Failed to apply edits to server " + featureTable.getTableName();
            errMsg += ". " + e.getMessage();
            ExceptionHandler.handleException(JTemplatePicker.this, errMsg);
          }
          
          @Override
          public void onCallback(List<GeodatabaseEditError> errors) {
            if (error || !errors.isEmpty()) {
              String errMsg = "Failed to apply edits to server " + featureTable.getTableName();
              errMsg += ". " + errors;
              ExceptionHandler.handleException(JTemplatePicker.this, errMsg);
            }
          }
        });
      }
    } catch (TableException | RuntimeException e) {
      ExceptionHandler.handleException(JTemplatePicker.this, e);
    } finally {
      drawingOverlay.clearFeature();
    }
  }

  private static final long serialVersionUID = 1L;
}
