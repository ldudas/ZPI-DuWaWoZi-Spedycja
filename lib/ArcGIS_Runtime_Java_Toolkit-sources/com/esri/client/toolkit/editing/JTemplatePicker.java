/* Copyright 2014 Esri
 
All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.client.toolkit.editing;

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

import com.esri.client.toolkit.PrototypeBuilder;
import com.esri.client.toolkit.PrototypeGraphic;
import com.esri.client.toolkit.overlays.DrawingCompleteEvent;
import com.esri.client.toolkit.overlays.DrawingCompleteListener;
import com.esri.client.toolkit.overlays.DrawingOverlay;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Geometry.Type;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureEditResult;
import com.esri.core.map.Graphic;
import com.esri.map.ArcGISFeatureLayer;
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

/**
 * This class extends JList to provide a list for picking feature prototypes 
 * from one or more <code>ArcGISFeatureLayer</code>s and adding features of the chosen 
 * prototype to the map via mouse clicks.
 * @deprecated From 10.2.3, use {@link com.esri.toolkit.editing.JTemplatePicker} instead.
 */
@Deprecated
public class JTemplatePicker extends JList {

    DrawingOverlay _overlay = null;

    @Override
    public void setEnabled(boolean enabled) {
      super.setEnabled(enabled);
      if (isEnabled()) {
        if (_overlay != null) {
          _overlay.setActive(true);
        }
      } else {
        clearSelection();
        if (_overlay != null) {
          _overlay.clearGraphic();
          _overlay.setActive(false);
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
     * Creates an instance that is not yet associated with a JMap
     */
    public JTemplatePicker() {
        setLayout(new BorderLayout(0, 0));

        setCellRenderer(new PrototypeGraphicListBoxRenderer());
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
     * Creates an instance and associates it with a JMap
     *
     * @param map
     * 		the map this picker is going to work with
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
        _overlay = new DrawingOverlay();
        _map.addMapEventListener(new MapEventListenerAdapter() {

            /*
             * Initialization steps carried out when the associated JMap becomes ready
             *
             * (non-Javadoc)
             * @see com.esri.map.MapEventListener#mapReady(com.esri.map.MapEvent)
             */
            @Override
      public void mapReady(MapEvent event) {
        _map.addMapOverlay(_overlay);

        _overlay.addDrawingCompleteListener(new DrawingCompleteListener() {

          @Override
          public void drawingCompleted(DrawingCompleteEvent event) {
            Graphic g = ((DrawingOverlay) event.getSource()).getGraphic();

            ArcGISFeatureLayer featureLayer = ((PrototypeGraphic) getSelectedValue()).getFeatureLayer();
            if (isShowAttributeEditor()) {
              PopupView editView = PopupView.createEditView("Edit attributes", featureLayer);
              editView.setGraphic(featureLayer, g);
              final PopupDialog popup = _map.createPopup(new JComponent[] { editView }, g);
              Envelope graphicBounds = new Envelope();
              g.getGeometry().queryEnvelope(graphicBounds);
              com.esri.core.geometry.Point center = _map.toScreenPoint(graphicBounds.getCenter());
              popup.setAnchorPoint(new Point((int) center.getX(), (int) center.getY()));
              editView.addPopupViewListener(new PopupViewListener() {

                @Override
                public void onCommitEdit(PopupViewEvent popupViewEvent, Feature graphic) {
                  popup.close();
                }

                @Override
                public void onCancelEdit(PopupViewEvent popupViewEvent, Feature graphic) {
                  popup.close();
                }
              });
              popup.setVisible(true);
            } else {
              addFeatureToLayer(g, featureLayer);
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
              _overlay.setUp((PrototypeGraphic) getSelectedValue());
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
                    if (layer instanceof ArcGISFeatureLayer) {
                        addLayer((ArcGISFeatureLayer)layer);
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
                  if (layer instanceof ArcGISFeatureLayer) {
                    addLayer((ArcGISFeatureLayer)layer);
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
                    if (layer instanceof ArcGISFeatureLayer) {
                        removeLayer((ArcGISFeatureLayer)layer);
                    }
                }
            }

            @Override
            public void multipleLayersRemoved(LayerEvent event) {
                if(_watchMap){
                    for(Layer curLayer: event.getChangedLayers().values()){
                        if(curLayer instanceof ArcGISFeatureLayer){
                            removeLayer((ArcGISFeatureLayer)curLayer);
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
     * @param map
     * 		the JMap the picker will work with
     */
    public void setMap(JMap map) {
        if (_map == null) {
            _map = map;
            init();
        } else {
            throw new IllegalArgumentException("Map has already been set");
        }
    }

    private HashMap<ArcGISFeatureLayer, List<PrototypeGraphic>> pgMap = new HashMap<ArcGISFeatureLayer, List<PrototypeGraphic>>();

    /**
     * Updates the picker list
     */
    private void updateList() {
        Vector<PrototypeGraphic> pgVector = new Vector<PrototypeGraphic>();
        for (List<PrototypeGraphic> pgList : pgMap.values()) {
            pgVector.addAll(pgList);
        }

        setListData(pgVector);
    }

    private ArrayList<ArcGISFeatureLayer> pending = new ArrayList<ArcGISFeatureLayer>();

    /**
     * Populate the picker with the feature layers in the map
     */
    public void addLayersFromMap() {
        if (_map == null) {
            throw new NullPointerException("Map has not been set");
        }
        for (Layer layer : _map.getLayers()) {
            if (layer instanceof ArcGISFeatureLayer) {
                ArcGISFeatureLayer featureLayer = (ArcGISFeatureLayer)layer;
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
    public void addLayer(ArcGISFeatureLayer featureLayer) {
        if (featureLayer == null) {
            throw new IllegalArgumentException("featurLayer cannot be null");
        }

        if (featureLayer.getStatus() != LayerStatus.INITIALIZED) {
            featureLayer.addLayerInitializeCompleteListener(new LayerInitializeCompleteListener() {

                @Override
                public void layerInitializeComplete(LayerInitializeCompleteEvent e) {
                    ArcGISFeatureLayer layer = (ArcGISFeatureLayer)e.getLayer();
                    // Handle exception to prevent layer initialization being failed
                    try {
                      internalAddLayer(layer);
                    } catch (IllegalArgumentException ex) {
                      ex.printStackTrace();
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
    private void internalAddLayer(ArcGISFeatureLayer featureLayer) {
      if (featureLayer.isEditable() && featureLayer.isAllowGeometryUpdates()) {
        PrototypeBuilder pb = new PrototypeBuilder(featureLayer);
        pgMap.put(featureLayer, pb.build());

        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            updateList();
          }
        });
      } else {
        throw new IllegalArgumentException("Feature layer (" + featureLayer.getName() + ") must be editable and allow geometry updates. ");
      }
    }

    /**
     * @param featureLayer
     * 		feature layer to remove from the picker
     */
    public void removeLayer(ArcGISFeatureLayer featureLayer) {
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
     * @return true if names are to be shown in the picker
     */
    public boolean isShowNames() {
        return _showNames;
    }

    /**
     * @param showNames
     * 		true to show names of feature types in the picker
     */
    public void setShowNames(boolean showNames) {
        _showNames = showNames;
        
        updateList();
    }

    /**
     * Sets if the attribute editor dialog is shown when a feature is created.
     *
     * @param True, if dialog is to be shown
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
                    PrototypeGraphic pg = (PrototypeGraphic)model.getElementAt(index);

                    tooltip = pg.getName();
    }
            }
        }

        return tooltip;
    }

    public DrawingOverlay getOverlay(){
        return _overlay;
    }

        /**
     * This class provides a custom renderer for the list box
         *
         */
    class PrototypeGraphicListBoxRenderer extends JLabel implements ListCellRenderer {

        private static final long serialVersionUID = 1L;

        public PrototypeGraphicListBoxRenderer() {
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
                setText(((PrototypeGraphic)value).getName());
            } else {
                setText(null);
            }
            setIcon(((PrototypeGraphic)value).getIcon(_iconWidth, _iconHeight));

            return this;
        }
    }

    private void addFeatureToLayer(Graphic g, ArcGISFeatureLayer featureLayer) {
        Graphic simplifiedGraphic = g;
        if (g.getGeometry().getType() != Type.POINT && g.getGeometry().getType() != Type.MULTIPOINT) {
            Geometry geom = GeometryEngine.simplify(g.getGeometry(), _map.getSpatialReference());
            simplifiedGraphic = new Graphic(geom, g.getSymbol(), g.getAttributes());
        }

        Graphic[] graphicsToAdd = new Graphic[] {simplifiedGraphic};

        featureLayer.applyEdits(graphicsToAdd, new Graphic[] {}, new Graphic[] {}, new FeatureAddedListener());

        _overlay.clearGraphic();
    }

    class FeatureAddedListener implements CallbackListener<FeatureEditResult[][]> {

        @Override
        public void onCallback(FeatureEditResult[][] objs) {
            System.out.println("Feature added result...");
            if (objs[0].length > 0) {
                for (int i = 0; i != objs[0].length; i++) {
                    System.out.println(objs[0][i]);
                }
            } else {
                throw new RuntimeException("Failed to add feature");
            }
        }

        @Override
        public void onError(Throwable e) {
            System.out.println("Failure");
            e.printStackTrace();
        }
    }

    private static final long serialVersionUID = 1L;
}